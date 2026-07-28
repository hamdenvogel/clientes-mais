import requests
import json
import base64

# Configuration
SONAR_URL = 'http://localhost:9000'
PROJECT_KEY = 'clientes'
TOKEN = 'sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956'

def get_issues(severity):
    url = f"{SONAR_URL}/api/issues/search"
    params = {
        'componentKeys': PROJECT_KEY,
        'severities': severity,
        'resolved': 'false',
        'ps': 500  # Page size
    }
    
    # Basic Auth using token (token is username, password is empty)
    auth = (TOKEN, '')
    
    response = requests.get(url, params=params, auth=auth)
    
    if response.status_code != 200:
        print(f"Failed to fetch {severity} issues: {response.status_code} - {response.text}")
        return []
        
    return response.json().get('issues', [])

def main():
    print("Fetching Critical issues...")
    critical_issues = get_issues('CRITICAL')
    print(f"Found {len(critical_issues)} Critical issues.")
    
    print("Fetching Major issues...")
    major_issues = get_issues('MAJOR')
    print(f"Found {len(major_issues)} Major issues.")
    
    all_issues = {
        'critical': critical_issues,
        'major': major_issues
    }
    
    with open('current_issues.json', 'w') as f:
        json.dump(all_issues, f, indent=4)
        
    print("Saved to current_issues.json")
    
    # Print a text summary
    print("\n--- CRITICAL ISSUES SPLIT ---")
    for issue in critical_issues:
        print(f"[{issue['rule']}] {issue['component'].split(':')[-1]}:{issue.get('line', '?')} - {issue['message']}")

if __name__ == "__main__":
    main()
