package com.smartpaper.domains.screen

class WebViewScreen extends Screen {

    public static int type = 8

    String content = ""

    static constraints = {
        content nullable: false, blank:true
    }

    static mapping = {
        cache true
        content type: "text"
		table 'sp_screen_webview'
    }
}
