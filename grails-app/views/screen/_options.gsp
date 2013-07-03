<g:set var="appid" value="${request.application.id}"/>
<g:set var="view" value="options"/>

<g:formRemote name="radio-form" class="options-active switch" update="main-config-content" url="[controller:'screen',action:'update',params:[application:appid,view:view,'screen.id':screen.id,phone:false]]">
    <input type="radio" onchange="jQuery(this).parent().submit()" id="title-enable" name="screen.hasOptions" value="true" ${screen.hasOptions?'checked="checked"':''}/>
    <input type="radio" onchange="jQuery(this).parent().submit()" id="title-disable" name="screen.hasOptions" value="false" ${screen.hasOptions?'':'checked="checked"'}/>
    <label for="title-enable" class="cb-enable selected"><span>${message(code:'ui.screen.options.enable')}</span></label><label for="title-disable" class="cb-disable"><span>${message(code:'ui.screen.options.disable')}</span></label>
</g:formRemote>

<g:if test="${screen.hasOptions}">
    <h2><g:message code="ui.screen.options.title"/></h2>

    <div id="options-table">
        <g:include view="/screen/_tableOptions.gsp" model="[screen:screen,init:true]"/>
    </div>

    <g:remoteLink controller="screen" action="options" update="options-table" method="post" params="[_action:'add',application:appid,id:screen.id]">
        <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
        <span><g:message code="ui.screen.options.add"/></span>
    </g:remoteLink>

    <h3><g:message code="ui.screen.background"/></h3>
    <div class="background-details">
        <g:if test="${!screen.backgroundOptions.image}">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
					screen:screen.id,
                    application:appid,
                    filter:'Image',
                    'update.controller':'screen',
                    'update.action':'update',
                    'update.value':'backgroundOptions',
                    'update.content':'main-config-content',
                    'update.params':'view='+view+'&phone=false&screen.id='+screen.id
                ]"
                update="popup">
                    <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                    <span><g:message code="ui.screen.image.add"/></span>
            </g:remoteLink>
        </g:if>
        <g:else>
            <div class="image"><img src="${screen.backgroundOptions.image.url}" title="${screen.backgroundOptions.image.name}.${screen.backgroundOptions.image.extension}"/></div>
            <span class="details filename">${screen.backgroundOptions.image.name}.${screen.backgroundOptions.image.extension}</span>
            <p class="details">
				<g:remoteLink
					controller="media"
					action="explorer"
					params="[
					    screen:screen.id,
						application:appid,
						filter:'Image',
						'update.controller':'screen',
						'update.action':'update',
						'update.value':'backgroundOptions',
						'update.content':'main-config-content',
						'update.params':'view='+view+'&phone=false&screen.id='+screen.id
					]"
					update="popup">
					<span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
					<span><g:message code="ui.screen.image.change"/></span>
				</g:remoteLink>
            </p>
            <p class="details">
				<g:remoteLink
						controller="screen"
						action="update"
						update="main-config-content"
						params="[application:appid,view:view,phone:false,'screen.id':screen.id,'backgroundOptions':'false']">
					<span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
					<span><g:message code="ui.screen.image.delete"/></span>
				</g:remoteLink>
            </p>
        </g:else>
    </div>
    <ui:colorPicker
            name='picker-options'
            submit="[controller:'screen', onSuccess:'jQuery.smartpaper.refreshPhone();', action:'update', params:'\'screen.backgroundOptions.color=\'+value+\'&screen.id='+screen.id+'&application='+request.application.id+'\'']"
            label="ui.screen.background.color"
            default="${screen.backgroundOptions.color}"/>
</g:if>
<jq:jquery>
    jQuery( function(){
            if($('#title-enable').is(':checked')){
                $('.cb-disable').removeClass('selected');
				$('.cb-enable').addClass('selected');
            }
            else{
                $('.cb-enable').removeClass('selected');
				$('.cb-disable').addClass('selected');
            }
			$(".cb-enable").click(function(){
				var parent = $(this).parents('.switch');
				$('.cb-disable',parent).removeClass('selected');
				$(this).addClass('selected');
				$('.checkbox',parent).attr('checked', true);
			});
			$(".cb-disable").click(function(){
				var parent = $(this).parents('.switch');
				$('.cb-enable',parent).removeClass('selected');
				$(this).addClass('selected');
				$('.checkbox',parent).attr('checked', false);
			});
		});
</jq:jquery>