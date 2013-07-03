<g:set var="app" value="${request.application?:screen.application}"/>
<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
   <div id="detailedZone" style="height:${416 - (screen.hasTitle ? screen.backgroundTitle.height : 0) - (app.hasMenu ? 49 : 0) - (screen.hasOptions ? 44 : 0)}px">
        <g:each in="${screen.overlays}" var="overlay">
            <g:if test="${overlay.image}">
                <div class="overlay-item" elemid="${overlay.id}" style="top:${overlay.y?:0}px;left:${overlay.x?:0}px;">
                    <img src="${overlay.image.url}">
                </div>
            </g:if>
        </g:each>
   </div>
   <g:each in="${screen.overlays}" var="overlay">
        <g:if test="${overlay.image}">
            <div class="overlay-item-content content" id="overlay-item-content-${overlay.id}" style="${overlay.background?.image ? 'background:url('+overlay.background.image.url+') no-repeat;':''} ${overlay.background?.color ? 'background-color:'+overlay.background?.color+';':''}">${overlay.content}</div>
        </g:if>
    </g:each>
   <div id="overlay-item-close"></div>
   <jq:jquery>
        if (!parent.top.$('#app-preview').length || parent.top.$('#app-preview').is(':hidden')) {
            $('#detailedZone .overlay-item').draggable({
                containment:'parent',
                iframeFix: true,
                stop: function(event, ui) {
                      <g:remoteFunction
                           controller="detailedView"
                           update="overlays-table"
                           action="updateOverlay"
                           onSuccess=""
                           params="'overlay.x='+ui.position.left+'&overlay.y='+ui.position.top+'&screen.id=${screen.id}&id='+jQuery(this).attr('elemid')+'&application=${app.id}'"/>
                }
            });
        }
        $('#detailedZone .overlay-item').click(function(){
            $('.overlay-item-content').removeClass('show');
            var id = $(this).attr('elemid');
            $('#overlay-item-content-'+id).addClass('show');
            $('#overlay-item-close').show();
        });
       $('#overlay-item-close').click(function(){ $(this).hide(); $('.overlay-item-content').removeClass('show'); });
   </jq:jquery>
   </body>
</html>