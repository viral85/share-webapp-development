
<meta name="layout" content="simple"/>
<r:require modules="smartpaper-css, smartpaper-js"/>

<div id="app-preview">
    <div class="phone" id="iphone">
    </div>
    <iframe id='app-frame-iphone'
            class="iframe-content"
            src="${createLink(controller:'screen', action:'sharePreview', params:[appId:params.application,id:homeScreen,scale:1])}">
    </iframe>
</div>
