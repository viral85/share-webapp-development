<%@ page import="org.codehaus.groovy.grails.commons.GrailsClassUtils; com.smartpaper.domains.media.VideoMedia; com.smartpaper.domains.media.ImageMedia" %>
<ul>
    <g:each in="${screen.medias}" var="mediaItem">
        <li class="item organise-element" elemid="${mediaItem.id}">
            <img src="${mediaItem.media.getThumbnail(null) != null ? mediaItem.media.getThumbnail(null) : createLinkTo(dir: 'images', file: 'no-'+GrailsClassUtils.getShortName(mediaItem.media.class).toLowerCase()+'.png')}" title="${mediaItem.media.name}" alt="+${mediaItem.media.name}"/>
            <span class="icon iconsmall move up" title="${message(code:'ui.application.menu.table.up')}">${message(code:'ui.application.menu.table.up')}</span>
            <span class="icon iconsmall move down" title="${message(code:'ui.application.menu.table.down')}">${message(code:'ui.application.menu.table.down')}</span>
            <p>${mediaItem.media.filename}</p>
            <p>
                <g:set var="type" value="${mediaItem.media instanceof VideoMedia ? '.video' : ''}"/>
                <span class="icon iconsmall edit" title="${message(code:'ui.screen.mediasList.change')}">${message(code:'ui.screen.mediasList.change'+type)}</span><span title="${message(code:'ui.screen.mediasList.change'+type)}" class="edit">${message(code:'ui.screen.mediasList.change'+type)}</span>
                <span class="icon iconsmall delete" title="${message(code:'ui.screen.mediasList.change')}">${message(code:'ui.screen.mediasList.delete'+type)}</span><span title="${message(code:'ui.screen.mediasList.change'+type)}" class="delete">${message(code:'ui.screen.mediasList.delete'+type)}</span>
            </p>
        </li>
    </g:each>
</ul>