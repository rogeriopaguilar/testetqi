package br.com.tqi.teste.web.ajax;

import java.io.Serializable;

/**
 * Classe utilizada para preencher dados que serão retornados ao usuário <br />
 * no formato json.
 * 
 * @author Rogério de Paula Aguilar
 */
public class RespostaAjax implements Serializable {

	private Object resposta;
	private boolean erro = false;
	private String mensagem = null;
	
	/**
	 * 
	 * @param resposta objeto que será retornado para o usuário
	 * @param erro indica se a mensagem indica um erro de processamento
	 * @param mensagem mensagem de texto retornada para o usuário
	 */
	public RespostaAjax(Object resposta, boolean erro, String mensagem) {
		this.resposta = resposta;
		this.erro = erro;
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		return "RespostaAjax [resposta=" + resposta + ", erro=" + erro
				+ ", mensagem=" + mensagem + "]";
	}
	
	
	
	
	
	
	
}
