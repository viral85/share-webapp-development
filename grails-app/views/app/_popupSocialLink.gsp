<ui:window title="${socialLink ? 'ui.socialLink.update.title' : 'ui.socialLink.create.title' }" modal="true" width="470">
    <g:formRemote
            name="socialLink"
            before="if (!jQuery('#socialLink-form').valid()){ return false; }"
            onFailure="jQuery.smartpaper.displayPopupErrors(XMLHttpRequest)"
            onSuccess="jQuery('#popup').dialog('close')"
            update="social-table"
            url="[controller:'app', action:'socialLinks', params:[_action:socialLink ?'update': 'add',application:request.application.id]]"
            id="socialLink-form">
            <ui:socialLinkSelector updateUrl='social-url' value="${socialLink}"/>
            <g:if test="${socialLink}">
                <input type='hidden' value='${socialLink.id}' name='id'/>
            </g:if>
            <input value="${socialLink?.url?:''}" type="text" id='social-url' placeholder="${message(code:'socialLink.url')}" title="${message(code:'socialLink.url')}" name="socialLink.url" class="required url" minlength="5">
    </g:formRemote>
    <jq:jquery>
        jQuery('#socialLink-form').validate();
    </jq:jquery>
</ui:window>