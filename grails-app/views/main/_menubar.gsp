<div id="menubar">
    <ul>
        <li>
    	  ${r.img(dir:'images', file:'logo-26-full.png')}
    	</li>
    	<li>
            <span>${message(code:'ui.menu.file')}</span>
            <ul>
              <li><g:remoteLink method="get" controller="app" action="create" update="popup" title="${message(code:'ui.menu.file.new')}">${message(code:'ui.menu.file.new')}</g:remoteLink></li>
              <li><g:remoteLink controller="app" action="open" update="popup" title="${message(code:'ui.menu.file.open')}">${message(code:'ui.menu.file.open')}</g:remoteLink></li>
              <g:if test="${request.application}">
                  <li><g:link controller="app" action="home" title="${message(code:'ui.menu.file.close')}">${message(code:'ui.menu.file.close')}</g:link></li>
                  <li><g:remoteLink
                          controller="app"
                          params="[application:request.application.id]"
                          action="delete"
                          before="if(!confirm('${message(code:'ui.application.confirm.delete')}')){return false;}"
                          onFailure="XMLHttpRequest.overrideError = true; alert(XMLHttpRequest.responseText)"
                          onSuccess="document.location = '${createLink(controller:'app', action:'index')}'"
                          title="${message(code:'ui.menu.file.delete')}">${message(code:'ui.menu.file.delete')}</g:remoteLink></li>
              </g:if>
            </ul>
        </li>
        <g:if test="${request.application}">
            <li>
                <span>${message(code:'ui.menu.page')}</span>
                <ul>
                  <li><g:remoteLink method="get" controller="screen" action="create" params="[application:request.application.id]" update="popup" title="${message(code:'ui.menu.page.new')}">${message(code:'ui.menu.page.new')}</g:remoteLink></li>
                  <li style="display:none;" id="menubar-rename-page"><g:remoteLink method="get" params="'application=${request.application.id}&id='+jQuery.smartpaper.currentPage" update="popup" controller="screen" action="rename" onFailure="XMLHttpRequest.overrideError = true; alert(XMLHttpRequest.responseText)" title="${message(code:'ui.menu.page.rename')}">${message(code:'ui.menu.page.rename')}</g:remoteLink></li>
                  <li style="display:none;" id="menubar-duplicate-page"><g:remoteLink update="main" params="'application=${request.application.id}&id='+jQuery.smartpaper.currentPage" controller="screen" action="duplicate" onFailure="XMLHttpRequest.overrideError = true; alert(XMLHttpRequest.responseText)" title="${message(code:'ui.menu.page.duplicate')}">${message(code:'ui.menu.page.duplicate')}</g:remoteLink></li>
                  <li style="display:none;" id="menubar-delete-page"><g:remoteLink params="'application=${request.application.id}&id='+jQuery.smartpaper.currentPage" controller="screen" before="if(!confirm('${message(code:'ui.screen.confirm.delete')}')){return false;}" action="delete" onFailure="XMLHttpRequest.overrideError = true; alert(XMLHttpRequest.responseText)" onSuccess="jQuery.smartpaper.deletePage(data)" title="${message(code:'ui.menu.page.new')}">${message(code:'ui.menu.page.delete')}</g:remoteLink></li>
                  <li style="display:none;" id="menubar-define-home"><g:remoteLink params="'application=${request.application.id}&id='+jQuery.smartpaper.currentPage" controller="app" action="setHome" onSuccess="jQuery.smartpaper.defineHomePage(data);" title="${message(code:'ui.menu.page.define.home')}">${message(code:'ui.menu.page.define.home')}</g:remoteLink></li>
                </ul>
            </li>
            <li>
                <span>${message(code:'ui.menu.tools')}</span>
                <ul>
                  <li><g:remoteLink
                            controller="media"
                            action="manage"
                            title="${message(code:'ui.menu.tools.library')}"
                            params="[application:request.application.id]"
                            update="popup">
                            ${message(code:'ui.menu.tools.library')}
                      </g:remoteLink>
                  </li>
                  <li>
                      <g:remoteLink
                              controller="app"
                              action="common"
                              update="main"
                              title="${message(code:'ui.menu.tools.common')}"
                              params="[application:request.application.id]">
                            ${message(code:'ui.menu.tools.common')}
                      </g:remoteLink>
                  </li>
                </ul>
            </li>
            <li id='menubar-trigger'><g:remoteLink
                            controller="media"
                            action="trigger"
                            title="${message(code:'ui.menu.tools.target')}"
                            params="[application:request.application.id]"
                            update="popup">
                            ${message(code:'ui.menu.tools.target')}
                      </g:remoteLink>
                  </li>
            <li><g:remoteLink
                            controller="app"
                            action="appPreview"
                            title="${message(code:'ui.menu.preview')}"
                            params="[application:request.application.id]"
                            update="popup">${message(code:'ui.menu.preview')}</g:remoteLink></li>
            <li id='menubar-publish' style='display: ${!request.application.openCvInProgress ? request.application.active ? 'none' : 'block' : 'none'};'>
                <g:remoteLink method="GET" update="popup" controller="app" params="[application:request.application.id]" action="publish">
                    ${message(code:'ui.menu.file.publish')}
                </g:remoteLink>
            </li>
            <li id='menubar-unpublish' style='display: ${!request.application.openCvInProgress ? request.application.active ? 'block' : 'none' : 'none'};'>
                <g:remoteLink method="GET" update="popup" controller="app" params="[application:request.application.id]" action="unpublish">
                    ${message(code:'ui.menu.file.unpublish')}
                </g:remoteLink>
            </li>
            <li id='menubar-inprogress' style='display: ${request.application.openCvInProgress ? 'block' : 'none'};'>
                <g:remoteLink
                            controller="app"
                            action="inProgress"
                            onSuccess="jQuery.smartpaper.toggleActive(data.inProgress,data.active)"
                            title="${message(code:'ui.menu.openCvInProgress')}"
                            params="[application:request.application.id]">
                    ${message(code:'ui.menu.file.openCvInProgress')}
                </g:remoteLink>
            </li>
            <li>
                <span>${message(code:'ui.menu.help')}</span>
                <ul>
                  <li><a href="${grailsApplication.config.smartpaper.guide}" target="_blank">${message(code:'ui.menu.help.guide')}</a></li>
                  <li><a href="${grailsApplication.config.smartpaper.contact}" target="_blank">${message(code:'ui.menu.help.contact')}</a></li>
                </ul>
            </li>
            <sec:ifAnyGranted roles="ROLE_ADMIN">
                <li>
                    <g:remoteLink
                                controller="app"
                                action="reloadDatabase"
                                onSuccess="alert('Base rechargée');"
                                title="${message(code:'ui.menu.reload')}">
                        ${message(code:'ui.menu.reload')}
                    </g:remoteLink>
                </li>
            </sec:ifAnyGranted>
        </g:if>
    </ul>
   
    <ul>
        <li id="profile-name">
            <span>${request.currentUser.firstname+' '+request.currentUser.lastname}</span>
            <ul>
              <li><g:remoteLink
                      method="get"
                      controller="user"
                      action="edit"
                      update="popup"
                      title="${message(code:'ui.menu.account.information')}">${message(code:'ui.menu.account.information')}</g:remoteLink></li>
              <li><g:remoteLink method="get" controller="user" action="editPassword" update="popup" title="${message(code:'ui.menu.account.password')}">${message(code:'ui.menu.account.password')}</g:remoteLink></li>
              <li><a href="${createLink(controller:'logout')}" title="${message(code:'ui.menu.account.logout')}">${message(code:'ui.menu.account.logout')}</a></li>
            </ul>
        </li>
    </ul>
</div>