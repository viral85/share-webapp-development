package com.smartpaper.controllers.screen

import grails.plugins.springsecurity.Secured
import com.smartpaper.domains.screen.FormScreen
import com.smartpaper.domains.screen.InputField
import grails.converters.JSON

@Secured('ROLE_USER')
class FormController {

    def formService
    def screenService

    def addInput = {
        withScreen { FormScreen screen ->
            try{
                formService.saveInputField(screen)
                screenService.createScreenShot(screen)
                render(status:200, template: 'table', model:[screen:screen,app:screen.application])
            }catch(RuntimeException e){
                if (log.debugEnabled)
                    log.debug(e.getMessage())
                render(status:400, contentType:'application/json', text:[error:'error'] as JSON)
            }
        }
    }

    def updateInput = {
        withInputField {InputField input ->
            try{
                if(params.input){
                    input.properties = params.input
                }
                formService.updateInputField(input)
                screenService.createScreenShot(input.form)
                render(status:200, template: 'table', model:[screen:input.form])
            }catch(RuntimeException e){
                if (log.debugEnabled)
                    log.debug(e.getMessage())
                render(status:400, contentType:'application/json', text:[error:renderErrors([bean:input.errors])] as JSON)
            }
        }
    }

    def removeInput = {
        withInputField { input ->
            try{
                def screen = input.form
                formService.deleteInputeField(input)
                screenService.createScreenShot(input.form)
                render(status:200, template: 'table', model:[screen:screen])
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(e.getMessage())
                }
                render(status:400, contentType:'application/json', text:[error:renderErrors([bean:input.errors])] as JSON)
            }
        }
    }

    private def withInputField(id="id", screenId="screen.id", Closure c) {
        def screen = FormScreen.findByApplicationAndId(request.application,params."${screenId}".toLong())
        def linkListItem = InputField.findByFormAndId(screen,params."${id}".toLong())
        if(linkListItem) {
            c.call linkListItem
        } else {
            render(status:404)
            return null
        }
    }

    private def withScreen(id="id", Closure c) {
        def screen = FormScreen.findByApplicationAndId(request.application,params."${id}".toLong())
        if(screen) {
            c.call screen
        } else {
            render(status:404)
            return null
        }
    }
}
