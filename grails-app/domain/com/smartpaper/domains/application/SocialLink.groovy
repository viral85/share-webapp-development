package com.smartpaper.domains.application

class SocialLink {

    String name
    String logo
    String url
    int order = 1

    static belongsTo = [
            application:Application
    ]

    static constraints = {
        name blank: false
        logo nullable:true
        url blank: false
    }

    static mapping = {
        cache true
        order column: '`orderNumber`'
        table 'sp_social_link'
    }
}
