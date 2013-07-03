<ui:window title="ui.publish.title" height="520" width="700" modal="true" actionSubmit="${remoteFunction([controller:'app', action:'publish', params:[application:request.application.id], onSuccess:'jQuery.smartpaper.toggleInProgress()'])}">
    <div class="publish-details">
    <h1>${message(code:'ui.publish.check')}</h1>
    
        <p>${message(code:'ui.publish.text')}</p>
        <p>
            <ul>
                <li>${message(code:'ui.publish.lastUpdate')} ${app.lastUpdated ? g.formatDate(formatName:'ui.date.format', date:app.lastUpdated) : message(code:'ui.publish.never')}</li>
                <li>${message(code:'ui.publish.lastPublish')} ${app.lastPublish ? g.formatDate(formatName:'ui.date.format', date:app.lastPublish) : message(code:'ui.publish.never')}</li>
                <li>${message(code:'ui.publish.orphan')} ${orphans?.size()?:0}</li>
                <g:each in="${orphans}" var="screen">
                    <ul>
                        <li>${message(code:'ui.publish.page')} ${screen.order} :  ${screen.name}<button class="button black" onclick="jQuery('#popup').dialog('close');jQuery('#page-list li[elemid=${screen.id}]').click()">${message(code:'ui.button.edit')}</button></li>
                    </ul>
                </g:each>
                <li>
                    ${message(code:'ui.publish.trigger')} : ${app.triggers?.size()?:0}
                    <g:if test="${app.triggers?.size() == 0}">
                        <p class="alert-info">
                            <img src=''>${message(code:'ui.publish.warning.triggers')}
                            <button class="button orange" onclick="jQuery('#menubar-trigger a').click()">${message(code:'ui.publish.add.trigger')}</button>
                        </p>
                    </g:if>
                </li>
            </ul>
        </p>
    </div>
</ui:window>