# Controle Ponto Web Full

Sistema web que calcula/armazena/exporta atraso e hora extra embasados em um horário de trabalho e marcações feitas.

![print-sistema](https://user-images.githubusercontent.com/12899288/176553982-7720c9cf-dd3f-4afb-a759-df65e7a886d3.png)


Backend:
Java 8 (com servlet) + Tomcat 8.5 + Postgres 42.3.6

Frontend:
Javascript + bootstrap + jquery

(download tomcat)
**Na hora de configurar um novo servidor tomcat, verifique a porta admin, essa versão pode vir com um erro onde não estará atribuido a nenhuma porta,(eu utilizo a porta padrão do admin 8005) atualize, salve e execute.
https://tomcat.apache.org/download-80.cgi

O sistema utiliza Maven para controle de dependências, existem alguns .JARS necessários pois o sistema permite gerar relatórios utilizando a ferramenta JasperReport, este necessita de algumas libs para funcionar:
(link comunidade)
https://community.jaspersoft.com/

