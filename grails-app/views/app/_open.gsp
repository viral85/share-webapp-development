<ui:explorer
        title="ui.application.create.title"
        width="700"
        resizable="true"
        className="app-explorer"
        in="${applications}"
        view="icon"
        onSubmit="document.location = '${createLink(controller:'app', action:'index')}/'+selected;"
        detailsView="[controller:'app', action:'detailsView', params:'\'id=\'+elem.attr(\'elemid\')']"/>