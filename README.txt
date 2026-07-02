======================================================
Projeto Final - APO2: Sistema de Cinema (Web J2EE)
======================================================

NOME DO PROJETO: Sistema de Reservas de Cinema
INTEGRANTES: Guilherme Barroso Costa (GU3054101) e Arthur Ferreira Fernandes (GU3055621)
DATA DE ENTREGA: 30 de Maio de 2026

1. USUÁRIOS E SENHAS INICIAIS
------------------------------------------------------
* Administrador (Acesso ao painel e CRUD de filmes)
  E-mail: admin@cinema.com
  Senha: admin123

* Cliente de Teste (Acesso à compra de ingressos)
  E-mail: teste@cinema.com
  Senha: teste123


2. DEPENDÊNCIAS DO SISTEMA
------------------------------------------------------
- Java JDK 11 ou superior.
- Eclipse IDE for Enterprise Java and Web Developers (ou Eclipse com JST Server Adapters Extensions instalados).
- Apache Tomcat v9.0.
- MySQL Server 8.0+.
- O projeto usa Maven, então as bibliotecas (Gson, Javax Mail, MySQL Connector) são baixadas automaticamente pelo arquivo pom.xml.


3. PASSO A PASSO PARA CONFIGURAÇÃO E EXECUÇÃO
------------------------------------------------------
PASSO A: BANCO DE DADOS
1. Abra seu gerenciador MySQL (ex: MySQL Workbench).
2. Abra e execute o arquivo /database/DDL.sql. Ele irá criar o banco "cinema" e as tabelas (cliente, filme, sala, sessao, reserva).
3. Abra e execute o arquivo /database/DML.sql. Ele irá popular o banco com os usuários iniciais e catálogo de filmes.

PASSO B: CONFIGURAÇÃO DO ECLIPSE & TOMCAT
1. No Eclipse, caso não possua o Tomcat nas opções de Server: Vá em Help -> Install New Software -> Busque por "JST Server" e instale a opção "JST Server Adapters Extensions". Reinicie o Eclipse.
2. Na aba Servers inferior, clique com o botão direito -> New -> Server -> Apache -> Tomcat v9.0 Server e aponte para a pasta de instalação do seu Tomcat baixado.

PASSO C: IMPORTAÇÃO DO PROJETO
1. Vá em File -> Import... -> Maven -> Existing Maven Projects.
2. Aponte para a pasta `/codigofonte/project-main`.
3. Clique em Finish e aguarde o Maven baixar as bibliotecas (status no canto inferior direito).
4. Caso o Eclipse não reconheça como projeto Web, clique com o botão direito no projeto -> Maven -> Update Project... (marque Force Update of Snapshots/Releases).

PASSO D: EXECUÇÃO
1. Na aba Servers, clique com o botão direito no seu servidor Tomcat v9.0.
2. Selecione "Add and Remove...". Mova o projeto "project-main" para a coluna da direita e finalize.
3. Clique com o botão direito no Tomcat e dê "Start".
4. Quando o console mostrar "Server startup in ...", acesse no navegador:
   http://localhost:8080/project-main/index.jsp

======================================================
OBS: Os testes manuais e de interface encontram-se documentados no diretório /documentacao nos arquivos .tex em formato LaTeX.
