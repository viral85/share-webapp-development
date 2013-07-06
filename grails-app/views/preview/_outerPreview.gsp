

<ui:window title="ui.application.preview.title" modal="true" width="600">
        <div id="app-preview">
            <div class="phone" id="iphone">
            </div>
            <iframe id='app-frame-iphone'
                    class="iframe-content"
                    src="${createLink(controller:'preview', action:'previewOuter', params:[appId:params.application,id:homeScreen,scale:1])}">
            </iframe>
        </div>
</ui:window>
