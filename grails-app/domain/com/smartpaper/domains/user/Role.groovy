package com.smartpaper.domains.user

class Role {

	String authority

	static mapping = {
		table 'sp_role'
        cache true
	}

	static constraints = {
		authority blank: false, unique: true
	}
}
