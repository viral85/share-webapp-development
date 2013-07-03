package com.smartpaper.controllers

import grails.converters.JSON
import javax.servlet.http.HttpServletRequest
import org.springframework.web.multipart.MultipartHttpServletRequest
import org.springframework.web.multipart.MultipartFile
import grails.plugins.springsecurity.Secured
import com.smartpaper.domains.media.Media
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import com.smartpaper.domains.application.Application
import com.smartpaper.domains.media.ImageMedia
import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.media.VideoMedia
import com.smartpaper.domains.media.AudioMedia

@Secured(['ROLE_USER'])
class MediaController {

    def applicationService
    def ajaxUploaderService

    static ExtensionsImage = ['\'jpg\'','\'png\'','\'jpeg\'','\'gif\'']
    static ExtensionsVideo = ['\'mp4\'']
    static ExtensionsAudio = ['\'mp3\'']
    static ExtensionsAll =  ['\'jpg\'','\'png\'','\'jpeg\'','\'gif\'','\'mp4\'','\'mp3\'']

    def explorer = {
        if (!params.filter in ['Image','Video','Audio','All'])
            params.filter = null
        withApplication { Application app ->


            def standardMedias = params.filter ? Application.get(1).medias.findAll{
                if (params.filter == 'All')
                    (GrailsClassUtils.getShortName(it.class) in ['ImageMedia','VideoMedia','AudioMedia'])
                else
                    GrailsClassUtils.getShortName(it.class) == "${params.filter+'Media'}"
            } : Application.get(1).medias


            def medias = params.filter ? app.medias.findAll{
                if (params.filter == 'Trigger'){
                    GrailsClassUtils.getShortName(it.class) == 'ImageMedia' && it.triggerApp
                }
                else if (params.filter == 'All'){
                    (GrailsClassUtils.getShortName(it.class) == 'ImageMedia' && !it.triggerApp) || (GrailsClassUtils.getShortName(it.class) in ['VideoMedia','AudioMedia'])
                }
                else if (params.filter == 'ImageMedia'){
                    (GrailsClassUtils.getShortName(it.class) == 'ImageMedia' && !it.triggerApp)
                }else {
                    (GrailsClassUtils.getShortName(it.class) == "${params.filter+'Media'}")
                }
            } : app.medias

            render(text:ui.mediaExplorer(
                 [
                    editable:false,
                    title:params.filter?"ui.media.explorer.${params.filter.toLowerCase()}.title":null,
                    onSubmit: params.update instanceof Map ? params.update.controller ? remoteFunction([
                            controller:params.update.controller,
                            action:params.update.action,
                            update:params.update.content?:null,
                            onSuccess:params.update.onSuccess?:null,
                            params:"'application=${request.application.id}&${params.update.value?:'media'}='+selected+'${params.update.params? '&'+params.update.params :''}'"])
                        : null : params.update ?: null,
                    in: medias,
                    tabs:[
                            [title: params.filter ? "ui.media.explorer.add.${params.filter.toLowerCase()?:null}.title": "ui.media.explorer.add.title",
                             content:[
                                     template:'uploadMedia',
                                     model:[
                                             allowedExtensions: params.filter ? this."${'Extensions'+params.filter}":[]
                                     ]
                             ]
                            ],
                            [title:'ui.media.explorer.standard.library.title',content:[template:'standard', model:[medias:standardMedias,view:'icon']]]
                    ]
                 ] as Map, null
            ))
        }
    }

    def manage = {
        withApplication { Application app ->
            render(text:ui.mediaExplorer(
                    defaultSelect:false,
                    title:"ui.media.explorer.title",
                    editable:true,
                    in: params.filter ? app.medias.findAll{ GrailsClassUtils.getShortName(it.class) == "${params.filter+'Media'}" && !it.triggerApp } : app.medias?.findAll{ if (GrailsClassUtils.getShortName(it.class) == "ImageMedia"){ !it.triggerApp } else { true } }, tabs:[[title:'ui.media.explorer.add.title',content:[template:'uploadMedia']]]
            ))
        }
    }

    def trigger = {
        withApplication { Application app ->
            def tabs = [[title:'ui.application.trigger.add.title',content:[template:'uploadMedia', model:[allowedExtensions:ExtensionsImage,trigger:true]]]]
            render(text:ui.mediaExplorer(
                    defaultSelect:false,
                    title:"ui.application.trigger.title",
                    editable: !app.active,
                    in: app.triggers, tabs:app.openCvInProgress ?false:app.active?null:tabs
            ))
        }
    }

