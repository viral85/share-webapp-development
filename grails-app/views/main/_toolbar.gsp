<div id="toolbar">
    <ul>
        <li><g:remoteLink method="get" class="icon iconsmall nouveau" controller="app" action="create" update="popup" title="${message(code:'ui.menu.file.new')}">${message(code:'ui.menu.file.new')}</g:remoteLink></li>
        <li><g:remoteLink controller="app" action="open" update="popup" title="${message(code:'ui.menu.file.open')}" class="icon iconsmall ouvrir">${message(code:'ui.menu.file.open')}</g:remoteLink></li>
        <g:if test="${request.application}">
            <li><span class="menu-separator"></span></li>
            <li><g:remoteLink controller="app" action="appPreview" params="[application:request.application.id]" update="popup" title="${message(code:'ui.menu.file.open')}" class="icon iconsmall preview" href="/">${message(code:'ui.menu.preview')}</g:remoteLink></li>
            <li><g:remoteLink controller="media" action="trigger" params="[application:request.application.id]" update="popup" title="${message(code:'ui.menu.file.open')}" class="icon iconsmall target" href="/">${message(code:'ui.menu.tools.target')}</g:remoteLink></li>
        </g:if>
    </ul>
</div>