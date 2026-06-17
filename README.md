# Trabalho Final NoSQL - Grupo G12 - InfluxDB

## Integrantes

* Lucas Gabriel Gomes Martins
* Gabriel de Carvalho Souza

## Banco escolhido

InfluxDB, banco NoSQL do tipo Time Series Database (TSDB).

## Tema

Sistema de monitoramento ambiental de salas e laboratorios com sensores. A aplicacao registra temperatura, umidade, CO2, bateria e status das leituras ao longo do tempo.

## Fundamentacao teorica

O InfluxDB e um banco NoSQL especializado em series temporais. Ele e indicado para dados com timestamp, alto volume de insercao e consultas por janelas de tempo, como IoT, metricas de aplicacoes, telemetria, monitoramento de infraestrutura e alertas.

Modelo de dados usado:

* `measurement`: agrupamento logico dos dados. O projeto usa `sensor_reading` e `sensor_info`.
* `timestamp`: instante da leitura.
* `tags`: metadados indexados para filtros e agrupamentos, como `sensorId`, `room`, `type`, `status` e `model`.
* `fields`: valores medidos, como `temperature`, `humidity`, `co2`, `battery`, `threshold_temp` e `threshold_co2`.
* `bucket`: repositorio dos dados. O projeto usa `monitoramento`.
* `organization`: espaco administrativo. O projeto usa `grupo12`.

Classificacao CAP no projeto:

* Consistencia: priorizada dentro da instancia unica local.
* Disponibilidade: existe enquanto o container do InfluxDB estiver ativo.
* Tolerancia a particao: nao e foco da demonstracao, pois o projeto usa InfluxDB OSS 2.7 em container unico, sem cluster.

Na pratica, a demonstracao local se comporta como uma implantacao CA single-node. Em cenarios distribuidos ou cloud, a classificacao depende da topologia e configuracao usadas.

Vantagens:

* Modelo natural para dados temporais.
* Boa performance para escrita continua de medicoes.
* Consultas por intervalo de tempo.
* Tags indexadas para filtros frequentes.
* Flux permite filtros, agregacoes, `join`, `pivot` e transformacoes.
* Interface web, CLI, API HTTP, drivers oficiais, Grafana e Telegraf.

Limitacoes:

* Nao e ideal para dados altamente relacionais.
* Atualizacao nao e o fluxo principal; no projeto, o update remove as leituras do sensor e insere uma nova.
* Fields nao sao indexados.
* Arrays e subdocumentos nao sao estruturas nativas como em MongoDB; foram representados por equivalencias de modelagem e consulta.

## Tecnologias

* InfluxDB 2.7
* Docker e Docker Compose
* Java 21
* Spring Boot
* Maven Wrapper
* Flux Query Language
* Postman ou navegador para testar a API

## Estrutura

```text
trabalho-final-g12-influxdb
├── README.md
├── database
│   ├── docker-compose.yml
│   ├── seed-data.lp
│   └── queries
│       ├── 01-find.flux
│       ├── 02-match.flux
│       ├── 03-project.flux
│       ├── 04-aggregate.flux
│       ├── 05-group.flux
│       ├── 06-lookup-join.flux
│       ├── 07-unwind-equivalente.flux
│       ├── 08-consulta-complexa-alertas.flux
│       └── 09-consulta-complexa-salas-em-risco.flux
└── app
    └── influxdb-app
```

## Entregaveis

* Codigo da aplicacao: `app/influxdb-app`
* Scripts do banco: `database/docker-compose.yml`, `database/seed-data.lp` e `database/queries`
* Slides finais: `outputs/apresentacao-programa-g12-influxdb-final.pptx`

## Como executar

Subir o InfluxDB:

```bash
cd database
docker compose up -d
```

Acessar a interface web:

```text
http://localhost:8086
```

Credenciais:

```text
Usuario: admin
Senha: Admin@12345
Organizacao: grupo12
Bucket: monitoramento
Token: g12-super-token-influxdb-2026
```

Importar dados iniciais:

