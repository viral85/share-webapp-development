grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        ebr()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    inherits("global") {
        excludes "xml-apis",
            'commons-logging',
            'slf4j-to-log4j'
    }

    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
        compile('org.seleniumhq.selenium:selenium-java:2.25.0') {
        }
        runtime 'mysql:mysql-connector-java:5.1.17'

        //compile 'org.slf4j:log4j-over-slf4j:1.6.2'
    }
}


grails.plugin.location.'smartpaper-recognition' = [
        '../SmartpaperRecognition',
        '../Recognition'
].find { File dir = new File(it); return dir.exists() }