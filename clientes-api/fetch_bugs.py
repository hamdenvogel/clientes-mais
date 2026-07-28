import urllib.request
import json
import base64

# Configuration
SONAR_URL = 'http://localhost:9000'
PROJECT_KEY = 'clientes'
TOKEN = 'sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956'

def get_issues(issue_type):
    url = f"{SONAR_URL}/api/issues/search?componentKeys={PROJECT_KEY}&types={issue_type}&resolved=false&ps=500"
    
    # Basic Auth
    auth_str = f"{TOKEN}:"
    b64_auth = base64.b64encode(auth_str.encode()).decode()
    
    req = urllib.request.Request(url)
    req.add_header("Authorization", f"Basic {b64_auth}")
    
    try:
        with urllib.request.urlopen(req) as response:
            if response.status != 200:
                print(f"Failed to fetch {issue_type} issues: {response.status}")
                return []
            data = json.loads(response.read().decode())
            return data.get('issues', [])
    except Exception as e:
        print(f"Error fetching {issue_type}: {e}")
        return []

def main():
    print("=" * 80)
    print("Fetching BUG issues...")
    bug_issues = get_issues('BUG')
    print(f"Found {len(bug_issues)} BUG issues.")
    print("=" * 80)
    
    for i, issue in enumerate(bug_issues, 1):
        rule = issue.get('rule', '')
        component = issue.get('component', '')
        file_path = component.split(':')[-1] if ':' in component else component
        line = issue.get('line', 'N/A')
        message = issue.get('message', '')
        severity = issue.get('severity', '')
        
        print(f"\n{i}. [{severity}] Rule: {rule}")
        print(f"   File: {file_path}")
        print(f"   Line: {line}")
        print(f"   Message: {message}")
    
    print("\n" + "=" * 80)
    print("Fetching CODE_SMELL issues...")
    code_smell_issues = get_issues('CODE_SMELL')
    print(f"Found {len(code_smell_issues)} CODE_SMELL issues.")
    print("=" * 80)
    
    # Show first 10 code smells
    for i, issue in enumerate(code_smell_issues[:10], 1):
        rule = issue.get('rule', '')
        component = issue.get('component', '')
        file_path = component.split(':')[-1] if ':' in component else component
        line = issue.get('line', 'N/A')
        message = issue.get('message', '')
        severity = issue.get('severity', '')
        
        print(f"\n{i}. [{severity}] Rule: {rule}")
        print(f"   File: {file_path}")
        print(f"   Line: {line}")
        print(f"   Message: {message}")

if __name__ == "__main__":
    main()
