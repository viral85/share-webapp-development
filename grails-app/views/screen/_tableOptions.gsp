<g:set var="appid" value="${request.application.id}"/>
<g:if test="${screen.options}">
    <table id="options-list" class="table-list">
        <tr class="table-title">
            <th width="25%">${message(code:'ui.screen.options.list.normal')}</th>
            <th width="25%">${message(code:'ui.screen.options.list.actif')}</th>
            <th width="45%">${message(code:'ui.screen.options.list.link')}</th>
            <th width="5%">${message(code:'ui.screen.options.list.delete')}</th>
        </tr>
        <g:each in="${screen.options}" var="option">
            <tbody class="content">
                <tr class="item" elemid="${option.id}">
                    <td>
                        <g:if test="${option.icon}">
                            <img src="${option.icon.url}"/>
                            <span for="normal" class="image iconmini icon edit"></span>
                        </g:if>
                        <g:else>
                            <button for="normal" class="image button small black"><g:message code="ui.screen.options.choose.image"/></button>
                        </g:else>
                    </td>
                    <td>
                        <g:if test="${option.iconActive}">
                            <img src="${option.iconActive.url}"/>
                            <span for="active" class="image iconmini icon edit"></span>
                        </g:if>
                        <g:else>
                            <button for="active" class="image button small black"><g:message code="ui.screen.options.choose.image"/></button>
                        </g:else>
                    </td>
                    <td>
                        <g:if test="${option.link}">
                            <span class="editpage" title="${option.link.name}">${ui.truncate([value:option.link.name,max:15])}</span>
                        </g:if>
                        <g:else>
                            <button class="button small black editpage"><g:message code="ui.screen.options.choose.screen"/></button>
                        </g:else>
                    </td>
                    <td><span class="icon iconmini delete"></span></td>
                </tr>
            </tbody>
        </g:each>
    </table>
</g:if>
<jq:jquery>
    <g:if test="${init}">

        jQuery('#options-list td .image').die().live('click',function(){
            var elem = $(this).addClass("selected");
            elem.siblings().removeClass("selected");
            <g:remoteFunction
                controller="media"
                action="explorer"
                params="[
                    application:appid,
                    filter:'Image',
                    'update.controller':'screen',
                    'update.action':'options',
                    'update.value':'icon',
                    'update.content':'options-list',
                    'update.params':'screen.id='+screen.id+'&_action=update&id=\'+jQuery(\'#options-list td .image.selected\').parents(\'tr.item\').attr(\'elemid\')+\'&for=\'+jQuery(\'#options-list td .image.selected\').attr(\'for\')+\''
                ]"
                update="popup"/>
        });
        jQuery('#options-list td span.delete').die().live('click',function(){
            <g:remoteFunction controller="screen" update="options-list" action="options" params="'screen.id=${screen.id}&_action=delete&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${appid}'"/>
        });
        jQuery('#options-list td .editpage').die().live('click',function(){
            var elem = $(this).addClass("selected");
            elem.siblings().removeClass("selected");
            <g:remoteFunction
                    controller="screen"
                    update="popup"
                    action="explorer"
                    params="[
                        application:appid,
                        'update.controller':'screen',
                        'update.action':'options',
                        'update.value':'link',
                        'update.content':'options-list',
                        'update.params':'screen.id='+screen.id+'&_action=update&id=\'+jQuery(\'#options-list td .editpage.selected\').parents(\'tr.item\').attr(\'elemid\')+\''
                    ]"/>
        });
    </g:if>
    <g:else>
        jQuery.smartpaper.refreshPhone();
    </g:else>
</jq:jquery>
