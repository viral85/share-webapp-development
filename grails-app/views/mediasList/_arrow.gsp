<%@ page import="com.smartpaper.domains.screen.MediasListScreen" %>
<h2>${message(code:'ui.screen.mediasList.arrowTitle')}</h2>
<div class="arrow-details">
    <h4>${message(code:'ui.screen.mediasList.arrow.left')}</h4>
    <g:if test="${!screen.leftArrow}">
        <g:remoteLink
            controller="media"
            action="explorer"
            params="[
                screen:screen.id,
                application:app.id,
                filter:'Image',
                'update.controller':'mediasList',
                'update.action':'updateArrows',
                'update.value':'leftArrow',
                'update.onSuccess':'jQuery.smartpaper.refreshPhone();',
                'update.content':'main-config-content .arrow-config',
                'update.params':'screen.id='+screen.id
            ]"
            update="popup">
                <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                <span><g:message code="ui.screen.image.add"/></span>
        </g:remoteLink>
    </g:if>
    <g:else>
        <div class="image"><img src="${screen.leftArrow.url}" title="${screen.leftArrow.filename}"/></div>
        <span class="details filename">${screen.leftArrow.filename}</span>
        <p class="details">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'Image',
                    'update.controller':'mediasList',
                    'update.action':'updateArrows',
                    'update.value':'leftArrow',
                    'update.onSuccess':'jQuery.smartpaper.refreshPhone();',
                    'update.content':'main-config-content .arrow-config',
                    'update.params':'screen.id='+screen.id
                ]"
                update="popup">
                <span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
                <span><g:message code="ui.screen.image.change"/></span>
            </g:remoteLink>
        </p>
        <p class="details">
            <g:remoteLink
                    controller="mediasList"
                    action="updateArrows"
                    update="main-config-content .arrow-config"
                    params="[application:app.id,'screen.id':screen.id,'leftArrow':'false']">
                <span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
                <span><g:message code="ui.screen.image.delete"/></span>
            </g:remoteLink>
        </p>
    </g:else>
</div>
<div class="arrow-details">
    <h4>${message(code:'ui.screen.mediasList.arrow.right')}</h4>
    <g:if test="${!screen.rightArrow}">
        <g:remoteLink
            controller="media"
            action="explorer"
            params="[
                screen:screen.id,
                application:app.id,
                filter:'Image',
                'update.controller':'mediasList',
                'update.action':'updateArrows',
                'update.value':'rightArrow',
                'update.onSuccess':'jQuery.smartpaper.refreshPhone();',
                'update.content':'main-config-content .arrow-config',
                'update.params':'screen.id='+screen.id
            ]"
            update="popup">
                <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                <span><g:message code="ui.screen.image.add"/></span>
        </g:remoteLink>
    </g:if>
    <g:else>
        <div class="image"><img src="${screen.rightArrow.url}" title="${screen.rightArrow.filename}"/></div>
        <span class="details filename">${screen.rightArrow.filename}</span>
        <p class="details">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'Image',
                    'update.controller':'mediasList',
                    'update.action':'updateArrows',
                    'update.value':'rightArrow',
                    'update.onSuccess':'jQuery.smartpaper.refreshPhone();',
                    'update.content':'main-config-content .arrow-config',
                    'update.params':'screen.id='+screen.id
                ]"
                update="popup">
                <span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
                <span><g:message code="ui.screen.image.change"/></span>
            </g:remoteLink>
        </p>
        <p class="details">
            <g:remoteLink
                    controller="mediasList"
                    action="updateArrows"
                    update="main-config-content .arrow-config"
                    params="[application:app.id,'screen.id':screen.id,'rightArrow':'false']">
                <span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
                <span><g:message code="ui.screen.image.delete"/></span>
            </g:remoteLink>
        </p>
    </g:else>
</div>
<h4>${message(code:'ui.screen.mediasList.arrow.visiblity')}</h4>
<ul>
    <li><input class='visibility' type="radio" ${screen.arrowsDisplay == MediasListScreen.ARROWS_ALWAYS ? 'checked="checked"' : ''} value="${MediasListScreen.ARROWS_ALWAYS}" name="screen.arrowsDisplay"/><label>${message(code:'ui.screen.mediasList.arrow.always')}</label></li>
    <li><input class='visibility' type="radio" ${screen.arrowsDisplay == MediasListScreen.ARROWS_ON_TOUCH ? 'checked="checked"' : ''} value="${MediasListScreen.ARROWS_ON_TOUCH}"name="screen.arrowsDisplay"/><label>${message(code:'ui.screen.mediasList.arrow.touch')}</label></li>
    <li><input class='visibility' type="radio" ${screen.arrowsDisplay == MediasListScreen.ARROWS_NEVER ? 'checked="checked"' : ''} value="${MediasListScreen.ARROWS_NEVER}" name="screen.arrowsDisplay"/><label>${message(code:'ui.screen.mediasList.arrow.never')}</label></li>
</ul>
<g:if test="${init}">
    <jq:jquery>
        jQuery('.arrow-config input.visibility:radio').die().live('change',function(){
                <g:remoteFunction
                        controller="mediasList"
                        action="updateVisibility"
                        onSuccess='jQuery.smartpaper.refreshPhone();'
                        params="\'application=${app.id}&id=${screen.id}&\'+jQuery(this).attr(\'name\')+\'=\'+jQuery(this).val()+\''"/>
            });
    </jq:jquery>
</g:if>