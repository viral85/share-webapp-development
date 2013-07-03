package com.smartpaper.controllers.screen

import com.smartpaper.domains.screen.WebViewScreen
import grails.converters.deep.JSON
import grails.plugins.springsecurity.Secured

@Secured('ROLE_USER')
class WebViewController {

    def screenService

    def update = {
        withScreen(){ WebViewScreen screen ->
            try{
                if(params.screen){
                    screen.properties = params.screen
                    screen.content = screen.content?.replaceAll('src="../resources','src="'+grailsApplication.config.grails.serverURL+'/resources')
                    screen.content = screen.content?.replaceAll('%28','(')?.replaceAll('%29',')')
                    screenService.update(screen)
                    screenService.createScreenShot(screen)
                }
            }catch(RuntimeException e){
                if(log.debugEnabled)
                    log.debug(e.getMessage())
                render(status:400, contentType:'application/json', text:[error:renderErrors(bean:screen.errors)] as JSON)
            }
            render(status:200, text:'success')
        }
    }

    private def withScreen(id="screen.id", Closure c) {
        def screen = WebViewScreen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
}
