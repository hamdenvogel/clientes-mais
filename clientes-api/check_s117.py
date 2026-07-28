import json
import os
import re

# Map of incorrect variable names to correct camelCase names
fixes = {
    'src/main/java/io/github/hvogel/clientes/model/repository/ItemPacoteRepository.java': [
        {'line': 35, 'old': 'idPacote', 'new': 'idPacote'},  # Already camelCase, check actual content
        {'line': 36, 'old': 'idServicoPrestado', 'new': 'idServicoPrestado'},  # Already camelCase
    ],
    'src/main/java/io/github/hvogel/clientes/model/repository/ServicoPrestadoRepository.java': [
        {'line': 54, 'old': 'idCliente', 'new': 'idCliente'},  # Already camelCase
    ],
}

# Let me check the actual content to see what variables need to be renamed
with open('sonar_minor_issues.json', encoding='utf-8') as f:
    data = json.load(f)

# Get S117 issues with their messages
s117_issues = [i for i in data['issues'] if i['rule'] == 'java:S117']

print(f"Found {len(s117_issues)} S117 issues\n")

for issue in s117_issues:
    file_path = issue['component'].replace('clientes:', '')
    line_num = issue.get('line')
    msg = issue['message']
    
    # Read the line to see what variable needs to be renamed
    if os.path.exists(file_path):
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
            if line_num > 0 and line_num <= len(lines):
                line_content = lines[line_num - 1].strip()
                print(f"{file_path}:{line_num}")
                print(f"  Message: {msg}")
                print(f"  Line: {line_content}")
                print()
