package com.smartpaper.services.screen

import com.smartpaper.domains.screen.MediaListItem
import com.smartpaper.domains.screen.MediasListScreen
import com.smartpaper.domains.media.Media

class MediasListService {

    static transactional = true

    def addItem(MediasListScreen screen, Media media) {
        MediaListItem mediaListItem = new MediaListItem()
        mediaListItem.media = media
        mediaListItem.order = (screen.medias?.size()?:0) + 1
        screen.addToMedias(mediaListItem)
        if(!screen.save(flush:true))
            throw new RuntimeException()
        return mediaListItem
    }


    def updateItem(MediaListItem item) {
        if(item.isDirty('order')){
            if (item.getPersistentValue('order') > item.order) {
                item.linkScreen.medias.each {it ->
                    if (it.order >= item.order && it.order <= item.getPersistentValue('order') && it.id != item.id) {
                        it.order = it.order + 1
                        it.save()
                    }
                }
            } else {
                item.linkScreen.medias.each {it ->
                    if (it.order <= item.order && it.order >= item.getPersistentValue('order') && it.id != item.id) {
                        it.order = it.order - 1
                        it.save()
                    }
                }
            }
        }
        if(!item.save())
            throw new RuntimeException()
    }


    def removeItem(MediaListItem item) {
        MediasListScreen screen = (MediasListScreen)item.linkScreen
        screen.medias.findAll{ it.order > item.order }.each{
            it.order--
            it.save()
        }
        screen.removeFromMedias(item)
        if(!screen.save(flush:true))
            throw new RuntimeException()
    }
}
