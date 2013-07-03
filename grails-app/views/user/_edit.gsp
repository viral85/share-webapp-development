<%@ page import="com.smartpaper.domains.user.User" %><ui:window title="ui.user.edit.title" modal="true" width="470">
    <g:formRemote name="userEdit" before="if (!jQuery('#edit-form').valid()){ return false; }" onFailure="jQuery.smartpaper.displayPopupErrors(XMLHttpRequest);" onSuccess="jQuery.smartpaper.updateProfile(data);" url="[controller:'user', action:'edit']" id="edit-form">
        <p><input name="user.firstname" value="${user?.firstname}" type="text" title="${message(code:'account.firstname')}" placeholder="${message(code:'account.firstname')}" class="required"></p>
        <p><input name="user.lastname" value="${user?.lastname}" type="text" title="${message(code:'account.lastname')}" placeholder="${message(code:'account.lastname')}" class="required"></p>
        <p><input name="user.phone" value="${user?.phone}" type="text" title="${message(code:'account.phone')}" placeholder="${message(code:'account.phone')}" class="${user?.type == User.COMPANY_ACCOUNT ? 'required number' : ''}"></p>
        <p>
            <label class="countries">${message(code:'account.country')}</label><g:countrySelect default="${user?.countryCode?:null}" class="black ${user?.type == User.COMPANY_ACCOUNT ? 'required number' : ''}" name="user.countryCode" id="userCountryCode" value="${country}" noSelection="['':message(code:'ui.register.country.choose')]"/>
        </p>
        <div id="company-fields" style="display:${user?.type == User.COMPANY_ACCOUNT ? 'block' : 'none'};">
            <legend>${message(code:'ui.register.company')}</legend>
            <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.companyName : null}" name="user.companyName" type="text" title="${message(code:'account.companyname')}" placeholder="${message(code:'account.companyname')}"  class="${user?.type == User.COMPANY_ACCOUNT ? 'required' : ''}"></p>
            <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.address : null}" name="user.address" type="text" title="${message(code:'account.address')}" placeholder="${message(code:'account.address')}"  class="${user?.type == User.COMPANY_ACCOUNT ? 'required' : ''}"></p>
            <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.city : null}" name="user.city" type="text" title="${message(code:'account.city')}" placeholder="${message(code:'account.city')}" class="${user?.type == User.COMPANY_ACCOUNT ? 'required' : ''}"></p>
            <p><input value="${user?.type == User.COMPANY_ACCOUNT ? user?.zipCode : null}" name="user.zipCode" type="text" title="${message(code:'account.zipcode')}" placeholder="${message(code:'account.zipcode')}" class="${user?.type == User.COMPANY_ACCOUNT ? 'required number' : ''}"></p>
        </div>
        <p><label class="countries">${message(code:'account.language')}</label><ui:localeSelecter id="edit-language" name="user.language" value="${user.language}"/></p>
        <div class="error"></div>
    </g:formRemote>
    <jq:jquery>
        jQuery('#edit-form').validate();
        jQuery("#popup select").uniform({selectClass: 'selector black-selector'});
    </jq:jquery>
</ui:window>