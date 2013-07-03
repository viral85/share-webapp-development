<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>${message(code:'ui.app.name')} - ${message(code:'ui.app.error')} 403</title>
	<style type="text/css">
  <!--

  body {
	background-color: #333;
  }
  div {
  position:absolute;
  left: 50%; 
  top: 50%;
  width: 260px;
  height: 60px;
  margin-top: -30px; /* moitié de la hauteur */
  margin-left: -130px; /* moitié de la largeur */
  }
  
  p {
  	text-align: center;
  	color:#fff;
  	font-family: Arial, Helvetica, sans-serif;
  }
  -->
  </style>
</head>

<body>

<div id="soon">
	<img src="${createLinkTo(dir: 'images', file: 'logo.png')}"/>
	<p>${message(code:'ui.403.text')}</p>
</div>
</body>
</html>