```bash
docker cp seed-data.lp g12-influxdb:/tmp/seed-data.lp
docker exec g12-influxdb sh -c "tr -d '\r' < /tmp/seed-data.lp > /tmp/seed-data-clean.lp"
docker exec g12-influxdb influx write --bucket monitoramento --org grupo12 --token g12-super-token-influxdb-2026 --precision s --file /tmp/seed-data-clean.lp
```

Executar a aplicacao:

```bash
cd app/influxdb-app
.\mvnw.cmd spring-boot:run
```

A API fica disponivel em:

```text
http://localhost:8080
```

## Endpoints

| Operacao | Metodo | Endpoint |
| --- | --- | --- |
| Criar leitura | `POST` | `/api/readings` |
| Listar leituras | `GET` | `/api/readings` |
| Buscar por sensor | `GET` | `/api/readings/{sensorId}` |
| Atualizar sensor | `PUT` | `/api/readings/{sensorId}` |
| Remover sensor | `DELETE` | `/api/readings/{sensorId}` |

Body para `POST` ou `PUT`:

```json
{
  "sensorId": "S99",
  "room": "LAB_TESTE",
  "type": "environment",
  "status": "OK",
  "temperature": 25.5,
  "humidity": 60.0,
  "co2": 700.0,
  "battery": 90
}
```

## Consultas obrigatorias

Como o enunciado cita operadores comuns em MongoDB, as consultas foram implementadas com equivalentes em Flux:

| Requisito | Equivalente no InfluxDB/Flux | Arquivo |
| --- | --- | --- |
| `find` | `from()`, `range()`, `filter()` | `01-find.flux` |
| `$match` | `filter()` | `02-match.flux` |
| `$project` | `keep()` | `03-project.flux` |
| `aggregate` | `mean()` | `04-aggregate.flux` |
| `$group` | `group(columns: ["room"])` + `mean()` | `05-group.flux` |
| `$lookup` | `join()` entre `sensor_reading` e `sensor_info` | `06-lookup-join.flux` |
| `$unwind` | formato longo `_field`/`_value` | `07-unwind-equivalente.flux` |
| Arrays | conjunto de fields manipulado por `_field`/`_value` | `07-unwind-equivalente.flux` |
| Subdocumentos | ponto logico com tags e fields; JSON de entrada na API | `ReadingRequest.java`, `seed-data.lp` |
| Consulta complexa 1 | `pivot()` + `map()` para classificar alertas | `08-consulta-complexa-alertas.flux` |
| Consulta complexa 2 | `pivot()`, `map()`, `filter()`, `keep()`, `sort()` | `09-consulta-complexa-salas-em-risco.flux` |

## Decisoes tecnicas

* Docker Compose foi usado para tornar o banco reproduzivel.
* Spring Boot expoe uma API REST simples para demonstrar CRUD.
* O driver `influxdb-client-java` conecta a aplicacao ao InfluxDB.
* O update usa delete + insert porque series temporais sao orientadas a insercoes historicas.
* As consultas Flux ficam em arquivos separados para facilitar a apresentacao.

## Roteiro de demonstracao

1. Mostrar o container `g12-influxdb` em execucao.
2. Abrir `http://localhost:8086` e mostrar a organizacao `grupo12` e o bucket `monitoramento`.
3. Importar `seed-data.lp`.
4. Executar a API Spring Boot.
5. Testar `POST`, `GET`, `PUT` e `DELETE`.
6. Executar pelo menos duas consultas complexas: `08-consulta-complexa-alertas.flux` e `09-consulta-complexa-salas-em-risco.flux`.
7. Explicar measurements, tags, fields, bucket e timestamp.
8. Explicar as equivalencias usadas para os operadores de MongoDB.

## Referencias

* https://docs.influxdata.com/influxdb/v2/reference/key-concepts/data-elements/
* https://docs.influxdata.com/influxdb/v2/query-data/flux/
* https://docs.influxdata.com/influxdb/v2/write-data/developer-tools/api/
