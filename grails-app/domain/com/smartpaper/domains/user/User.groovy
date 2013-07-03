package com.smartpaper.domains.user
import com.smartpaper.domains.application.Application

class User {

    static final int COMPANY_ACCOUNT = 1
    static final int PERSONAL_ACCOUNT = 2

	transient springSecurityService

    String firstname
    String lastname
	String username

    //optional
    String countryCode
    String phone
    String companyName
    String address
    String zipCode
    String city
    String language = 'en'

    int type = PERSONAL_ACCOUNT

	String password
	boolean enabled
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
    Date dateCreated
    Date lastUpdated

    static hasMany = [
            applications: Application
    ]

	static constraints = {
        countryCode blank: false, nullable:true
        phone       blank: false, nullable:true
        username    blank: false, unique: true, email:true

        firstname   blank: false
        lastname    blank: false

        phone       blank:true, nullable:true
        countryCode blank:true, nullable:true
        companyName blank:true, nullable:true
        address     blank:true, nullable:true
        city        blank:true, nullable:true
        zipCode     blank:true, nullable:true

		password    blank: false
	}

	static mapping = {
        table 'sp_account'
        cache true
		password column: '`password`'
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this).collect { it.role } as Set
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService.encodePassword(password)
	}
}
