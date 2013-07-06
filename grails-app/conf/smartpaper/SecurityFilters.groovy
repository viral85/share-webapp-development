package smartpaper

import com.smartpaper.domains.user.User
import com.smartpaper.domains.application.Application

class SecurityFilters {

    def springSecurityService

    def filters = {
        secureApplication(controller: '*', action: '*') {
            before = {
                User user = null
				if ('preview'.equalsIgnoreCase(controllerName)) {
                    return true
                }
                if ('screen'.equalsIgnoreCase(controllerName) && 'preview'.equalsIgnoreCase(actionName)){
                    return true
                }
                if (springSecurityService.isLoggedIn()){
                        user = (User)springSecurityService.currentUser
                        request.currentUser = user
                }
                if (params.application) {
                    if (springSecurityService.isLoggedIn()){
                        Application application
                        application = Application.get(params.application)
                        def valid = application ? application.owner == user : false
                        if (!valid){
                            render(status: 404)
                            return
                        }else{
                            request.application = application
                            return
                        }
                    }else{
                        redirect(controller:'login', action:'auth')
                        return
                    }
                }
            }
        }
    }

}
