<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>${message(code:'ui.app.name')}  - ${message(code:'ui.app.error')} 500</title>
	<style type="text/css">
  <!--

  body {
	background-color: #333;
    color:#fff;
  }
  #soon {
  position:absolute;
  left: 50%;
  top: 50%;
  width: 260px;
  height: 60px;
  margin-top: -30px; /* moitié de la hauteur */
  margin-left: -130px; /* moitié de la largeur */
  }

  .message {}

  p {
  	text-align: center;
  	color:#fff;
  	font-family: Arial, Helvetica, sans-serif;
  }

  .message {
      text-align: left;
      color:#fff;
  }
  -->
  </style>
</head>

<body>

<div id="soon">
	<img src="${createLinkTo(dir: 'images', file: 'logo.png')}"/>
	<p>${message(code:'ui.500.text')}</p>
</div>
<div style='display:none;'>
    <div class="message">
        <strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
        <strong>Servlet:</strong> ${request.'javax.servlet.error.servlet_name'}<br/>
        <strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/>
        <g:if test="${exception}">
            <strong>Exception Message:</strong> ${exception.message?.encodeAsHTML()} <br />
            <strong>Caused by:</strong> ${exception.cause?.message?.encodeAsHTML()} <br />
            <strong>Class:</strong> ${exception.className} <br />
            <strong>At Line:</strong> [${exception.lineNumber}] <br />
            <strong>Code Snippet:</strong><br />
            <div class="snippet">
                <g:each var="cs" in="${exception.codeSnippet}">
                    ${cs?.encodeAsHTML()}<br />
                </g:each>
            </div>
        </g:if>
    </div>
    <g:if test="${exception}">
        <h2>Stack Trace</h2>
        <div class="stack">
          <pre><g:each in="${exception.stackTraceLines}">${it.encodeAsHTML()}<br/></g:each></pre>
        </div>
    </g:if>
</div>
</body>
</html>