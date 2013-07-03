<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
    <g:if test="${screen.image}">
        <img src="${screen.image.url}"/>
   </g:if>
   </body>
</html>