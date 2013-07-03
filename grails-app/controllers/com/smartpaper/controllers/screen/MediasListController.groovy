package com.smartpaper.controllers.screen

import com.smartpaper.domains.screen.Screen
import grails.converters.deep.JSON
import com.smartpaper.domains.screen.MediaListItem
import com.smartpaper.domains.media.Media
import com.smartpaper.domains.screen.MediasListScreen
import com.smartpaper.domains.media.ImageMedia
import grails.plugins.springsecurity.Secured

@Secured('ROLE_USER')
class MediasListController {

    def mediasListService
    def screenService

    def updateVisibility = {
        withScreen('id') { screen ->
                screen.properties = params.screen
                screenService.update(screen)
                screenService.createScreenShot(screen)
                render(status:200, text:'success')
        }
    }

    def updateArrows = {
        withScreen { MediasListScreen screen ->
                if(params.leftArrow == 'false' && screen.leftArrow){
                    screen.leftArrow = null
                }else if (params.leftArrow){
                    screen.leftArrow = (ImageMedia)Media.findInStandardAndAppLibrary(request.application.id,params.long('leftArrow')).list()[0]
                }
                if(params.rightArrow == 'false' && screen.rightArrow){
                    screen.rightArrow = null
                }else if (params.rightArrow){
                    screen.rightArrow = (ImageMedia)Media.findInStandardAndAppLibrary(request.application.id,params.long('rightArrow')).list()[0]
                }
                screenService.update(screen)
                screenService.createScreenShot(screen)
                render(status:200, template:'arrow', model:[screen:screen,app:screen.application])
        }
    }

    def addItem = {
        withScreen{MediasListScreen screen ->
            try{
                if (params.media){
                    def media = Media.findInStandardAndAppLibrary(screen.application.id,params.long('media')).list()[0]
                    if (media){
                        mediasListService.addItem(screen,media)
                        screenService.createScreenShot(screen)
                        render(status:200, template:'table', model:[screen:screen])
                    }else{
                        render(status:400)
                    }
                }
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
            }
        }
    }

    def updateItem = {
        withMediaItem{MediaListItem item ->
            try{
                if(params.mediaListItem){
                    item.properties = params.mediaListItem
                }
                if (params.media){
                    def media = Media.findInStandardAndAppLibrary(item.linkScreen.application.id,params.long('media')).list()[0]
                    item.media = media
                }
                mediasListService.updateItem(item)
                screenService.createScreenShot(item.linkScreen)
                render(status:200, template:'table', model:[screen:item.linkScreen])
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
            }
        }
    }

    def removeItem = {
        withMediaItem{MediaListItem item ->
            try{
                def screen = item.linkScreen
                mediasListService.removeItem(item)
                screenService.createScreenShot(screen)
                render(status:200, template:'table', model:[screen:screen])
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
            }
        }
    }

    private def withScreen(id="screen.id", Closure c) {
        def screen = Screen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }

    private def withMediaItem(id="id", screenId="screen.id", Closure c) {
        def screen = Screen.findByApplicationAndId(request.application,params."${screenId}".toLong())
        def mediaItem = MediaListItem.findByLinkScreenAndId(screen,params."${id}".toLong())
        if(mediaItem) {
            c.call mediaItem
        } else {
            render(status:404)
            return null
        }
    }
}
