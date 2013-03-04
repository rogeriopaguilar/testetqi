<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>Votação - Resultado Parcial</title>


<script type="text/javascript">



	$(function(){


		<c:if test="${liberarResultadoParcial}">
			$('#divBodyResultado').fadeIn("slow");		
		</c:if>

		<c:if test="${not liberarResultadoParcial}">
			$('#divBodyResultadoMsg').fadeIn("slow");		
		</c:if>
	

				
		}
	)


</script>



</head>
<body>


		<div id="divBody" class="clear">
			<c:if test="${liberarResultadoParcial}">
					<div id="divBodyResultado" style="display: none;">
						<fieldset>
							<legend>Confira o resultado parcial da votação:</legend>
							
							<div class="divResultado">
							Amijubi
							</div>
	
	
							<div class="divResultado corCinza divResultadoPorcentagem">
							 <fmt:formatNumber type="number" pattern="00.00" value="${porcentagemAmijubi}" />%
							</div>
	
	
							<div class="divResultado">
							Fuleco
							</div>
							
							<div class="divResultado corCinza divResultadoPorcentagem">
								<fmt:formatNumber type="number" pattern="00.00" value="${porcentagemFuleco}" />%
							</div>
	
							<div class="divResultado">
							Zuzeco
							</div>
							
							<div class="divResultado corCinza divResultadoPorcentagem">
								<fmt:formatNumber type="number" pattern="00.00" value="${porcentagemZuzeco}" />%
							</div>
							
							
							
						</fieldset>
					</div>
				</c:if>
				
				<c:if test="${not liberarResultadoParcial}">
										
					<div id="divBodyResultadoMsg" style="display: none;">
						<fieldset>
							<legend>Confira o resultado parcial da votação:</legend>
							<center><h4>Obrigado por votar! Resultado parcial não está liberado para o público</h4></center>
						</fieldset>
					</div>
				</c:if>
		</div>


		<div id="divLinkHome" style="clear:both;">
			<a href="<c:url value='/'/>">Home</a> <br />		
		</div>


</body>
</html>