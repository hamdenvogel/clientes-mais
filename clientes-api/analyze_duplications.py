import urllib.request
import json
import base64
from collections import defaultdict

# Configuration
SONAR_URL = 'http://localhost:9000'
PROJECT_KEY = 'clientes'
TOKEN = 'sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956'

def get_duplications():
    """Fetch duplication metrics from SonarQube"""
    url = f"{SONAR_URL}/api/measures/component"
    params = {
        'component': PROJECT_KEY,
        'metricKeys': 'duplicated_lines_density,duplicated_blocks,duplicated_files,duplicated_lines,new_duplicated_lines_density,new_duplicated_lines,new_duplicated_blocks'
    }
    
    query_string = '&'.join([f"{k}={v}" for k, v in params.items()])
    full_url = f"{url}?{query_string}"
    
    # Basic Auth using token
    auth_string = f"{TOKEN}:"
    auth_bytes = auth_string.encode('ascii')
    auth_b64 = base64.b64encode(auth_bytes).decode('ascii')
    
    request = urllib.request.Request(full_url)
    request.add_header('Authorization', f'Basic {auth_b64}')
    
    try:
        with urllib.request.urlopen(request) as response:
            data = json.loads(response.read().decode('utf-8'))
            return data
    except Exception as e:
        print(f"Error fetching duplications: {e}")
        return None

def get_duplication_details():
    """Fetch detailed information about duplicated blocks"""
    # Get all files with duplications
    url = f"{SONAR_URL}/api/measures/component_tree"
    params = {
        'component': PROJECT_KEY,
        'metricKeys': 'duplicated_lines_density,duplicated_lines,duplicated_blocks',
        'ps': 500
    }
    
    query_string = '&'.join([f"{k}={v}" for k, v in params.items()])
    full_url = f"{url}?{query_string}"
    
    auth_string = f"{TOKEN}:"
    auth_bytes = auth_string.encode('ascii')
    auth_b64 = base64.b64encode(auth_bytes).decode('ascii')
    
    request = urllib.request.Request(full_url)
    request.add_header('Authorization', f'Basic {auth_b64}')
    
    try:
        with urllib.request.urlopen(request) as response:
            data = json.loads(response.read().decode('utf-8'))
            return data
    except Exception as e:
        print(f"Error fetching duplication details: {e}")
        return None

def analyze_s1192_issues():
    """Analyze S1192 (string literal duplication) issues"""
    url = f"{SONAR_URL}/api/issues/search"
    params = {
        'componentKeys': PROJECT_KEY,
        'rules': 'java:S1192',
        'resolved': 'false',
        'ps': 500
    }
    
    query_string = '&'.join([f"{k}={v}" for k, v in params.items()])
    full_url = f"{url}?{query_string}"
    
    auth_string = f"{TOKEN}:"
    auth_bytes = auth_string.encode('ascii')
    auth_b64 = base64.b64encode(auth_bytes).decode('ascii')
    
    request = urllib.request.Request(full_url)
    request.add_header('Authorization', f'Basic {auth_b64}')
    
    try:
        with urllib.request.urlopen(request) as response:
            data = json.loads(response.read().decode('utf-8'))
            return data
    except Exception as e:
        print(f"Error fetching S1192 issues: {e}")
        return None

