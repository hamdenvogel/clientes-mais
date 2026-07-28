import json

file_path = 'current_issues.json'

try:
    with open(file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
except Exception as e:
    print(f"Error reading file: {e}")
    exit(1)

critical_issues = data.get('critical', [])
print(f"Total Critical Issues: {len(critical_issues)}")

# Group by Rule
from collections import defaultdict
issues_by_rule = defaultdict(list)

for issue in critical_issues:
    rule = issue.get('rule')
    component = issue.get('component', '').split(':')[-1]
    message = issue.get('message')
    line = issue.get('line', '?')
    issues_by_rule[rule].append(f"{component}:{line} - {message}")

for rule, issues in issues_by_rule.items():
    print(f"\n--- Rule {rule} ({len(issues)}) ---")
    files = sorted(list(set(i.split(':')[0] for i in issues)))
    for f in files[:5]:
        print(f"  {f}")
    if len(files) > 5:
        print(f"  ... and {len(files)-5} more files")
