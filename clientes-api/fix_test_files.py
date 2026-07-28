import os
import re

# Mapeamento de nomes snake_case para camelCase
MAPPINGS = {
    'getAno_fabricacao': 'getAnoFabricacao',
    'setAno_fabricacao': 'setAnoFabricacao',
    'getAno_modelo': 'getAnoModelo',
    'setAno_modelo': 'setAnoModelo',
    'getMes_ano': 'getMesAno',
    'setMes_ano': 'setMesAno',
    'getEm_atendimento': 'getEmAtendimento',
    'setEm_atendimento': 'setEmAtendimento',
    'getStatus_atendimento': 'getStatusAtendimento',
    'setStatus_atendimento': 'setStatusAtendimento',
    'getI_percentual': 'getIPercentual',
    'setI_percentual': 'setIPercentual',
    'i_percentual': 'iPercentual',
    'getA_percentual': 'getAPercentual',
    'setA_percentual': 'setAPercentual',
    'a_percentual': 'aPercentual',
    'getE_percentual': 'getEPercentual',
    'setE_percentual': 'setEPercentual',
    'e_percentual': 'ePercentual',
    'getC_percentual': 'getCPercentual',
    'setC_percentual': 'setCPercentual',
    'c_percentual': 'cPercentual',
    'getF_percentual': 'getFPercentual',
    'setF_percentual': 'setFPercentual',
    'f_percentual': 'fPercentual',
    'em_atendimento': 'emAtendimento',
    'getAccess_token': 'getAccessToken',
    'setAccess_token': 'setAccessToken',
    'getExpires_in': 'getExpiresIn',
    'setExpires_in': 'setExpiresIn',
    'getToken_type': 'getTokenType',
    'setToken_type': 'setTokenType',
}

def fix_test_files(file_path, mappings):
    """Corrige nomes em arquivos de teste"""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    for old_name, new_name in mappings.items():
        # Usa word boundary para evitar substituições parciais
        pattern = r'\b' + re.escape(old_name) + r'\b'
        content = re.sub(pattern, new_name, content)
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def process_test_directory(directory, mappings):
    """Processa todos os arquivos de teste"""
    modified_files = []
    
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if fix_test_files(file_path, mappings):
                    modified_files.append(file_path)
                    print(f"✅ Fixed: {os.path.basename(file_path)}")
    
    return modified_files

if __name__ == '__main__':
    test_dir = r'c:\Hamden\Sistemas\Backend\clientes\des\clientes\src\test\java'
    
    print("=== Corrigindo arquivos de teste ===\n")
    
    modified = process_test_directory(test_dir, MAPPINGS)
    
    print(f"\n✅ Total de arquivos de teste corrigidos: {len(modified)}")
