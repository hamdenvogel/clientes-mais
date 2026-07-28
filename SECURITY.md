# Security Policy

## Reporting

Se encontrar vazamento de credencial ou vulnerabilidade neste repositório, avise o mantenedor (Hamden Vogel) por canal privado — não abra issue pública com o segredo.

## Practice

- Secrets apenas em `.env` local, CI secrets ou secret managers.
- Rotacionar qualquer chave que tenha sido commitada por engano.
- Pacotes GHCR e Actions usam `GITHUB_TOKEN` do próprio GitHub (sem token pessoal no código).
