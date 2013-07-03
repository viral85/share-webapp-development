import com.smartpaper.domains.user.Role
import com.smartpaper.domains.user.User
import com.smartpaper.domains.user.UserRole
import com.smartpaper.domains.application.Application
import grails.util.GrailsUtil
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.apache.commons.io.FilenameUtils
import com.smartpaper.domains.media.Media

class BootStrap {

   def springSecurityService
   def grailsApplication
   def applicationService
   def screenService

   def init = { servletContext ->
        log.info("Init application...")
        if (GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT){
            def defaultAppPath = grailsApplication.config.smartpaper.baseDir.toString() + File.separator + 'applications'
            def dir = new File(defaultAppPath)
            //if (dir.exists())
            //    dir.deleteDir()
        }

        initFolders()
        initRoles()
        if(Application.count() == 0){
            createDefaultApplicationAndAdminUser()
        }
        loadStandardLibrary()

        if (GrailsUtil.environment == GrailsApplication.ENV_DEVELOPMENT){
            dummyData()
        }

        screenService.initializeFirefoxDrivers()
   }

   def destroy = {
        screenService.drivers?.each{
            it.close()
        }
   }

   private initFolders(){
        log.info("... init folders")

        def dirPath = grailsApplication.config.smartpaper.baseDir.toString() + File.separator
        println "basedir: "+dirPath

        def dir = new File(grailsApplication.config.smartpaper.recognition.data.toString())
        if (!dir.exists())
          dir.mkdirs()
        println "data train: "+dir.absolutePath

        dir = new File(grailsApplication.config.smartpaper.recognition.keypoints.toString())
        if (!dir.exists())
          dir.mkdirs()
        println "data keypoints: "+dir.absolutePath

       dir = new File(grailsApplication.config.smartpaper.recognition.descriptors.toString())
        if (!dir.exists())
          dir.mkdirs()
        println "data descriptors: "+dir.absolutePath
   }

   private initRoles(){
       ['ROLE_ADMIN', 'ROLE_USER'].each {
           if (Role.countByAuthority(it) == 0) {
               new Role(authority: it).save(flush: true, failOnError: true)
           }
       }
   }

   private createDefaultApplicationAndAdminUser(){
        log.info("... create Default Application")
        def admin = new User(
            firstname:'Admin',
            lastname:'Smartsystem',
            countryCode:'FR',
            username: 'admin@smartsystem.fr',
            enabled: true,
            password: 'password')
        admin.save(flush: true)

        UserRole.create admin, Role.findByAuthority('ROLE_USER'), true
        UserRole.create admin, Role.findByAuthority('ROLE_ADMIN'), true

        Application app = new Application(name:'default smartapp', owner:admin)
        applicationService.save(app,false)
        grailsApplication.config.smartpaper.defaultApp = app.id
   }

   private dummyData(){
       log.info("... create dummy data")
       if (User.countByUsername('vbarrier@kagilum.com') > 0) {
           return
       }
        def testUser = new User(
                firstname:'Vincent',
                lastname:'Barrier',
                countryCode:'FR',
                username: 'vbarrier@kagilum.com',
                enabled: true,
                password: 'password')
        testUser.save(flush: true, failOnError: true)
        UserRole.create testUser, Role.findByAuthority('ROLE_USER'), true
   }

    void loadStandardLibrary() {
        log.info("... load standard library")

        Application app = Application.get(grailsApplication.config.smartpaper.defaultApp as Long)
        if (!app) {
            log.error("Can't find default app (id = ${grailsApplication.config.smartpaper.defaultApp})")
            return
        }
        String defaultMediaPath = [grailsApplication.config.smartpaper.baseDir.toString(), 'applications', 'default', 'medias'].join(File.separator)
        File dir = new File(defaultMediaPath)
        if (!dir.exists()) {
            log.warn("Dir ${dir.absolutePath} doesn't exist")
            return
        }
        if (!dir.directory) {
            log.error("Path ${dir.absolutePath} isn't a directory")
            return
        }

        int processed = 0
        int added = 0

        dir.listFiles().each { File file ->
            processed++
            def ext = FilenameUtils.getExtension(file.name)
            def name = FilenameUtils.getBaseName(file.name)
            if (Media.countByApplicationAndNameAndExtensionAndSize(app, name, ext, file.length().intValue()) == 0) {
                try {
                    Media media = applicationService.addMedia(app, file.newInputStream(), file.name, false)
                    log.debug("File $file.name added as media ${media?.id}")
                    added++
                } catch (Throwable t) {
                    log.error("Can't add file $file.name to standard library", t)
                    t.printStackTrace()
                }
            }
        }

        log.info("Standard Library: $processed files processed, $added added")
    }
}