package com.smartpaper.domains.screen

import com.smartpaper.domains.media.ImageMedia
import com.smartpaper.domains.media.Media

class MediasListScreen extends Screen {

   public static int type = 4

   public static final int ARROWS_ALWAYS = 1
   public static final int ARROWS_ON_TOUCH = 2
   public static final int ARROWS_NEVER = 3

   ImageMedia leftArrow
   ImageMedia rightArrow

   static hasMany = [
           medias:MediaListItem
   ]

   static mappedBy = [
            medias:'linkScreen',
            options:'screen'
   ]

   int arrowsDisplay = 1


   static mapping = {
        cache true
		table 'sp_screen_images_list'
        medias sort:'order', order:'asc'
        medias cascade:"all-delete-orphan"
   }

   static constraints = {
        leftArrow nullable:true
        rightArrow nullable:true
   }

   int getMediaUsed(def media){
        int used = super.getMediaUsed(media)
        if (this.leftArrow == media){
            used += 1
        }
        if (this.rightArrow == media){
            used += 1
        }
        if (this.medias.contains(media)){
            used += 1
        }
        return used
    }
}
