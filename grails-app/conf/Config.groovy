smartpaper.baseDir = new File(System.getProperty('user.home'), appName).canonicalPath
smartpaper.recognition.data=smartpaper.baseDir+File.separator+"data"
smartpaper.recognition.keypoints=smartpaper.baseDir+File.separator+"data/keypoints"
smartpaper.recognition.descriptors=smartpaper.baseDir+File.separator+"data/descriptors"

environments {
    production {
        grails.serverURL = "http://ns223478.ovh.net"
    }
    development {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    test {
        grails.serverURL = "http://localhost:8080/${appName}"
    }
    awsproduction {
        grails.serverURL = "http://generator.smartsy.us"
        smartpaper.baseDir = '/home/smartpaper/generator'
        smartpaper.recognition.data=smartpaper.baseDir+File.separator+"data"
        smartpaper.recognition.keypoints=smartpaper.baseDir+File.separator+"data/keypoints"
        smartpaper.recognition.descriptors=smartpaper.baseDir+File.separator+"data/descriptors"
    }
}

smartpaper.guide="http://smartsy.us/index.php/user-guide"
smartpaper.contact="http://new.smartsy.fr/index.php/contact"
smartpaper.debug=true
smartpaper.vw=2
smartpaper.sp=3
smartpaper.defaultApp=1
smartpaper.email="info@smartsy.us"

grails {
   mail {
     host = "smtp.gmail.com"
     port = 465
//     username = "barrier.vincent@gmail.com"
//     password = "bahz4lol\$"
     username = "info@smartsy.us"
     password = "Smartsy.01!"
     props = ["mail.smtp.auth":"true",
              "mail.smtp.socketFactory.port":"465",
              "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
              "mail.smtp.socketFactory.fallback":"false"]

} }

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.config.locations = [ "classpath:${appName}-config.properties",
                             "classpath:${appName}-config.groovy",
                             "file:${userHome}/.grails/${appName}-config.properties",
                             "file:${userHome}/.grails/${appName}-config.groovy"]

 if(System.properties["${appName}.config.location"]) {
    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}

grails.views.javascript.library="jquery"
grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']


// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
        console name:'stdout', layout:pattern(conversionPattern: '%d{ABSOLUTE} %5p %c{1}:%L - %m%n')

        environments {
            awsproduction {
                rollingFile name: "stacktrace", maxFileSize: 1024,
                            file: "/var/log/smartpaper/stacktrace2.log"
            }
        }
    }

    root {
        info 'stdout'
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'

    error stdout: "StackTrace"

    debug 'grails.app.services.com.smartpaper'
    debug 'grails.app.controllers.com.smartpaper'
    debug 'grails.app.domain.com.smartpaper'
    debug 'grails.app.com.smartpaper'
    debug 'com.smartpaper'
}

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.smartpaper.domains.user.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.smartpaper.domains.user.UserRole'
grails.plugins.springsecurity.authority.className = 'com.smartpaper.domains.user.Role'
grails.plugins.springsecurity.auth.loginFormUrl = '/login'
grails.plugins.springsecurity.adh.errorPage = '/denied'
