package com.smartpaper.services.screen

import com.smartpaper.domains.screen.ImageScreen
import com.smartpaper.domains.media.ImageMedia

class ImageService {

    static transactional = true

    def updateImage(ImageScreen screen, ImageMedia media){
        screen.image = media
        if (!screen.save())
             throw new RuntimeException()
    }
}
