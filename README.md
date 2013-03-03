==================================================================================================================
Item: Configuração
==================================================================================================================

Para que a aplicação execute corretamente, é necessário:

1 - instalar o jdk versão 1.6 ou superior (http://www.oracle.com/technetwork/java/javase/downloads/index.html);
2 - instalar o maven3 (http://maven.apache.org/users/index.html);
	OBS: a instalação dos produtos anteriores requer a configuração de variáveis de ambiente
	para que os executáveis sejam encontrados. Os detalhes sobre estas configurações podem ser
	encontrados nos sites dos produtos e não serão detalhados neste documento.

3 - ler o Item:Funcionamento para configurar os caminhos necessários para a apliação funcionar corretamente;


==================================================================================================================




==================================================================================================================
Item: Executar
==================================================================================================================

1 - após instalar os programas necessários (Item: Configuração), é necessário certificar-se de que o computador
    possua conexão com a internet, pois na primeira execução o maven irá realizar o download dos artefatos
    necessários para execução. Também é necessário certificar-se que a porta 8080 não esteja sendo utilizada
    por algum outro processo, pois esta porta será utilizada pelo servidor;

2 - abrir o terminal e posicionar no diretório onde encontra-se o arquivo pom.xml;

3 - rodar o comando mvn clean install (caso ocorra algum problema, será necessário verificar a instalação dos produtos
    e configurações de variáveis de ambiente). Este item pode demorar um pouco na primeira vez, pois o maven executará
    o download de diversas dependências;

4 - executar o comando mvn jetty:run

5 - abrir o browser e navegar na url 
	http://localhost:8080/
    
    Caso a url acima não funcione, 
	navegar para a url http://127.0.0.1:8080/

6 - a tela da aplicação será exibida e será possível realizar os testes no browser;

OBS: por padrão, a aplicação está configurada para aguardar 5 minutos antes de exibir o resultado da pesquisa
para qualquer um que esteja votando. Isso significa que, ao acessar a tela de votação através do menu votar e
realizar um voto válido, o usuário não irá ver os resultados parciais antes de 5 minutos após a inicialização
do servidor. Este parâmetro pode ser alterado conforme descrito em Item:Funcionamento
Apesar o usuário não poder ver o resultado após votar até que 5 minutos passem após o servidor ser inicializado,
existe um link na página principal chamado Resultado Parcial (admin) que permite a visualização dos resultados
parciais a qualquer momento, independente do parâmetro configurado.
Este link acessa a tela de resultados utilizando uma url diferente. A idéia é que, num ambiente de produção,
esta url fosse configurada para exigir algum tipo de usuário e senha para administradores que permitisse
a visualização dos resultados parciais independentemente da configuração. Já a tela de votação irá respeitar 
o parâmetro ao exibir o resultado.

==================================================================================================================


==================================================================================================================
Item: testes unitários
==================================================================================================================

1 - após configurar os passos em Item: Configuração, faça:

2 - caso não tenha executado o comando mvn clean install (Item: Executar), faça isso agora. Caso
    já tenha executado não será necessário executar novamente;

3 - execute o comando mvn test;

4 - os testes unitários serão executados e exibidos no terminal;

==================================================================================================================



==================================================================================================================
Item: Log
==================================================================================================================

Por padrão a aplicação não está configurada para gerar mensagens
de debug, porém é possível alterar a configuração para exibir mensagens
de debug, seguindo os seguintes passos: 

1 - abra o arquivo src/main/resources/log4j.properties;

2 - Modifique a linha:
	log4j.appender.AdminFileAppender.File=/tmp/votacao.log
    para
	log4j.appender.AdminFileAppender.File=<arquivo>

    substituindo <arquivo> por um caminho válido para um arquivo em um diretório onde
    o usuário possua direito de escrita.
	
3 - Modifique o nível do log para DEBUG substituindo a linha

	log4j.logger.br.com.tqi=WARN, AdminFileAppender

   por
	
	log4j.logger.br.com.tqi=DEBUG, AdminFileAppender

   Salve o arquivo com as alterações	

4 - caso a aplicação esteja sendo executada, é necessário parar a mesma (CTRL+C no terminal)
    e executar novamente o comando para inicializar a aplicação (Item: Executar)
==================================================================================================================


==================================================================================================================
Item: Funcionamento
==================================================================================================================

Ao abrir a aplicação (http://localhost:8080) são exibidos dois links:

1 - Votar:
	abrirá a tela para o usuário realizar o voto;
	Após realizar o voto, o usuário verá o resultado parcial caso o tempo configurado para exibição
	do resultado de forma pública já tenha passado. Por padrão, a aplicação está configurada para aguardar 5 minutos antes de 		exibir o resultado da pesquisa para qualquer um que esteja votando. Isso significa que, ao acessar a tela de votação através 		do menu votar e realizar um voto válido, o usuário não irá ver os resultados parciais antes de 5 minutos após a inicialização
	do servidor. Este parâmetro pode ser alterado da seguinte forma:

		1.1 - abrir o arquivo src/main/webapp/WEB-INF/web.xml

		1.2 - Localizar as linhas:

			<context-param>
				<description>Número de minutos a esperar para liberar o resultado parcial da pesquisa</description>
				<param-name>MINUTOS_PARA_LIBERAR_PESQUISA</param-name>
				<param-value>5</param-value>		
			</context-param>

		1.3 - Alterar o número cinco do elemento <param-value> acima para o número de minutos que o usuário terá que
		      esperar para visualizar os votos parciais após realizar um voto válido. Caso este valor não seja um 
		      número válido ou seja um valor menor que zero, a aplicação utilizará um valor padrão de 10 minutos
		      ignorando o valor informado;

		1.4 - salvar o arquivo com as alterações;

		1.5 - Caso a aplicação esteja sendo executada, será necessário interrompê-la (CTRL-C no terminal) 
		      e executá-la novamente (Item: Executar);


2 - Resultado Parcial (admin) 

	Apesar o usuário não poder ver o resultado após votar até que 5 minutos (ou o parâmetro configurado conforme
	o item 1) passem após o servidor ser inicializado, existe um link na página principal chamado Resultado Parcial (admin) que 		permite a visualização dos resultados parciais a qualquer momento, independente do parâmetro configurado.
	Este link acessa a tela de resultados utilizando uma url diferente. A idéia é que, num ambiente de produção,
	esta url fosse configurada para exigir algum tipo de usuário e senha para administradores que permitisse
	a visualização dos resultados parciais independentemente da configuração. Já a tela de votação irá respeitar 
	o parâmetro ao exibir o resultado.


Internamente, a aplicação utiliza uma estrutura de dados que guarda em memória os votos computados e, de 10 em 10 minutos,
é gerado um snapshot da votação que é então gravado em um arquivo xml. Quando o servidor sobe, a aplicação
procura este arquivo e caso encontre, carrega os dados guardados no mesmo. Isso foi implementado para que a pesquisa
não seja perdida totalmente caso o servidor "caia", entretanto como os dados são atualizados de 10 em 10 minutos,
é possível que, em um eventual problema no servidor, os votos de até 10 minutos atrás sejam perdidos, dependendo
de quando a última gravação foi feita. Isto é o "preço" a ser pago pela não utilização de um banco de dados que grave
os votos um a um, devido aos requisitos de performance estabelecidos. 

Por padrão o tempo de escrita deste arquivo xml é 10 minutos, porém é possível alterar este tempo seguindo os passos:

		2.1 - abrir o arquivo src/main/webapp/WEB-INF/applicationContext.xml

		2.1.1 - localizar as linhas:

			  <bean id="jobDetailTrigger" class="org.springframework.scheduling.quartz.CronTriggerBean">
			    <property name="jobDetail" ref="jobDetail" />
			    <property name="cronExpression" value="0 0/10 * * * ?" />
			  </bean>

		2.1.2 - Na linha:
			    <property name="cronExpression" value="0 0/10 * * * ?" />

		substituir o número 10 pela quantidade de minutos que será utilizada ( > 0).


		2.1.3 - Salvar o arquivo com as alterações;

		2.1.4 - Caso a aplicação esteja sendo executada, será necessário interrompê-la (CTRL-C no terminal) 
		        e executá-la novamente (Item: Executar);
		

Pod padrão o arquivo xml está configurado para ser gerado em /tmp/testexmlpesquisa.xml

		2.2 - abrir o arquivo src/main/webapp/WEB-INF/web.xml

		2.2.1 - localizar as linhas:

			  <context-param>
			    <description>Caminho do arquivo que será gerando contendo o snapshot da pesquisa</description>
			    <param-name>CAMINHO_XML_PESQUISA</param-name>
			    <param-value>/tmp/testexmlpesquisa.xml</param-value>
			  </context-param>

		2.2.2 - Alterar o caminho definido no item param-value para um caminho de arquivo válido onde o usuário possua direito 				de escrita;

		2.2.3 - Salvar o arquivo com as alterações;

		2.2.4 - Caso a aplicação esteja sendo executada, será necessário interrompê-la (CTRL-C no terminal) 
		        e executá-la novamente (Item: Executar);


		OBS:Caso ocorra algum problema durante a leitura deste arquivo, a aplicação irá executar normalmente, porém
		como se fosse uma pesquisa nova. Da mesma forma, a aplicação não é interrompida caso ocorra um erro na gravação 		do arquivo xml, por isso é de extrema importância que os caminhos sejam configurados de forma correta (um caminho
		de arquivo em um local onde o usuário tenha direito de acesso e com espaço suficiente para geração 			do arquivo),conforme definido acima.



==================================================================================================================

