import csv
import os

file_path = r'c:\Hamden\Sistemas\Backend\clientes\des\clientes\target\site\jacoco\jacoco.csv'

if not os.path.exists(file_path):
    print("JaCoCo CSV not found.")
    exit(1)

with open(file_path, 'r') as f:
    reader = csv.DictReader(f)
    data = []
    for row in reader:
        missed = int(row['INSTRUCTION_MISSED'])
        covered = int(row['INSTRUCTION_COVERED'])
        total = missed + covered
        coverage = (covered / total * 100) if total > 0 else 100
        data.append({
            'package': row['PACKAGE'],
            'class': row['CLASS'],
            'missed': missed,
            'coverage': coverage
        })

# Sort by missed instructions desc
data.sort(key=lambda x: x['missed'], reverse=True)

print(f"{'Class':<50} | {'Missed':<10} | {'Coverage %':<10}")
print("-" * 80)
for item in data[:20]:
    full_name = f"{item['package']}.{item['class']}"
    # shorten package for display
    short_name = item['class']
    print(f"{short_name:<50} | {item['missed']:<10} | {item['coverage']:.1f}")
