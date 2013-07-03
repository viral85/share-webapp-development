<html>
   <head>
      <meta name="layout" content="main"/>
      <r:require modules="smartpaper-css, smartpaper-js"/>
   </head>
   <body>
    <g:if test="${applications}">
        <ui:window title="ui.window.home.title" id="popup-accueil" width="600">
            <div id="popup-accueil" class="popup-accueil">
                <div class="two-cols">
                    <div class="col-one">
                        <ul>
                            <li>
                                <g:remoteLink method="get" class="button-icon black" title="${message(code:'ui.window.home.create.application')}" controller="app" action="create" update="popup">
                                    <span class="icon iconbig nouveaubig">${message(code:'ui.window.home.create.application')}</span>
                                </g:remoteLink>
                             <span>${message(code:'ui.window.home.create.application')}</span>
                            </li>
                            <li>
                                <g:remoteLink class="button-icon black" title="${message(code:'ui.window.home.open.application')}" controller="app" action="open" update="popup">
                                    <span class="icon iconbig ouvrirbig">${message(code:'ui.window.home.open.application')}</span>
                                </g:remoteLink>
                                <span>${message(code:'ui.window.home.open.application')}</span>
                            </li>
                        </ul>
                    </div>
                    <div class="col-two">
                        <p>${message(code:'ui.window.home.open.application.recent')}</p>
                        <ul>
                            <g:each in="${applications}" var="${app}">
                                <li>
                                    <g:link title="${app.name}" controller="app" action="index" params="[application:app.id]">
                                        <ui:truncate value="${app.name}" max="30"/>
                                    </g:link>
                            </g:each>
                        </ul>
                    </div>
                </div>
            </div>
        </ui:window>
    </g:if>
    <g:else>
        <ui:window title="ui.window.home.title" id="popup-accueil" width="500">
            <div id="popup-accueil" class="popup-accueil">
                <div class="one-col">
                    <ul>
                        <li>
                            <g:remoteLink method="get" class="button-icon black" title="${message(code:'ui.window.home.create.application')}" controller="app" action="create" update="popup">
                                <span class="icon iconbig nouveaubig">${message(code:'ui.window.home.create.application')}</span>
                            </g:remoteLink>
                         <span>${message(code:'ui.window.home.create.application')}</span>
                        </li>
                    </ul>
                </div>
            </div>
        </ui:window>
    </g:else>
   <div id="popup"></div>
   </body>
</html>