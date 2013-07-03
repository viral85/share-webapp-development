package smartpaper

import org.springframework.web.servlet.support.RequestContextUtils as RCU
import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.util.BuildSettingsHolder

class UiTagLib {
    static namespace = "ui"

    def displaySmallPage = { attrs ->
        assert attrs.screen

        out << """<li elemid="${attrs.screen.id}">
                    <ul>"""
        if (attrs.screen.home)
            out << "<li><span class='icon home active' elemid=\"${attrs.screen.id}\" title=\"${message(code:'ui.sidebar.page.home')}\">home</a></li>"
        else
            out << "<li><span class='icon home' elemid=\"${attrs.screen.id}\" title=\"${message(code:'ui.sidebar.page.define.home')}\">home</a></li>"

        out << "<li><div class='icon link' title='${message(code:'ui.sidebar.page.links', args:[attrs.screen.nbLinks])}'></div><span class='count' title='${message(code:'ui.sidebar.page.links', args:[attrs.screen.nbLinks])}'>${attrs.screen.nbLinks?:0}</span></li>"
        out <<     """</ul>"""
        if (attrs.screen.getThumbnail(null)){
            out <<     """<img src='${attrs.screen.getThumbnail(null)}'/>"""
        }
            out <<     """<div class='background-image'>&nbsp;</div>
                    <p> <span class="order">${attrs.screen.order}</span> - ${attrs.screen.name} </p>
                </li>"""
    }

    def selectModel = { attrs ->
        assert attrs.id
        def screens = grailsApplication.getArtefacts("Domain").findAll { it.name.contains('Screen') && it.name != 'Screen' }
        screens.sort{
            it.clazz.type
        }
        def screenValues = []
        def screenKeys = []
        screens.collect { it ->
            screenValues << message(code:'screen.model.'+GrailsClassUtils.getShortName(it.clazz.name).toLowerCase())
            screenKeys << it.clazz.type
        }
        out << ui.select([title:'ui.screen.create.screen',from:screenValues, keys:screenKeys, id:attrs.id, name:attrs.name?:'type', value:attrs.value?:null, disabled:attrs.disabled?:false])
    }

    def select = { attrs ->
        assert attrs.id
        if (attrs.title){
            out << "<span>${message(code:'ui.screen.create.screen')}</span>"
        }
        attrs.remove('title')
        def color = attrs.remove('color')
        def size = attrs.remove('size')
        out << g.select(attrs)
        out << jq.jquery(null,"jQuery('#${attrs.id}').uniform({selectClass: 'selector ${size?:''} ${color?:'black'}-selector'});")
    }

    def window = { attrs, body ->
        attrs.title = attrs.title ? g.message(code:attrs.title) : null

        def buttons
        if (!attrs.buttons && !attrs.nobuttons){
            buttons = "[ {text: 'Cancel', class:'button gray', click: function() { jQuery(this).dialog('close'); } }, {text: 'Ok', class:'button orange', click: function() { ${attrs.actionSubmit ?:'jQuery(this).find(\'form\').submit();'} jQuery(this).dialog('close'); }} ]"
        } else {
            buttons = "["
            attrs.buttons.each{ button ->
                buttons += " {text: '${button.text}', class:'${button.class}', click: function() { ${button.action} } }, "
            }
            buttons += "]"
        }

        out << body()
        out << "<div class='errors' style='display:none'></div>"
        out << jq.jquery(null,"""jQuery('#${attrs.id?:'popup'}').dialog('destroy').dialog({
                                    buttons: ${buttons},
                                    resizable: false,
                                    draggable: ${attrs.draggable?:false},
                                    modal: ${attrs.modal?:false},
                                    width: ${attrs.width?:400},
                                    ${attrs.height?'height:'+attrs.height+',':''}
                                    title: "${attrs.title}"
        });""")
    }

