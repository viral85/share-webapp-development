<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
   <g:javascript>
   function target(id){
        document.location = "${createLink(controller:'screen', action:'preview', params:[application:screen.application.id,scale:params.scale])}&id="+id;
   }
   </g:javascript>
    ${screen.content?.replaceAll('="smartpaper://target','="javascript:target')}
   </body>
</html>