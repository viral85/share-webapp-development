package com.smartpaper.domains.media

class VideoMedia extends Media{

    String duration

    static mapping = {
        cache true
        table 'sp_media_video'
    }

    String getThumbnail(def size){
        return null
    }
}