    def explorer = {  attrs, body ->

        def content = ""
        if(attrs.menu){
            content += '<div class="popup-menu">'
            attrs.menu.each{
                content += remoteLink(attrs.action,"<button class='button black medium'>${message(code:it.name)}</button>")
            }
            content += '</div>'
        }

        content += "<div class='${attrs.className?:''} popup-content ${attrs.tabs? 'ui-tabs' : ''}'>"
        if (attrs.tabs){
            content += "<ul>"

            int i = 0
            for(def tab in attrs.tabs){
                content += "<li><a href='#tabs-${i}'>${message(code:tab.title)}</a></li>"
                i++
            }

            content += "<li><a href='#tabs-${attrs.tabs.size()}'>${message(code:'ui.explorer.title')}</a></li>"
            content += "</ul>"
        }

        if (attrs.tabs){
            int i = 0
            for(def tab in attrs.tabs){
                content += "<div id='tabs-${i}'>"
                content += tab.content instanceof String ? tab.content : g.render(tab.content)
                content += "</div>"
                i++
            }
        }

        if (attrs.tabs){
            content += "<div id='tabs-${attrs.tabs.size()}'>"
        }

        content +="""<div id='default-explorer' class='popup-explorer'>
                        <div class='empty'>${message(code:'ui.explorer.empty')}</div>"""

        def elementThumbnail = attrs.remove('elementThumbnail') ?: 'Thumbnail'
        def elementName = attrs.remove('elementName') ?: 'name'
        String elementUrl = attrs.remove('elementUrl') ?: 'url'

        def view = attrs.remove('view') ?: 'icon'

        def url = ''
        if (attrs.in){
            content += "<ul class='files'>"
            attrs.in?.each{ element ->
                if (element.hasProperty(elementUrl)){
                    url = "url='${element."$elementUrl"}'"
                }
                if (view == 'icon')
                    content += "<li elemid='"+element.id+"' "+url+""" >
                                        <img src='${element."get$elementThumbnail"(attrs.thumbnailSize?:null) != null? element."get$elementThumbnail"(attrs.thumbnailSize?:null) :createLinkTo(dir: 'images', file: 'no-'+GrailsClassUtils.getShortName(element.class).toLowerCase()+'.png')}' title='"""+element."$elementName"+"' alt='"+element."$elementName"+"""'/>
                                        <div class='background-icon'></div>
                                        <p title='"""+element."$elementName"+"' class='break-word'>"+ ui.truncate([value:(element."$elementName"), max:15]) +"""</p>
                               </li>"""
            }
            content += "</ul>"
        }else{
            content += "<ul class='files'></ul>"
        }
        content += '''
                      </div>
                    <div id="popup-explorer-details" class="popup-explorer-details"></div>
                    <div class="clear"></div>
                    </div>'''

        if (attrs.tabs){
            content += "</div>"
        }

        attrs.modal = true

        def onSelect = ""
        def onSubmit = ""
        if (attrs.detailsView){
            attrs.detailsView.update = 'popup-explorer-details'
            onSelect += remoteFunction(attrs.detailsView)
        }

        def defaultSelect = attrs.defaultSelect != null ? attrs.defaultSelect : true

        if(attrs.onSelect){
            onSelect += attrs.remove('onSelect')
        }
        if (attrs.onSubmit instanceof Map){
            attrs.onSubmit.onSuccess = attrs.onSubmit.onSucess ? attrs.onSubmit.onSuccess + " jQuery(this).dialog('close'); " : "jQuery(this).dialog('close');"
            onSubmit += remoteFunction(attrs.onSubmit)
        }else if(attrs.onSubmit){
            onSubmit = attrs.remove('onSubmit') + " jQuery(this).dialog('close'); "
        }else{
            onSubmit = " jQuery(this).dialog('close'); "
        }
        attrs.buttons = [
                [text: message(code:'ui.button.close'),
                        class:'button gray',
                        action: "jQuery(this).dialog('close')"],
                [text: message(code:'ui.button.valid'),
                        class:'button orange',
                        action: "var selected = jQuery(this).find('.ui-selected:visible').attr('elemid'); var url = jQuery(this).find('.ui-selected:visible').attr('url'); ${onSubmit}" ]]
        out << ui.window(attrs,content)
        out << jq.jquery(null,(attrs.tabs ? 'jQuery("#'+(attrs.id?:'popup')+' .ui-tabs").tabs();' : '')+"jQuery.smartpaper.explorer({onSelect:function(elem){${onSelect}},defaultSelect:${defaultSelect},container:'#default-explorer'})")
    }

    def mediaExplorer = { attrs, body ->
        def attr = [
                in:attrs.in,
                title:attrs.title?:"ui.media.explorer.image.title",
                width:"700",
                resizable:"true",
                className:"app-explorer",
                view:"icon",
                tabs:attrs.tabs,
                buttons:attrs.buttons,
                onSubmit:attrs.onSubmit,
                defaultSelect:attrs.defaultSelect,
                detailsView:[controller:'media', action:'detailsView', params:'\'application='+request.application.id+'&id=\'+elem.attr(\'elemid\')+\'&editable='+(attrs.editable?:false)+'\'']
        ]
        out << ui.explorer(attr)
    }

