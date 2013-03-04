package br.com.tqi.teste.model;

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicLong;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.tqi.teste.util.LogUtil;


/**
 * Classe que realiza o gerenciamento de votos no sistema.             <br />
 * A implementação define uma enum com os tipos de votos disponíveis,  <br />
 * variáveis de contagem atômicas para cada tipo de voto, uma variável <br/>
 * para o total de votos e variáveis de controle de acesso			   <br />
 * para as threads que acessam os métodos de votação e de geração	   <br />
 * de estatística de votos.											   <br />
 * A classe é guardada no contexto da aplicação web e compartilhada	   <br />
 * pelos usuários do sistema.
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 */
@Component
@ApplicationScoped
public class GerenciadorVotos {

	public static enum EnumOpcoesVoto{Amijubi, Fuleco, Zuzeco};
	
	private static AtomicLong[] votos = new AtomicLong[EnumOpcoesVoto.values().length];
	private static AtomicLong totalVotos = new AtomicLong();
	private static volatile boolean gerandoEstatistica = false;
	private static volatile boolean votando = false;
	public static final int TOTAL_OPCOES = EnumOpcoesVoto.values().length;
	
	/**
	 * Inicializa os contadores
	 */
	static {
		for(int i = 0; i < TOTAL_OPCOES; votos[i] = new AtomicLong(), i++);
		
	}
	
	/**
	 * Reinicializa os contadotes. Este método é utilizo para testes apenas, <br />
	 * já que o mesmo objeto é utilizado nos diferentes métodos dos testes.
	 */
	public static void reset() {
		for(int i = 0; i < TOTAL_OPCOES; votos[i] = new AtomicLong(), i++);
		totalVotos = new AtomicLong();
	}
	
	
	/**
	 * Carrega os valors do objeto estatística no gerenciador.	Este método é utilizado apenas	<br />
	 * durante a inicialização da aplicação, para carregar uma esstatística que foi				<br />
	 * computada anteriormente. O método não está preparado para ser executado					<br />
	 * de forma concorrente, quando a votação já estiver ocorrendo.								<br />
	 * @throws IllegalArgumentException caso estatistica = null ou não existam totais para todas as opções
	 * */
	public static void resetComEstatistica(Estatistica estatistica) {
		if(estatistica == null){
			throw new IllegalArgumentException("Objeto Estatistica deve ser diferente de null!");
		}
		long[] totais = estatistica.getTotalVotosPorOpcao();
		if(totais == null || totais.length != TOTAL_OPCOES) {
			throw new IllegalArgumentException("A estatística deve conter os totais para todos as opções!");
		}
		reset();
		totalVotos.set(estatistica.getTotalVotos());
		for(int i = 0; i < TOTAL_OPCOES; votos[i].set(totais[i]), i++);
	}
	
