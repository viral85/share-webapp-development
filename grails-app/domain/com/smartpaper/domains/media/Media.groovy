package com.smartpaper.domains.media

import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import com.smartpaper.domains.application.Application
import org.codehaus.groovy.grails.commons.ApplicationHolder

class Media {

    transient grailsApplication
    String name
    String extension
    int    size
    Date dateCreated
    Date lastUpdated

    static belongsTo = [
            application:Application
    ]

    static transients = ['thumbnail','url','path','pathThumbnail','filename','used']

    static constraints = {
    }

    static mapping = {
        cache true
        table 'sp_media'
        tablePerHierarchy false
    }

    String getThumbnail(def size){
        size = size ?: 57
        return this.application.urlBaseMedias+this.id+'-'+size+'.'+this.extension
    }

    String getUrl(){
        return this.application.urlBaseMedias+this.id+'.'+this.extension
    }

    String getPath(){
        return this.application.pathMedias+this.id+'.'+this.extension
    }

    String getPathThumbnail(def size){
        size = size ?: 57
        return this.application.pathMedias+this.id+'-'+size+'.'+this.extension
    }

    String getFilename(){
        return this.name+'.'+this.extension
    }

    static namedQueries = {
        findInStandardAndAppLibrary {app, id ->
            application{
                or {
                    eq 'id', app
                    eq 'id', ApplicationHolder.application.config.smartpaper.defaultApp.toLong()
                }
            }
            eq 'id', id
            maxResults(1)
        }
    }

    int getUsed(){
        int used = 0
        application.screens?.each{
            used += it.getMediaUsed(this)
        }
        return used
    }

    def beforeDelete(){
        if(this.application){
            def file = new File(this.getPath())
            if (file.exists())
                file.delete()
            file = new File(this.getPathThumbnail(null))
            if (file.exists())
                file.delete()
            file = new File(this.getPathThumbnail(125))
            if (file.exists())
                file.delete()
        }
    }
}
