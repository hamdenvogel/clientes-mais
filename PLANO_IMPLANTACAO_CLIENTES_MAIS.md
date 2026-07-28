# Plano de Implantacao - Rebranding e Padronizacao de Layout

Data: 2026-04-19  
Escopo: `clientes-app` (Angular) com analise de impacto no ecossistema do projeto

## 1) Objetivo
Este documento consolida a estrategia para:

1. Trocar a marca visual de `Clientes` para `Clientes Mais+`.
2. Adicionar logo profissional ao login e dashboard/home.
3. Manter os nomes tecnicos atuais (`clientes`, packages e estrutura) neste ciclo para evitar retrabalho.
4. Padronizar os CRUDs para o mesmo layout/CSS visual da home.

## 2) Resultado esperado
Ao final da implantacao:
- login e home exibem `Clientes Mais+` com identidade visual consistente;
- menus/topo/dashboard seguem o mesmo branding;
- CRUDs (`lista`, `form`, etc.) usam o mesmo padrao visual da area logada;
- nao ha rename tecnico neste ciclo; mudanca restrita ao visual do frontend.

## 3) Escopo funcional

### 3.1 Rebranding (UI)
- Atualizar textos visiveis de `Clientes` para `Clientes Mais+`.
- Inserir logo profissional ao lado do nome (versao horizontal e icone).
- Ajustar favicon, titulo da aba e pontos principais de navegacao.

### 3.2 Rename tecnico (fora do escopo neste ciclo)
- Nao alterar nomes de arquivos, pastas, packages Java, artefatos nem scripts.
- Tratar qualquer rename tecnico apenas em fase futura dedicada.

### 3.3 Padronizacao visual de CRUDs
- Unificar shell/layout de paginas autenticadas.
- Unificar estilo de cards, cabecalhos, tabelas, botoes e formularios.
- Aplicar padrao aos principais modulos de cadastro/listagem.

## 4) Arquivos-alvo (frontend)

## 4.1 Branding e nome
- `clientes-app/src/index.html`
- `clientes-app/src/app/login/login.component.html`
- `clientes-app/src/app/login/login.component.css`
- `clientes-app/src/app/home/home.component.html`
- `clientes-app/src/app/home/home.component.css`
- `clientes-app/src/app/template/navbar/navbar.component.html`
- `clientes-app/src/app/template/navbar/navbar.component.css`
- `clientes-app/src/app/app.component.ts`
- `clientes-app/src/assets/` (novos assets de marca)

## 4.2 Shell e padronizacao de layout
- `clientes-app/src/styles.css`
- `clientes-app/src/app/layout/` (ou pasta equivalente de shell)
- `clientes-app/src/app/clientes/clientes-lista/*`
- `clientes-app/src/app/clientes/clientes-form/*`
- `clientes-app/src/app/prestador/*`
- `clientes-app/src/app/servico-prestado/*`
- `clientes-app/src/app/pacote/*`

## 4.3 Rename tecnico
- Fora do escopo desta implantacao.
- Manter estrutura e nomes tecnicos atuais sem mudancas.

## 5) Identidade visual proposta

## 5.1 Nome de marca
- Nome principal: `Clientes Mais+`
- Nome curto: `CM+`

## 5.2 Logo
- `logo-clientes-mais.svg` (horizontal)
- `logo-clientes-mais-icon.svg` (icone para sidebar/menu)

## 5.3 Diretrizes rapidas
- destaque do `+` com cor de acento;
- manter legibilidade em tema claro/escuro;
- preservar contraste minimo para acessibilidade;
- evitar efeitos pesados; priorizar estilo comercial limpo.

## 6) Diretriz de naming neste ciclo

## 6.1 Regra de implantacao
**Rebranding de marca (UI) apenas**, sem alterar nomes tecnicos internos.

Escopo permitido:
- textos, logos, favicon, estilos e layout no frontend.

Escopo bloqueado:
- rename de pastas/projetos;
- rename de packages Java/TypeScript;
- alteracao de nomes em `angular.json`, `package.json`, `pom.xml` e scripts de deploy.

## 6.2 Recomendacao
Manter a implantacao de baixo risco:
1. **Agora**: rebranding visual + padronizacao dos CRUDs.
2. **Depois (opcional)**: discutir rename tecnico em sprint separada, se houver necessidade real.

