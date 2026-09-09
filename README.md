# whatsapp-core-api

Backend de um **bot de atendimento via WhatsApp**, escrito em Kotlin com Spring Boot.
A aplicação recebe os eventos de webhook da [WhatsApp Cloud API](https://developers.facebook.com/docs/whatsapp/cloud-api)
(Meta Graph API), identifica o cliente, mantém o estado da conversa e conduz o
atendimento por um menu interativo.

O domínio usado como exemplo é o de uma empresa de **passagens de ônibus**, com os fluxos:
comprar passagem, atendimento, cancelamento e perguntas frequentes.

> **Status:** projeto de estudo / protótipo (branch de trabalho: `develop`). Os fluxos de
> negócio estão parcialmente implementados e a saída ainda usa `println` em vez de logger.

## Stack

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Kotlin 1.8 (JVM 17) |
| Framework | Spring Boot 3.1 (Web, Data JPA) |
| Cliente HTTP | Spring Cloud OpenFeign (chamadas à Graph API) |
| JSON | Gson / Jackson |
| Banco | PostgreSQL |
| Build | Gradle (Kotlin DSL, wrapper incluído) |

## Como funciona

```
WhatsApp Cloud API (Meta)
        │  webhook (POST /webhooks)
        ▼
WebhookController
        │
        ├─ é callback de status? → ignora
        │
        ├─ contato novo?      → ClientService.save + saudação + envia lista de opções
        │
        └─ contato existente e ativo nas últimas 24h?
                 │  roteia pelo selectedMenuId
                 ▼
        MenuFlow / OrderFlow / CancellationFlow / CommonQuestionsFlow / ServiceFlow
                 │  resposta
                 ▼
        SendingMessageFeign ──► https://graph.facebook.com/v18.0/{phone-number-id}/messages
```

1. **Verificação do webhook** — a Meta faz um `GET /webhooks?hub.challenge=...` no
   cadastro; o endpoint devolve o `challenge` para validar a URL.
2. **Recebimento** — cada mensagem chega em `POST /webhooks` como um `WhatsAppObject`.
   Callbacks de status (entregue/lido) são descartados.
3. **Estado do cliente** — a entidade `Client` (chave = número do WhatsApp) guarda o
   menu selecionado (`selectedMenuId`), a fase do fluxo (`phaseOfSelectedMenu`) e se
   houve contato nas últimas 24 h (janela de atendimento do WhatsApp).
4. **Fluxos** — cada caminho de conversa fica no pacote `flows/`. O `WebhookController`
   apenas roteia; a lógica de cada etapa fica no fluxo correspondente.
5. **Envio** — respostas (texto e listas interativas) são serializadas em JSON e
   enviadas pela Graph API via `SendingMessageFeign`. O `AuthInterceptor` injeta o
   header `Authorization` com o token da API.

### Endpoints

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/webhooks` | Verificação do webhook (responde `hub.challenge`) |
| `POST` | `/webhooks` | Recebe eventos de mensagem da WhatsApp Cloud API |
| `POST` | `/webhooks/send` | Envia uma mensagem avulsa (`DefaultMessage` no corpo) |

## Configuração inicial

### Pré-requisitos

- JDK 17+
- Docker e Docker Compose
- Uma conta no [Meta for Developers](https://developers.facebook.com/) com um app
  WhatsApp configurado (número de teste, *phone number ID* e token de acesso)

### 1. Variáveis de ambiente

Crie um arquivo `.env` na raiz (não versionado):

```dotenv
POSTGRES_DB=whatsapp_core
POSTGRES_USER=whatsapp_core
POSTGRES_PASSWORD=troque-me
whatsapp_api_token=Bearer EAAG...        # token da WhatsApp Cloud API
```

### 2. Banco de dados

```bash
docker compose up -d
```

Sobe um PostgreSQL na porta `5432` usando as credenciais do `.env`.
O schema é recriado a cada inicialização (`spring.jpa.hibernate.ddl-auto=create-drop`).

### 3. Executar a aplicação

```bash
./gradlew bootRun
```

A API sobe em `http://localhost:8080`.

### 4. Expor o webhook

A Meta precisa alcançar `POST /webhooks` por uma URL pública HTTPS. Em
desenvolvimento, use um túnel (ex.: `ngrok http 8080`) e cadastre a URL resultante
no painel do app WhatsApp, junto com o *verify token*.

> **Observação:** o *phone number ID* de destino (`.../v18.0/{id}/messages`) e alguns
> números de telefone estão fixos no código (`SendingMessageFeign`, `WebhookController`).
> Ajuste-os para o seu número antes de testar.

## Estrutura do projeto

```
src/main/kotlin/com/cesarlucasjunior/whatsappcoreapi
├── client/       configuração e interface OpenFeign para a Graph API
├── controller/   endpoints de webhook
├── domain/       entidades JPA (Client, Chat) e modelos das mensagens do WhatsApp
├── flows/        lógica de cada caminho de conversa
└── services/     acesso ao banco e regras de atendimento
```

## Testes

```bash
./gradlew test
```
