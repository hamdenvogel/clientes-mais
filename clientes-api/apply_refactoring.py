import os
import re

# Mapeamento de arquivos e substituições
refactorings = [
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/AuthController.java',
        'replacements': [
            ('  "Error: Role is not found."', 'Messages.ROLE_NOT_FOUND_ERROR')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/ImagemController.java',
        'replacements': [
            ('"Imagem não encontrada."', 'Messages.IMAGEM_NAO_ENCONTRADA')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/ItemPacoteController.java',
        'replacements': [
            ('"Pacote Inexistente"', 'Messages.PACOTE_INEXISTENTE'),
            ('"Serviço Inexistente."', 'Messages.SERVICO_INEXISTENTE'),
            ('"Informação"', 'Messages.MSG_INFORMACAO')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/PacoteController.java',
        'replacements': [
            ('"Informação"', 'Messages.MSG_INFORMACAO'),
            ('"Pacote não encontrado."', 'Messages.PACOTE_NAO_ENCONTRADO')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/PrestadorController.java',
        'replacements': [
            ('"Informação"', 'Messages.MSG_INFORMACAO'),
            ('"Prestador não encontrado."', 'Messages.PRESTADOR_NAO_ENCONTRADO')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/ProdutoController.java',
        'replacements': [
            ('"Informação"', 'Messages.MSG_INFORMACAO'),
            ('"Produto não encontrado."', 'Messages.PRODUTO_NAO_ENCONTRADO')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/rest/ServicoPrestadoController.java',
        'replacements': [
            ('"Cliente inexistente."', 'Messages.CLIENTE_INEXISTENTE'),
            ('"Informação"', 'Messages.MSG_INFORMACAO'),
            ('"Serviço não encontrado."', 'Messages.SERVICO_NAO_ENCONTRADO')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/model/entity/Imagem.java',
        'replacements': [
            ('"default"', 'Messages.DEFAULT_VALUE')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/service/DocService.java',
        'replacements': [
            ('"SimpleBookmark"', 'Messages.SIMPLE_BOOKMARK'),
            ('"AddBookmarks.docx"', 'Messages.ADD_BOOKMARKS_FILE')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/service/impl/PrestadorServiceImpl.java',
        'replacements': [
            ('"O campo avaliação é obrigatório!"', 'Messages.CAMPO_AVALIACAO_OBRIGATORIO')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/service/impl/RelatorioServiceImpl.java',
        'replacements': [
            ('"DATA_INICIO"', 'Messages.PARAM_DATA_INICIO'),
            ('"DATA_FIM"', 'Messages.PARAM_DATA_FIM')
        ],
        'add_import': True
    },
    {
        'file': 'src/main/java/io/github/hvogel/clientes/service/impl/ValidadorServiceImpl.java',
        'replacements': [
            ('"valor inválido."', 'Messages.VALOR_INVALIDO')
        ],
        'add_import': True
    }
]

# Processar cada arquivo
for refactoring in refactorings:
    file_path = refactoring['file']
    
    if not os.path.exists(file_path):
        print(f"❌ Arquivo não encontrado: {file_path}")
        continue
    
    print(f"\n📝 Processando: {file_path}")
    
    # Ler o arquivo
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    changes_made = 0
    
    # Aplicar substituições
    for old_text, new_text in refactoring['replacements']:
        count = content.count(old_text)
        if count > 0:
            content = content.replace(old_text, new_text)
            changes_made += count
            print(f"   ✓ Substituído '{old_text}' → '{new_text}' ({count}x)")
    
    # Adicionar import se necessário
    if refactoring.get('add_import') and 'import io.github.hvogel.clientes.util.Messages;' not in content:
        # Encontrar a última linha de import
        import_pattern = r'(import .*?;\n)(?!import)'
        matches = list(re.finditer(import_pattern, content))
        if matches:
            last_import = matches[-1]
            insert_pos = last_import.end()
            content = content[:insert_pos] + '\nimport io.github.hvogel.clientes.util.Messages;\n' + content[insert_pos:]
            print(f"   ✓ Adicionado import Messages")
    
    # Salvar se houve mudanças
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        print(f"   ✅ Arquivo atualizado ({changes_made} substituições)")
    else:
        print(f"   ℹ️  Nenhuma mudança necessária")

print("\n" + "=" * 80)
print("✅ Refatoração concluída!")
print("=" * 80)
print("\nPróximos passos:")
print("1. Compile o projeto: mvn clean compile")
print("2. Execute os testes: mvn test")
print("3. Execute o SonarQube para verificar as duplicações:")
print("   mvn clean verify sonar:sonar \\")
print('     -Dsonar.projectKey=clientes \\')
print('     -Dsonar.host.url=http://localhost:9000 \\')
print('     -Dsonar.login=sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956')
