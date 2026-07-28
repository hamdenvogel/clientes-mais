import os
import re
from collections import defaultdict

# Buscar padrões duplicados em arquivos de teste
test_dir = "src/test/java/io/github/hvogel/clientes"

def extract_method_bodies(file_path):
    """Extrai corpos de métodos de um arquivo Java"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Encontrar métodos @Test
    test_methods = re.findall(r'@Test\s+(?:public\s+)?void\s+(\w+)\s*\([^)]*\)\s*\{([^}]+)\}', content, re.DOTALL)
    return test_methods

def find_similar_test_methods():
    """Procura por métodos de teste similares"""
    all_methods = defaultdict(list)
    
    for root, dirs, files in os.walk(test_dir):
        for file in files:
            if file.endswith('Test.java'):
                file_path = os.path.join(root, file)
                try:
                    methods = extract_method_bodies(file_path)
                    for method_name, method_body in methods:
                        # Normalizar o corpo do método
                        normalized = re.sub(r'\s+', ' ', method_body).strip()
                        # Ignorar métodos muito pequenos
                        if len(normalized) > 50:
                            all_methods[normalized].append((file_path, method_name))
                except Exception as e:
                    print(f"Erro ao processar {file_path}: {e}")
    
    # Encontrar duplicações
    duplications = []
    for body, locations in all_methods.items():
        if len(locations) > 1:
            duplications.append((body, locations))
    
    return duplications

def analyze_dto_tests():
    """Analisa testes de DTOs que geralmente têm padrões similares"""
    dto_tests = []
    for root, dirs, files in os.walk(test_dir):
        for file in files:
            if file.endswith('DTOTest.java'):
                dto_tests.append(os.path.join(root, file))
    return dto_tests

def find_setup_teardown_patterns():
    """Procura por padrões similares em @BeforeEach e @AfterEach"""
    setup_patterns = defaultdict(list)
    
    for root, dirs, files in os.walk(test_dir):
        for file in files:
            if file.endswith('Test.java'):
                file_path = os.path.join(root, file)
                try:
                    with open(file_path, 'r', encoding='utf-8') as f:
                        content = f.read()
                    
                    # Procurar @BeforeEach
                    setups = re.findall(r'@BeforeEach\s+(?:public\s+)?void\s+\w+\s*\([^)]*\)\s*\{([^}]+)\}', content, re.DOTALL)
                    if setups:
                        for setup in setups:
                            normalized = re.sub(r'\s+', ' ', setup).strip()
                            if len(normalized) > 30:
                                setup_patterns[normalized].append(file_path)
                except Exception as e:
                    pass
    
    duplicates = {k: v for k, v in setup_patterns.items() if len(v) > 1}
    return duplicates

print("=" * 100)
print("ANÁLISE DE DUPLICAÇÕES DE CÓDIGO")
print("=" * 100)
print()

# 1. DTOs de teste
print("1. ARQUIVOS DE TESTE DE DTO")
print("-" * 100)
dto_tests = analyze_dto_tests()
print(f"Total de arquivos *DTOTest.java: {len(dto_tests)}")
for test_file in sorted(dto_tests):
    print(f"  • {test_file.replace('src/test/java/io/github/hvogel/clientes/', '')}")
print()

# 2. Métodos de teste similares
print("2. MÉTODOS DE TESTE DUPLICADOS")
print("-" * 100)
duplications = find_similar_test_methods()
if duplications:
    print(f"Encontradas {len(duplications)} duplicações de métodos:\n")
    for i, (body, locations) in enumerate(duplications[:5], 1):
        print(f"Duplicação {i}:")
        print(f"  Encontrado em {len(locations)} locais:")
        for file_path, method_name in locations:
            short_path = file_path.replace('src/test/java/io/github/hvogel/clientes/', '')
            print(f"    • {short_path}::{method_name}()")
        print(f"  Corpo (primeiras 200 chars): {body[:200]}...")
        print()
else:
    print("Nenhuma duplicação de método de teste encontrada.")
print()

# 3. Padrões de setup/teardown
print("3. PADRÕES DUPLICADOS EM @BeforeEach")
print("-" * 100)
setup_dupes = find_setup_teardown_patterns()
if setup_dupes:
    print(f"Encontrados {len(setup_dupes)} padrões duplicados:\n")
    for i, (pattern, files) in enumerate(list(setup_dupes.items())[:3], 1):
        print(f"Padrão {i} (encontrado em {len(files)} arquivos):")
        for file_path in files:
            short_path = file_path.replace('src/test/java/io/github/hvogel/clientes/', '')
            print(f"  • {short_path}")
        print()
else:
    print("Nenhum padrão duplicado encontrado.")
print()

# 4. Analisar similaridade entre Controllers
print("4. ANÁLISE DE CONTROLLERS")
print("-" * 100)
controller_dir = "src/main/java/io/github/hvogel/clientes/rest"
controllers = []
for file in os.listdir(controller_dir):
    if file.endswith('Controller.java') and 'Abstract' not in file:
        controllers.append(file)

print(f"Total de Controllers: {len(controllers)}")
for controller in sorted(controllers):
    print(f"  • {controller}")
print()

print("=" * 100)
print("RECOMENDAÇÕES")
print("=" * 100)
print()
print("Para reduzir duplicações de BLOCOS DE CÓDIGO (não apenas strings):")
print()
print("1. TESTES DE DTO:")
print("   - Criar classe base abstrata BaseDTO_Test com métodos comuns")
print("   - Usar testes parametrizados (@ParameterizedTest)")
print()
print("2. CONTROLLERS:")
print("   - Já existe AbstractController, garantir que todos herdam dele")
print("   - Extrair métodos auxiliares comuns para classes helper")
print()
print("3. SETUP DE TESTES:")
print("   - Criar classes base de teste (ex: BaseControllerTest, BaseServiceTest)")
print("   - Centralizar mocks e configurações comuns")
print()
print("4. NOTA SOBRE 'NEW CODE' NO SONARQUBE:")
print("   - O SonarQube só mostra duplicações em 'New Code' se:")
print("     a) As linhas foram modificadas recentemente")
print("     b) Estão dentro do período definido em 'New Code Period'")
print("   - Verificar em: Project Settings > New Code")
print("   - Considerar alterar para 'Previous Version' ou 'Number of days'")
