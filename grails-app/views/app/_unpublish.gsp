<ui:window title="ui.unpublish.title" height="200" width="400" modal="true" actionSubmit="${remoteFunction([controller:'app', action:'unpublish', params:[application:request.application.id], onSuccess:'jQuery.smartpaper.toggleInProgress();'])}">
    <p class='unpublish-message'>
        ${message(code:'ui.unpublish.message')}
    </p>
</ui:window>