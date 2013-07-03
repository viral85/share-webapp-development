<!DOCTYPE html>
<html>
   <head>
      <title><g:layoutTitle default="${message(code:'ui.app.name')}"/></title>
      <r:layoutResources/>
   </head>
   <body>
      <g:include view="/simple/_header.gsp"/>
      <g:layoutBody/>
      <r:layoutResources/>
      <ui:validationMessages/>
   </body>
</html>