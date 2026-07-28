import csv
import sys
import os

def analyze_coverage(file_path):
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        return

    print(f"Analyzing coverage from: {file_path}")
    print(f"{'Package':<50} | {'Class':<40} | {'Coverage %':<10} | {'Missed Instructions':<20}")
    print("-" * 130)

    low_coverage_classes = []

    with open(file_path, 'r') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            missed = int(row['INSTRUCTION_MISSED'])
            covered = int(row['INSTRUCTION_COVERED'])
            total = missed + covered
            
            if total > 0:
                coverage = (covered / total) * 100
            else:
                coverage = 100.0 # No instructions to cover

            if coverage < 80 and total > 20: # Filter out trivial classes and those meeting target
                low_coverage_classes.append({
                    'package': row['PACKAGE'],
                    'class': row['CLASS'],
                    'coverage': coverage,
                    'missed': missed,
                    'total': total
                })

    # Sort by missed instructions (highest impact first)
    low_coverage_classes.sort(key=lambda x: x['missed'], reverse=True)

    for item in low_coverage_classes[:20]: # Show top 20
        print(f"{item['package']:<50} | {item['class']:<40} | {item['coverage']:<10.1f} | {item['missed']:<20}")

if __name__ == "__main__":
    analyze_coverage(r"c:\Hamden\Sistemas\Backend\clientes\des\clientes\target\site\jacoco\jacoco.csv")
