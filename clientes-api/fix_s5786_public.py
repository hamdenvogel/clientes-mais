import json
import os
import re

# Load the sonar issues
with open('sonar_minor_issues.json', encoding='utf-8') as f:
    data = json.load(f)

# Filter S5786 issues (public modifier in JUnit tests)
s5786_issues = [i for i in data['issues'] if i['rule'] == 'java:S5786']

# Group by file
files_to_fix = {}
for issue in s5786_issues:
    file_path = issue['component'].replace('clientes:', '')
    line_num = issue.get('line')
    if file_path not in files_to_fix:
        files_to_fix[file_path] = []
    files_to_fix[file_path].append(line_num)

print(f"Found {len(s5786_issues)} S5786 issues in {len(files_to_fix)} files")

# Fix each file
for file_path, lines in files_to_fix.items():
    full_path = file_path
    if not os.path.exists(full_path):
        print(f"Warning: File not found: {full_path}")
        continue
    
    with open(full_path, 'r', encoding='utf-8') as f:
        content = f.read()
        original_content = content
    
    # Remove 'public ' from test methods (lines are sorted in reverse to avoid line number shifts)
    lines_sorted = sorted(lines, reverse=True)
    content_lines = content.split('\n')
    
    for line_num in lines_sorted:
        if line_num > 0 and line_num <= len(content_lines):
            line = content_lines[line_num - 1]
            # Remove 'public ' modifier
            new_line = re.sub(r'\bpublic\s+', '', line, count=1)
            content_lines[line_num - 1] = new_line
    
    new_content = '\n'.join(content_lines)
    
    if new_content != original_content:
        with open(full_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"✓ Fixed {len(lines)} issues in {file_path}")
    else:
        print(f"- No changes needed in {file_path}")

print("\nDone!")