def main():
    print("=" * 100)
    print("ANÁLISE DE DUPLICAÇÕES - SONARQUBE")
    print("=" * 100)
    
    # 1. Get overall duplication metrics
    print("\n1. MÉTRICAS GERAIS DE DUPLICAÇÃO\n")
    metrics = get_duplications()
    if metrics and 'component' in metrics and 'measures' in metrics['component']:
        for measure in metrics['component']['measures']:
            metric_key = measure['metric']
            value = measure.get('value', measure.get('period', {}).get('value', 'N/A'))
            print(f"   {metric_key}: {value}")
    
    # 2. Get files with duplications
    print("\n" + "=" * 100)
    print("2. ARQUIVOS COM DUPLICAÇÕES")
    print("=" * 100 + "\n")
    
    details = get_duplication_details()
    if details and 'components' in details:
        duplicated_files = []
        for component in details['components']:
            if 'measures' in component and len(component['measures']) > 0:
                dup_density = None
                dup_lines = None
                dup_blocks = None
                
                for measure in component['measures']:
                    if measure['metric'] == 'duplicated_lines_density':
                        dup_density = float(measure.get('value', 0))
                    elif measure['metric'] == 'duplicated_lines':
                        dup_lines = int(measure.get('value', 0))
                    elif measure['metric'] == 'duplicated_blocks':
                        dup_blocks = int(measure.get('value', 0))
                
                if dup_density and dup_density > 0:
                    duplicated_files.append({
                        'name': component['name'],
                        'path': component['path'],
                        'density': dup_density,
                        'lines': dup_lines,
                        'blocks': dup_blocks
                    })
        
        # Sort by density descending
        duplicated_files.sort(key=lambda x: x['density'], reverse=True)
        
        print(f"Total de arquivos com duplicações: {len(duplicated_files)}\n")
        for i, file in enumerate(duplicated_files[:20], 1):
            print(f"{i}. {file['name']}")
            print(f"   Caminho: {file['path']}")
            print(f"   Densidade: {file['density']:.1f}%")
            print(f"   Linhas duplicadas: {file['lines']}")
            print(f"   Blocos duplicados: {file['blocks']}")
            print()
    
    # 3. Analyze S1192 issues (string literal duplications)
    print("=" * 100)
    print("3. STRINGS LITERAIS DUPLICADAS (S1192)")
    print("=" * 100 + "\n")
    
    s1192_data = analyze_s1192_issues()
    if s1192_data and 'issues' in s1192_data:
        issues = s1192_data['issues']
        print(f"Total de issues S1192: {len(issues)}\n")
        
        # Group by file
        issues_by_file = defaultdict(list)
        for issue in issues:
            component = issue['component'].replace('clientes:', '')
            issues_by_file[component].append(issue)
        
        # Sort files by number of issues
        sorted_files = sorted(issues_by_file.items(), key=lambda x: len(x[1]), reverse=True)
        
        print("Arquivos com mais strings duplicadas:\n")
        for file_path, file_issues in sorted_files[:15]:
            print(f"📄 {file_path} ({len(file_issues)} duplicações)")
            
            # Show details of each duplication
            for issue in file_issues[:3]:  # Show up to 3 examples per file
                message = issue['message']
                line = issue.get('line', '?')
                print(f"   Linha {line}: {message}")
            
            if len(file_issues) > 3:
                print(f"   ... e mais {len(file_issues) - 3} duplicações")
            print()
    
    # 4. Summary and recommendations
    print("=" * 100)
    print("4. RECOMENDAÇÕES PARA REDUZIR DUPLICAÇÕES")
    print("=" * 100 + "\n")
    
    print("Para reduzir as duplicações no código, considere:")
    print()
    print("1. STRINGS LITERAIS (S1192):")
    print("   - Criar constantes para strings repetidas")
    print("   - Centralizar mensagens de erro/sucesso em classes de constantes")
    print("   - Exemplo: private static final String MSG_NOT_FOUND = \"...\";")
    print()
    print("2. BLOCOS DE CÓDIGO DUPLICADOS:")
    print("   - Extrair métodos comuns em classes utilitárias")
    print("   - Criar métodos helper privados para lógica repetida")
    print("   - Considerar herança ou composição para comportamentos similares")
    print()
    print("3. TESTES DUPLICADOS:")
    print("   - Criar classes base abstratas para testes similares")
    print("   - Usar métodos @BeforeEach para setup comum")
    print("   - Parametrizar testes com @ParameterizedTest")
    print()
    print("4. DTOS E ENTIDADES:")
    print("   - Usar Lombok para reduzir código boilerplate")
    print("   - Criar classes base com campos comuns (id, timestamps, etc)")
    print("   - Considerar usar record classes (Java 14+) para DTOs imutáveis")
    print()

if __name__ == "__main__":
    main()
