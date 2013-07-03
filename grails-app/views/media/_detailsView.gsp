<%@ page import="org.codehaus.groovy.grails.commons.GrailsClassUtils; com.smartpaper.domains.media.ImageMedia" %>
<img src="${media.getThumbnail(null) != null? media.getThumbnail(null) :createLinkTo(dir: 'images', file: 'no-'+GrailsClassUtils.getShortName(media.class).toLowerCase()+'-96.png')}" title="${media.filename}"  alt="${media.filename}"/>
<div class="details">
	<p>${message(code:'media.size')} ${ui.filesize(size:media.size)}</p>
	<p>${media.dateCreated?.format('dd MMM yyyy')}</p>
	<p><g:link controller="media" action="download" params="[application:media.application.id,id:media.id]" class="icon clipper">clipper</g:link></p>
	<p>${message(code:'ui.media.used', args:[media.used])}</p>
</div>
<h4>${media.filename}</h4>

<g:if test="${media instanceof ImageMedia}">
    <p>${message(code:'media.dim',args:[media.width.toString().trim(),media.height.toString().trim()])}</p>
</g:if>

<g:if test="${editable}">
<p><g:remoteLink class="button orange" before="if(!confirm('${message(code:'ui.media.confirm.delete')}')){return false;}" onFailure="XMLHttpRequest.overrideError = true; alert(XMLHttpRequest.responseText)" onSuccess="jQuery.smartpaper.deleteMedia(data);" url="[controller:'media', action:'delete', params:[application:media.application.id,id:media.id]]">${message(code:'ui.button.delete')}</g:remoteLink></p>
</g:if>
