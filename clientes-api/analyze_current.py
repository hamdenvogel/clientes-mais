import json

with open('current_issues.json', 'r') as f:
    data = json.load(f)

print("=" * 80)
print("CRITICAL ISSUES (2):")
print("=" * 80)
for i, issue in enumerate(data['critical'], 1):
    print(f"\n{i}. Rule: {issue['rule']}")
    print(f"   File: {issue['component'].split(':')[-1]}")
    print(f"   Line: {issue.get('line', 'N/A')}")
    print(f"   Message: {issue['message']}")

print("\n" + "=" * 80)
print("MAJOR ISSUES (16):")
print("=" * 80)
for i, issue in enumerate(data['major'], 1):
    print(f"\n{i}. Rule: {issue['rule']}")
    print(f"   File: {issue['component'].split(':')[-1]}")
    print(f"   Line: {issue.get('line', 'N/A')}")
    print(f"   Message: {issue['message']}")
