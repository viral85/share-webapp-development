package com.smartpaper.controllers.screen

import grails.plugins.springsecurity.Secured
import com.smartpaper.domains.screen.DetailedViewScreen
import com.smartpaper.domains.screen.Overlay
import com.smartpaper.domains.screen.FormScreen
import com.smartpaper.domains.application.Background
import com.smartpaper.domains.media.ImageMedia

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 15.08.12
 */
@Secured('ROLE_USER')
class DetailedViewController {

    def updateOverlay() {
        //application=${app.id}&screen.id=${screen.id}&id='+editor.attr('elemid')+'&overlay.content
        withScreen('screen.id') { DetailedViewScreen screen ->
            Long id = params.id as Long
            Overlay overlay = screen.overlays?.find { it.id == id }
            if (!overlay) {
                log.error("Unknown overlay $id")
                render(status: 404)
                return
            }
            overlay.content = params.overlay?.content ?: ""
            if (params.overlay?.x) {
                overlay.x = params.overlay.x as Integer
            }
            if (params.overlay?.y) {
                overlay.y = params.overlay.y as Integer
            }
            if (params.image) {
                ImageMedia image = ImageMedia.load(params.image as Long)
                if (image) {
                    overlay.image = image
                } else {
                    log.error("Unknown image $params.image")
                }
            }
            overlay.save()
            render(status:200, template: 'table', model: [screen: screen])
        }
    }

    def removeOverlay() {
        withScreen('screen.id') { DetailedViewScreen screen ->
            Long id = params.id as Long
            Overlay overlay = screen.overlays?.find { it.id == id }
            if (!overlay) {
                log.error("Unknown overlay $id")
                render(status: 404)
                return
            }
            screen.removeFromOverlays(overlay)
            screen.save()
            overlay.delete()
            render(status:200, template: 'table', model: [screen: screen])
        }
    }

    def addOverlay() {
        withScreen { DetailedViewScreen screen ->
            Background background = new Background()
            background.save()
            Overlay overlay = new Overlay(
                    linkScreen: screen,
                    background: background,
                    content: ""
            )
            overlay.save(failOnError: true)
            screen.addToOverlays(overlay)
            screen.save()
            render(status:200, template: 'table', model: [screen: screen])
        }
    }

    private def withScreen(String idName="id", Closure c) {
        Long id = params."${idName}" as Long
        def screen = DetailedViewScreen.findByApplicationAndId(request.application, id)
        if (screen) {
            c.call screen
        } else {
            log.error("Can't find screen. Application: $request.application, id: $id")
            render(status:404)
            return null
        }
    }
}
