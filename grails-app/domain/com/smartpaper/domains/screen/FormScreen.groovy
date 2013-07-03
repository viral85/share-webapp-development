package com.smartpaper.domains.screen

class FormScreen extends Screen {

    String form = ""
    String emailTo = ""

    public static int type = 1

    static hasMany = [
            inputs:InputField
    ]

    static constraints = {
        form blank:true
        emailTo email:true, blank:true
    }

    static mapping = {
        cache true
		table 'sp_screen_form'
        inputs sort:'order', order:'asc'
        inputs cascade:"all-delete-orphan"
	}

    def beforeInsert(){
        if (!this.emailTo){
            this.emailTo = this.application.owner.username
        }
    }
}
