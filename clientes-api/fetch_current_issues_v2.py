import urllib.request
import json
import base64

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
                print(f"Failed to fetch {severity} issues: {response.status} - {response.read().decode()}")
                return []
            data = json.loads(response.read().decode())
            return data.get('issues', [])
    except Exception as e:
        print(f"Error fetching {severity}: {e}")
        return []

def main():
    print("Fetching Critical issues...")
    critical_issues = get_issues('CRITICAL')
    print(f"Found {len(critical_issues)} Critical issues.")
    
    print("Fetching Major issues...")
    major_issues = get_issues('MAJOR')
    print(f"Found {len(major_issues)} Major issues.")

    print("Fetching Minor issues...")
    minor_issues = get_issues('MINOR')
    print(f"Found {len(minor_issues)} Minor issues.")
    
    all_issues = {
        'critical': critical_issues,
        'major': major_issues,
        'minor': minor_issues
    }
    
    with open('current_issues.json', 'w') as f:
        json.dump(all_issues, f, indent=4)
        
    print("Saved to current_issues.json")
    
    # Print a text summary
    issues = critical_issues + major_issues + minor_issues # Combine all fetched issues for summary
    print(f"Total issues found: {len(issues)}")

    count = 0
    for issue in issues:
        rule = issue.get('rule', '')
        if rule == 'java:S1192':
             continue
        component = issue.get('component', '')
        message = issue.get('message', '')
        print(f"[{rule}] {component} - {message}")
        count += 1
        if count >= 50:
            break

if __name__ == "__main__":
    main()
