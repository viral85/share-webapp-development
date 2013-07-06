<%--
  Created by IntelliJ IDEA.
  User: amitmpatel
  Date: 7/5/13
  Time: 6:38 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>${message(code:'ui.app.name')} - ${message(code:'ui.login.title')}</title>
    <meta name="layout" content="simple"/>
    <r:require modules="smartpaper-css, smartpaper-js"/>
</head>
<body>
<script type="text/javascript">
    window.onload = function () {

        jQuery.ajax({
                    type : 'POST',
                    url : '/generator/preview/shareAppPreview',
                    data:{'application': '${appId}'},
                    success : function(data, textStatus) {
                        jQuery('#popup').html(data);
                    },
                    error : function(XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
    }
</script>
<div id="title-login">
    <h1>${message(code:'ui.login.title')}</h1>
    <h2>${message(code:'ui.login.subtitle')}</h2>
</div>
<form action='${postUrl}' method='POST' class="user-form one-col" id="login-form" >
    <fieldset>
        <p>
            <input type='text' title='${message(code:'account.username')}' name='j_username' id='username' value="${params.login?:null}" placeholder="${message(code:'account.username')}" class="required email"/>
        </p>
        <p>
            <input type='password' title='${message(code:'account.password')}' name='j_password' id='password' name='password' placeholder="${message(code:'account.password')}" class="required" minlength="5"/>
        </p>
        <p>
            <input type='checkbox' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if> >
            <label for='remember_me'>${message(code:'ui.login.rememberme')}</label>
        </p>
        <div id="login-control">
            <button type="submit" class='submit button orange bigrounded' title="${message(code:'ui.login.submit')}" id="login">${message(code:'ui.login.submit')}</button>
            <span>${message(code:'ui.login.noaccount')} <g:link controller="user" title="${message(code:'ui.login.create')}" action="register">${message(code:'ui.login.create')}</g:link></span>
            <span><g:link controller="user" action="retrieve" title="${message(code:'ui.login.lost')}">${message(code:'ui.login.lost')}</g:link></span>
        </div>
    </fieldset>
</form>
<g:if test="${params.login_error}">
    <div class="errors">
        ${message(code:'ui.login.error')}
    </div>
</g:if>
<div id="popup" class="ui-popup"></div>
<jq:jquery>
    jQuery("#login-form").validate();
</jq:jquery>
</body>
</html>
