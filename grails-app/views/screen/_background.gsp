<div class="background-details">
    <h3><g:message code="ui.screen.background"/></h3>
    <g:if test="${!screen.background.image}">
        <g:remoteLink
            controller="media"
            action="explorer"
            params="[
                screen:screen.id,
                application:app.id,
                filter:'Image',
                'update.controller':'screen',
                'update.action':'update',
                'update.value':'background',
                'update.content':'main-config-content',
                'update.params':'view='+view+'&phone=false&screen.id='+screen.id
            ]"
            update="popup">
                <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                <span><g:message code="ui.screen.image.add"/></span>
        </g:remoteLink>
    </g:if>
    <g:else>
        <div class="image"><img src="${screen.background.image.url}" title="${screen.background.image.name}.${screen.background.image.extension}"/></div>
        <span class="details filename">${screen.background.image.name}.${screen.background.image.extension}</span>
        <p class="details">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'Image',
                    'update.controller':'screen',
                    'update.action':'update',
                    'update.value':'background',
                    'update.content':'main-config-content',
                    'update.params':'view='+view+'&phone=false&screen.id='+screen.id
                ]"
                update="popup">
                <span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
                <span><g:message code="ui.screen.image.change"/></span>
            </g:remoteLink>
        </p>
        <p class="details">
            <g:remoteLink
                    controller="screen"
                    action="update"
                    update="main-config-content"
                    params="[application:app.id,view:view,phone:false,'screen.id':screen.id,'background':'false']">
                <span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
                <span><g:message code="ui.screen.image.delete"/></span>
            </g:remoteLink>
        </p>
    </g:else>
</div>

<ui:colorPicker
        name='picker-options'
        submit="[controller:'screen', onSuccess:'jQuery.smartpaper.refreshPhone();', update:'main-config-content', action:'update', params:'\'screen.background.color=\'+value+\'&screen.id='+screen.id+'&application='+app.id+'&phone=false&view='+view+'\'']"
        label="ui.screen.background.color"
        default="${screen.background.color}"
        class="colorpicker"/>