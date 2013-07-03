package com.smartpaper.domains.screen

import com.smartpaper.domains.media.AudioMedia

class AudioScreen extends Screen {

    AudioMedia audio

    public static int type = 6

    static constraints = {
        audio nullable: true
    }

    static mapping = {
        cache true
		table 'sp_screen_audio'
	}

    int getMediaUsed(def media){
        int used = super.getMediaUsed(media)
        if (this.audio == media){
            used += 1
        }
        return used
    }
}
