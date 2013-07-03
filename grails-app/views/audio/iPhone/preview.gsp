<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
        <g:if test="${screen.audio}">
            <ui:audio audio="${screen.audio.url}" id='audio-screen' phone="true"/>
        </g:if>
   </body>
</html>