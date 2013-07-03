<h2>${message(code:'ui.screen.mediasList.title')}</h2>

<g:remoteLink
        controller="media"
        action="explorer"
        params="[
                    screen:screen.id,
                    application:app.id,
                    filter:'All',
                    'update.controller':'mediasList',
                    'update.action':'addItem',
                    'update.content':'medias-list',
                    'update.onSuccess':'jQuery.smartpaper.refreshPhone()',
                    'update.params':'phone=false&screen.id='+screen.id]"
        update="popup">
    <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
    <span><g:message code="${'ui.screen.mediasList.add'}"/></span>
</g:remoteLink>

<div id="medias-list">
    <g:include view="mediasList/_table.gsp" model="[screen:screen,app:app]"/>
</div>

<div class="arrow-config">
    <g:include view="mediasList/_arrow.gsp" model="[screen:screen,app:app,init:true]"/>
</div>
<jq:jquery>
    jQuery('#medias-list ul span.delete').die().live('click',function(){
        <g:remoteFunction
                controller="mediasList"
                update="medias-list"
                action="removeItem"
                onSuccess="jQuery.smartpaper.refreshPhone()"
                params="'screen.id=${screen.id}&id='+jQuery(this).parents('li.organise-element').attr('elemid')+'&application=${app.id}'"/>
    });
    jQuery('#medias-list ul span.edit').die().live('click',function(){
                var elem = $(this).parents('li').addClass("selected");
                $(this).parents('li').siblings().removeClass("selected");
                <g:remoteFunction
                    controller="media"
                    action="explorer"
                    params="[
                        application:app.id,
                        filter:'Image',
                        'update.controller':'mediasList',
                        'update.action':'updateItem',
                        'update.value':'media',
                        'update.onSuccess':'jQuery.smartpaper.refreshPhone()',
                        'update.content':'medias-list',
                        'update.params':'screen.id='+screen.id+'&id=\'+jQuery(\'#medias-list li.selected\').attr(\'elemid\')+\''
                    ]"
                    update="popup"/>
        });

    jQuery.smartpaper.moveRowPosition('#medias-list ul',function(position){
            <g:remoteFunction
                    controller="mediasList"
                    action="updateItem"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${app.id}&screen.id=${screen.id}&id=\'+jQuery(\'#medias-list .selected\').attr(\'elemid\')+\'&mediaListItem.order=\'+position"/>
        },'li');
</jq:jquery>