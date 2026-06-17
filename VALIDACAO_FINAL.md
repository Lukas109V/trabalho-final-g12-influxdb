# Validacao final - Grupo G12 InfluxDB

Data: 17/06/2026

## Resumo

Validacao ponta a ponta executada. A estrutura do projeto, README, plano de execucao, Docker/InfluxDB, importacao de seed, consultas Flux, teste Maven e CRUD real da API foram verificados.

## Backups

Antes das novas acoes, foram criados backups em:

```text
backups/20260617-095543/
```

Arquivos preservados:

* `README.md`
* `PLANO_EXECUCAO_RESTANTE.md`
* `.gitignore`
* `apresentacao-programa-g12-influxdb.original.pptx`

## Verificacoes concluidas

### Protecao dos backups

O arquivo `.gitignore` foi atualizado para ignorar `backups/`, evitando que as copias locais entrem no Git por acidente.

### Estrutura de arquivos

Arquivos obrigatorios encontrados:

* `README.md`
* `database/docker-compose.yml`
* `database/seed-data.lp`
* `database/queries/01-find.flux`
* `database/queries/02-match.flux`
* `database/queries/03-project.flux`
* `database/queries/04-aggregate.flux`
* `database/queries/05-group.flux`
* `database/queries/06-lookup-join.flux`
* `database/queries/07-unwind-equivalente.flux`
* `database/queries/08-consulta-complexa-alertas.flux`
* `database/queries/09-consulta-complexa-salas-em-risco.flux`
* `app/influxdb-app/pom.xml`
* `app/influxdb-app/mvnw.cmd`

### Ferramentas locais

Docker CLI encontrado:

```text
Docker version 29.5.3
Docker Engine 29.5.3
```

Maven Wrapper encontrado:

```text
Apache Maven 3.9.16
Java version: 25.0.3
```

### Testes Maven

Comando executado:

```powershell
cd app/influxdb-app
.\mvnw.cmd test
```

Resultado:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Observacao: a primeira execucao falhou porque o Maven nao podia gravar no cache `~/.m2` dentro do sandbox. A execucao com permissao escalonada passou.

## Docker e InfluxDB

Comando executado:

```powershell
cd database
docker compose up -d
docker ps --filter "name=g12-influxdb"
```

Resultado:

```text
g12-influxdb | Up | 0.0.0.0:8086->8086/tcp
```

Ping interno:

```powershell
docker exec g12-influxdb influx ping
```

Resultado:

```text
OK
```

## Importacao do seed

Comandos executados:

```powershell
cd database
docker cp seed-data.lp g12-influxdb:/tmp/seed-data.lp
docker exec g12-influxdb sh -c "tr -d '\r' < /tmp/seed-data.lp > /tmp/seed-data-clean.lp"
docker exec g12-influxdb influx write --bucket monitoramento --org grupo12 --token g12-super-token-influxdb-2026 --precision s --file /tmp/seed-data-clean.lp
```

Resultado:

```text
Sem erro de importacao.
```

## Consultas Flux validadas

### `01-find.flux`

Resultado confirmado:

```text
Retornou linhas da measurement sensor_reading com fields battery, co2, humidity etc.
```

### `05-group.flux`

Resultado confirmado:

```text
LAB_A    26.333333333333332
LAB_B    24.966666666666665
SALA_101 22.633333333333336
SALA_102 29.099999999999998
```

### `06-lookup-join.flux`

Resultado confirmado:

```text
Retornou sensorId, _time, model, room, temperature e threshold_temp.
```

### `08-consulta-complexa-alertas.flux`

```text
Retornou alert_level NORMAL, ATENCAO e CRITICO conforme temperatura/CO2.
```

### `09-consulta-complexa-salas-em-risco.flux`

Resultado confirmado:

```text
LAB_A S01 ATENCAO
LAB_B S02 ATENCAO
SALA_102 S04 CRITICO
```

## API Spring Boot e CRUD

A aplicacao foi iniciada com:

```powershell
cd app/influxdb-app
.\mvnw.cmd spring-boot:run
```

Log confirmado:

```text
Tomcat started on port 8080
Started InfluxdbAppApplication
```

CRUD testado com sensor temporario `S99`:

```http
POST   http://localhost:8080/api/readings
GET    http://localhost:8080/api/readings/S99
PUT    http://localhost:8080/api/readings/S99
DELETE http://localhost:8080/api/readings/S99
```

Resultado real usando `S99`:

```json
{
  "create": "Leitura inserida com sucesso.",
  "readAfterCreateCount": 4,
  "update": "Leitura atualizada com sucesso.",
  "readAfterUpdateCount": 4,
  "updatedRooms": "LAB_VALIDACAO_ATUALIZADO",
  "delete": "Leituras do sensor S99 removidas com sucesso.",
  "readAfterDeleteCount": 0
}
```

A aplicacao Spring Boot iniciada em background foi encerrada apos os testes. O container `g12-influxdb` foi mantido ativo para facilitar prints e demonstracao.

## PowerPoint analisado e finalizado

Arquivo analisado:

```text
C:\Users\buddhadaiki\Downloads\apresentacao-programa-g12-influxdb.pptx
```

Resumo:

* 36 slides.
* 7 arquivos de midia.
* Prints encontrados nos slides 3, 4, 9, 11, 16 e 28.
* Ha evidencias visuais de InfluxDB/Docker, Data Explorer, resultado de group e Spring Boot iniciado.

Arquivo final gerado:

```text
outputs/apresentacao-programa-g12-influxdb-final.pptx
```

Atualizacao feita no deck final:

* Mantidos os 36 slides originais.
* Adicionados 4 slides finais com evidencias do programa funcionando.
* Corrigido visualmente o slide 31 para usar o repositorio real: `https://github.com/Lukas109V/trabalho-final-g12-influxdb`.
* Slide 37: Docker, InfluxDB, Maven e API Spring Boot validados.
* Slide 38: CRUD completo via API REST.
* Slide 39: consultas obrigatorias em Flux, incluindo `$group` e `$lookup` equivalente.
* Slide 40: consultas complexas 08 e 09 com classificacao de risco.

Validacao do deck final:

```text
40 slides renderizados/importaveis pelo runtime de apresentacoes.
Slides 37 a 40 revisados visualmente em PNG.
```

Observacao: a apresentacao original em Downloads nao foi alterada diretamente; a versao de entrega esta em `outputs/`.

## Status final

* Documentacao: pronta.
* Plano de execucao: pronto.
* Codigo: compila e passa no teste Maven disponivel.
* Docker/InfluxDB: validado.
* Importacao de seed: validada.
* Consultas Flux principais: validadas.
* CRUD real contra InfluxDB: validado.
* Slides: finalizados em `outputs/apresentacao-programa-g12-influxdb-final.pptx`.
