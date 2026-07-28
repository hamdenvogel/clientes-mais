
import os

def get_java_files(root_dir):
    java_files = set()
    for dirpath, _, filenames in os.walk(root_dir):
        for f in filenames:
            if f.endswith(".java") and "Application" not in f:
                # Store relative path from root, or just class name
                # Let's use class name for loose matching
                java_files.add(f.replace(".java", ""))
    return java_files

main_files = get_java_files("src/main/java")
test_files = get_java_files("src/test/java")

# Filter out main files that are typically excluded or don't need tests
# e.g. DTOs, Entities, Configs if excluded
# But script just prints diff
missing = []
for f in main_files:
    # Check if fTest or fIT exists
    if (f + "Test") not in test_files and (f + "IT") not in test_files:
         # Also check if it is an Exception, Enum, etc if we want to filter
         missing.append(f)

print("Files without tests:")
for m in sorted(missing):
    print(m)
