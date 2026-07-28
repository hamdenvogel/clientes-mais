import json

with open('sonar_minor_issues.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

issues = [i for i in data['issues'] if i['severity'] == 'MINOR']

print(f"Total MINOR issues: {len(issues)}\n")

for issue in issues:
    file_path = issue['component'].replace('clientes:', '')
    line = issue.get('line', 'N/A')
    rule = issue['rule']
    message = issue['message']
    print(f"{file_path}:{line} - {rule} - {message}")
