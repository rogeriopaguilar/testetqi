package br.com.tqi.teste.model;

import static br.com.tqi.teste.util.LogUtil.debug;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.tqi.teste.util.LogUtil;

/**
 * Classe que fica no contexto da aplicação e é responsável por verificar se a pesquisa	   <br />
 * já está liberada para visualização pelos usuários. Caso o usuário seja um administrados <br />
 * , ou seja, acessou a url através do menu principal, a visualização é liberada; caso     <br />
 * contrário, a visualização é liberada após o número de minutos configurado no 		   <br />
 * arquivo web.xml 
 * 
 * @author Rogério de Paula Aguilar
 */

@Component
@ApplicationScoped
public class GerenciadorLiberacao  { 

	static Logger log = Logger.getLogger(GerenciadorLiberacao.class.getName());
	
	private ServletContext ctx = null;
	
	private long numeroMinLiberarPesquisa = 10; //10 minutos por padrão
	private long inicioPeriodo = -1;
	private long numeroSegundosLiberarPesquisa;
   	private volatile boolean liberarResultadoParcial = false; 

	
	public GerenciadorLiberacao(ServletContext ctxp) {
		this.ctx = ctxp;
        String strNumeroMinLiberarPesquisa = ctx.getInitParameter("MINUTOS_PARA_LIBERAR_PESQUISA");
        
        if(log.isDebugEnabled()) {
        	debug(this.getClass(), " MINUTOS_PARA_LIBERAR_PESQUISA - ARQUIVO CONF - " + strNumeroMinLiberarPesquisa);
        }
        
        try{
        	numeroMinLiberarPesquisa = Integer.parseInt(strNumeroMinLiberarPesquisa, 10);
        } catch(NumberFormatException e){
        	LogUtil.error(getClass(), "Parãmetro de inicialização inválido! Será utilizado o parâmetro padrão (10 minutos)", e);
        }

        numeroSegundosLiberarPesquisa = numeroMinLiberarPesquisa * 60;        
       	inicioPeriodo = System.currentTimeMillis();

        if(log.isDebugEnabled()) {
        	debug(this.getClass(), " inicioPeriodo - " + inicioPeriodo);
        	debug(this.getClass(), " numeroMinLiberarPesquisa - " + numeroMinLiberarPesquisa);
        	debug(this.getClass(), " numeroSegundosLiberarPesquisa - " + numeroSegundosLiberarPesquisa);
        }
       	
       	
	}

	public boolean liberarResultadoParcial(boolean isAdmin) {
   		if(isAdmin) {
		 /*neste caso o usuário veio da url para administradores, que é diferente da normal 
 		  utilizada para o público em geral. Neste caso, o resultado parcial é exibido
 		  independente da configuração de tempo.*/
 		  return true;
   		}
		
		if(!liberarResultadoParcial) {
	   		long tempoDecorrido = ((System.currentTimeMillis() - inicioPeriodo) / 1000 );
	        if(log.isDebugEnabled()) {
	        	debug(this.getClass(), " tempoDecorrido - " + tempoDecorrido);
	        }
			liberarResultadoParcial = tempoDecorrido > numeroSegundosLiberarPesquisa;
		}
        if(log.isDebugEnabled()) {
        	debug(this.getClass(), " liberarResultadoParcial????? " + liberarResultadoParcial);
        }
		return liberarResultadoParcial;
	}

}
