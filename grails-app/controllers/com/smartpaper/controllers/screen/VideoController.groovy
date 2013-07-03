package com.smartpaper.controllers.screen

import com.smartpaper.domains.screen.VideoScreen
import com.smartpaper.domains.media.VideoMedia
import grails.converters.JSON
import com.smartpaper.domains.media.Media
import grails.plugins.springsecurity.Secured

@Secured('ROLE_USER')
class VideoController {

    def videoService
    def screenService

    def changeVideo = {
        withScreen{ VideoScreen screen ->
            if (params.media){
                def media = Media.findInStandardAndAppLibrary(request.application.id,params.long('media')).list()[0]
                if (media && media instanceof VideoMedia){
                    try{
                        videoService.updateVideo(screen,media)
                        screenService.createScreenShot(screen)
                    }catch(RuntimeException e){
                        if(log.debugEnabled)
                            log.debug(e.getMessage())
                        render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
                    }
                    render(status:200, template:'content', model:[app:request.application,screen:screen])
                }else{
                    render(status:400)
                }
            }
        }
    }

    private def withScreen(id="screen.id", Closure c) {
        def screen = VideoScreen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
}
