<g:if test='${app.getThumbnail(125)}'>
    <img src="${app.getThumbnail(125)}" alt="${app.name}" title="${app.name}"/>
</g:if>
<div class='background-image' title="${app.name}">&nbsp;</div>

<div class="details">
	<p>${app.screens.size()} ${message(code:'application.screens')}</p>
	<p>${ui.filesize(size:app.size)}</p>
	<p>${app.medias.size()} ${message(code:'application.medias')}</p>
	<g:if test="${app.active}">
		<p class="icon online">${message(code:'application.status.online')}</p>
		<p>${message(code:'application.status.online')}</p>
	</g:if>
	<g:else>
		<p class="icon offline">${message(code:'application.status.offline')}</p>
		<p>${message(code:'application.status.offline')}</p>
	</g:else>
</div>
<h4>${app.name}</h4>
<g:if test="${app.screens}">
    <h5>${message(code:'ui.application.details.screens')}</h5>
    <ul class='small-pages'>
      <g:each in="${screens}" var="screen">
          <li>
            <g:if test="${screen.getThumbnail(57)}">
                <img src="${screen.getThumbnail(57)}" alt="${screen.name}" title="${screen.name}" />
                <div class='background-icon' title="${app.name}">&nbsp;</div>
            </g:if>
          </li>
      </g:each>
    </ul>
</g:if>