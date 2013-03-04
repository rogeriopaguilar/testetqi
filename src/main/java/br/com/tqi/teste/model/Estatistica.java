package br.com.tqi.teste.model;

import java.io.Serializable;
import java.util.Arrays;

import br.com.tqi.teste.model.GerenciadorVotos.EnumOpcoesVoto;


/**
 * Classe que guarda as informações sobre o total de votos		<br />
 * em um determinado momento e implementa a lógica para			<br />
 * calcular a porcentagem da votação.							<br />
 * Os métodos que recebem o total de votos não fazem validação  <br />
 * somando os votos para cada opção, pois esta classe será		<br />
 * criada em momentos onde pode estar ocorrendo um processo		<br />
 * de votação com muitos usuários, então optei por realizar     <br />
 * o menor número de operações durante a criação/alteração		<br />
 * dos valores no objeto para não impactar a performance.		<br />
 * da votação pela criação do objeto. O mesmo raciocínio for	<br />
 * utilizado no cálculo das porcentagens, que não é realizado	<br />
 * quando o objeto é criado, e sim quando a informação é de     <br />
 * fato solicitada.
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 **/
public class Estatistica implements Serializable{
	
	private long totalVotos;
	private long totalVotosPorOpcao[] = new long[EnumOpcoesVoto.values().length];
	
	/**
	 * Inicializa o objeto com um total de votos = 0
	 */
	public Estatistica() {
		totalVotos = 0;
	}

	/**
	 * Inicializa o objeto com o total de votos e o total por opção.		<br />
	 * O objeto não faz a validação do total de votos somando o				<br />
	 * total por opção, pois a criação do objeto deve ser rápida.			<br />
	 * 
	 * @param totalVotos
	 * @param totalVotosPorOpcao
	 * @throws IllegalArgumentException caso o total de votos seja < 0 ou se o tamanho do array seja inválido
	 */
	public Estatistica(long totalVotos, long[] totalVotosPorOpcao) {
		setTotalVotos(totalVotos);
		setTotalVotosPorOpcao(totalVotosPorOpcao);
	}

	/**
	 * Retorna o total de votos
	 * */
	public long getTotalVotos() {
		return totalVotos;
	}
	
	/**
	 * Retorna o total de votos por opção em um novo array.
	 * 
	 * @return
	 */
	public long[] getTotalVotosPorOpcao() {
		return Arrays.copyOf(totalVotosPorOpcao, totalVotosPorOpcao.length);
	}

	
	/**
	 * Atualiza o array que contém o total de votos por opção
	 * 
	 * 
	 * @param totalVotosPorOpcao
	 * @throws IllegalArgumentException caso o tamanho do array seja inválido
	 */
	public void setTotalVotosPorOpcao(long[] totalVotosPorOpcao) {
		if(totalVotosPorOpcao == null || totalVotosPorOpcao.length != GerenciadorVotos.TOTAL_OPCOES) {
			throw new IllegalArgumentException("O número de votos por opção passado não corresponde às opções disponíveis!");
		}
		this.totalVotosPorOpcao = totalVotosPorOpcao;
	}
	
	/**
	 * Atualiza o total de votos
	 * 
	 * @param totalVotos
	 * @throws IllegalArgumentException caso o total de votos seja < 0
	 * */
	public void setTotalVotos(long totalVotos) {
		if(totalVotos < 0) {
			throw new IllegalAccessError("O total de votos deve ser maior ou igual a zero!");
		}
		this.totalVotos = totalVotos;
	}

	/**
	 * Retorna o total de votos para uma determinada opção
	 * 
	 * @param opcao
	 * @return total de votos para a opção
	 * @throws NullPointerException caso opcao = null
	 */
	public long getTotalVotosParaOpcao(EnumOpcoesVoto opcao){
		return totalVotosPorOpcao[opcao.ordinal()];
	}
	

	/**
	 * Calcula a porcentagem para uma determinada opção
	 * 
	 * @param opcao
	 * @return porcentagem para a opção
	 * @throws NullPointerException caso opcao = null
	 */
	public double calculaPorcentagemParaOpcao(EnumOpcoesVoto opcao) {
		double porcentagemParaOpcao = 0;
		if(totalVotos == 0) {
			porcentagemParaOpcao = 0;
		} else {
			porcentagemParaOpcao = (double)((getTotalVotosParaOpcao(opcao) * 100.0)/ (double)totalVotos);
		}
		
		return porcentagemParaOpcao;
	}

	@Override
	public String toString() {
		return "Estatistica [totalVotos=" + totalVotos
				+ ", totalVotosPorOpcao=" + Arrays.toString(totalVotosPorOpcao)
				+ "]";
	}
	
	
}
