package com.smartpaper.domains.screen

import com.smartpaper.domains.media.ImageMedia

class ImageScreen extends Screen {

    public static int type = 2

    ImageMedia image

    static constraints = {
        image nullable:true
    }

    static mapping = {
        cache true
		table 'sp_screen_image'
	}

    int getMediaUsed(def media){
        int used = super.getMediaUsed(media)
        if (this.image == media){
            used += 1
        }
        return used
    }
}
