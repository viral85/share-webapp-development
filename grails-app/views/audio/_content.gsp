<div class="audio-config">
	<h2>${message(code:'ui.screen.audio.title')}</h2>
    <g:remoteLink
        controller="media"
        action="explorer"
        params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'Audio',
                    'update.controller':'audio',
                    'update.action':'changeAudio',
                    'update.content':'main-config-screen',
                    'update.params':'phone=false&screen.id='+screen.id]"
        update="popup">
        <button class="button-icon black mini"><span class="icon iconsmall miniplus  ${screen.audio ?'active':''}">+</span></button>
        <span><g:message code="${screen.audio ? 'ui.screen.audio.change' : 'ui.screen.audio.add'}"/></span>
    </g:remoteLink>

    <g:if test="${screen.audio}">
        <ui:audio audio="${screen.audio.url}" id='audio-screen'/>
        <p class="filename">${screen.audio.filename}</p>
	    <p class="details">${message(code:'media.size')} ${ui.filesize(size:screen.audio.size)}</p>
	    <p class="details">${message(code:'media.dateCreated')} ${g.formatDate(date:screen.audio.dateCreated,formatName:'ui.date.format')}</p>
    </g:if>

</div>