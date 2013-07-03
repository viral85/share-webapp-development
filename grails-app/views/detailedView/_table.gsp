<g:set var="app" value="${request.application}"/>
<g:if test="${screen.overlays}">
    <table id="overlays-list" class="table-list" cellpadding="10">

        <tbody>
            <tr class="table-title">
                <th width="30%">${message(code:'ui.screen.detailedView.list.image')}</th>
                <th width="55%">${message(code:'ui.screen.detailedView.list.content')}</th>
                <th width="15%">${message(code:'ui.screen.detailedView.list.delete')}</th>
            </tr>
        </tbody>
        <tbody class="content">
            <g:each in="${screen.overlays}" var="overlay">
                <tr class="item" elemid="${overlay.id}">
                    <td>
                        <g:if test="${overlay.image}">
                            ${r.img(uri:overlay.image.url,title:overlay.image.name,alt:overlay.image.name)}
                            <span class="image iconmini icon edit"></span>
                        </g:if>
                        <g:else>
                            <button class="image button small black"><g:message code="ui.screen.detailedView.choose.image"/></button>
                        </g:else>
                    </td>
                    <td>
                        <g:if test="${overlay.content}">
                            <button class="editcontent button small black"><g:message code="ui.screen.detailedView.list.change.content"/></button>
                        </g:if>
                        <g:else>
                            <button class="editcontent button small black"><g:message code="ui.screen.detailedView.list.add.content"/></button>
                        </g:else>
                    </td>
                    <td>
                        <span class="icon iconmini delete" title="${message(code:'ui.screen.detailedView.list.delete')}"></span>
                    </td>
                </tr>
                <tr class="content-editor ui-helper-hidden" elemid="${overlay.id}">
                    <td class='editor' colspan="3">
                        <ui:textRich
                                     type="tiny"
                                     name="link.content"
                                     id="linkcontent${overlay.id}"
                                     inScrollPane="#main-config"
                                     width="280"
                                     overflow="visible"
                                     bodyStyle="padding:10px;"
                                     backgroundImage="${overlay.background?.image?.url}"
                                     backgroundColor="${overlay.background?.color}"
                                     height="300">
                            ${overlay.content}
                        </ui:textRich>
                        <div class="background-details" elemid="${overlay.id}">
                            <g:include view="detailedView/_backgroundOverlayImage.gsp" model="[overlay:overlay]"/>
                        </div>
                        <ui:colorPicker
                            name='picker-options-${overlay.id}'
                            submit="[controller:'detailedView', action:'updateOverlay', onSuccess:'jQuery.smartpaper.updateBackgroundOverlayRichText('+overlay.id+', value)', params:'\'overlay.background.color=\'+value+\'&id='+overlay.id+'&screen.id='+screen.id+'&application='+app.id+'\'']"
                            label="ui.screen.background.color"
                            default="${overlay.background.color}"
                            class="colorpicker"/>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
    <jq:jquery>
        $('table td .editcontent').click(function(){
            var editor = $(this).closest('tr').next();
            if(editor.is(':visible')){
                editor.hide();
                <g:remoteFunction
                        action="updateOverlay"
                        before="editor.find('textarea').cleditor()[0].updateTextArea();"
                        params="'application=${app.id}&screen.id=${screen.id}&id='+editor.attr('elemid')+'&overlay.content='+editor.find('textarea').val()"
                        onSuccess="jQuery.smartpaper.refreshPhone();"
                        controller="detailedView"/>
                $(this).text('<g:message code="ui.screen.detailedView.list.change.content"/>');
            }else{
                $('table tr.content-editor').hide();
                editor.show();
                editor.find('textarea').next().css('height',300);
                $(this).text('<g:message code="ui.screen.detailedView.list.validate.content"/>');
            }
        });
    </jq:jquery>
</g:if>