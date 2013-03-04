package br.com.tqi.teste.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import br.com.tqi.teste.model.Estatistica;
import br.com.tqi.teste.model.GerenciadorVotos;
import br.com.tqi.teste.util.LogUtil;

/**
 * Esta classe tenta carregar o arquivo de configuração de uma pesquisa realizada anteriormente.
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 */
public class CarregaArquivoListener implements ServletContextListener {

	public static final String CHAVE_CAMINHO_XML_PESQUISA = "CAMINHO_XML_PESQUISA";
	
    public CarregaArquivoListener() {
    }

    public void contextInitialized(ServletContextEvent evt) {
    	LogUtil.debug(getClass(), "*****EXECUTANDO LEITURA DO ARQUIVO XML*****");
		String caminhoArquivoXML = evt.getServletContext().getInitParameter(CHAVE_CAMINHO_XML_PESQUISA);
    	if(caminhoArquivoXML != null) {
			 try {
				LogUtil.debug(getClass(), "Tentando ler arquivo xml de uma pesqusia anterior..." + caminhoArquivoXML);
				Estatistica estatistica = GerenciadorVotos.lerXml(caminhoArquivoXML);
				GerenciadorVotos.resetComEstatistica(estatistica); 
				LogUtil.debug(getClass(), "Gerenciador carregado com sucesso com as estatísticas --> " + estatistica);
			} catch (Exception e) {
	      	 	LogUtil.error(getClass(), "Erro ao ler arquivo xml de configuração de uma pesquisa anterior", e);
			}
    	}
		LogUtil.debug(getClass(), "*****FIM LEITURA XML*****");

    }

    public void contextDestroyed(ServletContextEvent evt) {
    }
	
}
