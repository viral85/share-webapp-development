
$(function()
{
    //MediaElementDefaults.flashName= 'images/flashmediaelement.swf';
    jQuery.validator.defaults.ignoreTitle = true;
	$("select.white, input:checkbox, input:radio").uniform();
    $(document.body).click(function(event){
        var picker = $('div.select-color .farbtastic');
        if(picker.length && !$(event.target).hasClass('select-color-input')){
            picker.parent().trigger('close.colorpicker');
            picker.parent().remove();
        }
    });
    $(".tooltip").live("mouseover", function() {
        $(this).tipTip();
        $(this).trigger('mouseenter');
    });
});

(function($){
		 $.fn.extend({
			  center: function (options) {
				   var options =  $.extend({ // Default values
						inside:window, // element, center into window
						transition: 0, // millisecond, transition time
						minX:0, // pixel, minimum left element value
						minY:0, // pixel, minimum top element value
						withScrolling:true, // booleen, take care of the scrollbar (scrollTop)
						vertical:true, // booleen, center vertical
						horizontal:true // booleen, center horizontal
				   }, options);
				   return this.each(function() {
						var props = {position:'absolute'};
						if (options.vertical) {
							 var top = ($(options.inside).height() - $(this).outerHeight()) / 2;
							 if (options.withScrolling) top += $(options.inside).scrollTop() || 0;
							 top = (top > options.minY ? top : options.minY);
							 $.extend(props, {top: top+'px'});
						}
						if (options.horizontal) {
							  var left = ($(options.inside).width() - $(this).outerWidth()) / 2;
							  if (options.withScrolling) left += $(options.inside).scrollLeft() || 0;
							  left = (left > options.minX ? left : options.minX);
							  $.extend(props, {left: left+'px'});
						}
						if (options.transition > 0) $(this).animate(props, options.transition);
						else $(this).css(props);
						return $(this);
				   });
			  }
		 });
	})(jQuery);

