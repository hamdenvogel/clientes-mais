import os
import re

def fix_getter_setter_names(file_path):
    """Corrige nomes de getters e setters com snake_case"""
    
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # Padrão para getters: public Type getName_part()
    getter_pattern = r'public\s+([A-Za-z<>, ]+)\s+get([A-Z][a-z]*_[a-z_]+)\s*\('
    setter_pattern = r'public\s+void\s+set([A-Z][a-z]*_[a-z_]+)\s*\('
    
    def snake_to_camel(name):
        """Converte snake_case para camelCase mantendo primeira letra maiúscula"""
        parts = name.split('_')
        return ''.join(part.capitalize() for part in parts)
    
    def replace_getter(match):
        return_type = match.group(1)
        method_name = match.group(2)
        camel_name = snake_to_camel(method_name)
        return f'public {return_type} get{camel_name}('
    
    def replace_setter(match):
        method_name = match.group(1)
        camel_name = snake_to_camel(method_name)
        return f'public void set{camel_name}('
    
    content = re.sub(getter_pattern, replace_getter, content)
    content = re.sub(setter_pattern, replace_setter, content)
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def process_directory(directory):
    """Processa todos os arquivos Java em um diretório"""
    modified_files = []
    
    for root, dirs, files in os.walk(directory):
        # Pular diretórios target, test
        if 'target' in root or '\\test\\' in root:
            continue
            
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if fix_getter_setter_names(file_path):
                    modified_files.append(file_path)
                    print(f"✅ Fixed: {file_path}")
    
    return modified_files

if __name__ == '__main__':
    base_dir = r'c:\Hamden\Sistemas\Backend\clientes\des\clientes\src\main\java'
    
    print("=== Corrigindo getters e setters com snake_case ===\n")
    
    modified = process_directory(base_dir)
    
    print(f"\n✅ Total de arquivos corrigidos: {len(modified)}")
