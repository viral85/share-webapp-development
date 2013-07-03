package com.smartpaper.domains.media

class AudioMedia extends Media{

    String duration

    static mapping = {
        cache true
        table 'sp_media_audio'
    }

    String getThumbnail(def size){
        return null
    }
}
