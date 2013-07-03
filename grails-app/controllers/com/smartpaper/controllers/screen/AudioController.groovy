package com.smartpaper.controllers.screen

import com.smartpaper.domains.media.Media
import com.smartpaper.domains.media.VideoMedia
import com.smartpaper.domains.screen.VideoScreen
import grails.converters.JSON
import grails.plugins.springsecurity.Secured
import com.smartpaper.domains.media.AudioMedia
import com.smartpaper.domains.screen.AudioScreen

@Secured('ROLE_USER')
class AudioController {

    def audioService
    def screenService

    def changeAudio = {
        withScreen{ AudioScreen screen ->
            if (params.media){
                def media = Media.findInStandardAndAppLibrary(request.application.id,params.long('media')).list()[0]
                if (media && media instanceof AudioMedia){
                    try{
                        audioService.updateAudio(screen,media)
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
        def screen = AudioScreen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
}
