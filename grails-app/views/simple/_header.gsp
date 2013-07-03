<div id="header">
    <g:link controller="app" action="index" class="logo">
        <img src="${createLinkTo(dir: 'images', file: 'logo.png')}">
    </g:link>
    <a href="http://smartpaper-app.com/">
        <p>${message(code:'ui.header.home')}</p>
        <p>${message(code:'ui.header.home.subtitle')}</p>
    </a>
    <a href="${grailsApplication.config.smartpaper.guide}">
        <p>${message(code:'ui.header.presentation')}</p>
        <p>${message(code:'ui.header.presentation.subtitle')}</p>
    </a>
    <g:link controller="login" action="auth">
        <p>${message(code:'ui.simple.menu.login')}</p>
        <p>${message(code:'ui.app.name')}</p>
    </g:link>
</div>