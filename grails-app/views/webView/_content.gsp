<h2>${message(code:'ui.screen.webview.title')}</h2>
 <g:formRemote
        name="webview-content"
        before="jQuery('#screencontent').cleditor()[0].updateTextArea();"
        onSuccess="jQuery.smartpaper.refreshPhone();"
        url="[controller:'webView',action:'update',params:[application:app.id,'screen.id':screen.id]]">
    <button type="submit" class="button orange">${message(code:'ui.button.valid')}</button>

    <ui:textRichButton
            action="[controller:'media',
                     update:'popup',
                     onSuccess:false,
                     action:'explorer',
                     params:[application:app.id,filter:'Image','update':'jQuery.smartpaper.addMediaInRich(\'#screencontent\',\'media\',url);']]"
            name="media"
            title="${message(code:'ui.rich.button.addMedia')}"
            command="insertimage"
            icon="${g.resource(absolute:true, dir:'images',file:'media.png')}"/>

    <ui:textRichButton
            action="[controller:'screen',
                     update:'popup',
                     onSuccess:false,
                     action:'explorer',
                     params:[application:app.id,'update':'jQuery.smartpaper.addScreenInRich(\'#screencontent\',\'screen\',selected);']]"
            name="screen"
            title="${message(code:'ui.rich.button.addScreen')}"
            command="createlink"
            icon="${g.resource(absolute:true, dir:'images',file:'screen.png')}"/>

    <ui:textRich name="screen.content"
                 inScrollPane="#main-config"
                 width="320"
                 additionControls="[media:' media screen table']"
                 overflow="visible"
                 height="${ui.screenHeight(screen:screen,app:app)}"
                 backgroundColor="${screen.background?.color}"
                 backgroundImage="${screen.background?.image?.url}">
    ${screen.content}
    </ui:textRich>
</g:formRemote>