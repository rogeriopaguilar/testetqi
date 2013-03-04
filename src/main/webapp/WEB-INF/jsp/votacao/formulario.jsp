<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contexto" value="${pageContext.request.contextPath}"></c:set>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Votação - Formulário de Votação</title>

<script type="text/javascript">

	$(function(){
		$('#cmdVotar').click(function(){
				$('#cmdVotar').hide();
				var value = $("input[name='escolha.voto']:checked").val();
				if(undefined == value) {
					objUtil.exibirMensagem('Selecione uma opção!', objUtil.TIPO_MENSAGEM.ERRO);
					$('#cmdVotar').show();
				} else {
					var url = '<c:url value="/votacao/votar"/>';
					var dados =  $('#frmVotacao').serialize();
					$.ajax({
						  type: 'POST',
						  url: url,
						  assync: false,
						  data: dados,
						  success: function(data)
						  {
							//log(data);
							if(data.erro) {
								objUtil.exibirMensagem(data.mensagem, objUtil.TIPO_MENSAGEM.ERRO);
								$("#imgCaptcha").remove();
								var urlCaptcha = '${contexto}/jcaptcha.jpg' + ('?' + Math.random()).replace('.','');
								var img = $("<img />").attr('id','imgCaptcha').attr('src', urlCaptcha)
							    .load(function() {
							            $("#divImagemCaptcha").append(img);
							    });
							} else {
								$('#divBody').load( '<c:url value="/votacao/resultadoParcial"/> #divBody',
										function(response, status, xhr) {
											$('#divBodyResultado').fadeIn("slow");		
											$('#divBodyResultadoMsg').fadeIn("slow");																	
											objUtil.exibirMensagem('Seu voto foi computado com sucesso!', objUtil.TIPO_MENSAGEM.INFO);
										}
								);									
							}
							$('#cmdVotar').show();
							
						  },
						  error: function (xhr, ajaxOptions, thrownError) {
						        alert(xhr.status);
						        alert(thrownError);
								$('#cmdVotar').show();
						      }						  
					      ,
						  dataType: 'json'
						});		
					
				}
				
		});


		<c:if test="${not empty ERRO}">
			objUtil.exibirMensagem('${ERRO}', objUtil.TIPO_MENSAGEM.ERRO);
		</c:if>

		<c:if test="${not empty param['escolha.voto']}">
			var radio = $('input:radio[name="escolha.voto"]');
			radio.filter('[value=${param["escolha.voto"]}]').attr('checked', true);
		</c:if>



		$('input').keypress(function (e) {
			  if (e.which == 13) {
			    return false;
			  }
			});			
		

		}


	
	)


</script>

<style type="text/css">
	#divBodyOptions {
		width: 470px;
		float:left;
	}

</style>

</head>
<body>

		<div id="divBody" class="clear">
			<form id="frmVotacao" action="${contexto}/votacao/votar" method="post">
				<input type="hidden" name="ajax" value="true"/>
				<div id="divBodyOptions" >
					<fieldset>
						<legend>1. Escolha o melhor nome para o mascote:</legend>
							<input type="radio" name="escolha.voto" value="0" class="floatLeft">Amijubi
							<span class="descOpcao" >
								União das palavras amizade e jubilo, que está ligado ao tupi- guarani , em que jubi significa amarelo, cor predominante no mascote
							</span>
						 	<div class="clear" /> <br />
							
							<input type="radio" name="escolha.voto" value="1" class="floatLeft">Fuleco
							<span class="descOpcao" >
								Uma mistura de futebol e ecologia. O nome busca incentivar o cuidado das pessoas com o meio ambiente.
							</span>
						 	<div class="clear" /> <br />
			
							<input type="radio" name="escolha.voto" value="2" class="floatLeft">Zuzeco
							<span class="descOpcao" >
								Mistura de cor azul com ecologia , que busca também incentivar cuidados relacionados à ecologia
							</span>
						 	<div class="clear" /> <br />
		
					</fieldset>
				</div>
				
				<div id="divBodySubmit" >
				
					2. Digite o código de verificação abaixo
					
					<br />
					<center>
						<div id="divImagemCaptcha">
							<img src="${contexto}/jcaptcha.jpg" id="imgCaptcha"/>
						</div>
					</center>						
					<br />
					<center>
					 <input type='text' name='j_captcha_response' value='' />			
					</center>
					<br />	
					
					
					<center>
						<a href="#" class="botaoVotar" id="cmdVotar">Votar</a>
					</center>					
					
					<div id="dialogo-espera" class="clear" style="text-align: center; padding-bottom: 5px; display:none;">
						<img src="${contexto}/img/ajax.gif"/> Aguarde...
					</div>
					
				</div>
			</form>				
			
		</div>

		<div id="divLinkHome" style="clear:both;">
			<a href="<c:url value='/'/>">Home</a> <br />		
		</div>




</body>
</html>