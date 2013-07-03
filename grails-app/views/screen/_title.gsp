<g:set var="appid" value="${request.application.id}"/>
<g:set var="view" value="title"/>

<g:formRemote name="display-forms" class="title-active switch" update="main-config-content" url="[controller:'screen',action:'update',params:[application:appid,view:view,'screen.id':screen.id,phone:false]]">
    <input type="radio" onchange="jQuery(this).parent().submit()" id="title-enable" name="screen.hasTitle" value="true" ${screen.hasTitle?'checked="checked"':''}/>
    <input type="radio" onchange="jQuery(this).parent().submit()" id="title-disable" name="screen.hasTitle" value="false" ${screen.hasTitle?'':'checked="checked"'}/>
    <label for="title-enable" class="cb-enable selected"><span>${message(code:'ui.screen.title.enable')}</span></label><label for="title-disable" class="cb-disable"><span>${message(code:'ui.screen.title.disable')}</span></label>
</g:formRemote>

<g:if test="${screen.hasTitle}">
    <h2><g:message code="ui.screen.title.title"/></h2>

        <g:formRemote
                name="content-title"
                before="jQuery('#screencontentTitle').cleditor()[0].updateTextArea(); if (!jQuery('#content-title').valid()){ return false; }"
                update="main-config-content"
                url="[controller:'screen',action:'update',params:[application:appid,view:view,'screen.id':screen.id,phone:false]]">

            <ui:textRichButton
            action="[controller:'media',
                     update:'popup',
                     onSuccess:false,
                     action:'explorer',
                     params:[application:app.id,filter:'Image','update':'jQuery.smartpaper.addMediaInRich(\'#screencontentTitle\',\'media\',url);']]"
            name="media"
            title="${message(code:'ui.rich.button.addMedia')}"
            command="insertimage"
            icon="${g.resource(absolute:true, dir:'images',file:'media.png')}"/>

            <ui:textRich
                    inScrollPane="#main-config"
                    name="screen.contentTitle"
                    type="tiny"
                    additionControls="[media:' | media ']"
                    width="320"
                    height="${screen.backgroundTitle?.height?:null}" backgroundColor="${screen.backgroundTitle?.color}" backgroundImage="${screen.backgroundTitle?.image?.url}">
                ${screen.contentTitle?:''}
            </ui:textRich>
            <div class="title-height">
                <span>${message(code:'ui.screen.title.height')}</span>
                <input type="text" class="required mini" name="screen.backgroundTitle.height" value="${screen.backgroundTitle.height}">
            </div>
            <button type="submit" class="button orange">${message(code:'ui.button.valid')}</button>
        </g:formRemote>

    <h3><g:message code="ui.screen.background"/></h3>

    <div class="background-details">
        <g:if test="${!screen.backgroundTitle.image}">
            <g:remoteLink
                controller="media"
                action="explorer"
                params="[
					screen:screen.id,
                    application:appid,
                    filter:'Image',
                    'update.controller':'screen',
                    'update.action':'update',
                    'update.value':'backgroundTitle',
                    'update.content':'main-config-content',
                    'update.params':'view='+view+'&phone=false&screen.id='+screen.id
                ]"
                update="popup">
                    <button class="button-icon black mini"><span class="icon iconsmall miniplus">+</span></button>
                    <span><g:message code="ui.screen.image.add"/></span>
            </g:remoteLink>
        </g:if>
        <g:else>
            <div class="image"><img src="${screen.backgroundTitle.image.url}" title="${screen.backgroundTitle.image.name}.${screen.backgroundTitle.image.extension}"/></div>
            <span class="details filename">${screen.backgroundTitle.image.name}.${screen.backgroundTitle.image.extension}</span>
            <p class="details">
				<g:remoteLink
					controller="media"
					action="explorer"
					params="[
					    screen:screen.id,
						application:appid,
						filter:'Image',
						'update.controller':'screen',
						'update.action':'update',
						'update.value':'backgroundTitle',
						'update.content':'main-config-content',
						'update.params':'view='+view+'&phone=false&screen.id='+screen.id
					]"
					update="popup">
					<span class="icon iconmini edit"><g:message code="ui.screen.image.change"/></span>
					<span><g:message code="ui.screen.image.change"/></span>
				</g:remoteLink>
            </p>
            <p class="details">
				<g:remoteLink
						controller="screen"
						action="update"
						update="main-config-content"
						params="[application:appid,view:view,phone:false,'screen.id':screen.id,'backgroundTitle':'false']">
					<span class="icon iconmini delete"><g:message code="ui.screen.image.delete"/></span>
					<span><g:message code="ui.screen.image.delete"/></span>
				</g:remoteLink>
            </p>
        </g:else>
    </div>

    <ui:colorPicker
            name='picker-options'
            submit="[controller:'screen', onSuccess:'jQuery.smartpaper.refreshPhone();', update:'main-config-content', action:'update', params:'\'screen.backgroundTitle.color=\'+value+\'&screen.id='+screen.id+'&application='+request.application.id+'&phone=false&view='+view+'\'']"
            label="ui.screen.background.color"
            default="${screen.backgroundTitle.color}"
            class="colorpicker"/>
</g:if>
<jq:jquery>
        jQuery('#content-title').validate();
        if($('#title-enable').is(':checked')){
            $('.cb-disable').removeClass('selected');
            $('.cb-enable').addClass('selected');
        }
        else{
            $('.cb-enable').removeClass('selected');
            $('.cb-disable').addClass('selected');
        }
        jQuery( function(){ 
			$(".cb-enable").click(function(){
				var parent = $(this).parents('.switch');
				$('.cb-disable',parent).removeClass('selected');
				$(this).addClass('selected');
				$('.checkbox',parent).attr('checked', true);
			});
			$(".cb-disable").click(function(){
				var parent = $(this).parents('.switch');
				$('.cb-enable',parent).removeClass('selected');
				$(this).addClass('selected');
				$('.checkbox',parent).attr('checked', false);
			});
		});
</jq:jquery>