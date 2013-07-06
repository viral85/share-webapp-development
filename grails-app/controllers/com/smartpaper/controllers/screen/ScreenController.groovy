package com.smartpaper.controllers.screen

import com.smartpaper.domains.screen.Screen
import grails.converters.JSON
import com.smartpaper.domains.application.Application
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import com.smartpaper.domains.media.Media
import com.smartpaper.domains.screen.Option
import com.smartpaper.domains.screen.LinkListItem
import org.springframework.dao.DataIntegrityViolationException
import com.smartpaper.domains.screen.WebViewScreen

@Secured(['ROLE_USER'])
class ScreenController {

    def screenService

    def show = {
        if(request.method != 'POST'){
            render(status: 405)
            return
        }
        withScreen{ screen ->
            def withPhone = params.phone ? Boolean.valueOf(params.phone) : true
            params.view = params.view?:'content'
            render(template:'iPhone/manage', model:[screen:screen, phone:withPhone, view:getTemplateScreen(screen,params.view, false, '_')])
        }
    }

    def create = {
        withApplication {Application application ->

            if (request.method == 'GET'){
                render(status:200, template: 'create')
                return
            }
            else if(request.method == 'POST'){
                def screen
                //default image screen
                int type = params.int('type') ?: 0
                def screenModel = grailsApplication.getArtefacts("Domain").find { it.name.contains('Screen') && it.name != 'Screen' && it.clazz.type == type }
                if (!screenModel){
                    render(status:404)
                    return
                }
                screen = screenModel.newInstance()
                screen.properties = params.screen
                try{
                    screenService.save(screen, application)
                    params.view = 'content'
                    render(template:'iPhone/manage', model:[view:getTemplateScreen(screen,params.view, false, '_'), phone:true, screen:screen])
                    render(text:jq.jquery(null,"""jQuery.smartpaper.createSmallScreen({
                                                                                        id:${screen.id},
                                                                                        home:${screen.home},
                                                                                        nbLinks:${screen?.nbLinks},
                                                                                        name:'${screen.name}',
                                                                                        order:${screen.order}
                                                                                        ${screen.getThumbnail(null) ? 'thumbnail:\''+screen.getThumbnail(null)+'\'' : ''}
                                                                                     });
                                                  jQuery('#page-list > li').removeClass('active');
                                                  jQuery('#page-list > li[elemid=${screen.id}]').addClass('active');"""))

                    screenService.createScreenShot(screen)
                }catch(RuntimeException e){
                    if(log.isDebugEnabled()){
                        log.debug(e.getMessage())
                    }
                    render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:screen.errors)] as JSON)
                }
            }else{
                render(status: 405)
            }
        }
    }

    def update = {
        if(request.method != 'POST'){
            render(status: 405)
            return
        }
        withScreen('screen.id'){ screen ->
            def withPhone = params.phone ? Boolean.valueOf(params.phone) : true
            try{
                screen.properties = params.screen

                if(params.background == 'false' && screen.background.image){
                    screen.background.image = null
                }else if (params.background){
                    screen.background.image = Media.findInStandardAndAppLibrary(request.application.id,params.long('background')).list()[0]
                }

                if (params.screen.backgroundTitle)
                    screen.backgroundTitle.properties = params.screen.backgroundTitle

                if (params.screen.backgroundOptions)
                    screen.backgroundOptions.properties = params.screen.backgroundOptions
                if (params.screen.background)
                    screen.background.properties = params.screen.background

                if(params.backgroundTitle == 'false' && screen.backgroundTitle.image){
                    screen.backgroundTitle.image = null
                }else if (params.backgroundTitle){
                    screen.backgroundTitle.image = Media.findInStandardAndAppLibrary(request.application.id,params.long('backgroundTitle')).list()[0]
                    screen.backgroundTitle.height = screen.backgroundTitle.height > screen.backgroundTitle.image.height ? screen.backgroundTitle.height : screen.backgroundTitle.image.height
                }

                if(params.backgroundOptions == 'false' && screen.backgroundOptions.image){
                    screen.backgroundOptions.image = null
                }else if (params.backgroundOptions){
                    screen.backgroundOptions.image= Media.findInStandardAndAppLibrary(request.application.id,params.long('backgroundOptions')).list()[0]
                }

                screenService.update(screen)
                screenService.createScreenShot(screen)

            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[error:renderErrors(bean:screen.errors)] as JSON)
            }
            render(template:'iPhone/manage', model:[screen:screen,phone:withPhone,view:getTemplateScreen(screen,params.view, false)])
        }
    }

    def changeOrder = {
        if(request.method != 'POST' || !params.order){
            render(status: 405)
            return
        }
        withScreen{ screen ->
            if(screenService.updateOrder(screen,params.int('order')))
                render(status:200,text:'success')
            else
                render(status:400,text:'error')
        }
    }

    def rename = {
        withScreen{ screen ->
            if(request.method == 'POST'){
                screen.name = params.screen.name
                try{
                    screenService.update(screen)
                    render(status:200,text:[name:screen.name, id:screen.id, order:screen.order] as JSON, contentType:'application/json')
                }catch(RuntimeException e){
                    render(status:400,text:'error')
                }
            }else{
                render(status:200, template:'edit', model:[screen:screen])
            }
        }
    }

    def duplicate = {
        if(request.method != 'POST'){
            render(status: 405)
            return
        }
        withScreen{ screen ->
            try{
                def copy = screenService.duplicate(screen)
                params.view = 'content'
                render(template:'iPhone/manage', model:[view:getTemplateScreen(screen,params.view, false), phone:true, screen:copy])
                render(text:jq.jquery(null,"""jQuery.smartpaper.createSmallScreen({
                                                                                    id:${copy.id},
                                                                                    home:${copy.home},
                                                                                    nbLinks:${copy?.nbLinks},
                                                                                    name:'${copy.name}',
                                                                                    order:${copy.order}
                                                                                    ${copy.getThumbnail(null) ? 'thumbnail:\''+copy.getThumbnail(null)+'\'' : ''}
                                                                                 });
                                              jQuery('#page-list > li').removeClass('active');
                                              jQuery('#page-list > li[elemid=${copy.id}]').addClass('active');"""))
                screenService.createScreenShot(copy)
            }catch(RuntimeException e){
                log.debug(e)
                render(status:400,text:'error')
            }
        }
    }

    def delete = {
        if(request.method != 'POST'){
            render(status: 405)
            return
        }
        withScreen{ screen ->
            try{
                def id = screen.id
                def order = screen.order
                screenService.delete(screen)
                render(status:200, contentType: 'application/json', text:[id:id,order:order] as JSON)
            }catch (IllegalStateException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, text:message(code:'ui.screen.error.delete'))
            }
            catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:500, contentType:'application/json', text:[error:renderErrors(bean:screen.errors)] as JSON)
            }
        }
    }

    def explorer = {
        withApplication { Application app ->
            render(status:200,text:ui.explorer([title:"ui.screen.choose.title",
                                                width:700,
                                                resizable:true,
                                                className:"app-explorer",
                                                in:app.screens,
                                                view:"icon",
                                                onSubmit: params.update instanceof Map ? params.update?.controller ? remoteFunction([
                                                            controller:params.update.controller,
                                                            action:params.update.action,
                                                            update:params.update.content,
                                                            onSuccess:params.update.onSuccess,
                                                            params:"'application=${request.application.id}&${params.update.value?:'media'}='+selected+'${params.update.params? '&'+params.update.params :''}'"])
                                                    : null : params.update ?: null,
                                                detailsView:[controller:'screen', action:'detailsView', params:'\'application='+app.id+'&id=\'+elem.attr(\'elemid\')']]))
        }
    }

    def options = {
        if(request.method != 'POST' || !params._action in ['add','update','delete']){
            render(status: 405)
            return
        }
        switch(params._action){
            case 'add':
                withScreen { screen ->
                    try{
                        screenService.saveOption(screen)
                        screenService.createScreenShot(screen)
                        render(status:200, template: 'tableOptions', model:[screen:screen])
                    }catch(RuntimeException e){
                        if(log.isDebugEnabled()){
                            log.debug(e.getMessage())
                        }
                        render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
                    }
                }
                break
            case 'delete':
                withOption { option ->
                    try{
                        def screen = option.screen
                        screenService.deleteOption(option)
                        screenService.createScreenShot(screen)
                        render(status:200, template: 'tableOptions', model:[screen:screen])
                    }catch(RuntimeException e){
                        if(log.isDebugEnabled()){
                            log.debug(e.getMessage())
                        }
                        render(status:400, contentType:'application/json', text:[error:renderErrors([bean:option.errors])] as JSON)
                    }
                }
                break
            case 'update':
               withOption { option ->
                    try{
                        if (params.for){
                            def icon = Media.findInStandardAndAppLibrary(request.application.id,params.long('icon')).list()[0]
                            params.for == 'normal' && icon ? (option.icon = icon) : (option.iconActive = icon)
                        }
                        if (params.link){
                            def screen = Screen.findByApplicationAndId(request.application,params.long('link'))
                            option.link = screen
                        }
                        screenService.updateOption(option)
                        screenService.createScreenShot(option.screen)
                        render(status:200, template: 'tableOptions', model:[screen:option.screen])
                    }catch(RuntimeException e){
                        if(log.isDebugEnabled()){
                            log.debug(e.getMessage())
                        }
                        render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
                    }
                }
                break
        }
    }
    @Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
    def preview = {
        if(request.method != 'GET'){
            render(status: 405)
            return
        }
        withScreen{ screen ->
            request.screen = screen
            render(status:200, view:getTemplateScreen(screen,'preview',true), model:[screen:screen])
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    def screenshot = {
        if(request.method != 'GET'){
            render(status: 405)
            return
        }
        def screen = Screen.get(params.long('id'))
        if (screen){
            request.screen = screen
            render(status:200, view:getTemplateScreen(screen,'preview',true), model:[screen:screen,optionsHeight:0])
        }
    }

    def detailsView = {
        withScreen { screen ->
            render(status:200, template:'detailsView', model:[screen:screen])
        }
    }

    private def withApplication(Closure c) {
        def application = request.application
        if(application) {
            c.call application
        } else {
            render(status:404)
            return null
        }
    }

    private def withScreen(id="id", Closure c) {
        def screen = Screen.findByApplicationAndId(request.application != null ? request.application :Application.get(params.application.toLong()) ,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }

    private def withOption(id="id", screenId="screen.id", Closure c) {
        def screen = Screen.findByApplicationAndId(request.application,params."${screenId}".toLong())
        def option = Option.findByScreenAndId(screen,params."${id}".toLong())
        if(option) {
            c.call option
        } else {
            render(status:404)
            return null
        }
    }

    private String getTemplateScreen(def screen, def template, def forPhone, String templatePrefix = ''){
        def controllerName = GrailsClassUtils.getShortName(screen.class).substring(0,1).toLowerCase() + GrailsClassUtils.getShortName(screen.class).substring(1)
        def folderView = template in ['options', 'title'] ? '/screen/' : '/'+(controllerName - 'Screen') + (forPhone ? '/iPhone' : '')
        String templateScreen = "${folderView}/${templatePrefix}${template in ['options', 'title', 'content'] ? template : (template ?:'content')}"
        //log.debug("Template: $templateScreen")
        return templateScreen
    }
}
