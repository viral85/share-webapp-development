<p>
    <label>${message(code:'ui.screen.linksList.appareance.for.font')}</label>
    <ui:select value="${screen.police}" class="edit-screen" size="normal" color="white" id="policeSelect" from="['Arial','Arial Bold','Helvetica','Helvetica Bold']" name="screen.police"/>
</p>
<p>
    <label>${message(code:'ui.screen.linksList.appareance.for.fontsize')}</label>
    <ui:select value="${screen.fontSize}" class="edit-screen"  size="small" color="white" id="fontSizeSelect" from="${1..20}" name="screen.fontSize"/>
</p>

    <ui:colorPicker
            name="fontcolor"
            default="${screen.fontColor}"
            label="ui.screen.linksList.appareance.for.fontcolor"
            submit="[controller:'linksList',
                     onSuccess:'jQuery.smartpaper.refreshPhone();',
                     action:'updateAppareance',
                     params:'\'screen.fontColor=\'+value+\'&id='+screen.id+'&application='+app.id+'&phone=false&view='+view+'\'']"/>
    <ui:colorPicker
            name="bgcell"
            display="${screen.bgcell != 'transparent'}"
            default="${screen.bgcell != 'transparent' ? screen.bgcell : '#FFFFFF'}"
            label="ui.screen.linksList.appareance.for.background.cell"
            submit="[controller:'linksList',
                     onSuccess:'jQuery.smartpaper.refreshPhone();',
                     action:'updateAppareance',
                     params:'\'screen.bgcell=\'+value+\'&id='+screen.id+'&application='+app.id+'&phone=false&view='+view+'\'']">
      <div class="transparency">
          <input type="checkbox" name="screen.bgcell" value="transparent" ${screen.bgcell == 'transparent' ? 'checked' : ''}>
          <label>${message(code:'ui.screen.linksList.appareance.for.background.cell.transparency')}</label>
      </div>
    </ui:colorPicker>
    <p>
        <label>${message(code:'ui.screen.linksList.appareance.for.background.alpha')}</label>
        <ui:select value="${screen.alpha}" class="edit-screen"  size="small" color="white" id="alphaSelect" from="${[1.0,0.9,0.8,0.7,0.6,0.5,0.4,0.3,0.2,0.1,0.0]}" name="screen.alpha"/>
    </p>


<g:if test="${init}">
    <jq:jquery>
        jQuery('#list-style select.edit-screen').die().live('change',function(){
                var elem = $(this).addClass("selected");
                elem.siblings().removeClass("selected");
                <g:remoteFunction
                        controller="linksList"
                        action="updateAppareance"
                        onSuccess='jQuery.smartpaper.refreshPhone();'
                        params="\'application=${app.id}&id=${screen.id}&\'+jQuery(\'#list-style .edit-screen.selected\').attr(\'name\')+\'=\'+jQuery(this).val()+\''"/>
            });
            jQuery('#list-style .transparency input[type=checkbox]').die().live('change',function(){
                <g:remoteFunction
                        controller="linksList"
                        action="updateAppareance"
                        onSuccess="jQuery.smartpaper.updateTransparency(jQuery('#list-style .transparency input[type=checkbox]')); jQuery.smartpaper.refreshPhone();"
                        params="\'application=${app.id}&id=${screen.id}&\'+jQuery(\'#list-style .transparency input[type=checkbox]\').attr(\'name\')+\'=\'+jQuery(this).val()+\''"/>
            });
    </jq:jquery>
</g:if>