<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
    <ul class="form">
        <g:each in="${screen.inputs}" var="input">
            <g:if test="${input.label}">
                <li>
                    <label>${input.label}${input.required ? '*' : ''} :</label>
                    <input type="text">
                </li>
            </g:if>
        </g:each>
        <input type="submit" value="${message(code:'ui.screen.form.phone.send')}" onclick="alert('${message(code:'ui.screen.form.phone.send.warning')}'); return false">
    </ul>
   </body>
</html>