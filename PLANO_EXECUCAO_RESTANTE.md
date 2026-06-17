# Plano de execucao restante - Grupo G12 InfluxDB

Status em 17/06/2026: etapas executadas e validadas. Este arquivo permanece como roteiro de reproducao para apresentacao ou revisao.

## Objetivo

Validar o projeto funcionando ponta a ponta, gerar evidencias para a apresentacao e fechar os entregaveis do trabalho.

## 1. Preflight local

Executar na raiz do projeto:

```powershell
git status --short
rg --files
```

Conferir:

* `README.md` existe e esta atualizado.
* `database/docker-compose.yml` existe.
* `database/seed-data.lp` existe.
* `database/queries` contem os arquivos `01` a `09`.
* `app/influxdb-app/pom.xml` existe.

Stop condition:

* Se algum arquivo obrigatorio estiver ausente, corrigir antes de rodar a demonstracao.

## 2. Subir o InfluxDB

```powershell
cd database
docker compose up -d
docker ps
```

Conferir:

* Container `g12-influxdb` esta `Up`.
* Porta `8086` esta publicada.
* Interface abre em `http://localhost:8086`.

Evidencia para os slides:

* Print do Docker Desktop ou `docker ps`.
* Print da tela web do InfluxDB aberta.

## 3. Inserir dados iniciais

```powershell
docker cp seed-data.lp g12-influxdb:/tmp/seed-data.lp
docker exec g12-influxdb sh -c "tr -d '\r' < /tmp/seed-data.lp > /tmp/seed-data-clean.lp"
docker exec g12-influxdb influx write --bucket monitoramento --org grupo12 --token g12-super-token-influxdb-2026 --precision s --file /tmp/seed-data-clean.lp
```

Conferir:

* Bucket `monitoramento` contem dados.
* Measurements `sensor_reading` e `sensor_info` aparecem no Data Explorer.

Evidencia para os slides:

* Print do Data Explorer com dados retornados.

## 4. Executar consultas Flux

No Data Explorer ou via CLI, executar:

* `database/queries/01-find.flux`
* `database/queries/05-group.flux`
* `database/queries/06-lookup-join.flux`
* `database/queries/08-consulta-complexa-alertas.flux`
* `database/queries/09-consulta-complexa-salas-em-risco.flux`

Conferir:

* `find` retorna leituras.
* `group` calcula media por sala.
* `join` combina `sensor_reading` com `sensor_info`.
* Consultas complexas retornam classificacao de alerta e salas em risco.

Evidencia para os slides:

* Print de pelo menos duas consultas complexas.

## 5. Rodar a aplicacao Spring Boot

Em outro terminal:

```powershell
cd app/influxdb-app
.\mvnw.cmd spring-boot:run
```

Conferir:

* Aplicacao sobe na porta `8080`.
* Nao ha erro de conexao com InfluxDB.

Evidencia para os slides:

* Print do terminal com Spring Boot iniciado.

## 6. Testar CRUD

Usar Postman, Insomnia ou `curl`.

Create:

```http
POST http://localhost:8080/api/readings
```

Body:

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

Read:

```http
GET http://localhost:8080/api/readings
GET http://localhost:8080/api/readings/S99
```

Update:

```http
PUT http://localhost:8080/api/readings/S99
```

Delete:

```http
DELETE http://localhost:8080/api/readings/S99
```

Conferir:

* `POST` retorna sucesso.
* `GET /S99` confirma criacao.
* `PUT` retorna sucesso e altera os dados.
* `DELETE` retorna sucesso.
* `GET /S99` apos delete nao retorna a leitura.

Evidencia para os slides:

* Print do Postman ou navegador com pelo menos um exemplo de cada operacao CRUD.

## 7. Rodar verificacao minima

Com o InfluxDB ativo:

```powershell
cd app/influxdb-app
.\mvnw.cmd test
```

Conferir:

* Build passa.
* Teste de contexto do Spring passa.

Stop condition:

* Se o teste falhar por conexao com InfluxDB, confirmar se o container esta ativo.

## 8. Revisar slides

Arquivo atual:

```text
C:\Users\buddhadaiki\Downloads\apresentacao-programa-g12-influxdb.pptx
```

Checklist:

* Remover ou corrigir qualquer slide que declare "testado" sem print real correspondente.
* Garantir que prints reais substituam placeholders de evidencia.
* Conferir slide com comando `tr -d '\r'`, pois o texto pode aparecer quebrado no PowerPoint.
* Corrigir aspas do trecho de `deleteBySensorId` se o codigo renderizado perdeu escape de aspas.
* Incluir a fundamentacao teorica resumida: CAP, vantagens, limitacoes e ecossistema.
* Confirmar que todos os integrantes aparecem e tem fala definida.

## 9. Fechar entrega

Antes de enviar:

```powershell
git status --short
```

Enviar:

* Codigo da aplicacao.
* Pasta `database` com Docker Compose, seed e consultas.
* `README.md`.
* Slides em PDF ou PPTX.

Nao enviar:

* `database/influxdb-data/`
* `database/influxdb-config/`
* `app/influxdb-app/target/`
* arquivos temporarios de IDE.
