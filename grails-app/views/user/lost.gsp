<html>
   <head>
       <title>${message(code:'ui.app.name')} - ${message(code:'ui.lost.title')}</title>
      <meta name="layout" content="simple"/>
      <r:require modules="smartpaper-css, smartpaper-js"/>
   </head>
   <body>
		<div id="title-login">  
			<h1>${message(code:'ui.lost.title')}</h1>
			<h2>${message(code:'ui.lost.subtitle')}</h2>
		</div>
		<g:form controller="user" action="retrieve" method="POST" class="user-form one-col" id="lost-form">
			<fieldset>
				<p>
					<input type='text' name='username' id='username' placeholder="${message(code:'account.username')}" class="required email" />
				</p>
				<p>
					<input type='text' name='firstname' id='firstname' placeholder="${message(code:'account.firstname')}" class="required"/>
				</p>
				<p style="text-align: right">
                    <g:link controller="login" action="auth" class='button gray bigrounded' title="${message(code:'ui.button.cancel')}">${message(code:'ui.button.cancel')}</g:link>
					<button type="submit" class='button orange bigrounded' title="${message(code:'ui.button.valid')}">${message(code:'ui.button.valid')}</button>
				</p>
			</fieldset>
		</g:form>
		<jq:jquery>
            jQuery(".lost-form").validate();
        </jq:jquery>
   </body>
</html>