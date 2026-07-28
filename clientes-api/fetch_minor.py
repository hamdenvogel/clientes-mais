import urllib.request
import json
import base64
from collections import Counter

# Configuration
SONAR_URL = 'http://localhost:9000'
PROJECT_KEY = 'clientes'
TOKEN = 'sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956'

def get_issues(severity):
    url = f"{SONAR_URL}/api/issues/search?componentKeys={PROJECT_KEY}&severities={severity}&resolved=false&ps=500"
    
    # Basic Auth
    auth_str = f"{TOKEN}:"
    b64_auth = base64.b64encode(auth_str.encode()).decode()
    
    req = urllib.request.Request(url)
    req.add_header("Authorization", f"Basic {b64_auth}")
    
    try:
        with urllib.request.urlopen(req) as response:
            if response.status != 200:
                print(f"Failed to fetch {severity} issues: {response.status}")
                return []
            data = json.loads(response.read().decode())
            return data.get('issues', [])
    except Exception as e:
        print(f"Error fetching {severity}: {e}")
        return []

def main():
    print("Fetching MINOR issues...")
    minor_issues = get_issues('MINOR')
    print(f"Found {len(minor_issues)} MINOR issues.")
    
    # Count by rule
    rule_counts = Counter(issue.get('rule') for issue in minor_issues)
    
    print("\n" + "=" * 80)
    print("MINOR Issues by Rule:")
    print("=" * 80)
    for rule, count in rule_counts.most_common():
        print(f"{rule}: {count}")
    
    print("\n" + "=" * 80)
    print("Top 20 MINOR Issues:")
    print("=" * 80)
    
    for i, issue in enumerate(minor_issues[:20], 1):
        rule = issue.get('rule', '')
        component = issue.get('component', '')
        file_path = component.split(':')[-1] if ':' in component else component
        line = issue.get('line', 'N/A')
        message = issue.get('message', '')
        
        print(f"\n{i}. Rule: {rule}")
        print(f"   File: {file_path}")
        print(f"   Line: {line}")
        print(f"   Message: {message}")

if __name__ == "__main__":
    main()
