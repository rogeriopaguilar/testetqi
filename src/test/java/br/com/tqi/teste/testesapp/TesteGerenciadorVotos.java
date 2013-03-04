package br.com.tqi.teste.testesapp;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import br.com.tqi.teste.model.Estatistica;
import br.com.tqi.teste.model.GerenciadorVotos;
import br.com.tqi.teste.model.GerenciadorVotos.EnumOpcoesVoto;
import br.com.tqi.teste.model.Voto;

public class TesteGerenciadorVotos {

	@Test
	public void testeVotosValidosSimples() {
		
		GerenciadorVotos.reset();
		Voto voto = new Voto();
		
		voto.setVoto(EnumOpcoesVoto.Amijubi.ordinal());
		GerenciadorVotos.votar(voto);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotos(), 1L);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotosParaOpcao(EnumOpcoesVoto.Amijubi), 1L);
		
		voto.setVoto(EnumOpcoesVoto.Fuleco.ordinal());
		GerenciadorVotos.votar(voto);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotos(), 2L);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotosParaOpcao(EnumOpcoesVoto.Amijubi), 1L);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotosParaOpcao(EnumOpcoesVoto.Fuleco), 1L);
		
		
		voto.setVoto(EnumOpcoesVoto.Zuzeco.ordinal());
		GerenciadorVotos.votar(voto);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotos(), 3L);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotosParaOpcao(EnumOpcoesVoto.Amijubi), 1L);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotosParaOpcao(EnumOpcoesVoto.Fuleco), 1L);
		assertEquals(GerenciadorVotos.getEstatisticasVoto().getTotalVotosParaOpcao(EnumOpcoesVoto.Zuzeco), 1L);

		
		
	}
	
	
	@Test
	public void testeVotosValidosMultiusuario() throws Exception {
		GerenciadorVotos.reset();
		int QTDE_USUARIOS = 500;
		ExecutorService exec = Executors.newCachedThreadPool();
		Future f[] = new Future[QTDE_USUARIOS];
		final int max = EnumOpcoesVoto.values().length-1;
		long qtdeVotosAmijubi = 0;
		long qtdeVotosFuleco = 0;
		long qtdeVotosZuzeco = 0;
		long tempoExecucao = 0;
		
		final CountDownLatch latch = new CountDownLatch(1);
		
		for(int i = 0; i < QTDE_USUARIOS; i++) {
			f[i] = exec.submit(
					new Callable<Integer>() {

						public Integer call() throws Exception {
							latch.await();
							int indiceVoto = randomInRange(0, max);
							Voto voto = new Voto();
							voto.setVoto(indiceVoto);
							GerenciadorVotos.votar(voto);
							return indiceVoto;
						}
					}
			);
		}

		tempoExecucao = System.currentTimeMillis();
		latch.countDown();
		
		exec.shutdown();
		try {
			exec.awaitTermination(10, TimeUnit.MINUTES);
			tempoExecucao = System.currentTimeMillis() - tempoExecucao;
			for(int i = 0; i < QTDE_USUARIOS; i++) {
				int valorAtual = ((Integer)f[i].get()).intValue();
				if(EnumOpcoesVoto.Amijubi.ordinal() == valorAtual) {
					qtdeVotosAmijubi++;
				} else if(EnumOpcoesVoto.Fuleco.ordinal() == valorAtual) {
					qtdeVotosFuleco++;
				} else if(EnumOpcoesVoto.Zuzeco.ordinal() == valorAtual) {
					qtdeVotosZuzeco++;
				}	
			}	

			long qtdeTotalVotos = qtdeVotosAmijubi + qtdeVotosFuleco + qtdeVotosZuzeco;
			
			Estatistica estatistica = GerenciadorVotos.getEstatisticasVoto();
			
			assertEquals(estatistica.getTotalVotosParaOpcao(EnumOpcoesVoto.Amijubi), qtdeVotosAmijubi);
			assertEquals(estatistica.getTotalVotosParaOpcao(EnumOpcoesVoto.Fuleco), qtdeVotosFuleco);
			assertEquals(estatistica.getTotalVotosParaOpcao(EnumOpcoesVoto.Zuzeco), qtdeVotosZuzeco);
			assertEquals(estatistica.getTotalVotos(), qtdeTotalVotos);
			assertTrue(tempoExecucao < 1000);
		} catch (Exception e) {
			throw e;
		}
		
		
		
		
	}

	@Test
	public void testeVotosValidosMultiusuarioPico() throws Exception {
		int QTDE_EXEC = 10;
		for(int i = 0; i < QTDE_EXEC; i++) {
			testeVotosValidosMultiusuario();
		}
	}	
	
	public static int randomInRange(int bound1, int bound2) {
	    int min = Math.min(bound1, bound2);
	    int max = Math.max(bound1, bound2);
	    return min + (int)(Math.random() * (max - min + 1));
	}
	
	@Test
	public void testeSerializacaoVotos() throws FileNotFoundException {
		GerenciadorVotos.reset();
		int qtdeVotosAmijubi = 10;
		int qtdeVotosFuleco = 20;
		int  qtdeVotosZuzeco = 15;
		Voto voto = new Voto();

		for(int i = 0 ; i < qtdeVotosAmijubi; i++) {
			voto.setVoto(EnumOpcoesVoto.Amijubi.ordinal());
			GerenciadorVotos.votar(voto);
		}

		for(int i = 0 ; i < qtdeVotosFuleco; i++) {
			voto.setVoto(EnumOpcoesVoto.Fuleco.ordinal());
			GerenciadorVotos.votar(voto);
		}

		for(int i = 0 ; i < qtdeVotosZuzeco; i++) {
			voto.setVoto(EnumOpcoesVoto.Zuzeco.ordinal());
			GerenciadorVotos.votar(voto);
		}
		
		long qtdeTotalVotos = qtdeVotosAmijubi + qtdeVotosFuleco + qtdeVotosZuzeco;
		
		String caminhoArquivo = System.getProperty("user.dir") + "/estatistica-votos.xml";
		GerenciadorVotos.gerarXml(caminhoArquivo);
		Estatistica estatistica = GerenciadorVotos.lerXml(caminhoArquivo);
		assertEquals(estatistica.getTotalVotosParaOpcao(EnumOpcoesVoto.Amijubi), qtdeVotosAmijubi );
		assertEquals(estatistica.getTotalVotosParaOpcao(EnumOpcoesVoto.Fuleco), qtdeVotosFuleco);
		assertEquals(estatistica.getTotalVotosParaOpcao(EnumOpcoesVoto.Zuzeco), qtdeVotosZuzeco);		
		assertEquals(estatistica.getTotalVotos(), qtdeTotalVotos);		
		
	}
	
	@Test
	public void testeArredondamentoVotos() {
		double v1 = 33.33, v2 = 33.33, v3 = 33.33;
		
		double[] valoresAjustados = GerenciadorVotos.ajustarEstatisticas(v1, v2, v3);
		
		assertEquals(""+valoresAjustados[0], "33.34");
		assertEquals(""+valoresAjustados[1], "33.33");
		assertEquals(""+valoresAjustados[2], "33.33");

		
		v1 = 33.33;
		v2 = 33.33;
		v3 = 20;
		
		valoresAjustados = GerenciadorVotos.ajustarEstatisticas(v1, v2, v3);
		
		assertEquals(""+valoresAjustados[0], "46.67");
		assertEquals(""+valoresAjustados[1], "33.33");
		assertEquals(""+valoresAjustados[2], "20.0");
		
		v1 = 16.68; 
		v2 = 66.67;
		v3 = 16.67;
		
		valoresAjustados = GerenciadorVotos.ajustarEstatisticas(v1, v2, v3);
		
		//System.out.println(Arrays.toString(valoresAjustados));

		assertEquals(""+valoresAjustados[0], "16.66");
		assertEquals(""+valoresAjustados[1], "66.67");
		assertEquals(""+valoresAjustados[2], "16.67");
		
	}
	
	

}
