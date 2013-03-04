package br.com.tqi.teste.web;
import static br.com.tqi.teste.util.LogUtil.debug;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.com.tqi.teste.model.Estatistica;
import br.com.tqi.teste.model.GerenciadorLiberacao;
import br.com.tqi.teste.model.GerenciadorVotos;
import br.com.tqi.teste.model.GerenciadorVotos.EnumOpcoesVoto;
import br.com.tqi.teste.model.Voto;
import br.com.tqi.teste.web.ajax.RespostaAjax;
import br.com.tqi.teste.web.captcha.CaptchaServiceSingleton;

/**
 * Classe que processa as requisições web
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 */

@Resource
public class VotacaoController {
	
	static Logger log = Logger.getLogger(VotacaoController.class.getName());
   
	private Result result;	
	private GerenciadorVotos gerenciadorVotos;
	private GerenciadorLiberacao gerenciadorLiberacao;
	private HttpServletRequest request;
	
	/**
	 * Construtor que recebe as dependências necessárias para o funcionamento da classe
	 * @param result
	 * @param gerenciadorVotos
	 * @param request
	 */
    public VotacaoController(Result result, GerenciadorVotos gerenciadorVotos, GerenciadorLiberacao gerenciadorLiberacao, HttpServletRequest request) {
        this.result = result;   
        this.gerenciadorVotos = gerenciadorVotos;
        this.gerenciadorLiberacao = gerenciadorLiberacao;
        this.request = request;
    }
   
    /**
     * Redireciona o usuário para o formulário de votação
     */
    public void formulario() {
    }

    /**
     * Contabiliza o voto do usuário caso o captcha seja válido  <br />
     * Após a contabilização, redireciona o usuário para a tela  <br />
     * que exibe as porcentagens caso a requisição seja comum   <br />
     * ou retorna informações no formato json caso a requisição <br />
     * utilize ajax.
     * 
     * @param escolha
     */
    @Post
    public void votar(Voto escolha) {
    	boolean debug = log.isDebugEnabled();
    	boolean ajax = Boolean.TRUE.equals(Boolean.valueOf(request.getParameter("ajax")));
    	
    	if(debug) {
    		debug(this.getClass(),"usuário votando --> " + escolha);
    	}
    	
        String captchaId = request.getSession().getId();
        String response = request.getParameter("j_captcha_response");
    	boolean captchaValido = CaptchaServiceSingleton.captchaIsValid(captchaId, response);
    	if(captchaValido) {
        	if(debug) {
        		debug(this.getClass(),"captcha válido...");
        	}
	    	gerenciadorVotos.votar(escolha);
	    	if(debug) {
	    		debug(this.getClass(),"voto computado...");
	    	}
	    	if(!ajax) {
		    	if(debug) {
		    		debug(this.getClass(),"requisição comum, redirecionando...");
		    	}
	    		result.redirectTo(this.getClass()).resultadoParcial();
	    	} else {
		    	if(debug) {
		    		debug(this.getClass(),"requisição ajax, retornando json...");
		    	}
		    	RespostaAjax resposta = new RespostaAjax("", false, "");
				result.use(Results.json()).withoutRoot().from(resposta).recursive().serialize();
	    	}
    	} else {
	    	if(debug) {
	    		debug(this.getClass(),"captcha INválido...");
	    	}

	    	if(!ajax) {
		    	if(debug) {
		    		debug(this.getClass(),"requisição comum, redirecionando...");
		    	}
	    		result.include("ERRO", "O código de verificação informado é inválido!");
	    		result.forwardTo(getClass()).formulario();
	    	} else {
		    	if(debug) {
		    		debug(this.getClass(),"requisição ajax, retornando json...");
		    	}
		    	RespostaAjax resposta = new RespostaAjax("", true, "O código de verificação informado é inválido! Tente novamente!");
				result.use(Results.json()).withoutRoot().from(resposta).recursive().serialize();
	    	}
	    	
	    	
    	}
    	
    }

    /**
     * URL diferente para o levar o usuário à tela de porcentagens   <br />
     * Esta URL exibirá o resultado independentemente do parâmetro   <br />
     * que configura o tempo que a pesquisa deve ser disponibilizada <br />
     * para qualquer usuário. A idéia seria configurar esta url		 <br />
     * no servidor apenas para alguns usuário administradores,		 <br />
     * que poderiam consultar os resultados independentemente 		 <br />
     * do parâmetro de configuração.
     */
    public void resultadoParcialAdmin() 
    {
    	boolean debug = log.isDebugEnabled();
    	request.setAttribute("ADMIN_URL", Boolean.TRUE);    	
      	if(debug) {
    		debug(this.getClass(),"resultado acessado por um administrador...");
    	}    	
    	result.forwardTo(this.getClass()).resultadoParcial();
    }	
    
    /**
     * Calcula as porcentagens e redireciona para a tela de resultado       <br />
     * Caso o usuário seja um usuário qualquer, o resultado somente será    <br />
     * exibido após o número de minutos configurado no arquivo web.xml.     <br />
     * Caso o usuário seja um administrador (acessou a url resultado		<br />
     * parcialAdmin, ele verá o resultado independentemente deste parâmetro.
     */
    public void resultadoParcial() 
    {
    	boolean debug = log.isDebugEnabled();
    	if(debug) {
    		debug(this.getClass(),"gerando resultado parcial...");
    	}
	   	Estatistica estatistica = gerenciadorVotos.getEstatisticasVoto();
    	if(debug) {
    		debug(this.getClass(),"resultado parcial gerado " + estatistica);
    	}
	   	result.include("estatistica", estatistica);
	   	double estatisticaAmijubi = estatistica.calculaPorcentagemParaOpcao(EnumOpcoesVoto.Amijubi);
	   	double estatisticaFuleco = estatistica.calculaPorcentagemParaOpcao(EnumOpcoesVoto.Fuleco);
	   	double estatisticaZuzeco = estatistica.calculaPorcentagemParaOpcao(EnumOpcoesVoto.Zuzeco);
	   	
	   	double[] valoresAjustados = gerenciadorVotos.ajustarEstatisticas(estatisticaAmijubi, estatisticaFuleco, estatisticaZuzeco);
	   	estatisticaAmijubi = valoresAjustados[0];
	   	estatisticaFuleco = valoresAjustados[1];
	   	estatisticaZuzeco = valoresAjustados[2];
	   	
	   	boolean liberarResultadoParcial = gerenciadorLiberacao.liberarResultadoParcial((request.getAttribute("ADMIN_URL") != null));
	   	
    	if(debug) {
    		debug(this.getClass(), "Resultados ajustados - " + Arrays.toString(valoresAjustados));
    		debug(this.getClass(), "LIBERAR RESULTADO PARCIAL? " + liberarResultadoParcial);
    		debug(this.getClass(), "Estatísticas: [Amijubi - " + estatisticaAmijubi +  "] [Fuleco - " + estatisticaFuleco + "] [ Zuzeco - " + estatisticaZuzeco + " ] "); 
    	}
    	
	   	result.include("porcentagemAmijubi", estatisticaAmijubi);
    	result.include("porcentagemFuleco", estatisticaFuleco);
    	result.include("porcentagemZuzeco", estatisticaZuzeco);
    	result.include("liberarResultadoParcial", liberarResultadoParcial);
    	
    }
    
    
}