# Guia Rapido: Falso "build failed" no Run/Debug (VS Code + Java)

Este guia explica como lidar com o erro intermitente de **build failed** no botao **Run/Debug** do VS Code, mesmo quando o Maven compila com sucesso.

## Como usar na pratica

1. Rodou `mvn clean install` e deu falso "build failed" no botao Run/Debug.
2. Pressione `Ctrl+Alt+J`.
3. Aguarde estabilizar.
4. Pressione `Ctrl+Alt+D`.
5. Selecione `SistemaDeClientesApplication`.

## Sintoma

Voce roda:

- `mvn clean install`

E o build termina com sucesso. Mesmo assim, ao iniciar debug pela topbar, o VS Code mostra "build failed" e/ou uma lista grande de problems que nao refletem o estado real do projeto.

## Causa mais comum

Na maioria dos casos, isso vem de estado inconsistente do **Java Language Server workspace/cache** (nao do Maven em si).

## O que ja foi ajustado no projeto

No workspace, foram aplicadas configuracoes para reduzir esse problema:

1. Exclusao de `bin` e `target` da importacao Java.
2. Ajuste de launch para evitar bloqueio no debug por build espurio.
3. Remocao de configuracoes conflitantes de source/output no modulo Maven.

Arquivos ajustados:

- `.vscode/settings.json`
- `.vscode/launch.json`
- `clientes-api/.vscode/settings.json`

## Como resolver quando acontecer

### Opcao A (manual, oficial)

1. Abra Command Palette: `Ctrl+Shift+P`.
2. Execute: `Java: Clean Java Language Server Workspace`.
3. Aguarde o reload/restart do Java LS.
4. Tente o debug novamente.

### Opcao B (atalho de teclado para acelerar)

Voce pode mapear um atalho para o comando acima.

Atalhos configurados neste ambiente:

1. `Ctrl+Alt+J` -> limpa o Java Language Server Workspace (reset de cache Java).
2. `Ctrl+Alt+D` -> abre a selecao e inicia o debug da configuracao desejada.

Padrao pratico de uso:

1. Pressione `Ctrl+Alt+J` quando aparecer falso "build failed".
2. Aguarde o workspace Java estabilizar.
3. Pressione `Ctrl+Alt+D` e escolha `SistemaDeClientesApplication`.

Uso recomendado de atalho:

- Somente quando aparecer falso "build failed" no Run/Debug.
- Quando os problems parecem "fantasma" apos build Maven bem-sucedido.
- Apos troca grande de branch/dependencias/estrutura.

Nao e necessario usar em toda execucao.

## Quando chamar o atalho

Fluxo pratico diario:

1. Rode `mvn clean install`.
2. Tente Run/Debug normalmente.
3. Se aparecer falso "build failed", use `Ctrl+Alt+J` uma vez.
4. Rode o debug com `Ctrl+Alt+D`.

## Checklist rapido de validacao

1. `mvn clean install` retorna sucesso.
2. Debug sobe a aplicacao sem bloquear por "build failed".
3. Problems list diminui/normaliza apos limpeza do Java LS workspace.

## Observacao importante

Reiniciar a IDE funciona como paliativo porque reinicia o Java LS e limpa estado em memoria. O comando `Java: Clean Java Language Server Workspace` existe justamente para fazer isso de forma mais direcionada, sem depender de reinicio completo toda vez.
