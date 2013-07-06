package com.smartpaper.controllers

import grails.plugins.springsecurity.Secured
import com.smartpaper.domains.application.Application
import com.smartpaper.domains.screen.Screen

import com.smartpaper.domains.user.User
import grails.converters.JSON
import com.smartpaper.domains.media.Media
import com.smartpaper.domains.application.SocialLink
import com.smartpaper.domains.application.MenuItem
import com.smartpaper.domains.media.ImageMedia
import org.apache.commons.io.FilenameUtils
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsHibernateUtil
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import com.smartpaper.domains.screen.MediasListScreen
import com.smartpaper.domains.screen.FormScreen
import groovy.xml.StreamingMarkupBuilder
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import com.smartpaper.domains.media.VideoMedia
import com.smartpaper.domains.media.AudioMedia

@Secured(['ROLE_USER'])
class AppController {

    def applicationService
    def springSecurityService
    def screenService
    def openCVService

    private static final screenToView = [
        'ImageScreen':'imageview',
        'LinksListScreen':'listview',
        'VideoScreen':'videoview',
        'AudioScreen':'audioview',
        'WebViewScreen':'webview',
        'MediasListScreen':'carouselview',
        'FormScreen':'formview'
    ]

    def index = {
        setLocale()
        withApplication{ application ->
            Screen currentScreen = application.screens?.size() ? null : null
            render(view:"index", model:[currentScreen:currentScreen])
        }
    }

    def home = {
        setLocale()
        def applications = Application.findAllByOwner((User) springSecurityService.currentUser,[sort:'id', desc:'desc', max:8, offset:0, cache:true])
        render(view: "home", model:[applications:applications])
    }

    def open = {
        def applications = Application.findAllByOwner((User) springSecurityService.currentUser,[sort:'id', desc:'desc', cache:true])
        render(status:200,template:"open", model:[applications:applications])
    }

    def publish = {
        withApplication { app ->
            if (!app.active && !app.openCvInProgress){
                if(request.method == 'GET'){
                        def orphans = app.screens.findAll{ it.nbLinks == 0 && !it.home}
                        render(status:200,template:"publish",model:[app:app,orphans:orphans])
                }else{
                    openCVService.addApplication(app) ? render(status:200, text:'success') : render(status:400, text:'success')
                }
            }else{
                render(status:400)
            }
        }
    }

    def unpublish = {
        withApplication { app ->
            if(app.active && !app.openCvInProgress){
                if(request.method == 'GET'){
                    render(status:200,template:"unpublish",model:[app:app])
                }else{
                    openCVService.removeApplication(app)
                    render(status:200, text:'success')
                }
            }else{
                render(status:400)
            }
        }
    }

