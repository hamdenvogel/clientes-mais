import json
import re
import os

# Load the issues
with open('sonar_minor_issues.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Filter only MINOR severity issues
minor_issues = [i for i in data['issues'] if i['severity'] == 'MINOR']

print(f"Total MINOR issues to fix: {len(minor_issues)}")

# Group issues by file and rule
issues_by_file = {}
for issue in minor_issues:
    file_path = issue['component'].replace('clientes:', '')
    if file_path not in issues_by_file:
        issues_by_file[file_path] = []
    issues_by_file[file_path].append(issue)

# Fix issues
for file_path, issues in issues_by_file.items():
    full_path = os.path.join(os.getcwd(), file_path)
    
    if not os.path.exists(full_path):
        print(f"File not found: {full_path}")
        continue
    
    print(f"\nProcessing: {file_path}")
    
    with open(full_path, 'r', encoding='utf-8') as f:
        lines = f.readlines()
    
    modified = False
    
    for issue in sorted(issues, key=lambda x: x.get('line', 0), reverse=True):
        rule = issue['rule']
        line_num = issue.get('line', 0)
        message = issue['message']
        
        if line_num == 0 or line_num > len(lines):
            continue
        
        line_idx = line_num - 1
        original_line = lines[line_idx]
        new_line = original_line
        
        # S100: Method names should comply with a naming convention
        if rule == 'java:S100':
            # Rename method from snake_case to camelCase
            match = re.search(r'(\s+)(public|private|protected|static|final|void|[A-Z]\w+|<[^>]+>|\s)+\s+([a-z_]+[A-Z]\w*|[a-z]+_[a-z_]+)\s*\(', original_line)
            if match:
                method_name = match.group(3)
                # Convert snake_case to camelCase
                parts = method_name.split('_')
                camel_case_name = parts[0] + ''.join(word.capitalize() for word in parts[1:])
                new_line = original_line.replace(method_name + '(', camel_case_name + '(')
                print(f"  Line {line_num}: Renamed method '{method_name}' to '{camel_case_name}'")
        
        # S117: Local variable names should comply with a naming convention
        elif rule == 'java:S117':
            # Rename variable from snake_case to camelCase
            if 'Local variable names should comply' in message or 'should comply' in message:
                # Extract variable name from message
                match = re.search(r"'([^']+)'", message)
                if match:
                    old_name = match.group(1)
                    # Convert to camelCase
                    parts = old_name.split('_')
                    camel_case_name = parts[0] + ''.join(word.capitalize() for word in parts[1:])
                    
                    # Replace in the line
                    new_line = re.sub(r'\b' + re.escape(old_name) + r'\b', camel_case_name, original_line)
                    if new_line != original_line:
                        print(f"  Line {line_num}: Renamed variable '{old_name}' to '{camel_case_name}'")
        
        # S1264: A "while" loop should be used instead of a "for" loop
        elif rule == 'java:S1264':
            # Convert for loop to while loop
            match = re.search(r'for\s*\(\s*;\s*([^;]+)\s*;\s*\)', original_line)
            if match:
                condition = match.group(1)
                indent = re.match(r'(\s*)', original_line).group(1)
                new_line = f"{indent}while ({condition})\n"
                print(f"  Line {line_num}: Converted for loop to while loop")
        
        # S1612: Lambdas should be replaced with method references
        elif rule == 'java:S1612':
            # Replace lambda with method reference
            if '-> ' in original_line:
                # Common patterns
                new_line = re.sub(r'(\w+)\s*->\s*\1\.(\w+)\(\)', r'\2::apply', original_line)
                new_line = re.sub(r'(\w+)\s*->\s*(\w+)\.(\w+)\(\1\)', r'\2::\3', new_line)
                if new_line != original_line:
                    print(f"  Line {line_num}: Replaced lambda with method reference")
        
        # S1643: Use StringBuilder instead of String concatenation
        elif rule == 'java:S1643':
            print(f"  Line {line_num}: Needs manual fix - Use StringBuilder for string concatenation in loops")
            continue
        
        # S1905: Remove redundant type cast
        elif rule == 'java:S1905':
            # Remove unnecessary cast
            match = re.search(r'\((\w+(?:<[^>]+>)?)\)\s*(\w+)', original_line)
            if match:
                cast_type = match.group(1)
                var_name = match.group(2)
                new_line = original_line.replace(f'({cast_type}) {var_name}', var_name)
                new_line = new_line.replace(f'({cast_type}){var_name}', var_name)
                print(f"  Line {line_num}: Removed redundant cast")
        
        # S2293: Use diamond operator
        elif rule == 'java:S2293':
            # Replace new Type<X>() with new Type<>()
            new_line = re.sub(r'new\s+(\w+)<([^>]+)>\s*\(', r'new \1<>(', original_line)
            if new_line != original_line:
                print(f"  Line {line_num}: Used diamond operator")
        
        # S2386: Make public static fields final
        elif rule == 'java:S2386':
            if 'public static' in original_line and 'final' not in original_line:
                new_line = original_line.replace('public static', 'public static final')
                print(f"  Line {line_num}: Made public static field final")
        
        # S2786: Nested enums should not be declared static
        elif rule == 'java:S2786':
            if 'static enum' in original_line:
                new_line = original_line.replace('static enum', 'enum')
                print(f"  Line {line_num}: Removed static from nested enum")
        
        # S4719: String.replace should be used instead of replaceAll
        elif rule == 'java:S4719':
            if '.replaceAll(' in original_line:
                # Check if it's a simple string replacement (no regex)
                match = re.search(r'\.replaceAll\(([^,]+),\s*([^)]+)\)', original_line)
                if match:
                    new_line = original_line.replace('.replaceAll(', '.replace(')
                    print(f"  Line {line_num}: Changed replaceAll to replace")
        
        # S5411: Use existing Enum methods instead of custom ones
        elif rule == 'java:S5411':
            print(f"  Line {line_num}: Needs manual review - Use Enum.valueOf() or values() instead of custom method")
            continue
        
        if new_line != original_line:
            lines[line_idx] = new_line
            modified = True
    
    if modified:
        with open(full_path, 'w', encoding='utf-8') as f:
            f.writelines(lines)
        print(f"  ✓ File updated")

print("\n✓ Done! Please review the changes and run tests.")
