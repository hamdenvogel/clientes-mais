import json
import os
from collections import defaultdict

# Load the S1192 issues from issues_critical.json
try:
    with open('issues_critical.json', 'r', encoding='utf-8') as f:
        data = json.load(f)
except UnicodeDecodeError:
    with open('issues_critical.json', 'r', encoding='utf-16') as f:
        data = json.load(f)

# Filter only S1192 issues (string literal duplications)
s1192_issues = [issue for issue in data['issues'] if issue['rule'] == 'java:S1192']

print("=" * 100)
print("ANÁLISE DE DUPLICAÇÕES DE STRINGS (S1192)")
print("=" * 100)
print(f"\nTotal de strings duplicadas: {len(s1192_issues)}\n")

# Group by file
issues_by_file = defaultdict(list)
for issue in s1192_issues:
    file_path = issue['component'].replace('clientes:', '')
    issues_by_file[file_path].append(issue)

# Sort files by number of issues
sorted_files = sorted(issues_by_file.items(), key=lambda x: len(x[1]), reverse=True)

print("=" * 100)
print("ARQUIVOS COM MAIS DUPLICAÇÕES")
print("=" * 100)
print()

total_estimated_time = 0
for file_path, issues in sorted_files:
    print(f"📄 {file_path}")
    print(f"   Quantidade de duplicações: {len(issues)}")
    
    for issue in issues:
        message = issue['message']
        line = issue.get('line', '?')
        effort = issue.get('effort', '0min')
        
        # Extract time in minutes
        if 'min' in effort:
            time_min = int(effort.replace('min', ''))
            total_estimated_time += time_min
        
        print(f"   • Linha {line}: {message}")
        print(f"     Esforço estimado: {effort}")
    print()

print("=" * 100)
print("SUMÁRIO")
print("=" * 100)
print(f"Total de arquivos com duplicações: {len(sorted_files)}")
print(f"Total de strings duplicadas: {len(s1192_issues)}")
print(f"Tempo estimado total para correção: {total_estimated_time} minutos ({total_estimated_time/60:.1f} horas)")
print()

print("=" * 100)
print("RECOMENDAÇÕES DE REFATORAÇÃO")
print("=" * 100)
print()

# Extract strings to create constants
print("1. Criar classe de constantes para mensagens:")
print()
print("```java")
print("package io.github.hvogel.clientes.util;")
print()
print("public final class Messages {")
print("    private Messages() {}")
print()

# Extract unique messages from issues
unique_messages = set()
for issue in s1192_issues:
    message = issue['message']
    # Extract the actual string from the message
    if '"' in message:
        start = message.index('"') + 1
        end = message.rindex('"')
        string_literal = message[start:end]
        unique_messages.add(string_literal)

# Group by category
error_messages = [m for m in unique_messages if 'não encontrado' in m.lower() or 'inexistente' in m.lower() or 'not found' in m.lower()]
info_messages = [m for m in unique_messages if m == 'Informação']
validation_messages = [m for m in unique_messages if 'obrigatório' in m.lower() or 'inválido' in m.lower()]
other_messages = [m for m in unique_messages if m not in error_messages and m not in info_messages and m not in validation_messages]

if error_messages:
    print("    // Mensagens de erro")
    for msg in sorted(error_messages):
        const_name = msg.upper().replace(' ', '_').replace('.', '').replace('Ã', 'A').replace('º', 'O').replace('├', '')
        const_name = const_name.replace('úO', '').replace('ú', '').replace('O_ENCONTRADO', '_NAO_ENCONTRADO')
        print(f'    public static final String {const_name} = "{msg}";')

if info_messages:
    print()
    print("    // Mensagens informativas")
    for msg in sorted(info_messages):
        const_name = 'MSG_INFO'
        print(f'    public static final String {const_name} = "{msg}";')

if validation_messages:
    print()
    print("    // Mensagens de validação")
    for msg in sorted(validation_messages):
        const_name = msg.upper().replace(' ', '_').replace('.', '').replace('Ã', 'A')
        print(f'    public static final String {const_name} = "{msg}";')

if other_messages:
    print()
    print("    // Outras mensagens")
    for msg in sorted(other_messages):
        if msg == 'default':
            const_name = 'DEFAULT_VALUE'
        elif msg == 'Error: Role is not found.':
            const_name = 'ROLE_NOT_FOUND_ERROR'
        elif msg == 'SimpleBookmark':
            const_name = 'SIMPLE_BOOKMARK'
        elif msg == 'AddBookmarks.docx':
            const_name = 'ADD_BOOKMARKS_FILE'
        elif msg == 'DATA_INICIO':
            const_name = 'PARAM_DATA_INICIO'
        elif msg == 'DATA_FIM':
            const_name = 'PARAM_DATA_FIM'
        else:
            const_name = msg.upper().replace(' ', '_').replace('.', '').replace('-', '_')
        print(f'    public static final String {const_name} = "{msg}";')

print("}")
print("```")
print()

print("2. Exemplos de uso nos Controllers:")
print()
print("```java")
print("// Antes:")
print('throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado.");')
print()
print("// Depois:")
print("import static io.github.hvogel.clientes.util.Messages.CLIENTE_NAO_ENCONTRADO;")
print('throw new ResponseStatusException(HttpStatus.NOT_FOUND, CLIENTE_NAO_ENCONTRADO);')
print("```")
print()

print("3. Próximos passos:")
print("   a) Criar a classe Messages com as constantes acima")
print("   b) Refatorar cada arquivo para usar as constantes")
print("   c) Executar testes para garantir que tudo funciona")
print("   d) Rodar SonarQube novamente para verificar a redução das duplicações")
print()

# Print file-by-file plan
print("=" * 100)
print("PLANO DE REFATORAÇÃO POR ARQUIVO")
print("=" * 100)
print()

for i, (file_path, issues) in enumerate(sorted_files, 1):
    print(f"{i}. {file_path}")
    for issue in issues:
        message = issue['message']
        line = issue.get('line', '?')
        if '"' in message:
            start = message.index('"') + 1
            end = message.rindex('"')
            string_literal = message[start:end]
            print(f"   Linha {line}: Substituir \"{string_literal}\" por constante")
    print()
