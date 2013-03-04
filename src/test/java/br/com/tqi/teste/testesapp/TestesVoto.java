package br.com.tqi.teste.testesapp;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.tqi.teste.model.Voto;
import br.com.tqi.teste.model.GerenciadorVotos.EnumOpcoesVoto;

public class TestesVoto {

	@Test
	public void testeCriacaoVotosValidos() {
		Voto voto = new Voto();
		
		voto.setVoto(EnumOpcoesVoto.Amijubi.ordinal());
		assertEquals(voto.getVoto(), EnumOpcoesVoto.Amijubi.ordinal());
		
		voto.setVoto(EnumOpcoesVoto.Fuleco.ordinal());
		assertEquals(voto.getVoto(), EnumOpcoesVoto.Fuleco.ordinal());

		voto.setVoto(EnumOpcoesVoto.Zuzeco.ordinal());
		assertEquals(voto.getVoto(), EnumOpcoesVoto.Zuzeco.ordinal());
		
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testeVotoInvalidoMenorQueZero() {
		Voto voto = new Voto();
		voto.setVoto(-1);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testeVotoInvalidoMaiorQueOpcoesDisponiveis() {
		Voto voto = new Voto();
		voto.setVoto(EnumOpcoesVoto.values().length + 1);
	}
	
	
}