    def download = {
        withApplication { application ->
            def media = Media.findInStandardAndAppLibrary(application.id, params.long('id')).list()[0]
            if (media){
                def file = new File(media.path)
                if (file.exists()){
                    String filename = media.filename
                    ['Content-disposition': "attachment;filename=\"$filename\"",'Cache-Control': 'private','Pragma': ''].each {k, v ->
                        response.setHeader(k, v)
                    }
                    OutputStream out = response.getOutputStream()
                    out.write(file.bytes)
                    out.close()
                }else{
                    render(status:404)
                }
            }
            else
                render(status:404)
        }
    }

    def upload = {
        withApplication { Application app ->
            InputStream inputStream = selectInputStream(request)
            try{
                def media
                def content = null
                if (Boolean.valueOf(params.trigger)){
                    media = applicationService.addMedia(app, inputStream, params.qqfile, true)
                    content = null
                }else{
                    media = applicationService.addMedia(app, inputStream, params.qqfile, false)
                    if (media instanceof ImageMedia){
                        content = g.render(template:'resizeImage', model:[image:media,screen:params.screen? Screen.get(params.screen) : null])
                    }else if(media instanceof VideoMedia){
                        content = g.render(template:'video', model:[video:media,screen:params.screen? Screen.get(params.screen) : null])
                    }else if(media instanceof AudioMedia){
                        content = g.render(template:'audio', model:[audio:media,screen:params.screen? Screen.get(params.screen) : null])
                    }

                }
                def mediaJSON = [filename:media.name,
                        url:media.url,
                        thumbnail:media.getThumbnail(null) ?: createLinkTo(dir: 'images', file: 'no-'+GrailsClassUtils.getShortName(media.class).toLowerCase()+'.png'),
                        id:media.id,
                        displayName:ui.truncate([value:media.name, max:15])
                ]
                render(status:200, contentType: 'application/json', text:[success:true,media:mediaJSON,content:content?:null] as JSON)
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[success:false,error:message(code:e.getMessage())] as JSON)
            }
        }
    }

    def resizeImage = {
        withMedia { media ->
            if (!params.rsize){
                render(status:404)
                return
            }
            def screen = null
            if (params.screen?.id){
                screen = Screen.findByApplicationAndId(request.application,params.screen.id.toLong())
            }
            def width, height;
            switch(params.rsize){
                case 'f':
                    width  = 320; height = 416 - (media.application.hasMenu ? 49 : 0);
                    break;
                case 'ft':
                    width  = 320; height = 416 - (screen?.hasTitle ? screen.backgroundTitle.height : 0) - (media.application.hasMenu ? 49 : 0);
                    break;
                case 'fo':
                    width  = 320; height = 416 - (screen?.hasOptions ? 44 : 0) - (media.application.hasMenu ? 49 : 0);
                    break;
                case 'fto':
                    width  = 320; height = 416 - (screen?.hasTitle ? screen.backgroundTitle.height : 0) - (screen.hasOptions ? 44 : 0) - (media.application.hasMenu ? 49 : 0);
                    break;
                case 'o':
                    width  = media.width; height = media.height;
                    break;
                case 'p':
                    width  = params.custom.width.toInteger(); height = params.custom.height.toInteger();
                    break;
            }
            if(width && height && (width != media.width || height != media.height)){
                applicationService.resizeImage(media,width,height)
            }
            render(status:200)
        }
    }

    def detailsView = {
        withMedia{ media ->
            render(template:'detailsView', model:[media:media,editable:params.editable?Boolean.valueOf(params.editable):false])
        }
    }

    def delete = {
        withMedia{ media ->
            try{
                    def idDeleted = media.id
                    applicationService.removeMedia(media)
                    render(status:200,text:[id:idDeleted] as JSON, contentType: 'application/json')
            }catch (IllegalStateException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, text:message(code:'ui.media.error.delete'))
            }
            catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(media.errors)
                    log.debug(e.getMessage())
                }
                render(status:500, contentType:'application/json', text:[error:renderErrors(bean:media.errors)] as JSON)
            }
        }
    }

    private def withApplication(Closure c) {
        def application = request.application
        if(application) {
            c.call application
        } else {
            render(status:404)
        }
    }

    private def withMedia(id = 'id',Closure c) {
        def application = request.application
        if (application){
            def media = Media.findInStandardAndAppLibrary(application.id,params."${id}".toLong()).list()[0]
            if(application && media) {
                c.call media
            } else {
                render(status:404)
            }
        }else{
            render(status:404)
        }
    }

    private InputStream selectInputStream(HttpServletRequest request) {
        if (request instanceof MultipartHttpServletRequest) {
            MultipartFile uploadedFile = ((MultipartHttpServletRequest) request).getFile('qqfile')
            return uploadedFile.inputStream
        }
        return request.inputStream
    }
}
