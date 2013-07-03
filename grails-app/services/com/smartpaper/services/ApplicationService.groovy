package com.smartpaper.services

import com.smartpaper.domains.application.Application
import com.smartpaper.domains.screen.ImageScreen
import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.media.ImageMedia
import javax.imageio.ImageIO
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.FileUtils
import com.smartpaper.domains.media.Media
import com.smartpaper.domains.application.Background
import com.smartpaper.domains.application.SocialLink
import com.smartpaper.domains.application.MenuItem
import com.smartpaper.domains.media.VideoMedia
import com.smartpaper.domains.media.AudioMedia

class ApplicationService {

    def socialLinks = []
    def openCVService
    def screenService
    def grailsApplication
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    def save(Application app, boolean withDummy){
        if(!app.save())
            throw new RuntimeException()

        def libPath = new File(app.getPathMedias())
        if (!libPath.exists()){
            libPath.mkdirs()
        }

        //Create a first screen to illustrate
        if(withDummy){
            def navBar = Media.findByApplicationAndName(Application.load(1),'navbar')
            app.backgroundHeader = new Background(image:navBar).save()
            Screen firstScreen = new ImageScreen(name:g.message(code:'screen.defaultName'))
            screenService.save(firstScreen,app)
        }
    }

    def delete(Application app){

        if(app.active)
            throw new IllegalStateException()

        app.backgroundHeader?.image = null
        app.logo = null
        app.delete(flush:true)

        def path = app.getRootPath()
        def file = new File(path)
        if (file.exists()){
            file.delete()
        }
    }

    def burningImageService

    def addMedia(Application application, InputStream uploadedFile, String originalFilename, boolean trigger) {
        def ext = FilenameUtils.getExtension(originalFilename)
        def name = FilenameUtils.getBaseName(originalFilename)
        try {

            if (trigger && application.triggers?.size() > 5){
                throw new RuntimeException('ui.application.error.max.triggers.reached')
            }

            def diskFile = null
            File tmp = File.createTempFile('grails', 'ajaxupload')
            tmp << uploadedFile
            def media = null

            if (ext in ['png','gif','jpeg','jpg']){
                def img = ImageIO.read(tmp)
                media = new ImageMedia(height: img.height, width:img.width, name:name, extension: ext, size:tmp.size(), triggerApp: trigger?:false)
            }else if (ext in ['mp4']){
                media = new VideoMedia(duration:'', name:name, extension: ext, size:tmp.size())
            }else if (ext in ['mp3']){
                media = new AudioMedia(duration:'', name:name, extension: ext, size:tmp.size())
            }

            if (media){
                application.addToMedias(media)
                media.save()
                diskFile = new File(application.pathMedias,"${media.id +'.'+ media.extension}")
                try {
                    FileUtils.moveFile(tmp,diskFile)
                } catch (IOException e) {
                    application.removeFromMedias(media)
                    media.delete()
                    log.error("Can't add new media to application: $e.message")
                    throw new RuntimeException(e)
                }
            }

            if(media instanceof ImageMedia){
                burningImageService.doWith(diskFile.absolutePath, application.pathMedias)
                .execute (media.id+'-57', {
                   it.scaleAccurate(57, 64)
                })
                .execute (media.id+'-125', {
                   it.scaleAccurate(125, 138)
                })
            }
            return media
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }

    def removeTrigger(ImageMedia media){
        try {
            def app = media.application
            def file = new File(media.getPath())
            if (file.exists())
                file.delete()
            file = new File(media.getPathThumbnail(null))
            if (file.exists())
                file.delete()
            app.removeFromTriggers(media)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }

    def removeMedia(Media media){
        def app = media.application
        if (media.used == 0){
            def file = new File(media.getPath())
            if (file.exists())
                file.delete()
            file = new File(media.getPathThumbnail(null))
            if (file.exists())
                file.delete()
            app.removeFromMedias(media)
        }else{
            throw new IllegalStateException()
        }
    }

    def resizeImage(ImageMedia media, width, height){
        burningImageService.doWith(media.path, media.application.pathMedias)
        .execute (media.id.toString(), {
           it.scaleAccurate(width, height)
        })
        media.width = width
        media.height = height
        media.save()
    }

    def saveSocialLink(Application app, SocialLink socialLink){
        socialLink.order = (app.socialLinks?.size()?:0) + 1
        app.addToSocialLinks(socialLink)
        if(!socialLink.save(flush:true))
            throw new RuntimeException()
    }

    def updateSocialLink(SocialLink socialLink){
        if (!socialLink.logo)
            socialLink.logo = ''
        if(!socialLink.save())
            throw new RuntimeException()
    }

    def deleteSocialLink(SocialLink socialLink){
        def app = socialLink.application
        app.socialLinks.findAll{ it.order > socialLink.order }.each{
            it.order--
            it.save()
        }
        app.removeFromSocialLinks(socialLink)
    }

    def saveMenuItem(Application app, MenuItem menuItem){
        menuItem.order = (app.menuItems?.size()?:0) + 1
        app.addToMenuItems(menuItem)
        if(!menuItem.save(flush:true))
            throw new RuntimeException()
    }

    def updateMenuItem(MenuItem menuItem){
        if(menuItem.isDirty('order')){
            if (menuItem.getPersistentValue('order') > menuItem.order) {
                menuItem.application.menuItems.each {it ->
                    if (it.order >= menuItem.order && it.order <= menuItem.getPersistentValue('order') && it.id != menuItem.id) {
                        it.order = it.order + 1
                        it.save()
                    }
                }
            } else {
                menuItem.application.menuItems.each {it ->
                    if (it.order <= menuItem.order && it.order >= menuItem.getPersistentValue('order') && it.id != menuItem.id) {
                        it.order = it.order - 1
                        it.save()
                    }
                }
            }
        }
        if(menuItem.isDirty('link')){
            Screen oldLink = (Screen)menuItem.getPersistentValue('link')
            if(oldLink){
                oldLink.nbLinks--
                oldLink.save()
            }
            if(menuItem.link){
                menuItem.link.nbLinks++
                menuItem.link.save()
            }
        }
        if(!menuItem.save())
            throw new RuntimeException()
    }

    def deleteMenuItem(MenuItem menuItem){
        def app = menuItem.application
        app.menuItems.findAll{ it.order > menuItem.order }.each{
            it.order--
            it.save()
        }
        def link = menuItem.link
        if (link){
            menuItem.link.nbLinks--
            menuItem.link.save()
        }
        app.removeFromMenuItems(menuItem)
    }

    def update(Application app){
        if(app.isDirty('hasMenu')){
            if(app.hasMenu){
                app.menuItems?.each{
                    if(it.link){
                        it.link.nbLinks++
                        it.link.save()
                    }
                }
            }else{
                app.menuItems?.each{
                    if(it.link){
                        it.link.nbLinks--
                        it.link.save()
                    }
                }
            }
        }
        if(!app.save()){
            throw new RuntimeException()
        }
    }
}

