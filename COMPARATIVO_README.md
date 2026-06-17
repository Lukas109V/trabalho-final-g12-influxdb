# Comparativo dos READMEs

Este documento compara o README original da branch `main` com o README atual da branch `parte-teorica-e-testes`.

## Resumo

O README original ja apresentava a estrutura basica do projeto, instrucoes de execucao, endpoints e lista de consultas Flux. O README atual manteve essas informacoes, mas reorganizou o conteudo e adicionou a fundamentacao teorica exigida no trabalho.

## Comparacao por topico

| Topico | README original | README atual |
| --- | --- | --- |
| Identificacao do grupo | Lista os integrantes em texto simples. | Lista os integrantes em formato de lista. |
| Banco escolhido | Informa apenas `InfluxDB`. | Informa `InfluxDB` e classifica como Time Series Database. |
| Tipo de banco | Cita NoSQL do tipo Time Series. | Explica o modelo de series temporais e os conceitos usados no projeto. |
| Tema | Descreve monitoramento ambiental de salas e laboratorios. | Mantem o tema e detalha os dados registrados: temperatura, umidade, CO2, bateria e status. |
| Fundamentacao teorica | Nao possui secao especifica. | Adiciona conceitos de measurement, timestamp, tags, fields, bucket e organization. |
| Teorema CAP | Nao aborda. | Adiciona classificacao CAP para a demonstracao single-node local. |
| Vantagens e limitacoes | Nao aborda de forma separada. | Lista vantagens, limitacoes e adequacao do InfluxDB. |
| Tecnologias | Lista ferramentas principais. | Mantem a lista e especifica InfluxDB 2.7 e Maven Wrapper. |
| Estrutura do projeto | Mostra arvore basica de pastas. | Mantem a estrutura principal. |
| Entregaveis | Nao possui secao dedicada. | Adiciona codigo, scripts do banco e slides finais. |
| Execucao | Divide banco, seed e aplicacao em secoes separadas. | Consolida em uma secao unica de execucao. |
| Endpoints | Documenta cada endpoint em subtitulos separados. | Resume os endpoints em tabela. |
| Consultas | Lista arquivos e finalidade. | Relaciona cada requisito do enunciado com o equivalente em Flux. |
| Arrays e subdocumentos | Nao detalha. | Explica as equivalencias usadas no InfluxDB. |
| Decisoes tecnicas | Nao possui secao dedicada. | Explica Docker Compose, API REST, driver Java, update e arquivos Flux. |
| Roteiro de demonstracao | Nao possui. | Adiciona roteiro para apresentacao. |
| Referencias | Nao possui. | Adiciona links oficiais da documentacao do InfluxDB. |

## Principais ganhos do README atual

* Atende melhor aos criterios de fundamentacao teorica.
* Explica as equivalencias entre operadores do enunciado e recursos do Flux.
* Deixa mais claro como executar banco, seed e aplicacao.
* Facilita a apresentacao com roteiro objetivo.
* Remove repeticoes dos endpoints ao usar tabela.

## Pontos mantidos do README original

* Integrantes.
* Tema do sistema.
* Estrutura de pastas.
* Credenciais locais do InfluxDB.
* Comandos de importacao do seed.
* Endpoints da API.
* Lista de consultas Flux.

## Conclusao

O README atual e uma evolucao do README original. Ele preserva as instrucoes praticas ja existentes e acrescenta os pontos teoricos e organizacionais exigidos para a entrega do trabalho.
