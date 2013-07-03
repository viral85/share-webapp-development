package com.smartpaper.domains.media

class ImageMedia extends Media {

    int width
    int height
    boolean triggerApp = false

    static transients = ['triggerPath']

    static mapping = {
        cache true
        table 'sp_media_image'
    }

    def getTriggerPath(){
        return this.application.pathMedias+this.id+'-trig.'+this.extension
    }

    def beforeDelete(){
        if(this.application){
            def file = new File(this.triggerPath)
            if (file.exists())
                file.delete()
        }
    }
}
