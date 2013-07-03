package com.smartpaper.domains.screen

class LinksListScreen extends Screen {

    public static int type = 3

    String police = "Arial"
    int fontSize = 14
    String fontColor = "#000000"
    int itemHeight = 44
    String alpha = "1.0"
    String bgcell = "transparent"

    static hasMany = [
            links:LinkListItem
    ]

    static mappedBy = [
            links:'linkScreen',
            options:'screen'
    ]

    static constraints = {
        police blank:false
        fontColor blank:false
    }

    static mapping = {
        cache true
		table 'sp_screen_links_list'
        links sort:'order', order:'asc'
        links cascade:"all-delete-orphan"
	}

    int getMediaUsed(def media){
        int used = super.getMediaUsed(media)
        links?.each{ it ->
            if(it.image == media)
                used += 1
        }
        return used
    }
}
