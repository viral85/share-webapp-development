class UrlMappings {

	static mappings = {

        "/resources/$app/medias/$file"(controller:'app', action:'resources')

        "/register"(controller:"user", action:"register")

        "/login"(controller:"login", action:"auth")

        "/logout"(controller:"logout", action:"index")

        "/app/" (controller:"app", action:"index")

        name defaultApp : "/app/$application/$controller/$action?/$id?"{
			constraints {
				application(matches: /[0-9]*/)
			}
		}

        "/app/$application" (controller:"app", action:"index"){
            constraints {
				application(matches: /[0-9]*/)
			}
        }

        "/$action?"(controller:"app")

        name defaultNoApp : "/$controller/$action?/$id?"{
			constraints {
                id(matches: /[0-9]*/)
				// apply constraints here
			}
		}

        "500"(view: '/errors/500')
        "/denied"(view: '/errors/403')
        "403"(view: '/errors/403')
        "404"(view: '/errors/404')
	}
}
