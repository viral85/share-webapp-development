<html>
   <head>
      <meta name="layout" content="iphone"/>
      <r:require modules="preview-iphone"/>
   </head>
   <body>
   <div class="wrap-links-list">
       <g:if test="${screen.links}">
           <ul class="links-list" style='display:none;'>
             <g:set var="index" value="${0}"/>
              <g:set var="last" value="${screen.links?.size() - 1}"/>
             <g:each in="${screen.links}" var="${link}">
                 <g:if test="${link.name && link.linkTo}">
                     <li class="${index == 0 ? 'first' : index == last ? 'last' : ''}" style="opacity:${screen.alpha}; background:${screen.bgcell};">
                         <%index++%>
                        <a style="font-size:${screen.fontSize}px; font-family: ${screen.police.replace('Bold','')}; ${screen.police.contains('Bold') ? 'font-weight:bold;' : ''} color:${screen.fontColor}; height:${screen.itemHeight}px;line-height:${screen.itemHeight}px;" href="${createLink(controller:'screen', action:'preview', params:[application:screen.application.id,id:link.linkTo.id,scale:params.scale])}">${link.image ? r.img(uri:link.image.url,title:link.name,alt:link.name) : ''} ${link.name}</a>
                     </li>
                 </g:if>
             </g:each>
             <jq:jquery>
                 <g:if test="${index}">
                    jQuery('.links-list').show();
                 </g:if>
             </jq:jquery>
           </ul>
       </g:if>
   </div>
   </body>
</html>
