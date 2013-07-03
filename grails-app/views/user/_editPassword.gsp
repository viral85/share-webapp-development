<ui:window title="ui.user.editPassword.title" modal="true" width="470">
    <g:formRemote name="editPassword" before="if (!jQuery('#edit-form').valid()){ return false; }" onFailure="jQuery.smartpaper.displayPopupErrors(XMLHttpRequest);" onSuccess="jQuery('#popup').dialog('close'); " url="[controller:'user', action:'editPassword']" id="edit-form">
        <p>
            <input id="password" type="password" placeholder="${message(code:'ui.user.editPassword.new')}" title="${message(code:'ui.user.editPassword.new')}"  name="user.password">
        </p>
        <p>
            <input type="password" placeholder="${message(code:'ui.user.editPassword.confirm')}" title="${message(code:'ui.user.editPassword.confirm')}"  name="confirm.password">
        </p>
        <div class="error"></div>
    </g:formRemote>
    <jq:jquery>
		jQuery("#edit-form").validate({
			rules:{
				"user.password":{
					required:true,
					minlength:5
				},
				"confirm.password":{
					required:true,
					minlength:5,
					equalTo:"#password"
				}
			}
		});
	</jq:jquery>
</ui:window>