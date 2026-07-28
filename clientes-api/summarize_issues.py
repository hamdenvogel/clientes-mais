import json
import os

def summarize(filename):
    print(f"Summarizing {filename}...")
    if not os.path.exists(filename):
        print("File not found.")
        return

    data = None
    # Try different encodings
    for enc in ['utf-16', 'utf-16le', 'utf-8', 'cp1252']:
        try:
            with open(filename, 'r', encoding=enc) as f:
                 content = f.read()
                 # sometimes BOM issues
                 if content.startswith(u'\ufeff'):
                     content = content[1:]
                 data = json.loads(content)
            print(f"Successfully read with {enc}")
            break
        except Exception:
            continue
            
    if data is None:
        print(f"Could not read {filename}")
        return

    issues = data.get('issues', []) if isinstance(data, dict) else data
    
    print(f"Total issues: {len(issues)}")
    
    by_rule = {}
    for i in issues:
        rule = i.get('rule', 'unknown')
        msg = i.get('message', '')
        component = i.get('component', '').split(':')[-1]
        line = i.get('line', 0)
        
        if rule not in by_rule:
             by_rule[rule] = []
        by_rule[rule].append(f"{component}:{line} - {msg}")

    for rule, items in by_rule.items():
        print(f"\nRule: {rule}")
        for item in items[:5]: 
             print(f"  {item}")
        if len(items) > 5:
            print(f"  ... and {len(items)-5} more")


summarize('sonar_minor_issues.json')
