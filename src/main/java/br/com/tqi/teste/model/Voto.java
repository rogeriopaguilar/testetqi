package br.com.tqi.teste.model;

import java.io.Serializable;

import br.com.tqi.teste.model.GerenciadorVotos.EnumOpcoesVoto;

/**
 * Classe que representa o voto feito pelo usuário
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 */
public class Voto implements Serializable{

	private int voto;
	private static int totalOpcoesVotos = EnumOpcoesVoto.values().length;

	/**
	 * Retorna o voto configurado
	 * 
	 * @return o voto configurado
	 */
	public int getVoto() {
		return voto;
	}

	/**
	 * Altera o valor do voto.
	 * 
	 * @param voto
	 */
	public void setVoto(int voto) {
		if(voto < 0 || voto > totalOpcoesVotos) {
			throw new IllegalArgumentException("Opção de voto inválida! Ver opções em EnumVotos");
		}

		this.voto = voto;
	}
	
	/**
	 * Altera o valor do voto utilizando a enum definida no gerenciador de votos
	 * @param voto
	 * @throws NullPointerException caso voto = null
	 */
	public void setVoto(EnumOpcoesVoto voto) {
		setVoto(voto.ordinal());
	}
	
	@Override
	public String toString() {
		return "Voto [voto=" + voto + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + voto;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Voto other = (Voto) obj;
		if (voto != other.voto)
			return false;
		return true;
	}
	
	
	
	
}
