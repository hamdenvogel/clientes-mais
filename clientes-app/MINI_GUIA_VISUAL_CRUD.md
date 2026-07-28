# Mini Guia Visual - Padrao CRUD (Clientes Mais+)

Este guia resume o padrao visual aplicado nas telas CRUD para manter consistencia, qualidade comercial e boa usabilidade.

## 1) Cores principais

- Primaria: `#1D4ED8`
- Accent: `#0EA5E9`
- Highlight: `#22D3EE`
- Superficie: `#FFFFFF`
- Superficie suave: `#F7FBFF`
- Borda suave: `#C9DCFF`
- Texto principal: `#0F172A`
- Texto secundario: `#64748B`

## 2) Gradientes do padrao

- Header (form): `linear-gradient(124deg, #081226 0%, #1D4ED8 42%, #0EA5E9 74%, #22D3EE 100%)`
- Card (form/lista): `linear-gradient(180deg, #FFFFFF 0%, #F7FBFF 100%)`
- Botoes primarios: `linear-gradient(135deg, #1D4ED8 0%, #0EA5E9 58%, #22D3EE 100%)`

## 3) Botoes (hierarquia)

- `btn-primary`: acao principal
- `btn-success`: confirmar/salvar
- `btn-warning`: limpar/acoes intermediarias
- `btn-danger`: voltar/remover/cancelar

Padrao visual:

- Raio: `12px`
- Altura minima: `40px`
- Peso da fonte: `700`
- Efeito hover: elevar `-1px` + sombra suave

## 4) Campos de formulario

- Altura minima: `42px`
- Raio: `14px`
- Fundo: gradiente branco suave
- Focus: borda azul + anel de foco (`--pp-focus-ring`)
- Placeholder: `#94A3B8`

## 5) Espacamentos

- Gap entre secoes da pagina: `16px` a `18px`
- `form-group`: margem inferior de `0.7rem`
- Acoes (`crud-actions`): gap de `8px` (desktop) / `10px` (mobile)
- Card: padding de `24px` (desktop) / `18px` (tablet/mobile)

## 6) Tabelas CRUD

- Cabecalho de tabela: tom escuro (`thead-dark`)
- Hover de linha (lista): `#F5FAFF`
- Borda externa leve no container responsivo

## 7) Sidebar e layout

- Altura da topbar padrao: `64px` (`--app-topbar-height`)
- Sidebar e conteudo sincronizados com a topbar
- Sidebar compacta para evitar scroll vertical desnecessario

## 8) Regras de uso

- Forms: usar `.crud-page.crud-form`
- Listas: usar `.crud-page` (sem `.crud-form`)
- **Nao alterar visual dos headers de lista** para manter referencia visual existente
- Reutilizar classes globais antes de criar CSS local

## 9) Checklist rapido para novas telas

- [ ] Header no padrao correto da pagina
- [ ] Card com borda/sombra padrao
- [ ] Botoes no estilo e hierarquia corretos
- [ ] Inputs com estados hover/focus consistentes
- [ ] Responsividade validada (desktop/tablet/mobile)
- [ ] Sem scroll vertical indevido no sidebar

