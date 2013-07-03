<g:set var="appid" value="${request.application.id}"/>
<g:set var="app" value="${request.application}"/>
<g:set var="view" value="menu"/>

<g:formRemote name="display-forms" class="menu-active switch" update="main-config-content" url="[controller:'app',action:'common',params:[application:appid,view:view,phone:false]]">
    <input type="radio" onchange="jQuery(this).parent().submit()" id="menu-enable" name="app.hasMenu" value="true" ${app.hasMenu?'checked="checked"':''}/>
    <input type="radio" onchange="jQuery(this).parent().submit()" id="menu-disable" name="app.hasMenu" value="false" ${app.hasMenu?'':'checked="checked"'}/>
    <label for="menu-enable" class="cb-enable selected"><span>${message(code:'ui.application.menu.enable')}</span></label><label for="menu-disable" class="cb-disable"><span>${message(code:'ui.application.menu.disable')}</span></label>
</g:formRemote>

<g:if test="${app.hasMenu}">
    <h2 class="first"><g:message code="ui.application.menu.title"/></h2>
    <div id="menu-table">
        <g:include view="app/_tableMenu.gsp" model="[init:true]"/>
    </div>
</g:if>

<jq:jquery>
        jQuery('#content-menu').validate();
        if($('#menu-enable').is(':checked')){
            $('.cb-disable').removeClass('selected');
            $('.cb-enable').addClass('selected');
        }
        else{
            $('.cb-enable').removeClass('selected');
            $('.cb-disable').addClass('selected');
        }
        jQuery( function(){
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
