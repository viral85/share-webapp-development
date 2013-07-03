<%@ page import="com.smartpaper.utils.ConstantUtils" %>
<g:if test="${phone}">
    <div id="main-preview">
        <h1>${message(code:'ui.screen')} ${screen.order} : ${ui.truncate(value:screen.name, max:13)}</h1><div id="zoom-phone"></div>
		<div class="phone" id="iphone">
			<ul>
				<li>
					<g:remoteLink controller="screen" onSuccess="jQuery.smartpaper.screenFrom('title')" action="show" update="main-config-content" params="[id:screen.id,application:request.application.id,phone:false,view:'title']" class="button-icon black">
						<span onmouseover="jQuery('.iframe-content').contents().find('#overlay-top').css('display','block')" 
							  onmouseout="jQuery('.iframe-content').contents().find('#overlay-top').css('display','none')" class="title icon iconmedium plus ${view == 'title' ? 'active' : ''}">+</span>
					</g:remoteLink>
				</li>
				<li>
					<g:remoteLink controller="screen" onSuccess="jQuery.smartpaper.screenFrom('content')" action="show" update="main-config-content" params="[id:screen.id,application:request.application.id,phone:false,view:'content']" class="button-icon black">
						<span onmouseover="jQuery('.iframe-content').contents().find('#overlay-center').css('display','block')"
							  onmouseout="jQuery('.iframe-content').contents().find('#overlay-center').css('display','none')" class="content icon iconmedium plus ${view?.contains('content') ? 'active' : ''}">+</span>
					</g:remoteLink>
				</li>
				<li>
					<g:remoteLink controller="screen" onSuccess="jQuery.smartpaper.screenFrom('options')" action="show" update="main-config-content" params="[id:screen.id,application:request.application.id,phone:false,view:'options']" class="button-icon black">
						<span onmouseover="jQuery('.iframe-content').contents().find('#overlay-bottom').css('display','block')"
							  onmouseout="jQuery('.iframe-content').contents().find('#overlay-bottom').css('display','none')" class="options icon iconmedium plus ${view == 'options' ? 'active' : ''}">+</span>
					</g:remoteLink>
				</li>
			</ul>
		</div>
       <iframe id='iframe-iphone' class="iframe-content" src="${createLink(controller:'screen', action:'preview', params:[application:request.application.id,id:screen.id])}"></iframe>
    </div>
    <div id='main-config'>
        <div id="model-select">
            <ui:selectModel id="screen-model" disabled="true" value="${screen.class.type?.toString()}"/>
        </div>
        <div id="main-config-content">
</g:if>
<div id="main-config-screen">
    <g:include view="${view}.gsp" model="[screen:screen,app:request.application]"/>
</div>

<g:if test="${params.view == 'content'}">
    <g:include view="/screen/_background.gsp" model="[screen:screen,view:params.view,app:request.application]"/>
</g:if>

<g:if test="${phone}">
        </div>
    </div>
</g:if>
<jq:jquery>
   <g:if test="${!phone}">
        jQuery.smartpaper.refreshPhone();
   </g:if>
   <g:else>
       jQuery('#main-config').jScrollPane({autoReinitialise:true, verticalDragMaxHeight:30, verticalGutter:-10});
       jQuery.smartpaper.setCurrentPage(${screen.id},'${screen.getThumbnail(null)}',${screen.nbLinks});
       jQuery.smartpaper.initPhoneZoom();
   </g:else>
    if(!jQuery('#page-list > li[elemid='+${screen.id}+'] img').length){
        var element = jQuery('<img/>').attr('src','${screen.getThumbnail(null)}${screen.getThumbnail(null)?.contains('?') ? '&' : '?'}nocache='+new Date().getTime());
        element.insertBefore(jQuery('#page-list > li[elemid='+${screen.id}+'] .background-image'));
    }
    jQuery.smartpaper.toggleActive(${request.application.openCvInProgress},${request.application.active});
</jq:jquery>
