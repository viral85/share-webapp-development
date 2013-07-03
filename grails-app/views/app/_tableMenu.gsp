<g:set var="appid" value="${request.application.id}"/>
<g:if test="${app.menuItems}">
<table id="menu-list" class="table-list">
    <tr class="table-title">
        <th width="25%">${message(code:'ui.application.menu.table.icon')}</th>
        <th width="25%">${message(code:'ui.application.menu.table.title')}</th>
        <th width="30%">${message(code:'ui.application.menu.table.link')}</th>
        <th width="10%">${message(code:'ui.application.menu.table.position')}</th>
        <th width="10%">${message(code:'ui.application.menu.table.delete')}</th>
    </tr>
    <g:each in="${app.menuItems}" var="menuItem">
        <tbody class="content">
            <tr class="item" elemid="${menuItem.id}">
                <td title="${menuItem.name}" class='${menuItem.icon ? 'iphone-menu' : ''}'>
                    <g:if test="${menuItem.icon}">
                        <span class="icon icon-menu" style='background:url(${menuItem.icon.url}) center no-repeat;' title="${menuItem.icon.name}"></span><span>${ui.truncate([value:menuItem.name,max:15])}</span>
                    </g:if>
                    <g:else>
                        <button class="icon-menu button small black"><g:message code="ui.application.menu.table.choose.icon"/></button>
                    </g:else>
                </td>
                <td title="${menuItem.name}">
                    <input title="${menuItem.name}" class="dynamic menu-title" type="text" value="${menuItem.name}">
                </td>
                <td>
                    <g:if test="${menuItem.link}">
                        <span class="editpage" title="${menuItem.link.name}">${ui.truncate([value:menuItem.link.name,max:15])}</span>
                    </g:if>
                    <g:else>
                        <button class="editpage button small black"><g:message code="ui.application.menu.table.choose.screen"/></button>
                    </g:else>
                </td>
                <td>
                    <span class="icon iconsmall move up" title="${message(code:'ui.application.menu.table.up')}">${message(code:'ui.application.menu.table.up')}</span>
                    <span class="icon iconsmall move down" title="${message(code:'ui.application.menu.table.down')}">${message(code:'ui.application.menu.table.down')}</span>
                </td>
                <td>
                    <span class="icon iconmini delete"></span>
                </td>
            </tr>
        </tbody>
    </g:each>
</table>
</g:if>
<g:if test="${app.menuItems?.size() < 5}">
    <g:remoteLink controller="app" action="menuItem" update="menu-table" params="[application:request.application.id,_action:'add']">
        <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
        <span><g:message code="ui.application.menu.add"/></span>
    </g:remoteLink>
</g:if>
<jq:jquery>
    <g:if test="${init}">
        jQuery('#menu-list td span.delete').die().live('click',function(){
            <g:remoteFunction controller="app" update="menu-table" action="menuItem" params="'&_action=delete&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${appid}'"/>
        });
        jQuery('#menu-list td .editpage').die().live('click',function(){
            var elem = $(this).parents('tr').addClass("selected");
                $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="screen"
                    update="popup"
                    action="explorer"
                    before="if(!jQuery('#menu-list tr.selected').attr('elemid')){ return false; }"
                    params="[
                        application:appid,
                        'update.controller':'app',
                        'update.action':'menuItem',
                        'update.value':'link',
                        'update.content':'menu-table',
                        'update.params':'_action=update&id=\'+jQuery(\'#menu-list tr.selected\').attr(\'elemid\')+\''
                    ]"/>
        });
        jQuery('#menu-list td .icon-menu').die().live('click',function(){
            var elem = $(this).parents('tr').addClass("selected");
            $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="media"
                    update="popup"
                    action="explorer"
                    before="if(!jQuery('#menu-list tr.selected').attr('elemid')){ return false; }"
                    params="[
                        application:appid,
                        'update.controller':'app',
                        'update.action':'menuItem',
                        'update.value':'icon',
                        'update.content':'menu-table',
                        'update.params':'_action=update&id=\'+jQuery(\'#menu-list tr.selected\').attr(\'elemid\')+\''
                    ]"/>
        });
        jQuery('#menu-list td .menu-title').die().live('change',function(){
            var elem = $(this).parents('tr').addClass("selected");
            $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="app"
                    action="menuItem"
                    update="menu-table"
                    before="if(!jQuery('#menu-list tr.selected').attr('elemid')){ return false; }"
                    params="\'application=${appid}&id=\'+jQuery(\'#menu-list tr.selected\').attr(\'elemid\')+\'&_action=update&menuItem.name=\'+jQuery(this).val()"/>
        });

        jQuery.smartpaper.moveRowPosition('#menu-list',function(position){
            <g:remoteFunction
                    controller="app"
                    action="menuItem"
                    before="if(!jQuery('#menu-list tr.selected').attr('elemid')){ return false; }"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${appid}&id=\'+jQuery(\'#menu-list tr.selected\').attr(\'elemid\')+\'&_action=update&menuItem.order=\'+position"/>
        });

    </g:if>
    <g:else>
        jQuery.smartpaper.refreshPhone();
    </g:else>
</jq:jquery>