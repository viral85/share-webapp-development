<g:if test="${overlay.background.image}">
    <span class="details filename">${overlay.background.image.name}.${overlay.background.image.extension}</span>
</g:if>
<g:remoteLink
    controller="media"
    action="explorer"
    params="[
        screen:overlay.linkScreen.id,
        application:overlay.linkScreen.application.id,
        filter:'Image',
        'update.controller':'detailedView',
        'update.action':'updateOverlay',
        'update.value':'background',
        'update.content':'overlays-list .background-details[elemid='+overlay.id+']',
        'update.params':'&id='+overlay.id+'&screen.id='+overlay.linkScreen.id
    ]"
    update="popup">
        <g:if test="${!overlay.background.image}">
            <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
            <span><g:message code="ui.screen.image.add"/></span>
        </g:if>
        <g:else>
            <span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
            <span><g:message code="ui.screen.image.change"/></span>
        </g:else>
</g:remoteLink>
<g:if test="${overlay.background.image}">
    <g:remoteLink
            controller="detailedView"
            action="updateOverlay"
            update="overlays-list .background-details[elemid=${overlay.id}]"
            params="[application:overlay.linkScreen.application.id,'screen.id':overlay.linkScreen.id,'background':'false','id':overlay.id]">
        <span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
        <span><g:message code="ui.screen.image.delete"/></span>
    </g:remoteLink>
</g:if>
<g:if test="${updateEditor}">
    <jq:jquery>
        jQuery.smartpaper.updateBackgroundOverlayRichText(${overlay.id}, '${overlay.background.image?.url}');
    </jq:jquery>
</g:if>