package com.smartpaper.services.screen

import com.smartpaper.domains.application.Application
import com.smartpaper.domains.screen.ImageScreen
import com.smartpaper.domains.screen.Screen
import com.smartpaper.domains.application.Background
import com.smartpaper.domains.screen.Option

import org.openqa.selenium.firefox.FirefoxDriver
import com.smartpaper.domains.screen.VideoScreen
import com.smartpaper.domains.screen.LinksListScreen

import com.smartpaper.domains.screen.LinkListItem
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import java.util.concurrent.Semaphore
import org.apache.ivy.util.StringUtils
import org.openqa.selenium.OutputType

class ScreenService {

    ArrayList<FirefoxDriver> drivers
    int firefoxDriversInstances
    def grailsApplication
    def burningImageService
    Semaphore screenshotSemaphore
    static transactional = true

    def save(def screen, Application application) {
        screen.order = (application.screens?.size()?:0) + 1

        screen.background = new Background().save()
        screen.backgroundTitle = new Background().save()
        screen.backgroundOptions = new Background().save()

        if (screen.order == 1){
            screen.home = true
        }
        application.addToScreens(screen)
        if (!screen.save()){
             throw new RuntimeException()
        }

        return screen
    }

    def update(Screen screen){
        if(screen.isDirty('hasOptions')){
            if(screen.hasOptions){
                screen.options?.each{
                    if(it.link){
                        it.link.nbLinks++
                        it.link.save()
                    }
                }
            }else{
                screen.options?.each{
                    if(it.link){
                        it.link.nbLinks--
                        it.link.save()
                    }
                }
            }
        }
        if(!screen.save())
            throw new RuntimeException()
    }

    def delete(def screen){

        if (screen.nbLinks)
            throw new IllegalStateException()

        def application = screen.application
        application.screens.findAll{ it.order > screen.order }.each{
            it.order--
            it.save()
        }
        screen.options?.each{
            it.link.nbLinks--
            it.link.save()
        }
        if (screen.class != Screen.class) {
            def serviceName = GrailsClassUtils.getShortName(screen.class).substring(0,1).toLowerCase() + GrailsClassUtils.getShortName(screen.class).substring(1) - 'Screen'
            if(grailsApplication.getMainContext().containsBean(serviceName+'Service'))
                if(grailsApplication.getMainContext().getBean(serviceName+'Service').metaClass.methods.find{it.name == 'delete'})
                    grailsApplication.getMainContext().getBean(serviceName+'Service').delete(screen)
        }

        if (screen.home){
            if (screen.application.screens.size() > 1){
                screen.application.screens.find{ it.id != screen.id }.home = true
            }
        }

        application.removeFromScreens(screen)

        screen.delete()
        application.save()
    }

    boolean updateOrder(Screen screen, int order) {
        if (screen.order != order) {
            if (screen.order > order) {
                screen.application.screens?.sort()?.each {it ->
                    if (it.order >= order && it.order <= screen.order && it != screen) {
                        it.order = it.order + 1
                        it.save()
                    }
                }
            } else {
                screen.application.screens?.sort()?.each {it ->
                    if (it.order <= order && it.order >= screen.order && it != screen) {
                        it.order = it.order - 1
                        it.save()
                    }
                }
            }
            screen.order = order
            return screen.save() ? true : false
        } else {
            return false
        }
    }

    def setHome(def screen){
        def oldHome = Screen.findByHome(true)
        if (oldHome) {
            oldHome.home = false
            if(!oldHome.save())
                throw new RuntimeException()
        }
        screen.home = true
        if(!screen.save())
            throw new RuntimeException()
    }

    def saveOption(Screen screen){
        def option = new Option()
        option.order = (screen.options?.size()?:0) + 1
        screen.addToOptions(option)
        if(!screen.save(flush:true))
            throw new RuntimeException()
        return option
    }

    def updateOption(Option option){
        if(option.isDirty('link')){
            Screen oldLink = (Screen)option.getPersistentValue('link')
            if(oldLink){
                oldLink.nbLinks--
                oldLink.save()
            }
            if(option.link){
                option.link?.nbLinks++
                option.link?.save()
            }
        }
        if(!option.save())
            throw new RuntimeException()
    }

    def deleteOption(Option option){
        def screen = option.screen
        screen.options.findAll{ it.order > option.order }.each{
            it.order--
            it.save()
        }
        def link = option.link
        if (link){
            option.link.nbLinks--
            option.link.save()
        }
        screen.removeFromOptions(option)
    }

    def initializeFirefoxDrivers(){
        drivers = new ArrayList<FirefoxDriver>()
        for(int i = 0; i < firefoxDriversInstances; i++){
            drivers.add(new FirefoxDriver())
        }
        screenshotSemaphore = new Semaphore(firefoxDriversInstances)
    }

    def createScreenShot(Screen screen){
        runAsync {
            log.debug("Create screenshot for screen $screen.id")
            try{
                screenshotSemaphore.acquire()
                FirefoxDriver driver =  drivers.get(screenshotSemaphore.availablePermits())
                driver.get(grailsApplication.config.grails.serverURL+'/'+'screen'+'/'+'screenshot'+'/'+screen.id)
                //def img = File.createTempFile('temp-'+screen.id,'.png')
                File img = driver.getScreenshotAs(OutputType.FILE)
                //driver.saveScreenshot(img)
                burningImageService.doWith(img.absolutePath, screen.application.pathMedias)
                           .execute('screen-'+screen.id+'-125'){
                               it.crop(0,0,320,460)
                               it.scaleAccurate(125, 138)
                           }
                            .execute('screen-'+screen.id+'-57'){
                               it.crop(0,0,320,460)
                               it.scaleAccurate(57, 63)
                           }
                img.deleteOnExit()
                log.debug("Screenshot for screen $screen.id has been created")
            }catch(Exception e){
                log.warn("Cannot create screenshot for screen $screen.id", e)
            }finally {
                screenshotSemaphore.release()
            }
        }
    }

    def duplicate(screen){
        //Our target instance for the instance we want to clone
        def newScreen = screen.getClass().newInstance()
        newScreen.properties = screen.properties
        newScreen.background = new Background(screen.background.properties).save()
        newScreen.backgroundOptions = new Background(screen.backgroundOptions.properties).save()
        newScreen.nbLinks = 0
        newScreen.order = screen.application.screens?.size() + 1
        screen.application.addToScreens(newScreen)
        newScreen.save()
        screen.options?.each{
            def opt = new Option(it.properties)
            opt.link = newScreen
        }
        return newScreen
    }
}
