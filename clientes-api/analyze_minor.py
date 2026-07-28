import json

with open('sonar_minor_issues.json', encoding='utf-8') as f:
    data = json.load(f)

rules = {}
for issue in data['issues']:
    if issue['severity'] in ['MINOR', 'INFO']:
        rule = issue['rule']
        if rule not in rules:
            rules[rule] = []
        rules[rule].append({
            'file': issue['component'].replace('clientes:', ''),
            'line': issue.get('line'),
            'msg': issue['message']
        })

for rule, issues in sorted(rules.items(), key=lambda x: -len(x[1])):
    print(f"\n{rule} ({len(issues)} issues):")
    for iss in issues[:10]:
        print(f"  {iss['file']}:{iss['line']} - {iss['msg']}")
