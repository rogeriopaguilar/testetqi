<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="pragma" content="no-cache" />

<title><sitemesh:write property='title' /></title>

<link rel="stylesheet" href="${contexto}/css/dialog.css" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="${contexto}/css/votacao.css" type="text/css" media="screen" charset="utf-8" />
<link rel="stylesheet" href="${contexto}/css/messages.css" type="text/css" media="screen" charset="utf-8" />
<script type="text/javascript" src="${contexto}/js/json-crockford.js"></script>
<script type="text/javascript" src="${contexto}/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${contexto}/js/util.js"></script>
<script type="text/javascript" src="${contexto}/js/jquery-ui-1.8.19.custom.min.js"></script>
<script type="text/javascript" src="${contexto}/js/jquery.maskedinput-1.3.min.js"></script>



<sitemesh:write property='head' />


</head>
<body>

	<div id="divContainer">

		<div id="divHeader">
			<div id="divHeaderImage">
				<img src="${contexto}/img/mascote.jpg" />
			</div>

			<div id="divHeaderTitle">
				<h1>Copa do Mundo 2014</h1>
				<p class="corCinza">Enquete</p>
			</div>
			
			
		</div>

	 	<div class="clear" /> <br />

		
        <div class="success" id="divSuccessMessage" >Successful operation message</div>
        <div class="error" id="divErrorMessage">Error message</div>
        
		<sitemesh:write property='body' />
	</div>


		<div id="dialog-overlay"></div>
		<div id="dialog-box">
		    <div class="dialog-content">
		        <div id="dialog-message"></div>
		        <a href="#" class="button">Close</a>
		    </div>
		</div>		





</script>
</body>
</html>