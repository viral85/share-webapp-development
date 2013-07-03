<ui:window title="ui.application.preview.title" modal="true" width="600">
   <div id="app-preview">
		<div class="phone" id="iphone">
		</div>
        <iframe id='app-frame-iphone'
               class="iframe-content"
               src="${createLink(controller:'screen', action:'preview', params:[application:request.application.id,id:homeScreen,scale:params.scale])}">
        </iframe>
    </div>
</ui:window>