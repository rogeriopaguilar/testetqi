<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}"></c:set>

<html>
	<head><title>Votação</title></head>

	<body>
		<div id="divLinks" style="clear:both;">
			<a href="<c:url value='/votacao/formulario'/>">Votar</a> <br />
			<a href="<c:url value='/votacao/resultadoParcialAdmin'/>">Resultado Parcial (admin)</a> <br />
		</div>
		
	</body>
	
</html>