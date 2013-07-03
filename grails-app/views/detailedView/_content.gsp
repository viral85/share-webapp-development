<h2>${message(code:'ui.screen.detailedView.title')}</h2>

<g:remoteLink
        controller="detailedView"
        action="addOverlay"
        onSuccess="jQuery.smartpaper.refreshPhone();"
        update="overlays-table" params="[application:app.id,id:screen.id]">
    <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
    <span><g:message code="ui.screen.detailedView.add"/></span>
</g:remoteLink>

<div id="overlays-table">
    <g:include view="detailedView/_table.gsp" model="[screen:screen,app:app]"/>
</div>

<jq:jquery>
    jQuery('#overlays-table td span.delete').die().live('click',function(){
        <g:remoteFunction
                controller="detailedView"
                update="overlays-table"
                action="removeOverlay"
                onSuccess="jQuery.smartpaper.refreshPhone()"
                params="'screen.id=${screen.id}&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${app.id}'"/>
    });

     jQuery('#overlays-table td .image').die().live('click',function(){
                var elem = $(this).parents('tr').addClass("selected");
                $(this).parents('tr').siblings().removeClass("selected");
                <g:remoteFunction
                    controller="media"
                    action="explorer"
                    params="[
                        application:app.id,
                        filter:'Image',
                        'update.controller':'detailedView',
                        'update.action':'updateOverlay',
                        'update.value':'image',
                        'update.onSuccess':'jQuery.smartpaper.refreshPhone()',
                        'update.content':'overlays-table',
                        'update.params':'screen.id='+screen.id+'&id=\'+jQuery(\'#overlays-table tr.selected\').attr(\'elemid\')+\''
                    ]"
                    update="popup"/>
     });
</jq:jquery>