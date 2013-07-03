<g:if test="${screen.inputs}">
    <table id="form-list" class="table-list" cellpadding="10">

        <tbody>
            <tr class="table-title">
                <th width="40%">${message(code:'ui.screen.form.list.label')}</th>
                <th width="20%">${message(code:'ui.screen.form.list.required')}</th>
                <th width="20%">${message(code:'ui.screen.form.list.position')}</th>
                <th width="20%">${message(code:'ui.screen.form.list.delete')}</th>
            </tr>
        </tbody>
        <tbody class="content">
            <g:each in="${screen.inputs}" var="input">
                <tr class="item" elemid="${input.id}">
                    <td>
                        <input title="${input.label}" class="n-big label-input" type="text" value="${input.label}">
                    </td>
                    <td>
                       <input type="checkbox" ${input.required ? 'checked="checked"' : ''} class='required-input' name='input.required'/>
                    </td>
                    <td>
                        <span class="icon iconsmall move up" title="${message(code:'ui.screen.form.list.up')}">${message(code:'ui.screen.form.list.up')}</span>
                        <span class="icon iconsmall move down" title="${message(code:'ui.screen.form.list.down')}">${message(code:'ui.screen.form.list.down')}</span>
                    </td>
                    <td>
                        <span class="icon iconmini delete" title="${message(code:'ui.screen.form.list.delete')}"></span>
                    </td>
                </tr>
            </g:each>
        </tbody>
    </table>
</g:if>