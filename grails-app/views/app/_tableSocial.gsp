<g:set var="appid" value="${request.application.id}"/>
<g:if test="${app.socialLinks}">
    <table id="social-list" class="table-list">
        <tr class="table-title">
            <th width="25%">${message(code:'ui.application.header.social.list.network')}</th>
            <th width="25%">${message(code:'ui.application.header.social.list.logo')}</th>
            <th width="50%">${message(code:'ui.application.header.social.list.url')}</th>
        </tr>
        <g:each in="${app.socialLinks}" var="link">
            <tbody class="content">
                <tr class="item" elemid="${link.id}">
                    <td title="${link.name}">
                        ${link.name}
                    </td>
                    <td title="${link.name}">
                        <img alt="${link.name}" title="${link.name}" src='${r.resource([dir:'/images/social/',file:link.logo])}' />
                    </td>
                    <td>
                        <input title="${link.url}" type="text" value="${link.url}" class="dynamic-with-margin" disabled="disabled">
                        <span class="icon iconmini edit"></span>
                        <span class="icon iconmini delete"></span>
                    </td>
                </tr>
            </tbody>
        </g:each>
    </table>
</g:if>
<g:if test="${app.socialLinks?.size() < 3}">
    <g:remoteLink controller="app" action="socialLinks" update="popup" method="post" params="[_action:'popup',application:appid]">
        <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
        <span><g:message code="ui.screen.options.add"/></span>
    </g:remoteLink>
</g:if>
<jq:jquery>
    <g:if test="${init}">
        jQuery('#social-list td span.delete').die().live('click',function(){
            <g:remoteFunction controller="app" update="social-table" action="socialLinks" params="'_action=delete&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${appid}'"/>
        });
        jQuery('#social-list td span.edit').die().live('click',function(){
            <g:remoteFunction controller="app" update="popup" action="socialLinks" params="'_action=update&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${appid}'"/>
        });
    </g:if>
    <g:else>
        jQuery.smartpaper.refreshPhone();
    </g:else>
</jq:jquery>