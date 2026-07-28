import requests
import json

# Configurações do SonarQube
SONAR_URL = "http://localhost:9000"
PROJECT_KEY = "clientes"
TOKEN = "sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956"

# Headers com autenticação (Basic Auth com token como username e senha vazia)
import base64
auth_string = f"{TOKEN}:"
auth_bytes = auth_string.encode('ascii')
base64_bytes = base64.b64encode(auth_bytes)
base64_auth = base64_bytes.decode('ascii')

headers = {
    "Authorization": f"Basic {base64_auth}"
}

# Buscar todos os issues MINOR
url = f"{SONAR_URL}/api/issues/search"
params = {
    "componentKeys": PROJECT_KEY,
    "severities": "MINOR",
    "resolved": "false",
    "ps": 500
}

response = requests.get(url, headers=headers, params=params)
if response.status_code == 200:
    data = response.json()
    issues = data.get("issues", [])
    total = data.get("total", 0)
    
    print(f"Total de issues MINOR: {total}\n")
    
    # Agrupar por regra
    rules_count = {}
    rules_issues = {}
    
    for issue in issues:
        rule = issue.get("rule", "unknown")
        rules_count[rule] = rules_count.get(rule, 0) + 1
        if rule not in rules_issues:
            rules_issues[rule] = []
        rules_issues[rule].append(issue)
    
    # Ordenar por quantidade
    sorted_rules = sorted(rules_count.items(), key=lambda x: x[1], reverse=True)
    
    print("=== Resumo por Regra ===\n")
    for rule, count in sorted_rules:
        print(f"{rule}: {count}")
    
    # Salvar detalhes dos issues em arquivo
    with open("all_minor_issues_detail.json", "w", encoding="utf-8") as f:
        json.dump(rules_issues, f, indent=2, ensure_ascii=False)
    
    print("\n✅ Detalhes salvos em 'all_minor_issues_detail.json'")
    
    # Mostrar alguns exemplos de cada regra
    print("\n=== Exemplos por Regra ===\n")
    for rule, count in sorted_rules[:5]:  # Top 5
        print(f"\n{rule} ({count} issues):")
        for issue in rules_issues[rule][:3]:  # Primeiros 3 exemplos
            component = issue.get("component", "").split(":")[-1]
            line = issue.get("line", "?")
            message = issue.get("message", "")
            print(f"  - {component}:{line} - {message}")
    
else:
    print(f"Erro ao buscar issues: {response.status_code}")
    print(response.text)