(function($) {
            $.smartpaper = {

                    baseUrl:null,
                    currentZoom:1,
                    currentPage:null,
                    i18n:{
                        defineHome:'',
                        home:''
                    },

                    resetUpload:function(media){
                        if (media){
                            jQuery('#edit-media').html('<div id="uploaded-media"><img class="ui-selected" src="'+media.url+'" elemid="'+media.id+'"/></div>');
                        }else{
                            jQuery('#edit-media').html('');
                        }
                        jQuery('#progressbar').progressbar('destroy');
                        jQuery('#progressbar-val').html('');

                        //jQuery('#popup .ui-tabs').tabs('select', 2);
                        //jQuery('#popup ul.files li:last').click();
                    },

                    resizeImage:function(title,menu,originalWidth,originalHeight){
                        $('input[name=rsize]').click(function(){
                            var val = $(this).val();
                            var width, height;
                            switch(val){
                                case 'f':
                                    width  = 320; height = 416 - menu;
                                break;
                                case 'ft':
                                    width  = 320; height = 416 - title - menu;
                                break;
                                case 'fo':
                                    width  = 320; height = 416 - 44 - menu;
                                break;
                                case 'fto':
                                    width  = 320; height = 416 - 44 - title - menu;
                                break;
                                case 'o':
                                    $('#resizable-image').attr('width','').attr('height','');
                                    break;
                                case 'p':
                                    $('#resizable-image').attr('width','').attr('height','');
                                break;
                            }
                            if (width && height){
                                $('#resizable-image').attr('width',width).attr('height',height);
                            }
                            if($(this).val() == 'p'){
                                $('#custom-size').show();
                                $('#custom-size input').bind('change',function(){
                                    var val = $(this).val();
                                    var val2 = 0;
                                    if($(this).attr('name') == 'custom.width'){
                                        if (val > 320){
                                            val = 320;
                                            $(this).val(val);
                                        }
                                        val2 = val * originalHeight / originalWidth;
                                        val2 = Math.round(val2);
                                        $('#custom-size input[name="custom.height"]').val(val2);
                                        $('#resizable-image').attr('width',val).attr('height',val2);
                                    }
                                    else if($(this).attr('name') == 'custom.height'){
                                        val2 = val *  originalWidth / originalHeight;
                                        val2 = Math.round(val2);
                                        $('#custom-size input[name="custom.width"]').val(val2);
                                        $('#resizable-image').attr('width',val2).attr('height',val);                                    }
                                });
                            }else{
                                $('#custom-size').hide();
                                $('#custom-size input').unbind();
                            }
                        });
                    },

                    defineHomePage:function(data){
                        $('#page-list li .icon.home.active').removeClass('active').attr('title',this.i18n.defineHome);
                        $('#page-list li .icon.home[elemid='+data.screen.id+']').addClass('active').attr('title',this.i18n.home);
                        if (this.currentPage == data.screen.id){
                            $('#menubar-define-home').hide();
                        }else{
                            $('#menubar-define-home').show();
                        }
                    },

                    createSmallScreen:function(screen){
                        var element = $('<li/>').attr('elemid',screen.id);
                        element.append('<ul><li><span elemid="'+screen.id+'" class="icon home '+(screen.home?'active':'')+'">home</span></li><li><div class="icon link"></div><span class="count">'+screen.nbLinks+'</span></li></ul>'+(screen.thumbnail ? '<img src="'+screen.thumbail+'">' : '')+'<div class="background-image"></div><p> <span class="order">'+screen.order+'</span> - '+screen.name+' </p>')
                        var last = $('#page-list > li:last');
                        last.length ? element.insertAfter(last) : element.appendTo($('#page-list'));
                        var api = $('#sidebar').jScrollPane().data('jsp');
                        //api.reinitialise({autoReinitialise:true, verticalDragMaxHeight:30, verticalGutter:0, contentWidth:215});
                        $('#sidebar').jScrollPane().data('jsp').scrollToPercentY(100);
                    },

                    explorer:function(options){
                        $('#popup').parent().find('.ui-dialog-buttonpane button.orange').hide();
                        $("#popup "+options.container+" li").die('click').live('click',function() {
                            if(options.defaultSelect){
                                $('#popup').parent().find('.ui-dialog-buttonpane button.orange').show();
                            }
                            var elem = $(this).addClass("ui-selected");
                            elem.siblings().removeClass("ui-selected");
                            if (options.onSelect){
                                options.onSelect(elem);
                            }
                        });
                        $('#popup .popup-explorer').jScrollPane({autoReinitialise:true, verticalDragMaxHeight:30});
                    },

                    manageMediaUploaded:function(media,content,autoSelect){
                        var element = $('<li/>').attr('elemid',media.id);
                        if (media.url){
                            element.attr('url',media.url);
                        }
                        element.append('<img alt="'+media.filename+'" title="'+media.filename+'" src="'+media.thumbnail+'"><p class="break-word" title="'+media.filename+'">'+media.displayName+'</p></li>')
                        var last = $('#default-explorer ul.files > li:last');
                        last.length ? element.insertAfter(last) : element.appendTo($('ul.files'));
                        $('ul.files').parent().find('.empty').hide();

                        if (content){
                            $('#edit-media').html(content);
                        }
                        if (autoSelect){
                            $('#uploaded-media').addClass('ui-selected');
                            $('#popup').parent().find('.ui-dialog-buttonpane button.orange').show();
                        }
                    },

                    deleteMedia:function(data){
                        $('ul.files li[elemid='+data.id+']').remove();
                        var last = $('ul.files > li:last');
                        last.length ? $('ul.files').parent().find('.empty').hide() : $('ul.files').parent().find('.empty').show();
                        $('#popup-explorer-details').html('');
                    },

                    initPhoneZoom:function(){

                        if (jQuery.smartpaper.currentZoom){
                            jQuery.smartpaper._zoomPhone(jQuery.smartpaper.currentZoom);
                        }

                        jQuery( "#zoom-phone" ).slider({
                           value:jQuery.smartpaper.currentZoom,
                           min: 0.5,
                           max: 1,
                           step: 0.25,
                           slide: function(event, ui) {
                                var iframe = jQuery('.iframe-content');
                                var scale = ui.value;
                                jQuery.smartpaper._zoomPhone(scale);
                           }
                        });
                    },

                    _zoomPhone:function(scale){
                        var iframe = jQuery('.iframe-content');
                        var src = iframe.attr("src");
                        src = src.split('?');
                        switch(scale){
                            case 0.5:
                                iframe.removeClass().addClass('iframe-content scale-half');
                                $('#main-preview').removeClass().addClass('scale-half');
                                $('#main-config').removeClass().addClass('scale-half');
                                jQuery.smartpaper.currentZoom = 0.5;
                            break;
                            case 0.75:
                                iframe.removeClass().addClass('iframe-content scale-three-quarter');
                                $('#main-preview').removeClass().addClass('scale-three-quarter');
                                $('#main-config').removeClass().addClass('scale-three-quarter');
                                jQuery.smartpaper.currentZoom = 0.75;
                            break;
                            case 1:
                                iframe.removeClass().addClass('iframe-content');
                                $('#main-preview').removeClass();
                                $('#main-config').removeClass();
                                jQuery.smartpaper.currentZoom = 1;
                            break;
                        }
                        iframe.attr("src",src[0]+'?scale='+scale);
                        jQuery( ".phone" ).css("-webkit-transform", "scale(" + scale + ")");
                        jQuery( ".phone" ).css("-moz-transform", "scale(" + scale + ")");
                    },

                    refreshPhone:function(){
                        window.setTimeout(function(){
                            var currSrc = $(".iframe-content").attr("src");
                            $(".iframe-content").attr("src", currSrc);
                        }, 500);
                    },

                    displayPopupErrors:function(xhr){
                        var data = jQuery.parseJSON(xhr.responseText);
                        var errors = jQuery("#popup .errors");
                        errors.html(data.error);
                        errors.show();
                    },

                    setCurrentPage:function(id,thumbnail,nblinks){
                        if (!id && !thumbnail){
                            this.currentPage = null;
                            $('#page-list > li').removeClass('active');
                            $('#menubar-define-home').hide();
                            $('#menubar-delete-page').hide();
                            $('#menubar-rename-page').hide();
                            $('#menubar-duplicate-page').hide();
                        }else{
                            this.currentPage = id;
                            $('#page-list > li').removeClass('active');
                            $('#page-list > li[elemid='+id+']').addClass('active');
                            if (thumbnail){
                                var separator = thumbnail.indexOf('?') != -1 ? '&' : '?';
                                $('#page-list > li[elemid='+id+'] img').attr('src',thumbnail+separator+'nocache='+new Date().getTime());
                            }
                            $('#page-list > li[elemid='+id+']');
                            $('#menubar-delete-page').show();
                            $('#menubar-rename-page').show();
                            $('#menubar-duplicate-page').show();
                            if ($('#page-list li .icon.home[elemid='+id+']').hasClass('active')){
                                $('#menubar-define-home').hide();
                            }else{
                                $('#menubar-define-home').show();
                            }
                            $('#page-list > li[elemid='+id+'] span.count').text(nblinks);
                        }
                    },

                    updateOrder:function(){
                        $('#page-list li').each(function(){
                            var order = $(this).find('span.order');
                            order.text($(this).index() + 1);
                        });
                    },

                    renamePage:function(data){
                        jQuery('#popup').dialog('close');
                        $('#page-list li[elemid='+data.id+'] p').text(data.order+' - '+data.name);
                        if (data.name.length >= 14){
                            data.name = data.name.substr(0,13)+'...';
                        }
                        $('#main-preview h1').text('Page '+data.order+' : '+data.name);
                    },

                    deletePage:function(data){
                        var prev = $('#page-list li[elemid='+data.id+']').prev();
                        $('#page-list li[elemid='+data.id+']').remove();
                        $('#page-list li span.order').each(function(){
                            var curr = parseInt($(this).text());
                            if(curr > data.order){
                                curr = curr - 1;
                                $(this).text(curr);
                            }
                        });
                        $('#menubar-delete-page').hide();
                        $('#menubar-rename-page').hide();
                        $('#menubar-duplicate-page').hide();
                        $('#main').html('');
                        var api = $('#sidebar').jScrollPane().data('jsp');
                        //api.reinitialise({autoReinitialise:true, verticalDragMaxHeight:30, verticalGutter:0, contentWidth:215});
                        if(prev){
                            prev.click();
                        }
                    },

                    screenFrom:function(view){
                        $('#main-preview .button-icon > span').removeClass('active');
                        $('#main-preview .button-icon > span.'+view).addClass('active');
                    },

                    rgb2hex:function(rgb) {
                         var rgb = rgb.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
                         function hex(x) {
                          return ("0" + parseInt(x).toString(16)).slice(-2);
                         }
                         return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
                    },

                    onSelectSocialLink:function(select,socialUrl){
                        var val = $(select).val();
                        $('#socialLinkSelector-input-name').val(this.social.names[val]);
                        $('#socialLinkSelector-input-logo').val(this.social.logos[val]);
                        $('#socialLinkSelector-logo img').attr('src',this.baseUrl+'images/social/'+this.social.logos[val]);
                        if(socialUrl){
                            $('#'+socialUrl).val(this.social.urls[val]);
                        }
                    },

                    addMediaInRich:function(editor,buttonName,data){
                        var button = jQuery.cleditor.buttons[buttonName];
                        $(editor).cleditor()[0].execCommand(button.command, data, null, button.name);
                    },

                    addScreenInRich:function(editor,buttonName,data){
                        var button = jQuery.cleditor.buttons[buttonName];
                        data = "smartpaper://target("+data+")";
                        $(editor).cleditor()[0].execCommand(button.command, data, null, button.name);
                    },

                    toggleInProgress:function(){
                        jQuery('#menubar-inprogress').show();
                        jQuery('#menubar-unpublish').hide();
                        jQuery('#menubar-publish').hide();
                    },

                    toggleActive:function(inProgress,active){
                        if (!inProgress){
                            if(active){
                                jQuery('#menubar-publish').hide();
                                jQuery('#menubar-unpublish').show();
                                jQuery('#sidebar h3 .icon').removeClass('offline').addClass('online');
                            }else{
                                jQuery('#menubar-publish').show();
                                jQuery('#menubar-unpublish').hide();
                                jQuery('#sidebar h3 .icon').addClass('offline').removeClass('online');
                            }
                            jQuery('#menubar-inprogress').hide();
                        }
                    },

                    checkinProgress:function(id){
                        $.smartpaper.toggleInProgress();
                        if ($('#menubar-inprogress').is(':visible')){
                            var stopCall;
                            var call = function(id){ $.ajax({type: "POST", timeout:1000, url:$.smartpaper.baseUrl+'app/inProgress', data:{application:id}, success:function(data){
                                    if(data.inProgress){
                                        debugger;
                                        var txt = $('#menubar-inprogress a').text();
                                        if((txt.split(",").length - 1) == 3){
                                            $('#menubar-inprogress a').text(txt.substr(0,txt.indexOf('.')));
                                        }else{
                                            $('#menubar-inprogress a').text(txt+'.');
                                        }
                                    }else{
                                        $.smartpaper.toggleActive(data.inProgress,data.active);
                                        clearInterval(stopCall);
                                    }
                                }});
                            };
                            stopCall = setInterval(function(){
                                call(id);
                            },1250);
                        }
                    },

                    updateProfile:function(data) {
                        jQuery('#popup').dialog('close');
                        $('#profile-name span').html(data.name);
                        if (data.refresh) {
                            document.location.reload(true);
                        }
                    },

                    moveRowPosition:function(container, callback, type){
                        type = type ? type : 'tr';
                        jQuery(container+' .move').die().live('click',function(){
                            var elem = $(this).parents(type).addClass("selected");
                            $(this).parents(type).siblings().removeClass("selected");
                            var position = $(container).find(type+'.item').index($(this).parents(type));
                            if ($(this).hasClass('down')){
                                $(container).moveRow(position,position + 1,type);
                            }else{
                                $(container).moveRow(position,position - 1,type);
                            }
                            var newPosition = $(container).find(type+'.item').index($(this).parents(type));
                            if (newPosition != position && callback){
                                callback(newPosition+1);
                            }
                        });
                    },

                    updateTransparency:function(checkbox){
                        jQuery('#list-style .transparency input[type=checkbox]').closest('div.select-color').find('.select-color-input').toggle();
                    },

                    updateBackgroundOverlayRichText:function(id,value){
                        var iframe = jQuery('tr.content-editor[elemid='+id+']').find('iframe').contents();
                        if (value.indexOf('#') > -1){
                            $(iframe).find('body').css('background-color',value);
                        }else{
                            if (value != '')
                                $(iframe).find('body').css('background-image','url('+value+')').css('background-repeat','no-repeat');
                            else
                                $(iframe).find('body').css('background-image','');
                        }
                        jQuery.smartpaper.refreshPhone();
                    }


            };
})(jQuery);

