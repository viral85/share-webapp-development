<%@ page import="com.smartpaper.utils.ConstantUtils" %>
<g:if test="${phone}">
    <div id="main-preview">
        <h1>${message(code:'ui.application.common.title')}</h1><div id="zoom-phone"></div>
		<div class="phone" id="iphone">
			<ul class="common">
				<li class="common">
					<g:remoteLink controller="app" onSuccess="jQuery.smartpaper.screenFrom('header')" action="common" update="main-config-content" params="[application:request.application.id,phone:false,view:'header']" class="button-icon black">
						<span class="header icon iconmedium plus ${view == '_header.gsp' ? 'active' : ''}">+</span>
					</g:remoteLink>
				</li>
				<li>
					<g:remoteLink controller="app" onSuccess="jQuery.smartpaper.screenFrom('menu')" action="common" update="main-config-content" params="[application:request.application.id,phone:false,view:'menu']" class="button-icon black">
						<span class="menu icon iconmedium plus ${view == '_menu.gsp' ? 'active' : ''}">+</span>
					</g:remoteLink>
				</li>
			</ul>
		</div>
       <iframe id='iframe-iphone' class="iframe-content" src="${createLink(mapping:'defaultApp', controller:'app', action:'commonPreview', params:[application:request.application.id])}"></iframe>
    </div>
    <div id='main-config'>
        <div id="main-config-content">
</g:if>

    <g:include view="/app/common/${view}" model="[app:app]"/>

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
       jQuery.smartpaper.initPhoneZoom();
       jQuery.smartpaper.setCurrentPage();
   </g:else>
</jq:jquery>
