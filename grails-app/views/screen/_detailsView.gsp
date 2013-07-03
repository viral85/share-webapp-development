<img src="${screen.getThumbnail(125)?:createLinkTo(dir: 'images', file: 'no-image.png')}" alt="${screen.name}" title="${screen.name}"/>
<div title="${screen.name}" class="background-image">&nbsp;</div>
<h4>${screen.name}</h4>
<p>${message(code:'screen.dateCreated')} ${g.formatDate(date:screen.dateCreated,formatName:'ui.date.format')}</p>
<p>${message(code:'screen.lastUpdated')} ${g.formatDate(date:screen.lastUpdated,formatName:'ui.date.format')}</p>