    def truncate = { attrs, body ->
        def content = attrs.value ?: body()
        def max = attrs.max.toInteger()?:10
        if (content.length() > max)
            out << content[0..max]+"..."
        else
            out << content
    }

    def colorPicker = { attrs, body ->
        def display = 'inline'
        if (attrs.display != null){
            display = attrs.display ? 'inline' : 'none'
        }
        out << "<div class='select-color'>"
        	out << "<span class='select-color-label'>${message(code:attrs.label)}</span>"
            out << """<input type='text'
                        class='select-color-input ui-corner-all' id='${attrs.name}'
                        style='background-color:${attrs.default}; display:${display};'
                        value='${attrs.default}'>"""
        out << body()
        out << "</div>"

        def jqCode = """ jQuery("#${attrs.name}").click(function(){
                            jQuery(this).parent().append("<div id='picker-${attrs.name}'></div>");
                            jQuery("#picker-${attrs.name}").farbtastic(this);
                            jQuery("#picker-${attrs.name}").bind('close.colorpicker',function(){
                                var value = jQuery('#${attrs.name}').css('background-color');
                                value = jQuery.smartpaper.rgb2hex(value);
                                ${remoteFunction(attrs.submit)};
                            });
                         });"""
        out << jq.jquery(null,jqCode)
    }

    def filesize = { attrs ->
        def labels = [ ' bytes', 'KB', 'MB', 'GB', 'TB' ]
        def size = attrs.size
        def label = labels.find {
            if( size < 1024 ) {
                true
            }
            else {
                size /= 1024
                false
            }
        }
            out << "${new java.text.DecimalFormat( attrs.format ?: '0.##' ).format( size )}$label"
    }

    def validationMessages = {

        def content = "jQuery.extend(jQuery.validator.ignoreTitle, true); jQuery.extend(jQuery.validator.messages, {"
            content += ['required','remote','email','url','date',
                        'dateISO','number','digits','creditcard',
                        'equalTo','accept'].collect{ "${it}:\"${message(code:'validation.message.'+it)}\"" }.join(',')
            content += ','
            content += ['maxlength','minlength','rangelength',
                        'range','max','min'].collect{ "${it}:jQuery.validator.format(\"${message(code:'validation.message.withFormat.'+it)}\")" }.join(',')
        content += "});"
        out << jq.jquery(null,content)
    }

    def textRichButton = { attrs ->
          attrs.action.onSuccess = attrs.action.onSuccess != null ?: "dataEditor.editor.execCommand(dataEditor.command, data, null, dataEditor.button)"
          def jqCode = """jQuery.cleditor.buttons.${attrs.name} = {
                            name: "${attrs.name}",
                            css: {
                              background: 'url(${attrs.icon}) no-repeat',
                              backgroundPosition: "4px 4px"
                            },
                            title: "${attrs.title}",
                            command: "${attrs.command}",
                            popupName: "${attrs.name.capitalize()}",
                            popupHover: true,
                            buttonClick: function(e, dataEditor) {
                                ${remoteFunction(attrs.action)}
                            }
                          };"""
          out << jq.jquery(null,jqCode)

    }

