package com.smartpaper.controllers.screen

import com.smartpaper.domains.screen.LinksListScreen
import com.smartpaper.domains.screen.Screen
import grails.converters.JSON
import com.smartpaper.domains.screen.LinkListItem
import com.smartpaper.domains.media.Media
import com.smartpaper.domains.media.ImageMedia
import grails.plugins.springsecurity.Secured

@Secured('ROLE_USER')
class LinksListController {

    def linksListService
    def screenService

    def updateAppareance = {
        withScreen { screen ->
                screen.properties = params.screen
                screenService.update(screen)
                render(status:200, text:'success')
        }
    }

    def addLink = {
        withScreen { LinksListScreen screen ->
            try{
                linksListService.saveLinkListItem(screen)
                screenService.createScreenShot(screen)
                render(status:200, template: 'table', model:[screen:screen,app:screen.application])
            }catch(RuntimeException e){
                if (log.debugEnabled)
                    log.debug(e.getMessage())
                render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
            }
        }
    }

    def updateLink = {
        withLinkListItem {LinkListItem linkListItem ->
            try{
                if(params.linkListItem){
                    linkListItem.properties = params.linkListItem
                }
                if(params.linkTo){
                    def screen = Screen.findByApplicationAndId(request.application,params.long('linkTo'))
                    linkListItem.linkTo = screen
                }
                if(params.image){
                    def icon = (ImageMedia)Media.findInStandardAndAppLibrary(request.application.id,params.long('image')).list()[0]
                    linkListItem.image = icon
                }
                linksListService.updateLinkListItem(linkListItem)
                screenService.createScreenShot(linkListItem.linkScreen)
                render(status:200, template: 'table', model:[screen:linkListItem.linkScreen])
            }catch(RuntimeException e){
                if (log.debugEnabled)
                    log.debug(e.getMessage())
                render(status:400, contentType:'application/json', text:[error:renderErrors([bean:linkListItem.errors])] as JSON)
            }
        }
    }

    def removeLink = {
        withLinkListItem { linkListItem ->
            try{
                def screen = linkListItem.linkScreen
                linksListService.deleteLinkListItem(linkListItem)
                screenService.createScreenShot(linkListItem.linkScreen)
                render(status:200, template: 'table', model:[screen:screen])
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[error:renderErrors([bean:linkListItem.errors])] as JSON)
            }
        }
    }

    private def withLinkListItem(id="id", screenId="screen.id", Closure c) {
        def screen = LinksListScreen.findByApplicationAndId(request.application,params."${screenId}".toLong())
        def linkListItem = LinkListItem.findByLinkScreenAndId(screen,params."${id}".toLong())
        if(linkListItem) {
            c.call linkListItem
        } else {
            render(status:404)
            return null
        }
    }

    private def withScreen(id="id", Closure c) {
        def screen = LinksListScreen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
}
