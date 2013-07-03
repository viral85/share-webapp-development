<g:set var="appid" value="${request.application.id}"/>

<div id="sidebar">
    <div id="sidebar-content">
        <h3 class="app-name" title='${request.application.name}'>
            <g:if test="${request.application.active}">
                <div class="icon online">${message(code:'application.status.online')}</div>
                ${ui.truncate(value:request.application.name,max:15)}
            </g:if>
            <g:else>
                <div class="icon offline">${message(code:'application.status.offline')}</div>
                ${ui.truncate(value:request.application.name,max:15)}
            </g:else>
        </h3>
        <hr/>
        <g:remoteLink controller="app" action="common" update="main" params="[application:appid]">
            <button class="button black" alt="${message(code:'ui.sidebar.common')}" title="${message(code:'ui.sidebar.common')}">${message(code:'ui.sidebar.common')}</button>
        </g:remoteLink>
        <hr/>

        <h3>${message(code:'ui.sidebar.title')}</h3>

        <g:remoteLink method="GET" controller="screen" action="create" update="popup" params="[application:appid]">
        <button class="button-icon black mini" title="${message(code:'ui.sidebar.add')}" alt="${message(code:'ui.sidebar.add')}"><span class="icon iconsmall miniplus">+</span></button><span class='orange'>${message(code:'ui.sidebar.add')}</span>

        </g:remoteLink>

        <ul id="page-list">
            <g:each in="${request.application.screens}" var="screen">
                <ui:displaySmallPage screen="${screen}"/>
            </g:each>
        </ul>

    </div>
</div>
<jq:jquery>
    $( "#page-list" ).sortable({
            containment: 'parent',
            handle: 'p',
            update: function(event, ui) {
                <g:remoteFunction onSuccess="jQuery.smartpaper.updateOrder();"
                              controller="screen"
                              action="changeOrder"
                              params="'id='+ui.item.attr('elemid')+'&application=${appid}&order='+(ui.item.index() + 1)"/>
            }
    });
    jQuery('#sidebar').jScrollPane({autoReinitialise:true, verticalDragMaxHeight:30, verticalGutter:0, contentWidth:215});
    jQuery('#page-list > li').live('click',function(){
            <g:remoteFunction update="main"
                              controller="screen"
                              action="show"
                              params="'id='+jQuery(this).attr('elemid')+'&application=${appid}'"/>
    });
    jQuery('#page-list li .icon.home').live('click',function(event){
            event.stopPropagation();
            <g:remoteFunction onSuccess="jQuery.smartpaper.defineHomePage(data);"
                              controller="app"
                              action="setHome"
                              params="'id='+jQuery(this).attr('elemid')+'&application=${appid}'"/>
    });
</jq:jquery>