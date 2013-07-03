<g:set var="app" value="${request.application}"/>
<h3>${message(code:'ui.media.edit.image.title')}</h3>
<g:formRemote name="resize" url="[controller:'media', action:'resizeImage', params:[id:image.id,application:request.application.id, 'screen.id':screen?.id]]" onSuccess="jQuery.smartpaper.resetUpload();">
<img src="${image.url}" id='resizable-image' />
<ul>
  <li><input value='f' type="radio" name="rsize" id="f"/><label for="f">${message(code:'ui.media.edit.image.resize.f',args:[(416 - (app.hasMenu ? 49 : 0))])}</label></li>
  <g:if test="${screen}">
    <li><input value='ft' type="radio" name="rsize" id="ft"/><label for="ft">${message(code:'ui.media.edit.image.resize.ft',args:[(416 - (screen.hasTitle ? screen.backgroundTitle.height : 0) - (app.hasMenu ? 49 : 0))])}</label></li>
    <li><input value='fo' type="radio" name="rsize" id="fo"/><label for="fo">${message(code:'ui.media.edit.image.resize.fo',args:[(416  - (screen.hasOptions ? 44 : 0) - (app.hasMenu ? 49 : 0))])}</label></li>
    <li><input value='fto' type="radio" name="rsize" id="fto"/><label for="fto">${message(code:'ui.media.edit.image.resize.fto',args:[(416 - (screen.hasTitle ? screen.backgroundTitle.height : 0) - (screen.hasOptions ? 44 : 0) - (app.hasMenu ? 49 : 0))])}</label></li>
  </g:if>
  <li><input value='o' type="radio" name="rsize" id="o" checked="checked"/><label for="o">${message(code:'ui.media.edit.image.resize.keep.size')}</label></li>
  <li><input value='p' type="radio" name="rsize" id="p"/><label for="p">${message(code:'ui.media.edit.image.resize.custom')}</label></li>
</ul>
<div id="custom-size" style="display:none;">
    <label id="iwidth">L : </label><input type="text" name='custom.width' class="mini short" value="${image.height}">
    <span class="icon iconsmall lock">${message(code:'ui.media.explorer.title')}</span>
    <label id="iheight">H : </label><input type="text" name='custom.height' class="mini short" value="${image.width}">
</div>
<button class="button black small" type="submit">${message(code:'ui.button.valid')}</button>
</g:formRemote>
<jq:jquery>
    jQuery.smartpaper.resizeImage(${screen?.hasTitle ? screen.backgroundTitle.height : 0},${app.hasMenu ? 49 : 0},${image.width},${image.height});
</jq:jquery>
<div class="clear"></div>