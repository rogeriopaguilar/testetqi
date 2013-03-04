package br.com.tqi.teste.model.schedule;


import java.io.FileNotFoundException;

import javax.servlet.ServletContext;

import org.quartz.JobExecutionContext;
import org.quartz.StatefulJob;
import org.springframework.scheduling.quartz.QuartzJobBean;

import br.com.tqi.teste.model.GerenciadorVotos;
import br.com.tqi.teste.util.LogUtil;
import br.com.tqi.teste.web.listener.CarregaArquivoListener;

/**
 * Classe que atualiza o arquivo xml contendo um "snapshot" das estatísticas computadas		<br />
 * até o momento quando o método é executado.												<br />
 * O padrão é executar a cada 10 minutos, porém é possível alterar a configuração			<br />
 * no arquivo de configuração da app web.													<br />
 * Quando a aplicação sobe, ela verifica a existência desse arquivo, e carrega				<br />
 * os dados do último "snapshot" gerado caso  oarquvio exista.								<br />
 * Este é um mecanismo para poder não perder todos os votos quando o servidor cai por		<br />
 * algum motivo. Ainda assim, os votos podem ser perdidos ente 1 minuto e o parâmetro		<br />	
 * configurado no arquivo. Fiz assim ao invés de utilizar uma base de dados					<br />
 * para que o banco não fosse o gargalo da aplicação, embora exista o risco					<br />
 * de perder parte da votação.
 * 
 * @author Rogério de Paula Aguilar
 * @since 0.1
 */

public class AtualizarXMLVotos extends QuartzJobBean implements StatefulJob {
	
	/**
	 * Grava o arquivo de tempos em tempos contendo o "snapshot" da votação
	 */
	protected void executeInternal(JobExecutionContext ctx) {
		LogUtil.debug(getClass(), "*****EXECUTANDO ESCRITA DO ARQUIVO XML*****");
		ServletContext servletContext=(ServletContext)ctx.getJobDetail().getJobDataMap().get("servletContext");		
		String caminhoArquivoXML = servletContext.getInitParameter(CarregaArquivoListener.CHAVE_CAMINHO_XML_PESQUISA);
		LogUtil.debug(getClass(), "Caminho do arquivo xml --> " + caminhoArquivoXML);
		 try {
			GerenciadorVotos.gerarXml(caminhoArquivoXML);
			LogUtil.debug(getClass(), "Arquivo xml gerado em " + caminhoArquivoXML);
		} catch (FileNotFoundException e) {
       	 	LogUtil.error(getClass(), "Erro ao gerar arquivo", e);
			//throw new JobExecutionException( "Erro ao gerar arquivo xml", e);
		}
		 
		LogUtil.debug(getClass(), "*****FIM ESCRITA XML*****");

	}

}