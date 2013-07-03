<g:set var="view" value="header"/>
<g:set var="appid" value="${request.application.id}"/>
<g:set var="app" value="${request.application}"/>

<h2 class="first"><g:message code="ui.application.header.social.title"/></h2>
<div id="social-table">
    <g:include view="app/_tableSocial.gsp" model="[init:true]"/>
</div>
<h2><g:message code="ui.application.header.logo.title"/></h2>
<div class="background-details">
    <g:if test="${!app.logo}">
        <g:remoteLink
            controller="media"
            action="explorer"
            params="[
                application:appid,
                filter:'Image',
                'update.controller':'app',
                'update.action':'common',
                'update.value':'logoHeader',
                'update.content':'main-config-content',
                'update.params':'view='+view+'&phone=false'
            ]"
            update="popup">
                <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                <span><g:message code="ui.screen.image.add"/></span>
        </g:remoteLink>
    </g:if>
    <g:else>
        <div class="image"><img src="${app.logo.url}" title="${app.logo.name}.${app.logo.extension}"/></div>
        <span class="details filename small">${app.logo.name}.${app.logo.extension}</span>
        <p class="details small">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
                    application:appid,
                    filter:'Image',
                    'update.controller':'app',
                    'update.action':'common',
                    'update.value':'logoHeader',
                    'update.content':'main-config-content',
                    'update.params':'view='+view+'&phone=false'
                ]"
                update="popup">
                <span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
                <span><g:message code="ui.screen.image.change"/></span>
            </g:remoteLink>
        </p>
        <p class="details small">
            <g:remoteLink
                    controller="app"
                    action="common"
                    update="main-config-content"
                    params="[application:appid,view:view,phone:false,'logoHeader':'false']">
                <span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
                <span><g:message code="ui.screen.image.delete"/></span>
            </g:remoteLink>
        </p>
    </g:else>
</div>
<h2><g:message code="ui.application.header.background.title"/></h2>
<div class="background-details">
<g:if test="${!app.backgroundHeader.image}">
        <g:remoteLink
            controller="media"
            action="explorer"
            params="[
                application:appid,
                filter:'Image',
                'update.controller':'app',
                'update.action':'common',
                'update.value':'backgroundHeader',
                'update.content':'main-config-content',
                'update.params':'view='+view+'&phone=false'
            ]"
            update="popup">
                <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                <span><g:message code="ui.screen.image.add"/></span>
        </g:remoteLink>
    </g:if>
    <g:else>
        <div class="image"><img src="${app.backgroundHeader.image.url}" title="${app.backgroundHeader.image.name}.${app.backgroundHeader.image.extension}"/></div>
        <span class="details filename">${app.backgroundHeader.image.name}.${app.backgroundHeader.image.extension}</span>
        <p class="details">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
                    application:appid,
                    filter:'Image',
                    'update.controller':'app',
                    'update.action':'common',
                    'update.value':'backgroundHeader',
                    'update.content':'main-config-content',
                    'update.params':'view='+view+'&phone=false'
                ]"
                update="popup">
                <span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
                <span><g:message code="ui.screen.image.change"/></span>
            </g:remoteLink>
        </p>
        <p class="details">
            <g:remoteLink
                    controller="app"
                    action="common"
                    update="main-config-content"
                    params="[application:appid,view:view,phone:false,'backgroundHeader':'false']">
                <span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
                <span><g:message code="ui.screen.image.delete"/></span>
            </g:remoteLink>
        </p>
    </g:else>
</div>

<ui:colorPicker
        name='picker-options'
        submit="[controller:'app', onSuccess:'jQuery.smartpaper.refreshPhone();', update:'main-config-content', action:'common', params:'\'app.backgroundHeader.color=\'+value+\'&application='+appid+'&phone=false&view='+view+'\'']"
        label="ui.screen.background.color"
        default="${app.backgroundHeader.color}"
        class="colorpicker"/>
