# Plano de Aperfeicoamento da Aplicacao (90 dias)

Data: 2026-04-19  
Escopo: `clientes-api` (Spring Boot) + `clientes-app` (Angular)

## 1) Visao Geral
Este plano organiza a evolucao profissional da plataforma de gestao de clientes, prestadores e servicos em 90 dias, com foco em:
- seguranca e confiabilidade do fluxo de autenticacao/cadastro,
- experiencia do usuario (UX) mais clara e consistente,
- qualidade tecnica e automacao de entrega,
- suporte a crescimento do produto com dados e relatorios.

## 2) Metas SMART
1. Reduzir em 80% incidentes de autenticacao/cadastro em ate 60 dias.
2. Elevar sucesso do fluxo de cadastro com confirmacao por e-mail para >= 95% em 90 dias.
3. Cobertura de testes backend em regras criticas de auth/cadastro >= 90% em 60 dias.
4. Reduzir tempo medio de entrega (PR->deploy) em 40% com CI/CD ate 90 dias.
5. Publicar pelo menos 3 dashboards gerenciais (clientes, prestadores, servicos) ate 90 dias.

## 3) Diagnostico Atual (resumo)
Fortes:
- Backend moderno (Spring Boot 3.x / Java 21).
- Fluxo de cadastro por e-mail ja iniciado (iniciar, validar token, concluir, reenviar).
- Frontend com telas de login e etapas de cadastro/confirmacao ja estruturadas.

Gaps:
- Vestigios de fluxo legado no frontend e possivel ambiguidade operacional.
- Validacoes de senha/confirmacao precisam blindagem total no backend.
- Ambiente de testes frontend legado com instabilidade no runner.
- Falta de padronizacao completa de erros, observabilidade e governanca de entrega.

## 4) Roadmap por Fases

### Fase 1: 0-30 dias (estabilizacao e seguranca)
Objetivo: fechar riscos de autenticacao/cadastro e padronizar comportamento.

Entregas:
- Consolidar fluxo unico de cadastro: `/cadastro` -> e-mail -> `/confirmar-cadastro`.
- Bloquear login de usuario com `emailConfirmed=false`.
- Aplicar politica forte de senha no backend (regra centralizada).
- Melhorar UX de confirmacao: checklist de senha, tratamento de erro, cooldown de reenvio.
- Padronizar respostas de erro para auth/registration.

Criterio de conclusao da fase:
- Nenhum endpoint legado sendo usado no fluxo oficial.
- Todos os cenarios criticos do fluxo cobertos por testes backend.

### Fase 2: 31-60 dias (qualidade e produtividade)
Objetivo: reduzir retrabalho e aumentar confiabilidade de entrega.

Entregas:
- Expandir testes de integracao REST (signin, signup-init, validate-token, complete, resend).
- Publicar template profissional de e-mail (HTML responsivo, branding, CTA, fallback URL).
- Implantar logs com correlacao e trilha de auditoria para eventos de auth.
- Organizar backlog tecnico (Sonar: blocker/critical/major) por sprint.

Criterio de conclusao da fase:
- Pipeline com build + testes backend obrigatorios em PR.
- Erros de fluxo rastreaveis por correlation-id.

### Fase 3: 61-90 dias (escala de produto)
Objetivo: elevar valor de negocio com visao gerencial e operacao assistida por dados.

Entregas:
- Dashboard executivo: funil de clientes, produtividade de prestadores, servicos por status.
- Modulo de agenda basica para servicos/prestadores.
- Relatorios operacionais (SLA, concluido/cancelado, tempo medio, receita por periodo).
- Primeira versao de automacoes operacionais (alertas por e-mail para pendencias).

Criterio de conclusao da fase:
- 3 dashboards em uso real.
- Indicadores de operacao com baseline e meta trimestral.

## 5) Backlog Priorizado (Must / Should / Could)

### Must (obrigatorio)
- [ ] Fluxo unico de cadastro por e-mail (sem atalhos legados).
- [ ] Bloqueio de login para usuario nao confirmado.
- [ ] Politica de senha forte validada no backend.
- [ ] Padrao unico de erros para auth/cadastro.
- [ ] Testes backend de cenarios criticos (token invalido/expirado/usado, senha fraca, duplicidade).

### Should (recomendado)
- [ ] Template de e-mail profissional (HTML).
- [ ] Rate limit em endpoints sensiveis (signin, signup-init, resend).
- [ ] Auditoria de eventos de autenticacao.
- [ ] Dashboards gerenciais iniciais.

