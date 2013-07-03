<ui:window title="ui.screen.create.title" modal="true" width="470">
    <g:formRemote name="createScreen" before="if (!jQuery('#create-form').valid()){ return false; }" onFailure="jQuery.smartpaper.displayPopupErrors(XMLHttpRequest);" update="main" onSuccess="jQuery('#popup').dialog('close'); " url="[controller:'screen', action:'create', params:[application:request.application.id]]" id="create-form">
        <p>
            <input type="text" placeholder="${message(code:'ui.screen.create.name')}" title="${message(code:'ui.screen.create.name')}" name="screen.name" class="required">
        </p>
        <p id="model-choose">
            <ui:selectModel id="create-screen-model"/>
        </p>
    </g:formRemote>
    <jq:jquery>
        jQuery('#create-form').validate();
    </jq:jquery>
</ui:window>