import os
import xml.etree.ElementTree as ET

def normalize_path(path):
    return path.replace('\\', '/').replace('/', '.')

def find_uncovered_files(src_dir, jacoco_xml):
    if not os.path.exists(jacoco_xml):
        print("JaCoCo XML not found.")
        return

    # 1. Get all source files
    source_files = set()
    for root, dirs, files in os.walk(src_dir):
        for file in files:
            if file.endswith(".java"):
                # Construct full class name (approximate)
                rel_path = os.path.relpath(os.path.join(root, file), src_dir)
                class_name = rel_path.replace(os.path.sep, '/').replace('.java', '')
                source_files.add(class_name)

    # 2. Parse JaCoCo XML
    tree = ET.parse(jacoco_xml)
    root = tree.getroot()
    
    covered_classes = {}
    
    for package in root.findall('package'):
        pkg_name = package.get('name') # e.org/example
        for clazz in package.findall('class'):
            model_class_name = clazz.get('name') # e.org/example/Foo
            
            # coverage info
            instruction = None
            for c in clazz.findall('counter'):
                if c.get('type') == 'INSTRUCTION':
                    instruction = c
                    break
            
            if instruction is not None:
                missed = int(instruction.get('missed'))
                covered = int(instruction.get('covered'))
                total = missed + covered
                
                if total > 0:
                     pct = (covered / total) * 100
                else:
                     pct = 0.0
                
                covered_classes[model_class_name] = pct

    # 3. Compare
    print(f"{'File':<80} | {'Status':<15}")
    print("-" * 100)
    
    missing_in_report = []
    zero_coverage = []
    
    for src in sorted(source_files):
        # src is like io/github/hvogel/clientes/rest/ClienteController
        if src in covered_classes:
            cov = covered_classes[src]
            if cov < 10.0: # Threshold for "low"
                zero_coverage.append((src, cov))
        else:
            # Check for inner classes or mismatches, but generally exact match expected for top level
            # Try to match simple name if package matches? 
            # JaCoCo uses slashes.
            missing_in_report.append(src)

    print(f"Found {len(missing_in_report)} files missing from report:")
    for f in missing_in_report:
        print(f"MISSING: {f}")

    print(f"\nFound {len(zero_coverage)} files with < 10% coverage:")
    for f, cov in zero_coverage:
        print(f"LOW COVERAGE ({cov:.1f}%): {f}")

if __name__ == "__main__":
    find_uncovered_files("src/main/java", "target/site/jacoco/jacoco.xml")
