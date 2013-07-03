package com.smartpaper.domains.application

import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.user.User
import com.smartpaper.domains.media.Media
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import com.smartpaper.domains.media.ImageMedia
import org.codehaus.groovy.grails.commons.GrailsClassUtils

class Application {

    transient grailsApplication

    String name
    boolean active = false
    ImageMedia logo
    Background backgroundHeader
    Date lastPublish
    boolean openCvInProgress = false

    boolean hasMenu = false

    Date dateCreated
    Date lastUpdated

    static hasMany = [
            screens : Screen,
            socialLinks:SocialLink,
            menuItems:MenuItem,
            medias:Media
    ]

    static belongsTo = [
            owner : User
    ]

    static transients = ['thumbnail','pathMedias','size','urlBaseMedias','triggers','rootPath']

    static constraints = {
        name blank: false, size:5..20
        logo nullable:true
        lastPublish nullable: true
        backgroundHeader nullable:true
    }

    static mapping = {
        cache true
        table 'sp_application'
        screens sort:'order', order:'asc'
        socialLinks sort:'order', order:'asc'
        menuItems sort:'order', order:'asc'
        screens cascade:"all-delete-orphan"
        medias cascade:"all-delete-orphan"
        socialLinks cascade:"all-delete-orphan"
        menuItems cascade:"all-delete-orphan"
    }

    def getThumbnail(def size){
        size = size ?: 57
        if (this.screens && !this.screens.empty) {
            return this.screens.find{ it.home }?.getThumbnail(size) ?: this.screens.asList().first().getThumbnail(size)
        }
        return null
    }

    def getSize(){
        int size = 0
        this.medias?.each{
            size += it.size
        }
        return size
    }

    String getPathMedias() {
        return grailsApplication.config.smartpaper.baseDir+File.separator+'applications'+File.separator+this.id+File.separator+'medias'+File.separator
    }

    String getRootPath() {
        return grailsApplication.config.smartpaper.baseDir+File.separator+'applications'+File.separator+this.id+File.separator
    }

    String getUrlBaseMedias() {
        return [grailsApplication.config.grails.serverURL, 'resources', this.id.toString(), 'medias'].join('/') + '/'
    }

    def beforeDelete() {
        this.backgroundHeader?.delete()
        this.logo?.delete()
    }

    def getTriggers() {
        return this.medias?.findAll { GrailsClassUtils.getShortName(it.class) == 'ImageMedia' && it.triggerApp }
    }
}
