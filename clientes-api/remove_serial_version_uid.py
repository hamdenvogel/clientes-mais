import os

files = [
    'src/main/java/io/github/hvogel/clientes/service/AtividadeService.java',
    'src/main/java/io/github/hvogel/clientes/service/ChamadoService.java',
    'src/main/java/io/github/hvogel/clientes/service/DiagnosticoService.java',
    'src/main/java/io/github/hvogel/clientes/service/EquipamentoService.java',
    'src/main/java/io/github/hvogel/clientes/service/NaturezaService.java',
    'src/main/java/io/github/hvogel/clientes/service/SolucaoService.java'
]

base_dir = r'c:\Hamden\Sistemas\Backend\clientes\des\clientes'

for rel_path in files:
    file_path = os.path.join(base_dir, rel_path)
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        continue
        
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
            
        new_lines = [line for line in lines if 'private static final long serialVersionUID' not in line]
        
        if len(lines) != len(new_lines):
            with open(file_path, 'w', encoding='utf-8') as f:
                f.writelines(new_lines)
            print(f"Updated {rel_path} (removed {len(lines) - len(new_lines)} lines)")
        else:
            print(f"No changes in {rel_path}")
            
    except Exception as e:
        print(f"Error processing {rel_path}: {e}")
