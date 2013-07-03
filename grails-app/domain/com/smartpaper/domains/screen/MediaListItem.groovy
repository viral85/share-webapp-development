package com.smartpaper.domains.screen

import com.smartpaper.domains.media.Media

class MediaListItem {

    Media media
    int order

    static belongsTo = [
            linkScreen:Screen
    ]

    static constraints = {
    }

    static mapping = {
        cache true
        order column: '`orderNumber`'
        table 'sp_media_list_item'
    }
}
