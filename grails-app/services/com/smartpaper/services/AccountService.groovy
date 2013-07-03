package com.smartpaper.services

import com.smartpaper.domains.user.User
import com.smartpaper.domains.user.UserRole
import com.smartpaper.domains.user.Role

class AccountService {

    def grailsApplication
    static transactional = true
    def g = new org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib()

    def create(User user) {
        if (!validateAccount(user))
            throw new RuntimeException()
        user.enabled = true
        if (!user.save())
            throw new RuntimeException()
        UserRole.create user, Role.findByAuthority('ROLE_USER'), true
    }

    def update(def user){
        if (!validateAccount(user))
            throw new RuntimeException()
        if (!user.save())
            throw new RuntimeException()
    }

    private validateAccount(User user){
        if (user.password.length() < 5)
            return false
        if(user.type == User.COMPANY_ACCOUNT){
            if (user.countryCode.trim() == "" || user.companyName.trim() == "" || user.city.trim() == "" || user.zipCode.trim() == "" || user.address.trim() == "" || user.phone.trim() == ""){
                return false
            }
        }else{
            user.companyName = null
            user.city = null
            user.zipCode = null
            user.address = null
        }
        return true
    }

    def resetPassword(def user){
        def allChars = [ 'A'..'Z', 'a'..'z', '0'..'9' ].flatten() - [ 'O', '0', 'l', '1', 'I' ]
        def generatePassword = { length ->
            (0..<length).collect { allChars[ new Random().nextInt( allChars.size() ) ] }.join()
        }
        def pass = generatePassword(8)
        user.password = pass
        if (!user.save(flush:true)){
            throw new RuntimeException()
        }
        else
        {
            sendMail {
              to user.username
              subject g.message(code:'email.resetPassword.subject')
              body g.message(code:'email.resetPassword.template', args:[user.firstname,pass,grailsApplication.config.grails.serverURL])
              from grailsApplication.config.smartpaper.email
            }
        }

    }
}
