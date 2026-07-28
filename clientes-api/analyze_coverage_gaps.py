import xml.etree.ElementTree as ET
import os
import csv

def parse_jacoco_xml(file_path):
    if not os.path.exists(file_path):
        print(f"File not found: {file_path}")
        return

    tree = ET.parse(file_path)
    root = tree.getroot()

    classes = []

    for package in root.findall('package'):
        package_name = package.get('name')
        for clazz in package.findall('class'):
            class_name = clazz.get('name')
            
            # Skip DTOs? Maybe not if we want 90%. But usually skip logic-less classes.
            # Let's inspect everything for now.
            
            instruction_counter = None
            for counter in clazz.findall('counter'):
                if counter.get('type') == 'INSTRUCTION':
                    instruction_counter = counter
                    break
            
            if instruction_counter is not None:
                missed = int(instruction_counter.get('missed'))
                covered = int(instruction_counter.get('covered'))
                total = missed + covered
                
                if total > 0:
                    percentage = (covered / total) * 100
                    classes.append({
                        'package': package_name,
                        'class': class_name,
                        'missed': missed,
                        'covered': covered,
                        'total': total,
                        'percentage': percentage
                    })

    # Sort by missed instructions (descending)
    classes.sort(key=lambda x: x['missed'], reverse=True)

    print(f"{'Class':<60} | {'Missed':<10} | {'Total':<10} | {'Coverage':<10}")
    print("-" * 100)
    for c in classes[:30]:
        print(f"{c['class']:<60} | {c['missed']:<10} | {c['total']:<10} | {c['percentage']:.2f}%")

    total_missed = sum(c['missed'] for c in classes)
    total_covered = sum(c['covered'] for c in classes)
    total_instructions = total_missed + total_covered
    if total_instructions > 0:
        overall_coverage = (total_covered / total_instructions) * 100
        print("-" * 100)
        print(f"Overall Coverage: {overall_coverage:.2f}%")

if __name__ == "__main__":
    parse_jacoco_xml("target/site/jacoco/jacoco.xml")