    def textRich = { attrs, body ->
        assert attrs.name
        def controls
        def height
        if(attrs.type == 'tiny'){
            controls = "bold italic underline strikethrough | font size style | color removeformat | alignleft center alignright justify | ${attrs.additionControls?.media?:''} undo redo | cut copy paste pastetext | source"
            height = 53
        }else if(attrs.type == 'normal'){
            controls = "bold italic underline strikethrough | font size style | color highlight removeformat | bullets numbering | outdent indent | alignleft center alignright justify | image ${attrs.additionControls?.media?:''} link unlink | cut copy paste pastetext | source"
            height = 79
        }else{
            controls = "bold italic underline italic underline strikethrough subscript superscript | font size style | color highlight removeformat | bullets numbering | outdent indent | alignleft center alignright justify | undo redo | rule image ${attrs.additionControls?.media?:''} link unlink | cut copy paste pastetext | source"
            height = 79
        }
        out << "<textarea name='${attrs.name}' id='${attrs.name.replaceAll(/\./,'')}' style='display:none;'>${body()}</textarea>"

        def jqCode = ""

        if (attrs.inScrollPane){
            jqCode += """jQuery('${attrs.inScrollPane}').unbind('jsp-initialised').bind('jsp-initialised',function(){"""
        }

        jqCode += "jQuery('#${attrs.name.replaceAll(/\./,'')}').cleditor({controls:'${controls}', ${attrs.width?'width:'+attrs.width+',':''} ${attrs.height?'height:'+(attrs.height?.toInteger() + height)+',':''} bodyStyle:'margin:0px; cursor:text'}).show();"

        if (attrs.backgroundColor || attrs.backgroundImage){
            jqCode += "var iframe = jQuery('${attrs.inScrollPane} iframe:first').contents().find('body');"
            jqCode += "iframe.css('background','${attrs.backgroundImage ? 'url('+attrs.backgroundImage+') no-repeat' : ''} ${attrs.backgroundColor ? attrs.backgroundColor : ''}');"
            if (attrs.height){
                jqCode += "iframe.css('min-height',${attrs.height?.toInteger()});"
                jqCode += "iframe.css('overflow','hidden');"
                jq
                if (attrs.overflow && attrs.overflow != 'hidden'){
                    jqCode += """jQuery('#${attrs.name.replaceAll(/\./,'')}').cleditor()[0].change(function(){
                        var cHeight = jQuery('${attrs.inScrollPane} iframe:first').contents().find('body').height();
                        var iHeight = jQuery('${attrs.inScrollPane} iframe:first').height();
                        if (cHeight > iHeight){
                            jQuery('${attrs.inScrollPane} iframe:first').css('width',${attrs.width?:320} + scrollbarWidth());
                            iframe.css('overflow-y','scroll');
                        }else{
                            jQuery('${attrs.inScrollPane} iframe:first').css('width',${attrs.width?:320});
                            iframe.css('overflow-y','hidden');
                        }
                    }).trigger('change');"""
                }
            }
        }
        if (attrs.inScrollPane){
            jqCode += "});"
        }
        out << jq.jquery(null,jqCode)
    }

    def setupVars = {
        def jqCode = """
            jQuery.extend(jQuery.smartpaper,{
                baseUrl:'${createLink([absolute:true]) - '/app'}/',
                i18n:{
                    home:"${message(code:'ui.sidebar.page.home')}",
                    defineHome:"${message(code:'ui.sidebar.page.define.home')}"
                }
            });
            AudioPlayer.setup("${createLink([absolute:true]) - '/app'}/images/player.swf", {
                width: 290
            });
        """
        out << jq.jquery(null,jqCode)
    }

    def SocialLinkSelector = { attrs ->
        def file = grailsAttributes.getApplicationContext().getResource("/socialLinks.xml").getFile()
        if (file.exists()){
            def socialLinks = new XmlSlurper().parse(file)
            def names = []
            def namesSelect = []
            def logos = []
            def urls = []
            socialLinks.socialLink.each{
                namesSelect << it.name.toString()
                names << '\''+it.name.toString()+'\''
                logos << '\''+it.logo.toString()+'\''
                urls << '\''+it.url.toString()+'\''
            }
            def index = 0
            if (attrs.value){
                namesSelect.eachWithIndex{ it, index2 ->
                    if(attrs.value?.name == it){
                        index = index2
                    }
                }
            }
            out << ui.select([value:index, name:'socialLinkSelector',id:'socialLinkSelector',from:namesSelect, keys:0..names.size(), onchange:"jQuery.smartpaper.onSelectSocialLink(this,'${attrs.updateUrl?:null}')"])
            out << "<input id='socialLinkSelector-input-name' type='hidden' value='${attrs.value?.name ?: names[index].replaceAll('\'','')}' name='socialLink.name'>"
            out << "<input id='socialLinkSelector-input-logo' type='hidden' value='${attrs.value?.logo ?: logos[index].replaceAll('\'','')}' name='socialLink.logo'>"
            out << """<div id="socialLinkSelector-logo">
                        ${r.img([dir:'/images/social/',file:attrs.value?.logo ?: logos[index].replaceAll('\'','')])}
                      </div>"""
            def jqCode = """
                jQuery.extend(jQuery.smartpaper,{social:{urls:[${urls.join(',')}],names:[${names.join(',')}],logos:[${logos.join(',')}]}});
            """
            out << jq.jquery(null,jqCode)
        }
    }

