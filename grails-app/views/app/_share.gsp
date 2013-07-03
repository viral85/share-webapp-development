<html>
   <head>
       <title>${message(code:'ui.app.name')} - ${message(code:'ui.share.title')} ${app.name}</title>
      <meta name="layout" content="simple"/>
      <r:require modules="smartpaper-css, smartpaper-js"/>
   </head>
   <body>
            <div id="title-share">
                <h1>${app.name}</h1>
                <h2><g:message code="ui.share.subtitle"/></h2>
                <h3><g:message code="ui.share.help.1"/></h3>
                    <a href="http://itunes.apple.com/fr/app/smartpaper/id474504591?mt=8" target="_blank">
                        <r:img dir="images" file="app-store.png" class="link-store"/>
                    </a>
                <h3 class="help"><g:message code="ui.share.help.2"/></h3>
           </div>

           <div class="description">
           </div>

           <div class="triggers">
              <g:each in="${app.triggers}" var="${trigger}">
                    <img src="${trigger.url}">
              </g:each>
           </div>
   </body>
</html>