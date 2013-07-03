package com.smartpaper.services.screen

import com.smartpaper.domains.screen.AudioScreen
import com.smartpaper.domains.media.AudioMedia

class AudioService {

    static transactional = true

    def updateAudio(AudioScreen screen, AudioMedia media){
        screen.audio = media
        if (!screen.save())
             throw new RuntimeException()
    }
}
