package com.smartpaper.domains.screen

import com.smartpaper.domains.media.ImageMedia

class LinkListItem {

    ImageMedia image
    Screen linkTo
    String name
    int order = 1

    static belongsTo = [
            linkScreen:Screen
    ]

    static mapping = {
        cache true
        order column: '`orderNumber`'
        table 'sp_list_link_item'
    }

    static constraints = {
        image nullable:true
        linkTo nullable:true
        name nullable:true
    }
}