	/**
	 * Realiza o processo de votação, tomando o cuidado			<br />
	 * de uma votação não acontecer ao mesmo tempo				<br />
	 * que uma geração de estatística de votação.				<br />
	 * @param voto
	 */
	public static void votar(Voto voto) {
		while(gerandoEstatistica){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		votando = true;
		try {
			if(voto != null) {
				votos[voto.getVoto()].incrementAndGet();
				totalVotos.incrementAndGet();
			} else {
				throw new IllegalAccessError("Voto deve ser != NULL!");
			}
		}finally{
			votando = false;
		}
	}
	
	/**
	 * Recupera as estatísticas de votação atuais, tomando o cuidado <br />
	 * de esperar até que ninguém esteja votando no momento			 <br />
	 * em que a estatística é gerada.								 <br />
	 * As porcentagens não são calculadas neste momento, apenas os   <br />
	 * contadores são retornados no objeto. Isto foi feito para que	 <br />
	 * a geração de estatística fosse feita no menor tempo possível. <br />
	 * O objeto retornado encapsula a lógica para calcular as 		 <br />
	 * porcentagens, porém este cálculo só é realizado quando o		 <br />
	 * usuário solicita alguma porcentagem ("lazy-loading").		<br />
	 * 
	 * @return Estatistica
	 */
	public static Estatistica getEstatisticasVoto() {
		while(votando){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gerandoEstatistica = true;
		Estatistica estatistica = null;
		try {
			long[] totalVotosPorOpcao = new long[TOTAL_OPCOES];
			for(int i = 0; i < TOTAL_OPCOES; totalVotosPorOpcao[i] = votos[i].longValue(), i++);
			estatistica = new Estatistica(totalVotos.get(), totalVotosPorOpcao);
		} finally {
			gerandoEstatistica = false;
		}
		return estatistica;
				
	}
	
	/**
	 * Gera a estatística atual e grava num arquivo xml que será gravado no local 	<br />
	 * informado pelo parâmetro caminhoArquivo.										<br />
	 * 
	 * @param caminhoArquivo local onde o arquivo será gerado
	 * @throws FileNotFoundException
	 */
	public static void gerarXml(String caminhoArquivo) throws FileNotFoundException{
		Estatistica estatistica = getEstatisticasVoto();
		OutputStream os = new BufferedOutputStream(new FileOutputStream(caminhoArquivo));
		XMLEncoder e = new XMLEncoder(os);
		try{
			e.writeObject(estatistica);
			e.setExceptionListener(new ExceptionListener() {
					public void exceptionThrown(final Exception e) {
					    LogUtil.error(GerenciadorVotos.class, "Erro ao serializar votação!", e);
					}
		    });		
		}finally{
			e.close();
		}
		
	}
	 
	/**
	 * Lê o arquivo xml no caminho especificado pelo parâmetro caminhoArquivo,   <br />
	 * cria um objeto Estatistica contendo os valores do arquivo e retorna		 <br />
	 * este objeto.
	 * 
	 * @param caminhoArquivo
	 * @return objeto estatístcia contendo o valor lido do arquivo
	 * @throws FileNotFoundException
	 */
	public static Estatistica lerXml(String caminhoArquivo) throws FileNotFoundException{
		Estatistica estatistica = null;
		InputStream is = new BufferedInputStream(new FileInputStream(caminhoArquivo));
		XMLDecoder d = new XMLDecoder(is);
		try{
			d.setExceptionListener(new ExceptionListener() {
				public void exceptionThrown(final Exception e) {
				    LogUtil.error(GerenciadorVotos.class, "Erro ao recuperar dados do arquivo de votação!", e);
				}
			});		
			estatistica = (Estatistica)d.readObject();
		}finally{
			d.close();
		}
		return estatistica;
		
	}
	
	/**
	 * Soma as porcentagens. Caso a soma não seja igual a 100 (por erros de arredondamento),
	 * calcula a diferença e adiciona na primeira porcentagem
	 * 
	 * @param e1 primeira porcentagem
	 * @param e2 segunda porcentagem
	 * @param e3 terceira porcentagem
	 * @return double[] contendo os valores corrigidos
	 */
	public static double[] ajustarEstatisticas(double e1, double e2, double e3) {
		final BigDecimal decimalCem = new BigDecimal("100.0").setScale(2, RoundingMode.HALF_EVEN);
		final BigDecimal decimalZero = new BigDecimal("0.0").setScale(2, RoundingMode.HALF_EVEN);

		BigDecimal d1 = new BigDecimal("" + e1).setScale(2, RoundingMode.HALF_EVEN);;
		BigDecimal d2 = new BigDecimal("" + e2).setScale(2, RoundingMode.HALF_EVEN);;
		BigDecimal d3 = new BigDecimal("" + e3).setScale(2, RoundingMode.HALF_EVEN);;
		
		if(decimalZero.compareTo(d1) == 0 && decimalZero.compareTo(d2) == 0 && decimalZero.compareTo(d3) == 0) {
			//Não ocorreu votação ainda
			return new double[]{d1.doubleValue(), d2.doubleValue(), d3.doubleValue()};
		}
		
		BigDecimal soma = d1.add(d2).setScale(2, RoundingMode.HALF_EVEN).add(d3).setScale(2, RoundingMode.HALF_EVEN);;
		int i = decimalCem.compareTo(soma);
		
		if(i != 0) {
			BigDecimal diferenca = null;
			if(i > 0) {
				diferenca = decimalCem.subtract(soma).setScale(2, RoundingMode.HALF_EVEN);
				d1 = d1.add(diferenca).setScale(2, RoundingMode.HALF_EVEN);
			} else {
				diferenca = soma.subtract(decimalCem).setScale(2, RoundingMode.HALF_EVEN);
				d1 = d1.add(diferenca).setScale(2, RoundingMode.HALF_EVEN);
			}
		}
		
		return new double[]{d1.doubleValue(), d2.doubleValue(), d3.doubleValue()};
	}
	
	
	
}