<g:if test="${screen.links}">
    <table id="link-list" class="table-list" cellpadding="10">

        <tbody>
            <tr class="table-title">
                <th width="15%">${message(code:'ui.screen.linksList.list.image')}</th>
                <th width="30%">${message(code:'ui.screen.linksList.list.link')}</th>
                <th width="30%">${message(code:'ui.screen.linksList.list.title')}</th>
                <th width="12%">${message(code:'ui.screen.linksList.list.position')}</th>
                <th width="13%">${message(code:'ui.screen.linksList.list.delete')}</th>
            </tr>
        </tbody>
        <tbody class="content">
            <g:each in="${screen.links}" var="link">
                <tr class="item" elemid="${link.id}">
                    <td>
                        <g:if test="${link.image}">
                            ${r.img(uri:link.image.url,title:link.name,alt:link.name)}
                            <span class="image iconmini icon edit"></span>
                        </g:if>
                        <g:else>
                            <button class="image button small black"><g:message code="ui.screen.linksList.choose.image"/></button>
                        </g:else>
                    </td>
                    <td>
                        <g:if test="${link.linkTo}">
                            <span class="editpage" title="${link.linkTo.name}">${ui.truncate([value:link.linkTo.name,max:15])}</span>
                        </g:if>
                        <g:else>
                            <button class="editpage button small black"><g:message code="ui.screen.linksList.list.choose.screen"/></button>
                        </g:else>
                    </td>
                    <td>
                        <input title="${link.name}" class="dynamic title-link" type="text" value="${link.name}">
                    </td>
                    <td>
                        <span class="icon iconsmall move up" title="${message(code:'ui.screen.linksList.list.up')}">${message(code:'ui.screen.linksList.list.up')}</span>
                        <span class="icon iconsmall move down" title="${message(code:'ui.screen.linksList.list.down')}">${message(code:'ui.screen.linksList.list.down')}</span>
                    </td>
                    <td>
                        <span class="icon iconmini delete" title="${message(code:'ui.screen.linksList.list.delete')}"></span>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
</g:if>