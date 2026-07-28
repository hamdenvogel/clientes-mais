# Guia de Implementação - GCP + Google Maps (Clientes e Prestador)

## Objetivo
Implementar geolocalização com Google Maps no front-end Angular para os formulários de **Cliente** e **Prestador**, permitindo:
- Busca e seleção de endereço no mapa.
- Confirmação do endereço pelo usuário.
- Captura de dados mapeados (endereço estruturado, `placeId`, latitude, longitude etc.).
- Persistência desses dados no backend Java e banco de dados.

Este guia cobre todo o fluxo: GCP, segurança, Angular, Java, banco, testes e rollout.

---

## 1) Decisão de arquitetura

### 1.1 Provedor de mapas
Para exibir **Google Maps** no Angular, o provedor correto é **Google Maps Platform (GCP)**.

### 1.2 Onde a AWS entra
A AWS pode continuar como infraestrutura de hospedagem (frontend/backend, secrets, observabilidade), mas **não substitui** a ativação das APIs do Google Maps.

---

## 2) Pré-requisitos
- Conta no Google Cloud com permissão de projeto e billing.
- Domínio(s) do front definidos (produção/homologação).
- Endereço da API backend (dev/hml/prod).
- Estratégia de ambientes:
  - `dev`
  - `hml`
  - `prod`

---

## 3) Setup no Google Cloud (passo a passo)

### 3.1 Criar projeto e habilitar faturamento
1. Console GCP: https://console.cloud.google.com/
2. Criar projeto: https://console.cloud.google.com/projectcreate
3. Billing: https://console.cloud.google.com/billing
4. Guia oficial de início: https://developers.google.com/maps/gmp-get-started

### 3.2 Habilitar APIs necessárias
No menu de APIs (Library): https://console.cloud.google.com/apis/library

Habilitar:
1. Maps JavaScript API  
   https://console.cloud.google.com/apis/library/maps-backend.googleapis.com
2. Places API (New)  
   https://console.cloud.google.com/apis/library/places-backend.googleapis.com
3. Geocoding API  
   https://console.cloud.google.com/apis/library/geocoding-backend.googleapis.com

### 3.3 Criar chaves de API separadas
Credenciais: https://console.cloud.google.com/apis/credentials

Criar **2 chaves**:
1. **Chave Frontend (Angular)**
   - Restrição por HTTP Referrer.
   - Exemplo de referrers:
     - `http://localhost:4200/*`
     - `https://app-hml.seudominio.com/*`
     - `https://app.seudominio.com/*`
   - Restrição de API:
     - Maps JavaScript API
     - Places API

2. **Chave Backend (Java)**
   - Restrição por IP (egresso do servidor) quando possível.
   - Restrição de API:
     - Geocoding API
     - Places API (quando usar Place Details no backend)

Boas práticas de segurança:
- https://developers.google.com/maps/api-security-best-practices

### 3.4 Configurar cotas e orçamento
1. Quotas (exemplo Maps JS):  
   https://console.cloud.google.com/apis/api/maps-backend.googleapis.com/quotas
2. Orçamento/alertas:  
   https://console.cloud.google.com/billing/budgets

Recomendação:
- Começar com limites diários conservadores.
- Definir alertas de custo em 50%, 80% e 100%.

---

## 4) Modelo de dados recomendado

### 4.1 Campos para Cliente e Prestador
Além dos campos já existentes de endereço, adicionar:
- `placeId` (string)
- `enderecoFormatado` (string)
- `latitude` (decimal)
- `longitude` (decimal)
- `numero` (string) - opcional, mas recomendado
- `bairro` (string) - opcional, mas recomendado
- `pais` (string) - opcional
- `origemGeocodificacao` (string, ex.: `GOOGLE_PLACES`)
- `confirmadoEm` (datetime)

### 4.2 Observação importante do cenário atual
- Cliente já possui parte do endereço textual.
- Prestador hoje não possui endereço completo persistido no mesmo padrão do cliente.

Logo, o projeto inclui:
1. Evoluir `Cliente` com coordenadas e metadados de place.
2. Evoluir `Prestador` para também persistir endereço geográfico.

