<div id='standard-explorer' class='popup-explorer'>
    <ul class='files'>
        <g:each in="${medias}" var="element">
            <g:if test="${view == 'icon'}">
                <li elemid="${element.id}" url="${element.url}"><img src='${element.getThumbnail(null)}' title="${element.name}" alt="${element.name}"/><p title="${element.name}" class='break-word'>${ui.truncate([value:(element.name), max:15])}</p></li>
            </g:if>
        </g:each>
    </ul>
</div>
<div id="popup-standard-explorer-details" class="popup-explorer-details"></div>
<div class="clear"></div>
<jq:jquery>
    jQuery.smartpaper.explorer({onSelect:function(elem){${g.remoteFunction([controller:'media', action:'detailsView', update:'popup-standard-explorer-details', params:'\'application='+request.application.id+'&id=\'+elem.attr(\'elemid\')+\'&editable=false\''])}},defaultSelect:true,container:'#standard-explorer'})
</jq:jquery>