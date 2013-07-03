<h2>${message(code:'ui.screen.form.title')}</h2>

<div id="form-table">
    <g:include view="/form/_table.gsp" model="[screen:screen, app:app]"/>
 </div>
<g:remoteLink
        controller="form"
        action="addInput"
        onSuccess="jQuery.smartpaper.refreshPhone();"
        update="form-table" params="[application:app.id,id:screen.id]">
    <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
    <span><g:message code="ui.screen.form.list.add"/></span>
</g:remoteLink>

<h4 id='form-emailTo'>${message(code:'ui.screen.form.emailTo')}</h4>

<g:formRemote name="create"
              before="if (!jQuery('#emailTo-form').valid()){ return false; }"
              url="[controller:'screen', action:'update', params:[application:app.id,'screen.id':screen.id]]"
              id="emailTo-form">

    <input name="screen.emailTo" title="${message(code:'ui.screen.form.emailTo')}" class="required email label-input" style="width:200px" type="text" value="${screen.emailTo}">
    <br/>
    <button class="button orange">${message(code:'ui.button.valid')}</button>
</g:formRemote>
<jq:jquery>
     jQuery('#form-list td input.label-input').live('change',function(){
            var elem = $(this).parents('tr').addClass("selected");
            $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="form"
                    action="updateInput"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${app.id}&screen.id=${screen.id}&id=\'+jQuery(\'#form-list tr.selected\').attr(\'elemid\')+\'&input.label=\'+jQuery(this).val()"/>
      });

      jQuery('#form-list td input.required-input').live('change',function(){
            var elem = $(this).parents('tr').addClass("selected");
            $(this).parents('tr').siblings().removeClass("selected");
            <g:remoteFunction
                    controller="form"
                    action="updateInput"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${app.id}&screen.id=${screen.id}&id=\'+jQuery(\'#form-list tr.selected\').attr(\'elemid\')+\'&input.required=\'+(jQuery(this).attr('checked') ? 1 : 0)"/>
      });

      jQuery('#form-list td span.delete').die().live('click',function(){
            <g:remoteFunction
                controller="form"
                update="form-list"
                action="removeInput"
                onSuccess="jQuery.smartpaper.refreshPhone()"
                params="'screen.id=${screen.id}&id='+jQuery(this).parents('tr.item').attr('elemid')+'&application=${app.id}'"/>
    });

        jQuery.smartpaper.moveRowPosition('#form-list',function(position){
            <g:remoteFunction
                    controller="form"
                    action="updateInput"
                    onSuccess="jQuery.smartpaper.refreshPhone()"
                    params="\'application=${app.id}&screen.id=${screen.id}&id=\'+jQuery(\'#form-list tr.selected\').attr(\'elemid\')+\'&input.order=\'+position"/>
        });
</jq:jquery>