---

## 5) Estratégia funcional (UX)

Fluxo recomendado no formulário:
1. Usuário digita endereço em campo de busca (autocomplete).
2. Seleciona um resultado.
3. Mapa centraliza no local e exibe marcador.
4. Usuário confirma o endereço.
5. Sistema preenche campos estruturados e coordenadas.
6. Ao salvar, payload vai para backend com todos os dados.

Regras:
- Se usuário alterar manualmente endereço após seleção, exigir nova confirmação no mapa.
- Se não houver `placeId`, bloquear salvamento (ou marcar como baixa confiança).

---

## 6) Implementação no Angular

## 6.1 Bibliotecas
Instalar:
```bash
npm i @angular/google-maps @googlemaps/js-api-loader
```

Docs:
- Angular Google Maps: https://angular.dev/ecosystem/google-maps
- Places Autocomplete JS: https://developers.google.com/maps/documentation/javascript/place-autocomplete

## 6.2 Componente reutilizável de endereço
Criar um componente compartilhado, por exemplo:
- `endereco-mapa.component.ts/html/css`

Responsabilidades:
- Renderizar input de busca/autocomplete.
- Renderizar mapa.
- Controlar marcador.
- Emitir evento com endereço confirmado.

Contrato de saída sugerido (`@Output`):
```ts
{
  placeId: string;
  enderecoFormatado: string;
  cep: string;
  logradouro: string;
  numero: string;
  complemento: string;
  bairro: string;
  cidade: string;
  uf: string;
  pais: string;
  latitude: number;
  longitude: number;
}
```

## 6.3 Aplicação nos formulários
- Integrar o componente no form de Cliente.
- Integrar o mesmo componente no form de Prestador.
- Preencher `ngModel` dos campos e incluir hidden fields quando necessário.

## 6.4 Chave por ambiente
Armazenar chave frontend em `environment.ts` / `environment.prod.ts`:
- `googleMapsApiKey`

Nunca usar chave backend no Angular.

---

## 7) Implementação no backend Java

## 7.1 DTOs e entidades
- Adicionar novos campos de geolocalização em DTOs e entidades de Cliente/Prestador.
- Atualizar mapeamentos controller/service.

## 7.2 Validação server-side (recomendado)
Ao receber `placeId`:
1. Consultar Place Details/Geocoding (server-side) usando chave backend.
2. Validar coerência dos dados críticos (`placeId`, lat/lng, cidade/UF, país).
3. Persistir versão normalizada.

Docs:
- Geocoding API: https://developers.google.com/maps/documentation/geocoding/overview
- Places Web Service: https://developers.google.com/maps/documentation/places/web-service/overview

## 7.3 Política de segurança e auditoria
- Registrar origem da geocodificação (`GOOGLE_PLACE_DETAILS`).
- Registrar timestamp de confirmação.
- Logar erros de geocoding sem expor chave API.

---

## 8) Banco de dados e migrações

Criar migration com colunas novas em tabelas de cliente e prestador.

Exemplo conceitual (ajustar nomes/tipos ao padrão do projeto):
```sql
ALTER TABLE meusservicos.cliente
  ADD COLUMN place_id VARCHAR(255),
  ADD COLUMN endereco_formatado VARCHAR(255),
  ADD COLUMN latitude NUMERIC(10,7),
  ADD COLUMN longitude NUMERIC(10,7),
  ADD COLUMN numero VARCHAR(20),
  ADD COLUMN bairro VARCHAR(120),
  ADD COLUMN pais VARCHAR(80),
  ADD COLUMN origem_geocodificacao VARCHAR(40),
  ADD COLUMN confirmado_em TIMESTAMP;

ALTER TABLE meusservicos.prestador
  ADD COLUMN cep VARCHAR(12),
  ADD COLUMN endereco VARCHAR(150),
  ADD COLUMN complemento VARCHAR(80),
  ADD COLUMN uf VARCHAR(2),
  ADD COLUMN cidade VARCHAR(120),
  ADD COLUMN place_id VARCHAR(255),
  ADD COLUMN endereco_formatado VARCHAR(255),
  ADD COLUMN latitude NUMERIC(10,7),
  ADD COLUMN longitude NUMERIC(10,7),
  ADD COLUMN numero VARCHAR(20),
  ADD COLUMN bairro VARCHAR(120),
  ADD COLUMN pais VARCHAR(80),
  ADD COLUMN origem_geocodificacao VARCHAR(40),
  ADD COLUMN confirmado_em TIMESTAMP;
```

