import json
import os

file_path = 'issues_critical.json'

try:
    with open(file_path, 'r', encoding='utf-16') as f:
        data = json.load(f)
except Exception:
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
    except Exception as e:
        print(f"Error reading file: {e}")
        exit(1)

issues = data.get('issues', [])
print(f"Total Critical Issues: {len(issues)}")

for issue in issues:
    component = issue.get('component', '').split(':')[-1]
    message = issue.get('message')
    line = issue.get('line')
    rule = issue.get('rule')
    print(f"[{rule}] {component}:{line} - {message}")
