<g:set var="view" value="content"/>
<h2><g:message code="ui.screen.image.title"/></h2>

<g:remoteLink
        controller="media"
        action="explorer"
        params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'Image',
                    'update.controller':'image',
                    'update.action':'changeImage',
                    'update.content':'main-config-screen',
                    'update.onSuccess':'jQuery.smartpaper.refreshPhone()',
                    'update.params':'phone=false&id='+screen.id]"
        update="popup">
    <button class="button-icon black mini"><span class="icon iconsmall miniplus  ${screen.image ?'active':''}">+</span></button>
    <span><g:message code="${screen.image ? 'ui.screen.image.change' : 'ui.screen.image.add'}"/></span>
</g:remoteLink>

<g:if test="${screen.image}">
    <div class="image-details">
        <div class="image"><img src="${screen.image.url}" align="left" width="200px"/></div>
        <span class="details filename">${screen.image.filename}</span>
        <span class="details">${message(code:'media.dim',args:[screen.image.width.toString().trim(),screen.image.height.toString().trim()])}</span>
        <span class="details">${message(code:'media.size')} ${ui.filesize(size:screen.image.size)}</span>
        <span class="details">${message(code:'media.dateCreated')} ${g.formatDate(date:screen.image.dateCreated, formatName:'ui.date.format')}</span>
    </div>
</g:if>