package com.smartpaper.domains.screen

import com.smartpaper.domains.media.VideoMedia

class VideoScreen extends Screen {

    VideoMedia video

    public static int type = 5

    static constraints = {
        video nullable: true
    }

    static mapping = {
        cache true
		table 'sp_screen_video'
	}

    int getMediaUsed(def media){
        int used = super.getMediaUsed(media)
        if (this.video == media){
            used += 1
        }
        return used
    }
}
