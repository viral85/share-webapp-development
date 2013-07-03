<div class="video-config">
	<h2>${message(code:'ui.screen.video.title')}</h2>
    <g:remoteLink
        controller="media"
        action="explorer"
        params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'Video',
                    'update.controller':'video',
                    'update.action':'changeVideo',
                    'update.content':'main-config-screen',
                    'update.params':'phone=false&screen.id='+screen.id]"
        update="popup">
        <button class="button-icon black mini"><span class="icon iconsmall miniplus  ${screen.video ?'active':''}">+</span></button>
        <span><g:message code="${screen.video ? 'ui.screen.video.change' : 'ui.screen.video.add'}"/></span>
    </g:remoteLink>

    <g:if test="${screen.video}">
        <ui:video poster="${null}" video="${screen.video.url}" height="${ui.screenHeight([screen:screen,app:app])}" id='video-screen'/>
        <p class="filename">${screen.video.filename}</p>
	    <p class="details">${message(code:'media.size')} ${ui.filesize(size:screen.video.size)}</p>
	    <p class="details">${message(code:'media.dateCreated')} ${g.formatDate(date:screen.video.dateCreated,formatName:'ui.date.format')}</p>
    </g:if>

</div>