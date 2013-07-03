<%@ page import="com.smartpaper.domains.user.User" %>
<html>
   <head>
       <title>${message(code:'ui.app.name')} - ${message(code:'ui.register.title')}</title>
      <meta name="layout" content="simple"/>
      <r:require modules="smartpaper-css, smartpaper-js"/>
   </head>
   <body>
       <div id="title-login">  
            <h1>${message(code:'ui.register.title_1')}</h1>
        	<h2>${message(code:'ui.register.subtitle')}</h2>
       </div>
       <div class="errors" style="display:${errors?'block':'none'}">
           ${error}
           <g:renderErrors bean="${user}" />
       </div>
       <g:form controller="user" action="register" method="POST" class="user-form two-col register-form" id="register-form">
            <fieldset>
                <legend>${message(code:'ui.register.about')}</legend>
                <p>
                    <g:select from="[message(code:'account.type.personal'),message(code:'account.type.company')]"
                              keys="[User.PERSONAL_ACCOUNT,User.COMPANY_ACCOUNT]"
                              value="${user?.type}"
                              id="type-account"
                              name="user.type"
                              class="white"
                              title="${message(code:'ui.register.account.type')}"/>
                </p>
                <p><input name="user.firstname" id="firstname" value="${user?.firstname}" type="text" title="${message(code:'account.firstname')}" placeholder="${message(code:'account.firstname')}"></p>
                <p><input name="user.lastname" value="${user?.lastname}" type="text" title="${message(code:'account.lastname')}" placeholder="${message(code:'account.lastname')}"></p>
                <p><input name="user.phone" value="${user?.phone}" type="text" title="${message(code:'account.phone')}" placeholder="${message(code:'account.phone')}"></p>
                <p>
                    <g:countrySelect default="${user?.countryCode?:null}" class="white" name="user.countryCode" id="userCountryCode" value="${country}" noSelection="['':message(code:'ui.register.country.choose')]"/>
                    <label for="userCountryCode" generated="false" class="error" style="display: none; "></label>
                </p>
                <p>
                    <ui:localeSelecter default="${user?.language?:''}" class="white" name="user.language" id="userLanguage"  noSelection="['':message(code:'ui.register.language.choose')]"/>
                    <label for="userCountryCode" generated="false" class="error" style="display: none; "></label>
                </p>
            </fieldset>
           
            <fieldset>
                <legend>${message(code:'ui.register.account')}</legend>
                <p><input value="${user?.username}" name="user.username" id="userusername" type="text" title="${message(code:'account.username')}" placeholder="${message(code:'account.username')}"></p>
                <p><input name="confirm.username" type="text" title="${message(code:'ui.register.confirm.email')}" placeholder="${message(code:'ui.register.confirm.email')}"></p>
                <p><input name="user.password" id="userpassword" type="password" title="${message(code:'account.password')}" placeholder="${message(code:'account.password')}"></p>
                <p><input name="confirm.password" type="password" title="${message(code:'ui.register.confirm.password')}" placeholder="${message(code:'ui.register.confirm.password')}"></p>
            </fieldset>
            
            <fieldset id="company-fields" style="display:${user?.type == User.COMPANY_ACCOUNT ? 'block' : 'none'};">
                    <legend>${message(code:'ui.register.company')}</legend>
                    <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.companyName : null}" name="user.companyName" type="text" title="${message(code:'account.companyname')}" placeholder="${message(code:'account.companyname')}"></p>
                    <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.address : null}" name="user.address" type="text" title="${message(code:'account.address')}" placeholder="${message(code:'account.address')}"></p>
                    <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.city : null}" name="user.city" type="text" title="${message(code:'account.city')}" placeholder="${message(code:'account.city')}"></p>
                    <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.zipCode : null}" name="user.zipCode" type="text" title="${message(code:'account.zipcode')}" placeholder="${message(code:'account.zipcode')}"></p>
           </fieldset>
           
            <fieldset id="account-valid">
                <p>
                	<label for="publication">${message(code:'ui.register.publication')}</label>
                	<input type="checkbox" ${confirm?.publication ? "checked='checked'" : ''} value="1" name="confirm.publication" id="publication"/>
                	<label for="publication" generated="false" class="error" style="display: none; "></label>
                </p>
                <p>
                	<label for="accept">${message(code:'ui.register.cgu')} <g:remoteLink action="cgu" controller="app" update="popup">${message(code:'ui.register.cgu.conditions')}</g:remoteLink></label>
                	<input type="checkbox" ${confirm?.accept ? "checked='checked'" : ''} value="1" name="confirm.accept" id="accept"/>
                	<label for="accept" generated="false" class="error" style="display: none; "></label>
                </p>
                <p>
                   <button class="button gray bigrounded" title="${message(code:'ui.button.cancel')}">${message(code:'ui.button.cancel')}</button>
                   <button class="button orange bigrounded" type="submit" title="${message(code:'ui.register.create.button')}">${message(code:'ui.register.create.button')}</button>
                </p>
            </fieldset>
        </g:form>
        <jq:jquery>
            jQuery('select#type-account').change(function(){
                jQuery(this).val() == ${User.PERSONAL_ACCOUNT} ? jQuery('fieldset#company-fields').hide() : jQuery('fieldset#company-fields').show();
            });
			
 			jQuery(".register-form").validate({
            	rules:{
            		"user.firstname":"required",
            		"user.lastname":"required",
            		"user.phone":{
            			required: function(element) {
							return $("#type-account").val() == 1
						},
            			number:true
            		},
            		"user.countryCode":{
            			required: function(element) {
							return $("#type-account").val() == 1
						}
            		},
            		"user.username":{
            			required:true,
            			email:true
            		},
            		"confirm.username":{
            			required:true,
            			email:true,
            			equalTo:"#userusername"
            		},
            		"user.password":{
            			required:true,
            			minlength:5
            		},
            		"confirm.password":{
            			required:true,
            			minlength:5,
            			equalTo:"#userpassword"
            		},
            		"user.companyName":{
            			required: function(element) {
							return $("#type-account").val() == 1
						}
            		},
            		"user.address":{
            			required: function(element) {
							return $("#type-account").val() == 1
						}
            		},
            		"user.city":{
            			required: function(element) {
							return $("#type-account").val() == 1
						}
            		},
            		"user.zipCode":{
            			required: function(element) {
							return $("#type-account").val() == 1
						},
            			number:true
            		},
            		"confirm.publication":{
            			required:true
            		},
            		"confirm.accept":{
            			required:true
            		}
            	}
            		
            });
        </jq:jquery>
        <div id='popup' class='cgu'></div>
   </body>
</html>