<ui:window title="ui.application.create.title" modal="true" width="470">
    <g:formRemote name="create" before="if (!jQuery('#create-form').valid()){ return false; }" onFailure="jQuery.smartpaper.displayPopupErrors(XMLHttpRequest);" onSuccess=" document.location = '${g.createLink(controller:'app',action:'index')}/'+data; " url="[controller:'app', action:'create']" id="create-form">
        <p>
            <input type="text" placeholder="${message(code:'ui.application.create.name')}" title="${message(code:'ui.application.create.name')}" name="app.name" class="required" minlength="5">
        </p>
    </g:formRemote>
    <jq:jquery>
        jQuery('#create-form').validate();
    </jq:jquery>
</ui:window>