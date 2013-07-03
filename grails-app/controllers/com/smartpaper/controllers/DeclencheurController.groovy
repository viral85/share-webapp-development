package com.smartpaper.controllers

import com.smartpaper.domains.application.Application
import org.codehaus.groovy.grails.commons.GrailsClassUtils

class DeclencheurController {

    def manage = {
        withApplication { Application app ->
            render(text:ui.mediaExplorer(
                    defaultSelect:false,
                    title:"ui.media.explorer.title",
                    editable:true,
                    in: params.filter ? app.medias.findAll{ GrailsClassUtils.getShortName(it.class) == "${params.filter+'Media'}" } : app.medias, tabs:[[title:'ui.media.explorer.add.title',content:[template:'uploadMedia']]]
            ))
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

    private def withApplication(Closure c) {
        def application = request.application
        if(application) {
            c.call application
        } else {
            render(status:404)
        }
    }
}
