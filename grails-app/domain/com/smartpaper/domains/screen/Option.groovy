package com.smartpaper.domains.screen

import com.smartpaper.domains.media.ImageMedia

class Option {

    ImageMedia icon
    ImageMedia iconActive
    Screen     link
    int order = 1

    static belongsTo = [
            screen:Screen
    ]

    static mapping = {
        cache true
        order column: '`orderNumber`'
        table 'sp_option'
    }

    static constraints = {
        icon nullable:true
        iconActive nullable:true
        link nullable:true
    }
}
