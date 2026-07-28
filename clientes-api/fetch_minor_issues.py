import urllib.request
import json
import base64

url = "http://localhost:9000/api/issues/search?componentKeys=clientes&resolved=false&ps=100&severities=MINOR,INFO"
username = "sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956"
password = ""

request = urllib.request.Request(url)
base64string = base64.b64encode(f'{username}:{password}'.encode()).decode().replace('\n', '')
request.add_header("Authorization", "Basic %s" % base64string)

try:
    with urllib.request.urlopen(request) as response:
        data = json.loads(response.read().decode())
        with open('sonar_minor_issues.json', 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=4)
    print("Successfully fetched issues to sonar_minor_issues.json")
except Exception as e:
    print(f"Error fetching issues: {e}")
