<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
        <g:if test="${screen.video}">
            <ui:video poster="${null}" video="${screen.video.url}" height="${ui.screenHeight([screen:screen,app:screen.application])}" id='video-screen'/>
        </g:if>
   </body>
</html>