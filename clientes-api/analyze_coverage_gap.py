import xml.etree.ElementTree as ET

jacoco_xml = 'target/site/jacoco/jacoco.xml'

tree = ET.parse(jacoco_xml)
root = tree.getroot()

# Calcular cobertura total
total_missed = 0
total_covered = 0

for counter in root.iter('counter'):
    if counter.get('type') == 'INSTRUCTION':
        total_missed += int(counter.get('missed'))
        total_covered += int(counter.get('covered'))

total = total_missed + total_covered
current_pct = (total_covered / total) * 100

print(f"=== ANÁLISE DE COBERTURA ===")
print(f"Total de instruções: {total}")
print(f"Cobertas: {total_covered}")
print(f"Não cobertas: {total_missed}")
print(f"Cobertura atual: {current_pct:.2f}%")
print(f"\nPara atingir 95%: precisa cobrir {int(total * 0.95) - total_covered} instruções adicionais")
print(f"Para atingir 96%: precisa cobrir {int(total * 0.96) - total_covered} instruções adicionais")
print(f"Para atingir 97%: precisa cobrir {int(total * 0.97) - total_covered} instruções adicionais")

# Encontrar classes com baixa cobertura mas que não sejam triviais
print(f"\n=== TOP 30 CLASSES COM BAIXA COBERTURA (excluindo triviais) ===")

class_stats = []

for package in root.findall('package'):
    pkg_name = package.get('name')
    
    # Pular packages que não devemos testar
    skip_packages = ['config', 'repository', 'validation']
    if any(skip in pkg_name for skip in skip_packages):
        continue
    
    for clazz in package.findall('class'):
        class_name = clazz.get('name')
        
        # Pular classes triviais
        if 'Application' in class_name or 'Config' in class_name:
            continue
        if 'Repository' in class_name or 'Service.class' in class_name:
            continue
            
        missed = 0
        covered = 0
        
        for counter in clazz.findall('counter'):
            if counter.get('type') == 'INSTRUCTION':
                missed = int(counter.get('missed'))
                covered = int(counter.get('covered'))
                break
        
        total_class = missed + covered
        
        if total_class > 0:
            pct = (covered / total_class) * 100
            
            # Apenas classes com alguma instrução não coberta
            if missed > 0:
                class_stats.append({
                    'name': class_name.split('/')[-1],
                    'full_name': class_name,
                    'pct': pct,
                    'missed': missed,
                    'covered': covered,
                    'total': total_class
                })

# Ordenar por instruções não cobertas
class_stats.sort(key=lambda x: x['missed'], reverse=True)

for i, s in enumerate(class_stats[:30], 1):
    print(f"{i:2d}. {s['name']:50s} | {s['pct']:5.1f}% | Missed: {s['missed']:4d} | Total: {s['total']:4d}")
