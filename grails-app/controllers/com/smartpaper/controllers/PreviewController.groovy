package com.smartpaper.controllers

import com.smartpaper.domains.application.Application
import com.smartpaper.domains.screen.Screen
import grails.plugins.springsecurity.Secured
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils

@Secured(['IS_AUTHENTICATED_ANONYMOUSLY'])
class PreviewController {

    def index() {}
        def showPreview= {
            def config = SpringSecurityUtils.securityConfig
            String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
            [appId: params.application, postUrl: postUrl, rememberMeParameter: config.rememberMe.parameter]
    }
    def shareAppPreview = {
        withApplication { application ->
            Screen screen = application.screens.find{ it.home } ?: application.screens?.asList()?.first() ?: null
            if (screen){
                 render(status:200, template:'outerPreview', model:[homeScreen:screen.id,application:application.id])
            }
        }
    }

    private def withApplication(String id = 'application', Closure c) {
        def application = request."$id" ?: Application.get(params."$id") ?: null
        if(application) {
            c.call application
        } else {
            redirect action:"home"
        }
    }

    def previewOuter = {
        def application =  Application.get(params.appId.toLong())
        if(request.method != 'GET'){
            render(status: 405)
            return
        }
        request.application = application
        withScreen{ screen ->
            request.screen = screen
            render(view: getTemplateScreen(screen,'preview',true), model:[screen:screen])
        }
    }
    private def withScreen(id="id", Closure c) {
        def screen = Screen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
    private String getTemplateScreen(def screen, def template, def forPhone, String templatePrefix = ''){
        def controllerName = GrailsClassUtils.getShortName(screen.class).substring(0,1).toLowerCase() + GrailsClassUtils.getShortName(screen.class).substring(1)
        def folderView = template in ['options', 'title'] ? '/screen/' : '/'+(controllerName - 'Screen') + (forPhone ? '/iPhone' : '')
        String templateScreen = "${folderView}/${templatePrefix}${template in ['options', 'title', 'content'] ? template : (template ?:'content')}"
        return templateScreen
    }
}