    def delete = {
        withApplication { Application app ->
            try{
                applicationService.delete(app)
                render(status:200, text:'success')
            }catch(IllegalStateException e){
                render(status:400, text:message(code:'ui.app.error.delete'))
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(app.errors)
                    log.debug(e.getMessage())
                }
            }
        }
    }

    def create = {
        if(request.method == 'GET'){
            render(status:200, template:'create')
        }else if(request.method == 'POST'){
            def app = new Application(owner:(User)springSecurityService.currentUser)
            app.properties = params.app
            try{
                applicationService.save(app,true)
                render(status:200, text:app.id)
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(app.errors)
                    log.debug(e.getMessage())
                }
                render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:app.errors)] as JSON)
            }
        }else{
            render(status:404)
        }
    }

    def setHome = {
        withApplication { application ->
            def screen = Screen.findByApplicationAndId(application,params.long('id'))
            if (screen){
                try{
                    screenService.setHome(screen)
                    render(status:200, contentType:'application/json', text:[screen:[id:screen.id]] as JSON)
                }catch(RuntimeException e){
                    if(log.isDebugEnabled()){
                        log.debug(screen.errors)
                        log.debug(e.getMessage())
                    }
                    render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:screen.errors)] as JSON)
                }
            }else{
                render(status:404)
            }
        }
    }

    def detailsView = {
        if(!params.id){
            render(status:404)
            return
        }
        def app = Application.findByIdAndOwner(params.long('id'),(User)springSecurityService.currentUser)
        if(app){
            render(status:200, template:'detailsView', model:[app:app,screens:app.screens.size() > 5 ? app.screens?.asList()?.subList(0,5) : app.screens])
        }
        else{
            render(status:400)
        }
    }

    @Secured(['ROLE_ANONYMOUS','ROLE_USER'])
    def recognition = {
        if(request.method != 'POST') {
            response.sendError(405)
            return
        }
        def image = request.getFile('image')
        if (!image.empty){
            File tmp = File.createTempFile("recog-"+new Date().time,'.jpg')
            image.transferTo(tmp)
            Application application = openCVService.findApplication(tmp)
            if (application){
                if (application.id == grailsApplication.config.smartpaper.vw.toLong())
                    render(status:200, contentType: "text/xml", text: new File(grailsApplication.config.smartpaper.baseDir+File.separator+"vw.xml").text)
                else if (application.id == grailsApplication.config.smartpaper.sp.toLong())
                    render(status:200, contentType: "text/xml", text: new File(grailsApplication.config.smartpaper.baseDir+File.separator+"sp.xml").text)
                else{
                    response.writer << '<?xml version="1.0" encoding="UTF-8"?>\n'
                    render(status:200, contentType: "text/xml", text: generateXml(application))
                }
            }else{
                log.debug("'image not recognized :'${tmp.absolutePath}")
                render(status:500, contentType: "text/xml", text:'<xml><error></error></xml>')
            }
            tmp.deleteOnExit()
        }
    }


    def common = {
        if(request.method != 'POST') {
            response.sendError(405)
            return
        }
        params.view = params.view in ['header', 'menu'] ? params.view : 'header'
        withApplication { app ->
            def withPhone = params.phone ? Boolean.valueOf(params.phone) : true

            switch(params.view){
                case 'header':
                    if(params.logoHeader == 'false' && app.logo?.url){
                        app.logo = null
                    }else if (params.logoHeader){
                        app.logo = Media.findInStandardAndAppLibrary(app.id,params.long('logoHeader')).list()[0]
                    }

                    if(params.app?.backgroundHeader)
                        app.backgroundHeader.properties = params.app.backgroundHeader

                    if(params.backgroundHeader == 'false' && app.backgroundHeader?.image){
                        app.backgroundHeader.image = null
                    }else if (params.backgroundHeader){
                        app.backgroundHeader.image = Media.findInStandardAndAppLibrary(app.id,params.long('backgroundHeader')).list()[0]
                    }
                break
                case 'menu':
                    if(params.app)
                        app.properties = params.app
                break
            }
            applicationService.update(app)
            render(status:200, template:'iPhone/manage', model:[view:'_'+params.view+'.gsp', phone:withPhone,app:request.application])
        }
    }

    def socialLinks = {
        if(request.method != 'POST' || !params._action in ['popup','add','update','delete']){
            render(status: 405)
            return
        }
        withApplication { Application app ->
            if (app.socialLinks?.size() >= 3 ){
                render(status:400)
            }
            def socialLink
            try {
                switch(params._action){
                    case 'popup':
                        render(status:200, template: 'popupSocialLink')
                        return
                    break
                    case 'add':
                        socialLink = new SocialLink()
                        socialLink.properties = params.socialLink
                        applicationService.saveSocialLink(app,socialLink)
                    break
                    case 'update':
                        socialLink = SocialLink.findByApplicationAndId(app,params.id)
                        if(!socialLink){
                            render(status:400)
                            return
                        }
                        if(!params.socialLink){
                            render(status:200, template: 'popupSocialLink', model:[socialLink:socialLink])
                            return
                        }else{
                            socialLink.properties = params.socialLink
                            applicationService.updateSocialLink(socialLink)
                        }
                    break
                    case 'delete':
                        socialLink = SocialLink.findByApplicationAndId(app,params.id)
                        if (socialLink)
                            applicationService.deleteSocialLink(socialLink)
                        else{
                            render(status:400)
                            return
                        }
                }
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(socialLink.errors)
                    log.debug(e.getMessage())
                }
                render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:socialLink.errors)] as JSON)
                return
            }
            render(status:200, template: 'tableSocial', model:[app:app])
        }
    }

    def menuItem = {
        if(request.method != 'POST' || !params._action in ['add','update','delete']){
            render(status: 405)
            return
        }
        withApplication { Application app ->
            if (app.menuItems?.size() >= 5 ){
                render(status:400)
            }
            def menuItem
            try {
                switch(params._action){
                    case 'add':
                        menuItem = new MenuItem()
                        applicationService.saveMenuItem(app,menuItem)
                    break
                    case 'update':
                        menuItem = MenuItem.findByApplicationAndId(app,params.id)
                        if(params.menuItem)
                            menuItem.properties = params.menuItem
                        if(params.icon)
                            menuItem.icon = (ImageMedia)Media.findInStandardAndAppLibrary(app.id,params.long('icon')).list()[0]
                        if(params.link)
                            menuItem.link = Screen.findByApplicationAndId(app,params.link)
                        applicationService.updateMenuItem(menuItem)
                    break
                    case 'delete':
                        menuItem = MenuItem.findByApplicationAndId(app,params.id)
                        if (menuItem)
                            applicationService.deleteMenuItem(menuItem)
                        else{
                            render(status:400)
                            return
                        }
                    break
                }
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(menuItem?.errors)
                    log.debug(e.getMessage())
                }
                render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:menuItem.errors)] as JSON)
                return
            }
            render(status:200, template: 'tableMenu', model:[app:app])
        }
    }

    def commonPreview = {
        withApplication {
            render(view: '/app/iPhone/common/preview', model: [:])
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    def submitForm = {
        if (request.method != 'POST'){
            response.sendError(405)
            return
        }

        if(!params.data."form_id"){
            render(status:404)
            return
        }else{
            def screen = FormScreen.findById(params.data."form_id".toLong())
            if(!screen)
                render(status:404)
            else{
                params.data.form_id = null
                def content = "Bonjour, \n\n un nouveau formulaire a été reçu pour l'application ${screen.application.name}: \n\n"
                params.data?.each{ k,v ->
                    if(v)
                        content += "${k} : ${v} \n"
                }
                content += "\n Bonne journée!"

                sendMail {
                  bcc grailsApplication.config.smartpaper.email
                  to screen.emailTo
                  subject "Formulaire de Smartpaper"
                  body content
                }
            }

        }

        def content = "Bonjour, \n\n un nouveau formulaire a été reçu: \n\n"
        params.data?.each{ k,v ->
            content += "${k} : ${v} \n"
        }
        content += "\n Bonne journée!"

        sendMail {
          to "admin@smartsystem.fr"
          subject "Formulaire de Smartpaper"
          body content
        }
        render(status:200)
    }

    def appPreview = {
        withApplication { application ->
            def screen = application.screens.find{ it.home } ?: application.screens?.asList()?.first() ?: null
            if (screen){
                render(status:200, template:'preview', model:[homeScreen:screen.id])
            }
        }
    }

    def inProgress = {
        withApplication { application ->
            render(status:200, contentType: 'application/json', text:[inProgress:application.openCvInProgress,active:application.active] as JSON)
        }
    }

    @Secured(['ROLE_ANONYMOUS','ROLE_USER'])
    def resources = {
        String path = [grailsApplication.config.smartpaper.baseDir, 'applications', params.app, 'medias', params.file].join(File.separator)
        def file = new File(path)
        if (file.exists()){
            Map<String, String> contentTypes = [
                    'mp4': 'video/mp4',
                    'mp3': 'video/mp3',
                    'jpg': 'image/jpeg',
                    'jpeg': 'image/jpeg',
                    'png': 'image/png',
                    'gif': 'image/gif',
            ]
            String extension = FilenameUtils.getExtension(path)
            if (contentTypes.containsKey(extension)) {
                response.setHeader('Content-Type', contentTypes[extension])
            }
            try{
                OutputStream out = response.getOutputStream()
                out.write(file.bytes)
                out.close()
            }catch(Exception e){}
        }else{
            log.warn("File ${file.absolutePath} doesn't exist")
            render(status:'404')
        }
    }

    @Secured(['ROLE_ANONYMOUS','ROLE_USER'])
    def cgu = {
        render(status:'200', template:'cgu', model:[text:grailsAttributes.getApplicationContext().getResource("/cgu.html").getFile().text])
    }

    @Secured('permitAll')
    def share = {
        withApplication('id') { app ->
            render(status:200, view:'_share', model:[app:app])
        }
    }

    private String generateXml(Application app){
        def xml = new StreamingMarkupBuilder()
        def writer = xml.bind {
                            campaign(id:app.id, name:app.name, sharelink:g.createLink([mapping:'defaultNoApp',absolute:true,controller:'app', action:'share', id:app.id])){

                                head(bgcolor:app.backgroundHeader.color, bg:app.backgroundHeader.image?.url?:'', logo:app.logo?.url?:''){
                                    app.socialLinks?.each{
                                        if (it.logo && it.url)
                                            item(icon:g.resource(absolute:true, dir:'images/social', file:it.logo), url:it.url)
                                    }
                                }

                                if (app.hasMenu){
                                    footer(){
                                        app.menuItems?.each{
                                            item(icon:it.icon?.url, contentId:it.link?.id,libelle:it.name)
                                        }
                                    }
                                }

                                screens(){
                                    app.screens.each{ def aScreen ->
                                        aScreen = GrailsHibernateUtil.unwrapIfProxy(aScreen)
                                        screen(id:aScreen.id, bg:aScreen.background.image?.url?:'', bgcolor:aScreen.background.color, home:aScreen.home){
                                            if (aScreen.hasTitle){
                                                title([height:aScreen.backgroundTitle.height, bgcolor:aScreen.backgroundTitle.color, bg:aScreen.backgroundTitle.image?.url?:'']){
                                                    mkp.yieldUnescaped("<![CDATA[<body bottommargin=\"0\" topmargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" marginheight=\"0\" marginwidth=\"0\" style=\"width:320px;${aScreen.backgroundTitle.image ? 'background:url('+aScreen.backgroundTitle.image.url+') no-repeat;':''}${aScreen.backgroundTitle.color ? 'background-color:'+aScreen.backgroundTitle.color+';':''}\">${aScreen.contentTitle}</body>]]>")
                                                }
                                            }

                                            if (aScreen.hasOptions){
                                                optionbar([height:44, bgcolor:aScreen.backgroundOptions.color, bg:aScreen.backgroundOptions.image?.url?:'']){
                                                    aScreen.options?.each{
                                                        item(icon:it.icon.url, iconactive:it.iconActive?.url?:'', target:it.link.id)
                                                    }
                                                }
                                            }

                                            def currentView = screenToView.get(GrailsClassUtils.getShortName(aScreen.class))

                                            switch(currentView) {
                                                case 'imageview':
                                                    content([type: currentView ]){
                                                        media(aScreen.image?.url)
                                                    }
                                                    break

                                                case 'listview':
                                                    content([type: currentView, bgcell: aScreen.bgcell, alpha:aScreen.alpha]){
                                                        aScreen.links?.each{
                                                            if (aScreen.police == 'Helvetica Bold'){
                                                                aScreen.police = 'HelvaticaBold'
                                                            }
                                                            item(libelle:it.name,icon:it.image?.url?:'',target:it.linkTo?.id, font:aScreen.police.replaceAll(' ',''), size:aScreen.fontSize, color:aScreen.fontColor)
                                                        }
                                                    }
                                                    break

                                                case 'videoview':
                                                    content([type: currentView ]){
                                                        media(aScreen.video?.url)
                                                    }
                                                    break

                                                case 'audioview':
                                                    content([type: currentView ]){
                                                        media(aScreen.audio?.url)
                                                    }
                                                    break

                                                case 'carouselview':
                                                    content([type: currentView, arrows: aScreen.arrowsDisplay?:MediasListScreen.ARROWS_NEVER, leftarrow:aScreen.leftArrow?.url, rightarrow:aScreen.rightArrow?.url]){
                                                        aScreen.medias?.each{
                                                            item(type: it.media instanceof ImageMedia ? 1 : it.media instanceof AudioMedia ? 2 : 3 , media: it.media.url)
                                                        }
                                                    }
                                                    break

                                                case 'formview':
                                                    content( type:currentView, emailto:aScreen.emailTo ){
                                                        aScreen.inputs?.each{
                                                            fieldInput(name:it.id+'-form-input', require:it.required, type:'text', label:it.label)
                                                        }
                                                    }
                                                    break

                                                case 'webview':
                                                    content( type:currentView ){
                                                        mkp.yieldUnescaped("<![CDATA[<head><meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=3.0; user-scalable=1\"/></head><body bottommargin=\"0\" topmargin=\"0\" leftmargin=\"0\" rightmargin=\"0\" marginheight=\"0\" marginwidth=\"0\" style=\"width:320px;${aScreen.background.image ? 'background:url('+aScreen.background.image.url+') no-repeat;':''}${aScreen.background.color ? 'background-color:'+aScreen.background.color+';':''}\">${aScreen.content}</body>]]>")
                                                    }
                                                    break
                                            }
                                        }
                                    }
                                }
                            }
                        }
        return writer.toString()
    }

    @Secured(['ROLE_ADMIN'])
    def reloadDatabase = {
        openCVService.fullReloadDatabase()
        render(status:200)
    }

    private def withApplication(String id = 'application', Closure c) {
        def application = request."$id" ?: Application.get(params."$id") ?: null
        if(application) {
            c.call application
        } else {
            redirect action:"home"
        }
    }

    private setLocale(){
        def locale = params.lang ?: null
        try {
            def localeAccept = request.getHeader("accept-language")?.split(",")
            if (localeAccept)
                localeAccept = localeAccept[0]?.split("-")

            if (localeAccept?.size() > 0) {
                locale = params.lang ?: localeAccept[0].toString()
            }
        } catch (Exception e) {}

        if (springSecurityService.isLoggedIn()) {
            def user = (User)springSecurityService.currentUser
            if (locale != user.language || RCU.getLocale(request).toString() != user.language) {
                RCU.getLocaleResolver(request).setLocale(request, response, new Locale(user.language))
            }
        } else {
            if (locale) {
                RCU.getLocaleResolver(request).setLocale(request, response, new Locale(locale))
            }
        }
    }
}
