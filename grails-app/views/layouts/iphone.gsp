<%@ page import="com.smartpaper.domains.screen.DetailedViewScreen; com.smartpaper.domains.media.VideoMedia" %>
<!DOCTYPE html>
<g:set var="screen" value="${request.screen}"/>
<g:set var="app" value="${request.application?:screen.application}"/>
<html>
   <head>
       <title><g:layoutTitle default="${message(code:'ui.app.name')}"/></title>
       <meta http-equiv="X-UA-Compatible" content="chrome=IE8">
       <r:layoutResources/>
   </head>
   <body>
        <g:if test="${params.scale != '1'}">
            <style type="text/css">
               html {
                    -webkit-transform:scale(${params.scale});
                    -moz-transform:scale(${params.scale});
               }
            </style>
        </g:if>
        <div style="height:44px;${app.backgroundHeader?.image ? 'background:url('+app.backgroundHeader.image.url+') no-repeat;':'background:url(http://generator.smartpaper-app.com/resources/1/medias/30.jpg) no-repeat;'} ${app.backgroundHeader?.color ? 'background-color:'+app.backgroundHeader?.color+';':''} width: 320px;" class="header">
            <div class="logo" style="${app.logo?.url ? 'background:url('+app.logo.url+') center no-repeat;':''}">
                <ul class="socialLinks">
                    <g:each in="${app.socialLinks}" var="socialLink">
                        <li class="socialLink" title='${socialLink.name}'><a title="${socialLink.name}" href="${socialLink.url}" target="_blank">${r.img([dir:'/images/social/',file:socialLink.logo])}</a></li>
                    </g:each>
                </ul>
                <div class="infoLogo" onclick="alert('Affichage des informations sur Smartpaper');">
                </div>
            </div>
        </div>
        <g:if test="${screen}">
            <div class="wrap" style="height:${app.hasMenu ? 367 : 416 }px; ${screen.background.image ? 'background:url('+screen.background.image.url+') no-repeat;':''} ${screen.background.color ? 'background-color:'+screen.background.color+';':''}">
            	<div class="overlay" id="overlay-top" style="height:${screen.hasTitle?screen.backgroundTitle.height:44}px">${message(code:'ui.phone.zone.title')}</div>
   				<div class="overlay" id="overlay-center">${message(code:'ui.phone.zone.center')}</div>
   				<div class="overlay" id="overlay-bottom">${message(code:'ui.phone.zone.options')}</div>
               <g:if test="${screen.hasTitle}">
                    <div class="title" style="height:${screen.backgroundTitle.height}px;${screen.backgroundTitle.image ? 'background:url('+screen.backgroundTitle.image.url+') no-repeat;':''} ${screen.backgroundTitle.color ? 'background-color:'+screen.backgroundTitle.color+';':''}"></div>
                    <div class="title-content" style="height: ${screen.backgroundTitle.height}px;">${screen.contentTitle}</div>
               </g:if>
               <div class="content" style="top:${(screen.hasTitle?screen.backgroundTitle.height:0)+'px'}; bottom:${(screen.hasOptions?46:0)+'px;'}; position: absolute;">
                    <g:layoutBody/>
               </div>
               <g:if test="${screen.hasOptions}">
                    <div class="options" style="height:46px;${screen.backgroundOptions.image ? 'background:url('+screen.backgroundOptions.image.url+') no-repeat;':''} ${screen.backgroundOptions.color ? 'background-color:'+screen.backgroundOptions.color+';':''}">
                        <table class="options-items" cellpadding="0" cellspacing="0">
                            <tr>
                            <g:each in="${screen.options}" var="option">
                                <g:if test="${option.icon && option.link}">
                                    <td><a class="option-item" href="${createLink(controller:'screen', action:'preview', params:[application:app.id,id:option.link.id])}"><img src="${option.link.id == screen.id ? (option.iconActive ? option.iconActive.url : option.icon.url) : option.icon.url}"></a></td>
                                </g:if>
                            </g:each>
                            </tr>
                        </table>
                    </div>
               </g:if>
            </div>
            <jq:jquery>
                <g:if test="${!(screen instanceof DetailedViewScreen)}">
                    jQuery(".content").overscroll({direction:'vertical'});
                </g:if>
                jQuery('#overlay-top').css('height', ${screen.backgroundTitle.height-2});
                jQuery('#overlay-center').css('top', ${screen.hasTitle ? screen.backgroundTitle.height : 0});
                jQuery('#overlay-center').css('height', ${ui.screenHeight([screen:screen,app:app])});
            </jq:jquery>
        </g:if>
        <g:if test="${app.hasMenu}">
            <table class="iphone-menu" cellpadding="0" cellspacing="0">
                <tr>
                    <g:each in="${app.menuItems}" var="menuItem">
                        <g:if test="${menuItem.icon && menuItem.name && menuItem.link}">
                            <td ><a href="${createLink(controller:'screen', action:'preview', params:[application:app.id,id:menuItem.link.id,scale:params.scale])}"><span class="icon" style="background: url(${menuItem.icon.url}) center no-repeat;"></span><span>${ui.truncate(value:menuItem.name,max:8)}</span></a></td>
                        </g:if>
                    </g:each>
                </tr>
            </table>
        </g:if>
        <jq:jquery>
            <g:if test="${!screen instanceof VideoMedia}">
                jQuery(".content").disableSelection();
            </g:if>
        </jq:jquery>
        <r:layoutResources/>
   </body>
</html>