### Could (evolutivo)
- [ ] Upgrade progressivo do Angular para reduzir risco de stack antiga.
- [ ] 2FA para perfis administrativos.
- [ ] Notificacoes multicanal (e-mail + WhatsApp corporativo, se aplicavel).

## 6) Arquitetura e Seguranca
- Manter regra de negocio critica no backend (nunca depender apenas do frontend).
- Isolar servicos de dominio: autenticacao, registro, notificacao, token, auditoria.
- Hash de token de confirmacao e expiração curta (1-2 horas).
- Invalidar tokens antigos no reenvio.
- Rate limiting e politicas anti abuso para endpoints publicos.
- Segredos e credenciais fora do codigo (env vars/secrets manager).

## 7) UX/UI (profissional e atraente)
- Jornada clara em 3 etapas: solicitacao, confirmacao de e-mail, definicao de senha.
- Feedback imediato de validacoes (senha forte, confirmacao, token expirado).
- Estados visuais padronizados: loading, erro, sucesso, disabled.
- Mensagens institucionais consistentes (tom profissional e objetivo).
- Responsividade e acessibilidade minima (contraste, foco, labels, leitura).

## 8) Dados e Relatorios
Dados recomendados para maturidade:
- Clientes: status, origem, historico de interacoes.
- Prestadores: especialidade, disponibilidade, produtividade.
- Servicos: status, SLA, custo, receita, margem.

Relatorios iniciais:
- Servicos por status e periodo.
- Produtividade por prestador.
- Taxa de conversao de cadastro e confirmacao de e-mail.

## 9) Observabilidade
- Logs estruturados com `correlation-id`.
- Metricas de auth/cadastro:
  - taxa de sucesso no signup-init,
  - taxa de expiracao de token,
  - taxa de reenvio,
  - taxa de bloqueio por nao confirmacao.
- Alertas para aumento de erro 4xx/5xx em endpoints de autenticacao.

## 10) Qualidade e Testes
Backend (prioridade imediata):
- Unitarios: regras de token/senha/confirmacao.
- Integracao: endpoints de auth e registration.
- Regressao: cenarios de erro mais incidentes.

Frontend (quando destravar runner legado):
- Fluxo de formulario (valido/invalido).
- Estados de loading e mensagens.
- Navegacao entre etapas.

## 11) DevOps e Entrega
- CI obrigatoria para PR: build + testes backend + analise estatica.
- Padrao de branch/PR com checklist tecnico.
- Ambientes separados (dev/hml/prod) com configuracoes externas.
- Gate de qualidade minimo para merge (sem falha critica).

## 12) Criterios de Aceite Globais
- Fluxo de cadastro por e-mail funcionando ponta a ponta em ambiente de homologacao.
- Usuario nao confirmado nao autentica.
- Erros de auth/cadastro padronizados e compreensiveis no frontend.
- Testes backend dos cenarios criticos executando no pipeline.
- E-mail de confirmacao com layout profissional aprovado.

## 13) Metricas de Sucesso (KPI)
- Cadastro concluido com sucesso (%).
- Tempo medio para concluir cadastro (min).
- Falhas de login por nao confirmacao (%).
- Reincidencia de bugs criticos por sprint.
- Lead time de entrega (dias).

## 14) Riscos e Mitigacoes
1. Risco: stack frontend de testes antiga gerar ruido de qualidade.  
   Mitigacao: priorizar testes backend + plano de modernizacao gradual do runner.

2. Risco: regressao ao remover legado.  
   Mitigacao: feature toggle e rollout controlado por ambiente.

3. Risco: aumento de suporte no lancamento do novo fluxo.  
   Mitigacao: mensagens claras, FAQ curto e observabilidade de erros.

4. Risco: backlog tecnico competir com features.  
   Mitigacao: reservar capacidade fixa (20-30%) por sprint para qualidade.

## 15) Governanca Recomendada
Ritmo sugerido (leve e eficaz):
- Daily tecnico (15 min).
- Reuniao quinzenal de roadmap (produto + engenharia).
- Revisao mensal executiva de KPIs e riscos.

Papeis:
- Product Owner: priorizacao e criterio de valor.
- Tech Lead: arquitetura, seguranca e padroes.
- Squad: entrega incremental com definicao de pronto clara.

Definicao de pronto (DoD) minima por item:
- Codigo revisado,
- testes aplicaveis executados,
- logs/erros padronizados,
- documentacao de uso/impacto atualizada.

---

## Anexo A - Proximo Ciclo (acao imediata)
1. Fechar senha forte no backend + mensagens padronizadas.
2. Publicar template HTML profissional de confirmacao.
3. Expandir suite JUnit de auth/registration.
4. Revisar endpoint legado e encerrar uso operacional.

