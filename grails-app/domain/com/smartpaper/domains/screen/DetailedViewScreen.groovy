package com.smartpaper.domains.screen

import com.smartpaper.domains.media.ImageMedia
import com.smartpaper.domains.application.Background

class DetailedViewScreen extends Screen {

    public static int type = 7
    static hasMany = [overlays: Overlay]

    static constraints = {
    }

    static mapping = {
        cache true
		table 'sp_screen_detailed_view'
	}

    int getMediaUsed(def media){
        int used = super.getMediaUsed(media)
        overlays?.each{ it ->
            if(it.image == media)
                used += 1
        }
        if (background) {
            used++
        }
        return used
    }

}
