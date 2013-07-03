package com.smartpaper.services.screen

import com.smartpaper.domains.screen.FormScreen
import com.smartpaper.domains.screen.InputField

class FormService {

    static transactional = true

    def saveInputField(FormScreen screen){
        def link = new InputField()
        link.order = (screen.inputs?.size()?:0) + 1
        screen.addToInputs(link)
        if(!screen.save(flush:true))
            throw new RuntimeException()
        return link
    }

    def updateInputField(InputField input){
        if(input.isDirty('order')){
            if (input.getPersistentValue('order') > input.order) {
                input.form.inputs.each {it ->
                    if (it.order >= input.order && it.order <= input.getPersistentValue('order') && it.id != input.id) {
                        it.order = it.order + 1
                        it.save()
                    }
                }
            } else {
                input.form.inputs.each {it ->
                    if (it.order <= input.order && it.order >= input.getPersistentValue('order') && it.id != input.id) {
                        it.order = it.order - 1
                        it.save()
                    }
                }
            }
        }
        if(!input.save())
            throw new RuntimeException()
    }

    def deleteInputeField(InputField input){
        FormScreen screen = (FormScreen)input.form
        screen.inputs.findAll{ it.order > input.order }.each{
            it.order--
            it.save()
        }
        screen.removeFromInputs(input)
    }
}
