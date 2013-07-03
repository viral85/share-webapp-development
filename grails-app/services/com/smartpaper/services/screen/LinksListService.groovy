package com.smartpaper.services.screen

import com.smartpaper.domains.screen.LinksListScreen
import com.smartpaper.domains.screen.LinkListItem
import com.smartpaper.domains.screen.Screen

class LinksListService {

    static transactional = true

    def saveLinkListItem(LinksListScreen screen){
        def link = new LinkListItem()
        link.order = (screen.links?.size()?:0) + 1
        screen.addToLinks(link)
        if(!screen.save(flush:true))
            throw new RuntimeException()
        return link
    }

    def updateLinkListItem(LinkListItem link){
        if(link.isDirty('order')){
            if (link.getPersistentValue('order') > link.order) {
                link.linkScreen.links.each {it ->
                    if (it.order >= link.order && it.order <= link.getPersistentValue('order') && it.id != link.id) {
                        it.order = it.order + 1
                        it.save()
                    }
                }
            } else {
                link.linkScreen.links.each {it ->
                    if (it.order <= link.order && it.order >= link.getPersistentValue('order') && it.id != link.id) {
                        it.order = it.order - 1
                        it.save()
                    }
                }
            }
        }
        if(link.isDirty('linkTo')){
            Screen oldLink = (Screen)link.getPersistentValue('linkTo')
            if(oldLink){
                oldLink.nbLinks--
                oldLink.save()
            }
            if(link.linkTo){
                link.linkTo.nbLinks++
                link.linkTo.save()
            }
        }
        if(!link.save())
            throw new RuntimeException()
    }

    def deleteLinkListItem(LinkListItem link){
        LinksListScreen screen = (LinksListScreen)link.linkScreen
        screen.links.findAll{ it.order > link.order }.each{
            it.order--
            it.save()
        }
        def linkTo = link.linkTo
        if (linkTo){
            linkTo.nbLinks--
            linkTo.save()
        }
        screen.removeFromLinks(link)
    }

    def delete(LinksListScreen screen){
        screen.links?.each{
            if(it.linkTo){
                it.linkTo.nbLinks--
                it.linkTo.save()
            }
        }
    }
}
