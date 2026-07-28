# Contributing — Clientes Mais

## Antes de abrir PR

1. Não commitar `.env`, senhas, tokens, chaves JWT ou reCAPTCHA **secret**.
2. Usar `clientes-api/.env.example` e `environment.example.ts` como modelo.
3. Rodar testes da API: `cd clientes-api && ./mvnw -B test` (ou `mvnw.cmd` no Windows).
4. Manter o `ROADMAP_EVOLUCAO_CLIENTES_MAIS.md` alinhado se a mudança fechar um item.

## Segredos

- Preferir variáveis de ambiente (`${NOME_DA_VAR}` no Spring).
- Site key do reCAPTCHA (pública) pode ir no `environment.ts` **local**; secret só no backend via env.
- Se descobrir vazamento: rotacionar a chave e abrir issue/PR removendo o valor do histórico se necessário.

## Estilo

- Backend: Java 21, Spring Boot 3, sem mudança fora do escopo do PR.
- Frontend legado (Angular 9): mudanças mínimas até a recriação no item 7 do roadmap.
