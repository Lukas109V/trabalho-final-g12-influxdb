# Trabalho Final NoSQL — Grupo G12 — InfluxDB

## Integrantes
Lucas Gabriel Gomes Martins
Gabriel de Carvalho Souza

## Banco escolhido

InfluxDB

## Tipo de banco

Banco de dados NoSQL do tipo Time Series.

## Tema do projeto

Sistema de monitoramento ambiental de salas e laboratórios usando sensores.

## Objetivo

O objetivo do projeto é registrar, consultar, atualizar e remover leituras de sensores ambientais. Os sensores armazenam informações como temperatura, umidade, CO2, bateria e status da leitura.

## Tecnologias utilizadas

* InfluxDB
* Docker
* Docker Compose
* Java 21
* Spring Boot
* Maven
* Postman
* Flux Query Language

## Estrutura do projeto

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

## Como executar o InfluxDB

Entre na pasta do banco:

```bash
cd database
```

Suba o container:

```bash
docker compose up -d
```

Acesse o InfluxDB no navegador:

```text
http://localhost:8086
```

Credenciais:

```text
Usuário: admin
Senha: Admin@12345
Organização: grupo12
Bucket: monitoramento
Token: g12-super-token-influxdb-2026
```

## Como inserir os dados iniciais

Copiar o arquivo para dentro do container:

```bash
docker cp seed-data.lp g12-influxdb:/tmp/seed-data.lp
```

Remover caracteres de quebra de linha do Windows:

```bash
docker exec g12-influxdb sh -c "tr -d '\r' < /tmp/seed-data.lp > /tmp/seed-data-clean.lp"
```

Inserir os dados no bucket:

```bash
docker exec g12-influxdb influx write --bucket monitoramento --org grupo12 --token g12-super-token-influxdb-2026 --precision s --file /tmp/seed-data-clean.lp
```

## Como executar a aplicação

Entre na pasta da aplicação:

```bash
cd app/influxdb-app
```

Execute usando Maven Wrapper:

```bash
.\mvnw.cmd spring-boot:run
```

A aplicação roda na porta:

```text
http://localhost:8080
```

## Endpoints da API

### Criar leitura

```http
POST /api/readings
```

Exemplo de body:

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

### Listar todas as leituras

```http
GET /api/readings
```

### Buscar leituras por sensor

```http
GET /api/readings/S01
```

### Atualizar leituras de um sensor

```http
PUT /api/readings/S99
```

Exemplo de body:

```json
{
  "sensorId": "S99",
  "room": "LAB_TESTE_ATUALIZADO",
  "type": "environment",
  "status": "ALERT",
  "temperature": 31.5,
  "humidity": 55.0,
  "co2": 1300.0,
  "battery": 75
}
```

### Remover leituras de um sensor

```http
DELETE /api/readings/S99
```

## CRUD implementado

* Create: inserir uma nova leitura de sensor.
* Read: consultar leituras registradas.
* Update: atualizar leituras de um sensor.
* Delete: remover leituras de um sensor.

## Consultas implementadas

| Arquivo                                  | Finalidade                                        |
| ---------------------------------------- | ------------------------------------------------- |
| 01-find.flux                             | Consulta equivalente ao find                      |
| 02-match.flux                            | Consulta equivalente ao $match                    |
| 03-project.flux                          | Consulta equivalente ao $project                  |
| 04-aggregate.flux                        | Consulta de agregação com média                   |
| 05-group.flux                            | Consulta equivalente ao $group                    |
| 06-lookup-join.flux                      | Consulta equivalente ao $lookup                   |
| 07-unwind-equivalente.flux               | Consulta equivalente ao $unwind                   |
| 08-consulta-complexa-alertas.flux        | Consulta complexa de classificação de alertas     |
| 09-consulta-complexa-salas-em-risco.flux | Consulta complexa mostrando apenas salas em risco |

## Observação sobre o InfluxDB

Como o InfluxDB é um banco de séries temporais, ele não trabalha exatamente da mesma forma que o MongoDB. Por isso, algumas operações foram feitas usando equivalentes em Flux, como `filter()`, `keep()`, `group()`, `mean()`, `join()`, `pivot()` e `map()`.
