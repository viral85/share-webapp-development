package com.smartpaper.domains.screen

import com.smartpaper.domains.media.ImageMedia
import com.smartpaper.domains.application.Background

/**
 * 
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 15.08.12
 */
class Overlay {

    String content
    ImageMedia image
    Background background
    int x = 0
    int y = 0

    static belongsTo = [linkScreen: DetailedViewScreen]

    static constraints = {
        image(nullable: true)
        background(nullable: true)
        content(blank: true)
    }

    static mapping = {
        cache true
		table 'sp_overlay'
	}
}
