import os
import re

# Mapeamento de nomes snake_case para camelCase
FIELD_MAPPINGS = {
    # Produto
    'ano_fabricacao': 'anoFabricacao',
    'ano_modelo': 'anoModelo',
    # Gráficos
    'mes_ano': 'mesAno',
    'em_atendimento': 'emAtendimento',
    'status_atendimento': 'statusAtendimento',
    'i_percentual': 'iPercentual',
    'a_percentual': 'aPercentual',
    'e_percentual': 'ePercentual',
    'c_percentual': 'cPercentual',
    'f_percentual': 'fPercentual',
    # TokenDTO
    'access_token': 'accessToken',
    'expires_in': 'expiresIn',
    'token_type': 'tokenType',
    # CepService
    'via_cep_format': 'viaCepFormat',
}

def rename_snake_to_camel(file_path, mappings):
    """Renomeia campos snake_case para camelCase em um arquivo"""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    modified = False
    
    for snake, camel in mappings.items():
        # Renomear declarações de campo: private Type snake_case;
        pattern1 = rf'\b{snake}\b'
        if re.search(pattern1, content):
            content = re.sub(pattern1, camel, content)
            modified = True
    
    if modified:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def process_directory(directory, mappings):
    """Processa todos os arquivos Java em um diretório recursivamente"""
    modified_files = []
    
    for root, dirs, files in os.walk(directory):
        # Pular diretórios target, test
        if 'target' in root or '\\test\\' in root:
            continue
            
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if rename_snake_to_camel(file_path, mappings):
                    modified_files.append(file_path)
                    print(f"✅ Modified: {file_path}")
    
    return modified_files

if __name__ == '__main__':
    base_dir = r'c:\Hamden\Sistemas\Backend\clientes\des\clientes\src\main\java'
    
    print("=== Renomeando campos snake_case para camelCase ===\n")
    
    modified = process_directory(base_dir, FIELD_MAPPINGS)
    
    print(f"\n✅ Total de arquivos modificados: {len(modified)}")
    
    if modified:
        print("\nArquivos modificados:")
        for f in modified:
            print(f"  - {f}")
