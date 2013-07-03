package com.smartpaper.domains.application

import com.smartpaper.domains.media.ImageMedia

class Background {

    String color = '#FFFFFF'
    ImageMedia image
    int height = 46

    static constraints = {
        image(nullable:true)
    }

    static mapping = {
        cache true
        table 'sp_background'
    }
}
