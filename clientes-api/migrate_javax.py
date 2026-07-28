import os

def replace_in_file(filepath):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    new_content = content.replace('javax.persistence', 'jakarta.persistence')
    new_content = new_content.replace('javax.validation', 'jakarta.validation')
    new_content = new_content.replace('javax.servlet', 'jakarta.servlet')
    new_content = new_content.replace('javax.annotation', 'jakarta.annotation')
    
    if content != new_content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated: {filepath}")

def main():
    start_dir = r"c:\Hamden\Sistemas\Backend\clientes\des\clientes\src\main\java"
    for root, dirs, files in os.walk(start_dir):
        for file in files:
            if file.endswith(".java"):
                replace_in_file(os.path.join(root, file))

if __name__ == "__main__":
    main()
