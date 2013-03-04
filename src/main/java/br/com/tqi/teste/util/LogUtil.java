package br.com.tqi.teste.util;

import org.apache.log4j.Logger;

/**
 * Classe que deve ser utilizada pelas outras do sistema para logar mensagens <br />
 * O arquivo de configuração do log da aplicação chama-se log4j.properties    <br />
 * e está na pasta src/main/resources do maven.								  <br/>
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 */

public class LogUtil {

	/**
	 * Exibe mensagens no log com nível debug
	 * 
	 * @param classe 
	 * @param msg texto da mensagem
	 */
	public static void debug(Class classe, String msg) {
		Logger log = Logger.getLogger(classe.getName());
		if(log.isDebugEnabled()) {
			log.debug("[" + Thread.currentThread().getId() + "] -"  + msg);
		}
	}

	/**
	 * Exibe mensagens de erro no log 
	 * 
	 * @param classe 
	 * @param msg texto da mensagem
	 * @param t objeto que representa o erro ocorrido na aplicação
	 */
	public static void error(Class classe, String msg, Throwable t) {
		Logger log = Logger.getLogger(classe.getName());
		log.error("[" + Thread.currentThread().getId() + "] " + msg, t);
	}
	
	
	
}
