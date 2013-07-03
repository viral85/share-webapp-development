 <ui:window title="ui.screen.rename.title" modal="true" width="470">
    <g:formRemote name="renameScreen" before="if (!jQuery('#rename-form').valid()){ return false; }" onFailure="jQuery.smartpaper.displayPopupErrors(XMLHttpRequest);" onSuccess="jQuery.smartpaper.renamePage(data);" url="[controller:'screen', action:'rename', params:[id:screen.id,application:request.application.id]]" id="rename-form">
        <p>
            <input type="text" value="${screen.name}" placeholder="${message(code:'ui.screen.rename.name')}" title="${message(code:'ui.screen.rename.name')}" name="screen.name" class="required">
        </p>
    </g:formRemote>
    <jq:jquery>
        jQuery('#rename-form').validate();
    </jq:jquery>
</ui:window>