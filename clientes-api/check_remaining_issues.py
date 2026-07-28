import requests
import json

# Configurações do SonarQube
SONAR_URL = "http://localhost:9000"
PROJECT_KEY = "clientes"
TOKEN = "sqp_ae579a1a6cf6638dcf1ab52a27a234f5adc74956"

# Headers com autenticação
headers = {
    "Authorization": f"Bearer {TOKEN}"
}

# Buscar issues por severidade
def get_issues_by_severity(severity):
    url = f"{SONAR_URL}/api/issues/search"
    params = {
        "componentKeys": PROJECT_KEY,
        "severities": severity,
        "resolved": "false",
        "ps": 500  # page size
    }
    
    response = requests.get(url, headers=headers, params=params)
    if response.status_code == 200:
        data = response.json()
        return data.get("total", 0), data.get("issues", [])
    else:
        print(f"Erro ao buscar issues: {response.status_code}")
        return 0, []

# Buscar issues por tipo
def get_issues_by_type(issue_type):
    url = f"{SONAR_URL}/api/issues/search"
    params = {
        "componentKeys": PROJECT_KEY,
        "types": issue_type,
        "resolved": "false",
        "ps": 500
    }
    
    response = requests.get(url, headers=headers, params=params)
    if response.status_code == 200:
        data = response.json()
        return data.get("total", 0), data.get("issues", [])
    else:
        print(f"Erro ao buscar issues: {response.status_code}")
        return 0, []

print("=== Resumo dos Issues do SonarQube ===\n")

# Issues por severidade
for severity in ["BLOCKER", "CRITICAL", "MAJOR", "MINOR", "INFO"]:
    total, _ = get_issues_by_severity(severity)
    if total > 0:
        print(f"{severity}: {total}")

print("\n=== Issues por Tipo ===\n")

# Issues por tipo
for issue_type in ["BUG", "VULNERABILITY", "CODE_SMELL"]:
    total, issues = get_issues_by_type(issue_type)
    if total > 0:
        print(f"{issue_type}: {total}")

# Verificar especificamente os S1488 que foram corrigidos
print("\n=== Verificando S1488 (corrigidos) ===\n")
url = f"{SONAR_URL}/api/issues/search"
params = {
    "componentKeys": PROJECT_KEY,
    "rules": "java:S1488",
    "resolved": "false",
    "ps": 500
}

response = requests.get(url, headers=headers, params=params)
if response.status_code == 200:
    data = response.json()
    total_s1488 = data.get("total", 0)
    print(f"S1488 (immediately returned variable): {total_s1488}")
    if total_s1488 == 0:
        print("✅ Todos os issues S1488 foram corrigidos!")
else:
    print(f"Erro ao verificar S1488: {response.status_code}")

# Issues MINOR restantes por regra
print("\n=== MINOR Issues por Regra ===\n")
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
    
    # Agrupar por regra
    rules_count = {}
    for issue in issues:
        rule = issue.get("rule", "unknown")
        rules_count[rule] = rules_count.get(rule, 0) + 1
    
    # Ordenar por quantidade
    sorted_rules = sorted(rules_count.items(), key=lambda x: x[1], reverse=True)
    
    for rule, count in sorted_rules:
        print(f"{rule}: {count}")
    
    print(f"\nTotal MINOR: {data.get('total', 0)}")

print("\n=== Cobertura de Testes ===\n")
url = f"{SONAR_URL}/api/measures/component"
params = {
    "component": PROJECT_KEY,
    "metricKeys": "coverage,line_coverage,branch_coverage"
}

response = requests.get(url, headers=headers, params=params)
if response.status_code == 200:
    data = response.json()
    measures = data.get("component", {}).get("measures", [])
    for measure in measures:
        metric = measure.get("metric")
        value = measure.get("value")
        print(f"{metric}: {value}%")
