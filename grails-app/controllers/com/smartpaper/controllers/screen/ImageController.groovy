package com.smartpaper.controllers.screen
import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.screen.ImageScreen
import com.smartpaper.domains.media.Media
import com.smartpaper.domains.media.ImageMedia
import grails.converters.JSON
import grails.plugins.springsecurity.Secured

@Secured('ROLE_USER')
class ImageController {

    def imageService
    def screenService

    def changeImage = {
        withScreen{ ImageScreen screen ->
            if (params.media){
                def media = Media.findInStandardAndAppLibrary(request.application.id,params.long('media')).list()[0]
                if (media && media instanceof ImageMedia){
                    try{
                        imageService.updateImage(screen,media)
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

    private def withScreen(id="id", Closure c) {
        def screen = Screen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
}
