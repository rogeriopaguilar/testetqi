  	var objLogConfig = {"log" : "true"};
  	function log(objJson){
  	  	if(objLogConfig.log){ alert(JSON.stringify(objJson, null, '\t')); }
  	}

	if(typeof objUtil == "undefined") {
		objUtil = {
					TIPO_MENSAGEM:{"ERRO":"ERRO", "INFO":"INFO", "WARNING":"WARNING"},
					
					exibirMensagem : function(message, tipo) {

					    $('#divErrorMessage').hide();
					    $('#divSuccessMessage').hide();

						if(tipo == objUtil.TIPO_MENSAGEM.ERRO) {	
						    $('#divErrorMessage').html(message);
							$('#divErrorMessage').fadeIn("slow");
						}else if(tipo == objUtil.TIPO_MENSAGEM.INFO) {	
						    $('#divSuccessMessage').html(message);
							$('#divSuccessMessage').fadeIn("slow");
						}
						
						
					}


		
				}
	}
  	

	$(
		function() {
			$('body').hide();
			$('body').fadeIn("slow");
			

			$('body').ajaxStart(function() {
				$( "#dialogo-espera" ).fadeIn('slow');
			});
			
			$('body').ajaxComplete(function() {
				$( "#dialogo-espera" ).fadeOut('slow');
			});
			
			$.ajaxSetup({cache:false});
			
			// if user clicked on button, the overlay layer or the dialogbox, close the dialog 
		    $('a.btn-ok, #dialog-overlay, #dialog-box').click(function () {    
		        $('#dialog-overlay, #dialog-box').hide();      
		        return false;
		    });
		     
		    // if user resize the window, call the same function again
		    // to make sure the overlay fills the screen and dialogbox aligned to center   
		    $(window).resize(function () {
		         
		        //only do it if the dialog box is not hidden
		        if (!$('#dialog-box').is(':hidden')) popup();      
		    });
		     			
		}

		
	);  	
