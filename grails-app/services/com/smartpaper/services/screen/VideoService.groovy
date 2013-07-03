package com.smartpaper.services.screen

import com.smartpaper.domains.screen.VideoScreen
import com.smartpaper.domains.media.VideoMedia

class VideoService {

    static transactional = true

    def updateVideo(VideoScreen screen, VideoMedia media){
        screen.video = media
        if (!screen.save())
             throw new RuntimeException()
    }
}
