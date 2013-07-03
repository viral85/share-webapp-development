package com.smartpaper.controllers

import grails.plugins.springsecurity.Secured
import com.smartpaper.domains.user.User
import grails.converters.JSON

class UserController {

    def accountService
    def springSecurityService

    @Secured(['ROLE_USER'])
    def edit = {
        User user = (User)springSecurityService.currentUser
        if (request.method == 'GET')
        {
            render(status:200, template: "edit", model:[user:user])
        }
        else if (request.method == 'POST')
        {
            def forceRefresh = false
            if (params.user.language != user.language) {
                forceRefresh = true
            }
            user.properties = params.user
            try{
                accountService.update(user)
                render(status:200, contentType: 'application/json',text:[refresh:forceRefresh,name:"${user.firstname} ${user.lastname}"] as JSON)
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(user.errors)
                    log.debug(e.getMessage())
                }
                render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:user.errors)] as JSON)
            }
        }
        else
        {
            render(status:404)
        }
    }

    @Secured(['ROLE_USER'])
    def editPassword = {
        if (request.method == 'GET')
        {
            render(status:200, template: "editPassword")
        }else if(request.method == 'POST')
        {
            User user = (User)springSecurityService.currentUser
            if (params.user.password == params.confirm.password){
                user.password = params.user.password
                try{
                    accountService.update(user)
                }catch(RuntimeException e){
                    if(log.isDebugEnabled()){
                        log.debug(user.errors)
                        log.debug(e.getMessage())
                    }
                    render(status:400, contentType: 'application/json', text:[error:renderErrors(bean:user.errors)] as JSON)
                }
            }
        }
        else
        {
            render(status:404)
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    def register = {
        if (request.method == 'GET')
        {
            render(view: "register")
        }
        else if(request.method == 'POST')
        {
            def error = ""
            User user = new User()
            user.properties = params.user
            try{
                error = params.confirm?.username != params.user?.username ? message(code:'ui.register.error.email') : ""
                error += params.confirm?.password != params.user?.password ? "${(error ? "<br/>": "" )+message(code:'ui.register.error.password')}" : ""
                error += params.confirm?.publication ? "" : "${(error != "" ? "<br/>" : null)}${message(code:'ui.register.error.publication')}"
                error += params.confirm?.accept ? "" : "${(error != "" ? "<br/>" : null)}${message(code:'ui.register.error.accept')}"
                if (error.trim() == ""){
                    accountService.create(user)
                    redirect(action:'auth', controller:'login', params:[login:user.username,lang:user.language])
                    return
                }
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(user.errors)
                    log.debug(e.getMessage())
                }
            }
            render(view: "register", model:[confirm:params.confirm,user:user,error:error])
        }
        else
        {
            render(status:404)
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    def retrieve = {
        if(request.method == 'GET')
        {
            render(view: "lost")
        }
        else if(request.method == 'POST')
        {
            def user = null
            def error = ""
            try{
                if(params.username?.trim() == "" || params.firstname?.trim() == ""){
                    error = ""
                }else{
                    user = User.findByUsername(params.username)
                    if (user?.firstname?.toLowerCase() != params.firstname.toLowerCase())
                        user = null
                }
                if (error.trim() == "" && user){
                    accountService.resetPassword(user)
                    redirect(action:'auth', controller:'login', params:[login:user.username])
                    return
                }
                if(!user){
                    error = message(code:'account.notfound')
                }
            }catch(RuntimeException e){
                if(log.isDebugEnabled()){
                    log.debug(user.errors)
                    log.debug(e.getMessage())
                }
            }
            render(view: "lost", model:[error:error])
        }
        else
        {
            render(status:404)
        }
    }
}
