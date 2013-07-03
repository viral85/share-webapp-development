package com.smartpaper.services
import com.smartpaper.recognition.OpenCVUtils
import com.smartpaper.domains.application.Application
import com.smartpaper.domains.media.ImageMedia
import com.googlecode.javacv.cpp.opencv_core.CvMat
import com.smartpaper.recognition.ImageData
import org.codehaus.groovy.grails.commons.ApplicationHolder
import java.util.concurrent.Semaphore
import com.googlecode.javacv.cpp.opencv_features2d.KeyPoint
import javax.imageio.ImageIO
import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.InitializingBean

class OpenCVService implements InitializingBean {

    static transactional = true
    def burningImageService
    static int MAX_WIDTH = 320
    static int MAX_HEIGHT = 255

    private final Semaphore semaphoreOpencv = new Semaphore(1)

    OpenCVUtils openCVUtils

    Application findApplication(File query) {
        semaphoreOpencv.acquire()
        try{
            def image = ImageIO.read(query)

            if(image.width > MAX_WIDTH || image.height > MAX_HEIGHT){
                burningImageService.doWith(query.getAbsolutePath(), query.getParentFile().getAbsolutePath())
                   .execute {
                       if (image.width > MAX_WIDTH)
                            it.scaleApproximate(MAX_WIDTH, (MAX_WIDTH * image.height / image.width).toInteger())
                       else
                            it.scaleApproximate((MAX_HEIGHT * image.width / image.height).toInteger(), MAX_HEIGHT)
                    }
            }

            Integer idx = openCVUtils.findSimilar(query.getAbsolutePath())
            if(idx){
                ImageMedia declencheur = ImageMedia.findByIdAndTriggerApp(idx,true)
                if(declencheur?.application && declencheur.application.active){
                    return declencheur.application
                }else{
                    return null
                }
            }else{
                return null
            }
        }finally {
            semaphoreOpencv.release()
        }
    }

    def addApplication(Application app){
        def triggers = app.triggers
        if (!app.triggers || !app.screens){
            return false
        }else{
            app.openCvInProgress = true
            app.save(flush:true)
        }
        runAsync {
            app = Application.get(app.id)
            addApplicationTriggers(app,triggers)
        }
        return true
    }

    def removeApplication(Application app){
        app.openCvInProgress = true
        app.save(flush:true)
        def triggers = app.triggers
        runAsync {
            app = Application.get(app.id)
            removeApplicationTriggers(app,triggers)
        }
        return true
    }

    def fullReloadDatabase(){
        try{
            semaphoreOpencv.acquire()
            ArrayList<CvMat> descriptorsImages = new ArrayList<CvMat>();
            ArrayList<ImageData> idx = new ArrayList<ImageData>();
            int i = 0
            Application.findAllByActive(true)?.each { Application it ->
                it.triggers?.each {ImageMedia trig ->
                    def path = resizeTriggerIfNecessary(trig)
                    CvMat image = OpenCVUtils.readImage(path)
                    KeyPoint imageKp = OpenCVUtils.detectKeyPoints(image,openCVUtils.getKeypointsPathForIdx(trig.id.toInteger()))
                    CvMat desc = OpenCVUtils.computeDescriptors(image,imageKp,openCVUtils.getDescriptorsPathForIdx(trig.id.toInteger()))
                    try{
                        if (desc.size() != 0){
                            descriptorsImages.add(desc)
                            idx.add(new ImageData(trig.id.toInteger(), trig.width, trig.height))
                            log.debug(i+" added image :"+trig.id+" appid: "+it.id)
                            i++
                        }
                    }catch(Exception e){
                        cleanCorruptedImage(trig)
                    }
                }
            }
            openCVUtils.reloadTrainDescriptors(descriptorsImages, idx)
            openCVUtils.writeTrainDescriptors(ApplicationHolder.getApplication().config.smartpaper.recognition.data+'/data.train')
        }finally{
            semaphoreOpencv.release()
        }
    }