$.fn.extend({
    moveRow: function (oldPosition, newPosition, type) {
        type = type ? type : 'tr';
        return this.each(function () {
            if (newPosition < 0 || newPosition >= $(this).find(type+'.item').length){
                return
            }
            var row = $(this).find(type+'.item').eq(oldPosition).detach();
            if (newPosition == $(this).find(type+'.item').length) {
                $(this).find(type+'.item').eq(newPosition - 1).after(row);
            }
            else {
                $(this).find(type+'.item').eq(newPosition).before(row);
            }
        });
    },

    hasScrollBar:function() {
        return this.get(0).scrollHeight > this.height();
    }

});

function scrollbarWidth() {
    var div = $('<div style="width:50px;height:50px;overflow:hidden;position:absolute;top:-200px;left:-200px;"><div style="height:100px;"></div>');
    // Append our div, do our calculation and then remove it
    $('body').append(div);
    var w1 = $('div', div).innerWidth();
    div.css('overflow-y', 'scroll');
    var w2 = $('div', div).innerWidth();
    $(div).remove();
    return (w1 - w2);
}

function changeLinkUnputCleditor(select){
    var inp = $('#link-input-cleditor');
    var selected = $(select).val();
    var val = inp.val();
    if (val.indexOf('://') != -1){
        val = val.substring(val.indexOf('://')+3,val.length);
    }
    if (selected == 'sms://' || selected == 'tel://'){
        if (!isNumber(val)){
            val = '';
        }
    }
    val = $(select).val()+val;
    $(inp).val(val);
}

function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}