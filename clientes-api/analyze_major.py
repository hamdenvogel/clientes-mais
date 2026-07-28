import json
from collections import defaultdict

def main():
    try:
        with open('current_issues.json', 'r') as f:
            data = json.load(f)
            
        major_issues = data.get('major', [])
        
        rule_counts = defaultdict(int)
        rule_files = defaultdict(list)
        
        print(f"Total Major Issues: {len(major_issues)}")
        
        for issue in major_issues:
            rule = issue.get('rule')
            component = issue.get('component', '').split(':')[-1] # Remove project key prefix
            message = issue.get('message')
            
            rule_counts[rule] += 1
            if component not in rule_files[rule]:
                rule_files[rule].append(component)
                
        # Sort by count desc
        sorted_rules = sorted(rule_counts.items(), key=lambda x: x[1], reverse=True)
        
        print("\nMajor Issues by Rule:")
        for rule, count in sorted_rules:
            print(f"{rule}: {count} issues")
            # List first few files
            files = rule_files[rule]
            for f in files[:5]:
                print(f"  - {f}")
            if len(files) > 5:
                print(f"  - ... ({len(files) - 5} more)")
            print()
            
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    main()
