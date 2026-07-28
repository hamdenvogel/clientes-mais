import json
from collections import defaultdict
import sys

def analyze_issues(filename):
    try:
        with open(filename, 'r', encoding='utf-8') as f: # Try utf-8 first
            data = json.load(f)
    except UnicodeDecodeError:
        with open(filename, 'r', encoding='utf-16') as f: # Fallback to utf-16
            data = json.load(f)
    except Exception as e:
        print(f"Error reading {filename}: {e}")
        return

    print(f"--- Analysis of {filename} ---")
    print(f"Total Issues: {len(data['issues'])}")
    
    issues_by_rule = defaultdict(list)
    for issue in data['issues']:
        rule = issue['rule']
        component = issue['component']
        message = issue['message']
        issues_by_rule[rule].append(f"{component}: {message}")

    sorted_rules = sorted(issues_by_rule.items(), key=lambda x: len(x[1]), reverse=True)
    
    for rule, items in sorted_rules:
        print(f"\nRule: {rule} ({len(items)} occurrences)")
        # Show first example to understand the rule
        if items:
            print(f"  Example: {items[0]}")

if __name__ == "__main__":
    analyze_issues('issues_critical.json')
    analyze_issues('issues_major.json')
    # analyze_issues('issues_minor.json') # Skip minor for now to avoid too much output