    private removeApplicationTriggers(Application app, def triggers){
        try{
            semaphoreOpencv.acquire()
            if (log.debugEnabled){
                log.debug("Removing app ${app.id} with ${triggers.size()}")
            }
            app.triggers?.each{
                def kp = new File(openCVUtils.getKeypointsPathForIdx(it.id.toInteger()))
                if (kp.exists())
                    kp.delete()
                def desc = new File(openCVUtils.getDescriptorsPathForIdx(it.id.toInteger()))
                if (desc.exists())
                    desc.delete()
            }
            app.active = false
            app.openCvInProgress = false
            app.save(flush:true)
            reloadDatabase()
            if (log.debugEnabled){
                log.debug("App ${app.id} with ${triggers.size()} has been unpublished")
            }
        }finally {
            semaphoreOpencv.release()
        }
    }

    private addApplicationTriggers(Application app, def triggers){
        try{
            semaphoreOpencv.acquire()
            if (log.debugEnabled){
                log.debug("Adding App ${app.id} with ${triggers.size()}")
            }
            def trigAdded = 0
            triggers?.each{ImageMedia trig ->
                def path = resizeTriggerIfNecessary(trig)
                def added = openCVUtils.addImageToTrainDescriptors(path,trig.id.toInteger())
                if (!added){
                    cleanCorruptedImage(trig)
                }else{
                    trigAdded++
                }
            }
            app.openCvInProgress = false
            if (trigAdded){
                openCVUtils.reloadTrainDescriptors()
                app.active = true
                app.lastPublish = new Date()
            }
            app.save(flush:true)
            if (log.debugEnabled){
                log.debug("App ${app.id} with ${trigAdded} has been published")
            }
        }finally {
            semaphoreOpencv.release()
        }
    }

    private reloadDatabase() {
        ArrayList<CvMat> descriptorsImages = new ArrayList<CvMat>();
        ArrayList<ImageData> idx = new ArrayList<ImageData>();
        Application.findAllByActive(true)?.each { Application it ->
            it.triggers?.each {ImageMedia trig ->
                CvMat desc = openCVUtils.retrieveDescriptors(trig.id.toInteger())
                try{
                    if (desc.size() != 0){
                        descriptorsImages.add(desc)
                        idx.add(new ImageData(trig.id.toInteger(), trig.width, trig.height))
                        log.debug("added image :"+trig.id+" appid: "+it.id)
                    }
                }catch(Exception e){
                    cleanCorruptedImage(trig)
                }
            }
        }
        openCVUtils.reloadTrainDescriptors(descriptorsImages, idx)
        openCVUtils.writeTrainDescriptors(ApplicationHolder.getApplication().config.smartpaper.recognition.data+'/data.train')
    }

    private void cleanCorruptedImage(ImageMedia image){
        def file = new File(openCVUtils.getDescriptorsPathForIdx(image.id.toInteger()))
        if (file.exists()){
            file.delete()
        }
        file = new File(openCVUtils.getKeypointsPathForIdx(image.id.toInteger()))
        if (file.exists()){
            file.delete()
        }
        image.delete()
        log.debug(image.name+" corrupted (id: "+image.id+", app:"+image.application.name+", app-id:"+image.application.id+")")
    }

    private String resizeTriggerIfNecessary(ImageMedia trigger){
        def file = new File(trigger.triggerPath)
        if(!file.exists() && (trigger.width > MAX_WIDTH || trigger.height > MAX_HEIGHT)){
            burningImageService.doWith(trigger.path, trigger.application.pathMedias)
               .execute(trigger.id+'-trig') {
                   if (trigger.width > MAX_WIDTH)
                        it.scaleApproximate(MAX_WIDTH, (MAX_WIDTH * trigger.height / trigger.width).toInteger())
                   else
                        it.scaleApproximate((MAX_HEIGHT * trigger.width / trigger.height).toInteger(), MAX_HEIGHT)
                }
            return file.getAbsolutePath()
        }
        return trigger.path
    }

    @Override
    void afterPropertiesSet() {
        def config = ApplicationHolder.application.config.smartpaper
        try {
            openCVUtils = new OpenCVUtils(config.recognition.data+'/data.train', config.recognition.keypoints,config.recognition.descriptors)
        } catch (UnsatisfiedLinkError e) {
            log.error("!!!! Can't initialize OpenCV")
            log.error("!!!! message: $e.message")
            log.error("!!!! Stacktrace:", e)
        }
    }
}
