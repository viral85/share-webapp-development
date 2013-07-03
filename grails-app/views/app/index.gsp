<html>
   <head>
      <title>${message(code:'ui.app.name')} - ${request.application.name}</title>
      <meta name="layout" content="main"/>
      <r:require modules="smartpaper-js, smartpaper-css, uploader, farbtastic"/>
   </head>
   <body>
        <g:include view="/main/_sidebar.gsp" model="[screen:currentScreen]"/>
        <div id="main">

        </div>
        <div id="popup" class="ui-popup"></div>
        <ui:setupVars/>
</html>