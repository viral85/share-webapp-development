package com.smartpaper.domains.screen

import com.smartpaper.domains.application.Application
import com.smartpaper.domains.media.ImageMedia
import com.smartpaper.domains.application.Background
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication

class Screen {

    transient grailsApplication
    transient screenService

    String name
    boolean home = false
    int order = 1
    Date dateCreated
    Date lastUpdated
    Background background

    String contentBgColor = "#000000"

    boolean hasTitle = false
    Background backgroundTitle
    String contentTitle

    boolean hasOptions = false
    Background backgroundOptions

    int nbLinks = 0

    static transients = [ "thumbnail", "pathThumbnail" ]

    static belongsTo = [
            application:Application
    ]

    static hasMany = [
            options:Option
    ]

    static mappedBy = [
            options:'screen'
    ]

    static constraints = {
        name blank:false
        contentTitle nullable: true
    }

    static mapping = {
        cache true
        order column: '`orderNumber`'
        options sort:'order', order:'asc', cache: true
        table 'sp_screen'
        tablePerHierarchy false
        options cascade:"all-delete-orphan"
        contentTitle type: "text"
    }

    int getMediaUsed(def media){
        int used = 0
        if(this.backgroundTitle.image == media){
            used++
        }
        if(this.backgroundOptions.image == media){
            used++
        }
        return used
    }

    def getThumbnail(def size){
        size = size ?: 125
        def thumName = 'screen-'+this.id+'-'+size+'.png'
        if (this.getPathThumbnail(size))
            return this.application.urlBaseMedias+thumName
        else
            return null
    }

    def getPathThumbnail(def size){
        size = size ?: 125
        def thumName = 'screen-'+this.id+'-'+size+'.png'
        if (new File(this.application.pathMedias+thumName).exists())
            return this.application.pathMedias+thumName
        else
            return null
    }

    def beforeDelete() {
        this.backgroundOptions.delete()
        this.backgroundTitle.delete()
        this.background?.delete()
    }
}
