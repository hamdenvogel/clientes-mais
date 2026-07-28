import json
from collections import defaultdict

try:
    with open('remaining_issues.json', 'r', encoding='utf-16') as f:
        data = json.load(f)

    issues_by_severity = defaultdict(list)
    issues_by_rule = defaultdict(list)

    for issue in data['issues']:
        severity = issue['severity']
        rule = issue['rule']
        component = issue['component']
        message = issue['message']
        issues_by_severity[severity].append(f"{component}: {message}")
        issues_by_rule[rule].append(f"{component}: {message}")

    print(f"Total Issues: {len(data['issues'])}")
    
    print(f"\nBLOCKERS: {len(issues_by_severity['BLOCKER'])}")
    for item in issues_by_severity['BLOCKER']:
        print(f"  - {item}")

    print(f"\nCRITICALS: {len(issues_by_severity['CRITICAL'])}")
    
    print("\nTop Critical Rules:")
    sorted_rules = sorted(issues_by_rule.items(), key=lambda x: len(x[1]), reverse=True)
    for rule, items in sorted_rules[:5]:
        print(f"  {rule}: {len(items)} occurrences")
        # print first example
        if items:
            print(f"    Example: {items[0]}")

except Exception as e:
    print(f"Error parsing json: {e}")
