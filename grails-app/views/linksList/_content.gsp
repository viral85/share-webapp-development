<g:set var="view" value="content"/>

<h2 class="first">${message(code:'ui.screen.linksList.title')}</h2>

<div id="link-table">
    <g:include view="/linksList/_table.gsp" model="[screen:screen, app:app]"/>
 </div>
<g:remoteLink
        controller="linksList"
        action="addLink"
        onSuccess="jQuery.smartpaper.refreshPhone();"
        update="link-table" params="[application:app.id,id:screen.id]">
    <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
    <span><g:message code="ui.application.menu.add"/></span>
</g:remoteLink>

<h2>${message(code:'ui.screen.linksList.appareance.title')}</h2>
<div id="list-style">
    <g:include view="/linksList/_appareance.gsp" model="[screen:screen,app:app, init:true]"/>
</div>
<jq:jquery>
     jQuery('#link-list td input.title-link').die().live('change',function(){
            var elem = $(this).parents('tr').addClass("selected");
            $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="linksList"
                    action="updateLink"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${app.id}&screen.id=${screen.id}&id=\'+jQuery(\'#link-list tr.selected\').attr(\'elemid\')+\'&linkListItem.name=\'+jQuery(this).val()"/>
      });

        jQuery('#link-list td .image').die().live('click',function(){
                var elem = $(this).parents('tr').addClass("selected");
                $(this).parents('tr').siblings().removeClass("selected");
                <g:remoteFunction
                    controller="media"
                    action="explorer"
                    params="[
                        application:app.id,
                        filter:'Image',
                        'update.controller':'linksList',
                        'update.action':'updateLink',
                        'update.value':'image',
                        'update.onSuccess':'jQuery.smartpaper.refreshPhone()',
                        'update.content':'link-list',
                        'update.params':'screen.id='+screen.id+'&id=\'+jQuery(\'#link-list tr.selected\').attr(\'elemid\')+\''
                    ]"
                    update="popup"/>
        });

        jQuery('#link-list td span.delete').die().live('click',function(){
            <g:remoteFunction
                    controller="linksList"
                    update="link-list"
                    action="removeLink"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="'screen.id=${screen.id}&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${app.id}'"/>
        });

        jQuery('#link-list td .editpage').die().live('click',function(){
            var elem = $(this).parents('tr').addClass("selected");
            $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="screen"
                    update="popup"
                    action="explorer"
                    before="if (!jQuery('#link-list tr.selected').attr('elemid')) return false;"
                    params="[
                        application:app.id,
                        'update.controller':'linksList',
                        'update.action':'updateLink',
                        'update.value':'linkTo',
                        'update.content':'link-list',
                        'update.onSuccess':'jQuery.smartpaper.refreshPhone()',
                        'update.params':'screen.id='+screen.id+'&id=\'+jQuery(\'#link-list tr.selected\').attr(\'elemid\')+\''
                    ]"/>
        });

        jQuery.smartpaper.moveRowPosition('#link-list',function(position){
            <g:remoteFunction
                    controller="linksList"
                    action="updateLink"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${app.id}&screen.id=${screen.id}&id=\'+jQuery(\'#link-list tr.selected\').attr(\'elemid\')+\'&linkListItem.order=\'+position"/>
        });
</jq:jquery>