---

## 9) Estratégia de rollout (sugerida)

## Fase 1 - Cliente
1. Ativar integração completa no formulário de cliente.
2. Persistir coordenadas e metadados.
3. Validar com usuários internos.

## Fase 2 - Prestador
1. Replicar componente e fluxo.
2. Liberar por feature flag.
3. Monitorar erros de geocoding e taxa de confirmação.

## Fase 3 - Endurecimento
1. Validação server-side obrigatória para `placeId`.
2. Dashboards de uso/custo das APIs.
3. Alertas de anomalia de tráfego.

---

## 10) Testes recomendados

### 10.1 Funcionais
- Seleção de endereço com número.
- Seleção de endereço sem número.
- Ajuste manual de endereço após seleção.
- Edição de cadastro já existente.
- Form de prestador com novo fluxo.

### 10.2 Integração API
- Payload completo com `placeId`, lat/lng.
- Payload sem `placeId` (esperar erro/aviso conforme regra).
- Place inválido/inconsistente.

### 10.3 Segurança
- Chave frontend não funciona fora dos domínios permitidos.
- Chave backend não exposta no browser.

### 10.4 Custos
- Verificar consumo por ambiente (`dev/hml/prod`).
- Disparar alertas de orçamento em teste controlado.

---

## 11) Operação e governança
- Revisar cotas mensalmente.
- Rotacionar chaves periodicamente.
- Auditar referrers e IPs permitidos.
- Monitorar erros de geocoding/place details.

---

## 12) Checklist rápido

## GCP
- [ ] Projeto criado
- [ ] Billing ativo
- [ ] APIs habilitadas (Maps JS, Places, Geocoding)
- [ ] Chave frontend criada e restrita por referrer
- [ ] Chave backend criada e restrita por IP/API
- [ ] Quotas configuradas
- [ ] Budget/alertas configurados

## Angular
- [ ] Dependências instaladas
- [ ] Componente de endereço-mapa criado
- [ ] Integrado em Cliente
- [ ] Integrado em Prestador
- [ ] Chave por ambiente configurada

## Java + Banco
- [ ] DTOs atualizados
- [ ] Entidades atualizadas
- [ ] Services/Controllers atualizados
- [ ] Validação server-side de place implementada
- [ ] Migrações SQL aplicadas

## Qualidade
- [ ] Testes funcionais aprovados
- [ ] Testes de integração aprovados
- [ ] Segurança de chaves validada
- [ ] Custos monitorados

---

## 13) Referências oficiais (links)
- Google Maps Platform - Getting Started:  
  https://developers.google.com/maps/gmp-get-started
- Console GCP:  
  https://console.cloud.google.com/
- APIs Library:  
  https://console.cloud.google.com/apis/library
- Credentials:  
  https://console.cloud.google.com/apis/credentials
- API Security Best Practices:  
  https://developers.google.com/maps/api-security-best-practices
- Maps JavaScript API:  
  https://developers.google.com/maps/documentation/javascript/overview
- Place Autocomplete:  
  https://developers.google.com/maps/documentation/javascript/place-autocomplete
- Geocoding API:  
  https://developers.google.com/maps/documentation/geocoding/overview
- Places API (Web Service):  
  https://developers.google.com/maps/documentation/places/web-service/overview
- Angular Google Maps package docs:  
  https://angular.dev/ecosystem/google-maps

---

## 14) Conclusão
Sim, o caminho ideal para esse requisito é **GCP (Google Maps Platform)** para mapas e geocodificação, com integração no Angular e persistência/validação no Java.

A recomendação prática é começar pelo formulário de **Cliente**, validar o fluxo ponta a ponta, e em seguida replicar no **Prestador** com o mesmo componente reutilizável.
