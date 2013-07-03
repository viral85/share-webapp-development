<!DOCTYPE html>
<html>
   <head>
      <title><g:layoutTitle default="${message(code:'ui.app.name')}"/></title>
      <r:layoutResources/>
   </head>
   <body>
        <g:if test="${request.application}">
           <div id="wrap">
        </g:if>
                <g:include view="/main/_menubar.gsp"/>
                <!--<g:include view="/main/_toolbar.gsp"/>--!>
                <g:layoutBody/>
                <r:layoutResources/>
        <g:if test="${request.application}">
            </div>
        </g:if>
        <ui:validationMessages/>
        <ui:handleGlobalErrorAjax/>
   </body>
</html>