    def video = { attrs ->
     attrs.width = attrs.width ?: '320';
     attrs.height = attrs.height ?: '460';
     attrs.posterFl = attrs.poster ?'"'+attrs.poster+'",':''
     out << """<div class="video-js-box" style='-webkit-transform:none;-moz-transform:none;'>
                    <video id="${attrs.id}" controls="controls" width="${attrs.width}" height="${attrs.height}">
                        <source src="${attrs.video}" type="video/mp4"/>
                        <object style='-webkit-transform:none;-moz-transform:none;' id="${attrs.id}_fallback_1" class="vjs-flash-fallback" width="${attrs.width}" height="${attrs.height}" type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf">
                            <param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
                            <param name="allowfullscreen" value="true" />
                            <param name="flashvars" value='config={"playlist":[${attrs.posterFl} {"url": "${attrs.video}","scaling":"orig","autoPlay":false,"autoBuffering":true}]}' />"""
                            if (attrs.poster){
                                out << """ <img src="${attrs.poster}" width="${attrs.width}" height="${attrs.height}" alt="Poster Image" title="${message(code:'ui.screen.video.error')}" /> """
                            }
              out << """</object>
                  </video>
              </div>"""
    out << jq.jquery(null,"jQuery('video').VideoJS();")
    }

     def audio = { attrs ->
     out << """<div class="audio-js-box" style='-webkit-transform:none;-moz-transform:none;'>
                    <audio id="${attrs.id}" controls="controls">
                        <source src="${attrs.audio}" type="audio/mp3"/>
                  </audio>
                  <div id="${attrs.id}_fallback"></div>
              </div>"""
     out << """ <script type="text/javascript">
            if (document.createElement('audio').canPlayType) {
              if (!document.createElement('audio').canPlayType('audio/mpeg')) {
            """
            if(attrs.phone){
                def player = "${createLink([absolute:true]) - '/screen'}/images/player.swf"
                out << """ jQuery('#${attrs.id}_fallback').html('<object width="180" height="24" type="application/x-shockwave-flash" name="default_player_fallback" style="outline: none" data="${player}" id="default_player_fallback"><param name="wmode" value="opaque"><param name="menu" value="false"><param name="flashvars" value="soundFile=${attrs.audio}&amp;playerID=${attrs.id}_fallback"></object>'); """
            }else{
                out << """ AudioPlayer.embed("${attrs.id}_fallback", {soundFile: "${attrs.audio}"});"""
            }
            out << """jQuery('#${attrs.id}').hide();
              } else {
                jQuery('#${attrs.id}').show();
              }
            }
        </script> """
    }

    def screenHeight = { attrs ->
        out << 416 - 4 - (attrs.screen.hasTitle ? attrs.screen.backgroundTitle.height : 0) - (attrs.app.hasMenu ? 49 : 0) - (attrs.screen.hasOptions ? 44 : 0)
    }

    def handleGlobalErrorAjax = {
        def jqCode = """
                jQuery(document).ajaxComplete(function(e,xhr,settings){
                    if(xhr.status == 401){
                        alert('${message(code:'ui.global.error.401')}')
                        document.location = jQuery.smartpaper.baseUrl;
                    }
                    else if(xhr.status == 400 && !xhr.overrideError){
                        alert('${message(code:'ui.global.error.400')}');
                    }else if(xhr.status == 500){
                        alert('${message(code:'ui.global.error.500')}');
                    }
                });
        """
        out << jq.jquery(null,jqCode)
    }

    def localeSelecter = {attrs ->
        List locales = []
        def i18n
        if (grailsApplication.warDeployed) {
            i18n = grailsAttributes.getApplicationContext().getResource("WEB-INF/grails-app/i18n/").getFile().toString()
        } else {
            i18n = "$BuildSettingsHolder.settings.baseDir/grails-app/i18n"
        }
        //Default language
        locales << new Locale("en")
        new File(i18n).eachFile {
            def arr = it.name.split("[_.]")
            if (arr[1] != 'svn' && arr[1] != 'properties' && arr[0].startsWith('messages'))
                locales << (arr.length > 3 ? new Locale(arr[1], arr[2]) : arr.length > 2 ? new Locale(arr[1]) : new Locale(""))
        }

        attrs.from = locales
        attrs.value = RCU.getLocale(request)
        attrs.optionValue = {"${it.getDisplayName(it).capitalize()}"}
        out << g.select(attrs)
    }
}
