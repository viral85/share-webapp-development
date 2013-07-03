<%@ page import="com.smartpaper.domains.media.VideoMedia; com.smartpaper.domains.media.AudioMedia; com.smartpaper.domains.screen.MediasListScreen; com.smartpaper.domains.media.ImageMedia" %><g:set var="app" value="${request.application?:screen.application}"/>
-
<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
   <div class="gallery" style="height:${416 - (screen.hasTitle ? screen.backgroundTitle.height : 0) - (app.hasMenu ? 49 : 0) - (screen.hasOptions ? 44 : 0)}px">
   		<g:if test="${screen.arrowsDisplay != MediasListScreen.ARROWS_NEVER}">
            <div class="arrow left" style="display:${screen.arrowsDisplay == MediasListScreen.ARROWS_ON_TOUCH ? 'none' : 'block'};"><g:if test="${screen.leftArrow}"><img id="left-arrow" src="${screen.leftArrow.url}"/></g:if></div>
   		    <div class="arrow right" style="display:${screen.arrowsDisplay == MediasListScreen.ARROWS_ON_TOUCH ? 'none' : 'block'};"><g:if test="${screen.rightArrow}"><img id="right-arrow" src="${screen.rightArrow.url}"/></g:if></div>
            <g:if test="${screen.arrowsDisplay == MediasListScreen.ARROWS_ON_TOUCH}">
                <jq:jquery>
                    jQuery('.gallery').hover(function(){ jQuery('.arrow').show(); },function(){ jQuery('.arrow').hide(); });
                </jq:jquery>
            </g:if>
   		</g:if>
        <div id="slider">
			<ul id="images-slide">
                <g:each in="${screen.medias}" var="${mediaItem}" status="${index}">
                    <g:if test="${mediaItem.media instanceof ImageMedia}">
                        <li class="slide" style="background: url(${mediaItem.media.url}) center no-repeat;"></li>
                    </g:if>
                    <g:elseif test="${mediaItem.media instanceof VideoMedia}">
                        <li class="slide"><ui:video video="${mediaItem.media.url}" id='video-upload'/></li>
                    </g:elseif>
                    <g:elseif test="${mediaItem.media instanceof AudioMedia}">
                        <li class="slide"><ui:audio audio="${mediaItem.media.url}" id='audio-upload-${index}' phone="true"/></li>
                    </g:elseif>
                </g:each>
			</ul>
   		</div>
   	</div>
   		<jq:jquery>
			
			
   			var slides = jQuery('.slide');
			var slidesNum = slides.length;
			var currentSlide = 0;
			var currentX = 0;
			var dx = 0;
   			var mouseX = 0;
   			
   			jQuery('#images-slide').css('width', 320 * slidesNum);
   			jQuery('.slide').css('height', ${416 - (screen.hasTitle ? screen.backgroundTitle.height : 0) - (app.hasMenu ? 49 : 0) - (screen.hasOptions ? 44 : 0)});
   			

   			
   			/* CLICK SLIDE
   			--------------------*/
   			
   			jQuery('#left-arrow').mouseup(function(event) {
   				if(currentSlide > 0)
					currentSlide--;

				currentX = -320*currentSlide;
				jQuery('#images-slide').animate({
					'marginLeft' : currentX
				});
   			});
   		
   			jQuery('#right-arrow').mouseup(function(event) {
   				if(currentSlide < slidesNum-1)
					currentSlide++;

				currentX = -320*currentSlide;
				jQuery('#images-slide').animate({
					'marginLeft' : currentX
				});
   			});
			
   			
   			/* FINGER SLIDE
   			--------------------*/

			jQuery('#slider').mousedown(function(event) {
			 	mouseX = event.pageX;
			  	dx=0;
			  	
				jQuery('#images-slide').stop(true, true);
				jQuery('#slider').mousemove(function(event) {
						dx = (mouseX-event.pageX)*-1;
						jQuery('#images-slide').css('margin-left', currentX+dx);
						isMoved = true;
				});
				jQuery('#slider').mouseleave(runSlide);
				jQuery('#slider').mouseup(runSlide);
			});
			
			function runSlide(event){
				jQuery('#slider').unbind('mousemove');
				jQuery('#slider').unbind('mouseleave');
				jQuery('#slider').unbind('mouseup');
				
				if(currentSlide > 0 && dx > 30)
					currentSlide--;
				else if(currentSlide < slidesNum-1 && dx < -30)
					currentSlide++;

				currentX = -320*currentSlide;
				jQuery('#images-slide').animate({
					'marginLeft' : currentX
				});
			}

		</jq:jquery>
   </body>
</html>

