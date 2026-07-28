import urllib.request
import json
import base64

# Configuration
SONAR_URL = 'http://localhost:9000'
PROJECT_KEY = 'clientes'
TOKEN = 'sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956'

def get_issues_by_rule(rule):
    url = f"{SONAR_URL}/api/issues/search?componentKeys={PROJECT_KEY}&rules={rule}&resolved=false&ps=500"
    
    # Basic Auth
    auth_str = f"{TOKEN}:"
    b64_auth = base64.b64encode(auth_str.encode()).decode()
    
    req = urllib.request.Request(url)
    req.add_header("Authorization", f"Basic {b64_auth}")
    
    try:
        with urllib.request.urlopen(req) as response:
            if response.status != 200:
                print(f"Failed to fetch {rule} issues: {response.status}")
                return []
            data = json.loads(response.read().decode())
            return data.get('issues', [])
    except Exception as e:
        print(f"Error fetching {rule}: {e}")
        return []

# Get S1488 issues
issues = get_issues_by_rule('java:S1488')
print(f"Found {len(issues)} S1488 issues\n")

for i, issue in enumerate(issues, 1):
    component = issue.get('component', '')
    file_path = component.split(':')[-1] if ':' in component else component
    line = issue.get('line', 'N/A')
    message = issue.get('message', '')
    
    print(f"{i}. File: {file_path}")
    print(f"   Line: {line}")
    print(f"   Message: {message}\n")