## 7) Estrategia de padronizacao dos CRUDs

## 7.1 Padrao de pagina (shell)
Criar/usar um layout comum para area logada com:
- topo (brand + usuario + acoes);
- menu lateral;
- container de conteudo com largura e espacamento padrao;
- rodape leve (opcional).

## 7.2 Componentes visuais padrao
- `PageHeader` (titulo, subtitulo, acoes)
- `PageCard` (caixa principal)
- `DataTableWrapper` (tabelas/listas)
- `FormSection` (blocos de formulario)
- `StatusBadge` (status padronizados)

## 7.3 Tokens CSS globais
Definir no `styles.css`:
- cores (primaria, secundaria, sucesso, alerta)
- espacamentos (`8/12/16/24/32`)
- borda/radius/sombra
- fontes e tamanhos

## 7.4 Aplicacao progressiva
Prioridade de telas:
1. `clientes/lista` e `clientes/form`
2. `prestador/lista` e `prestador/form`
3. `servico-prestado/lista` e `servico-prestado/form`
4. `pacote/lista` e `pacote/form`

## 8) Plano de execucao por fases

### Fase A - Preparacao (rapida)
- consolidar logo final (SVG);
- definir paleta e tipografia;
- validar nome final da marca (`Clientes Mais+`).

### Fase B - Rebranding login + home
- atualizar textos e logo no login;
- atualizar textos e logo na home/navbar;
- atualizar titulo da aba e identidade global.

### Fase C - Layout comum dos CRUDs
- aplicar shell padrao da home nos CRUDs;
- normalizar cards/tabelas/formularios;
- alinhar espacamentos e tipografia.

### Fase D - Estabilizacao
- validacao funcional das rotas principais;
- ajustes visuais finos;
- fechamento de checklist e aprovacao.

## 9) Criterios de aceite

### Branding
- login e home exibem `Clientes Mais+` com logo;
- nome antigo nao aparece nos pontos principais de navegacao.

### Layout
- CRUDs principais apresentam o mesmo padrao de estrutura da home;
- estilos de botoes, campos, cards e cabecalhos estao consistentes.

### Operacional
- aplicacao abre, navega e executa CRUDs sem regressao funcional;
- build do frontend conclui com sucesso.

## 10) Riscos e mitigacoes

1. **Risco**: alteracao visual impactar responsividade.
   - **Mitigacao**: validar desktop + resolucoes menores antes de publicar.

2. **Risco**: tentativa de alterar naming tecnico no meio da entrega visual.
   - **Mitigacao**: bloquear rename tecnico neste ciclo e focar apenas em UI.

3. **Risco**: inconsistencias entre modulos CRUD.
   - **Mitigacao**: aplicar componentes padrao reutilizaveis, nao CSS pontual isolado.

## 11) Checklist de implantacao (manha)

## 11.1 Pre-implantacao
- [ ] Confirmar assets finais da marca (`logo-clientes-mais.svg`, `logo-clientes-mais-icon.svg`).
- [ ] Validar nome comercial final na UI: `Clientes Mais+`.
- [ ] Congelar escopo: sem qualquer rename tecnico neste ciclo.

## 11.2 Implantacao
- [ ] Atualizar login/home/navbar com novo nome e logo.
- [ ] Criar/aplicar shell visual comum para CRUDs.
- [ ] Padronizar pelo menos `clientes/lista` e `clientes/form`.
- [ ] Rodar build frontend e smoke test das rotas criticas.

## 11.3 Pos-implantacao
- [ ] Revisar visual com checklist de consistencia.
- [ ] Registrar pendencias de naming tecnico apenas como backlog futuro.
- [ ] Formalizar aprovacao para expandir aos demais CRUDs.

## 12) O que vamos executar juntos amanha
1. Ajuste de branding imediato (login + home + navbar).
2. Criacao do shell padrao visual.
3. Aplicacao do padrao no modulo `clientes` (lista/form).
4. Replicacao para `prestador` e `servico-prestado` conforme tempo.
5. Fechamento com validação funcional e visual.

---

## Nota final
Este plano privilegia ganho visual e comercial rapido com baixo risco tecnico. Nesta fase, a migracao para `Clientes Mais+` e estritamente estetica no frontend, preservando nomes e estrutura internos atuais para evitar retrabalho.

