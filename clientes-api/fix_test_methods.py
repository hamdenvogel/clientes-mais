import os
import re

# Define replacements for test files
replacements = {
    # GraficoAtendimentoLinear
    r'\.withMes_ano\(': '.withMesAno(',
    r'\.getMesAno\(\)': '.getMonthYear()',
    r'\.setMesAno\(': '.setMonthYear(',
    
    # GraficoTipoServico
    # Same as above - mesAno methods
    
    # GraficoAtendimentoTorta
    r'\.withStatus_atendimento\(': '.withStatusAtendimento(',
    
    # GraficoStatusPacotePercentual
    r'\.withI_percentual\(': '.withIPercentual(',
    r'\.withA_percentual\(': '.withAPercentual(',
    r'\.withE_percentual\(': '.withEPercentual(',
    r'\.withC_percentual\(': '.withCPercentual(',
    r'\.withF_percentual\(': '.withFPercentual(',
    r'\.getIPercentual\(\)': '.getIniciadoPercentual()',
    r'\.setIPercentual\(': '.setIniciadoPercentual(',
    r'\.getAPercentual\(\)': '.getAprovadoPercentual()',
    r'\.setAPercentual\(': '.setAprovadoPercentual(',
    r'\.getEPercentual\(\)': '.getExecutandoPercentual()',
    r'\.setEPercentual\(': '.setExecutandoPercentual(',
    r'\.getCPercentual\(\)': '.getCanceladoPercentual()',
    r'\.setCPercentual\(': '.setCanceladoPercentual(',
    r'\.getFPercentual\(\)': '.getFinalizadoPercentual()',
    r'\.setFPercentual\(': '.setFinalizadoPercentual(',
    
    # Repository and Service methods
    r'\.descricao_status\(\)': '.descricaoStatus()',
    r'\.quantidade_servicos\(\)': '.quantidadeServicos()',
    
    # Builder methods
    r'\.withEm_atendimento\(': '.withEmAtendimento(',
}

test_dir = 'src/test/java/io/github/hvogel/clientes'

count = 0
for root, dirs, files in os.walk(test_dir):
    for file in files:
        if file.endswith('.java'):
            file_path = os.path.join(root, file)
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            original_content = content
            for pattern, replacement in replacements.items():
                content = re.sub(pattern, replacement, content)
            
            if content != original_content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(content)
                print(f"✓ Updated: {file_path}")
                count += 1

print(f"\nTotal files updated: {count}")
