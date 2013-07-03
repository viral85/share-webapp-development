package com.smartpaper.domains.application

import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.media.ImageMedia

class MenuItem {

    String name = 'menu'
    ImageMedia icon
    int order = 1
    Screen link

    static belongsTo = [
            application: Application
    ]

    static constraints = {
        icon blank:true, nullable:true
        link nullable:true
    }

    static mapping = {
        cache true
        order column: '`orderNumber`'
        table 'sp_application_menu_items'
    }
}
