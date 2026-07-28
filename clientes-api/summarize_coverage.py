import xml.etree.ElementTree as ET
import os

report_path = "target/site/jacoco/jacoco.xml"

if not os.path.exists(report_path):
    print("No coverage report found at", report_path)
    exit(0)

tree = ET.parse(report_path)
root = tree.getroot()

total_covered = 0
total_missed = 0

for counter in root.findall(".//counter"):
    if counter.get("type") == "INSTRUCTION":
        total_missed += int(counter.get("missed"))
        total_covered += int(counter.get("covered"))

total = total_missed + total_covered
if total > 0:
    coverage = (total_covered / total) * 100
    print(f"Overall Coverage: {coverage:.2f}%")
    
    print("\nTop 20 classes with low coverage (missed instructions):")
    class_stats = []
    
    for package in root.findall(".//package"):
        pkg_name = package.get("name")
        for cls in package.findall("class"):
            cls_name = cls.get("name")
            missed = 0
            covered = 0
            for counter in cls.findall("counter"):
                if counter.get("type") == "INSTRUCTION":
                    missed = int(counter.get("missed"))
                    covered = int(counter.get("covered"))
                    break
            
            total = missed + covered
            if total > 0:
                 pct = (covered / total) * 100
                 class_stats.append({
                     "name": f"{pkg_name}/{cls_name}",
                     "missed": missed,
                     "covered": covered,
                     "pct": pct
                 })

    # Sort by missed instructions descending
    class_stats.sort(key=lambda x: x["missed"], reverse=True)
    
    for s in class_stats[:20]:
         print(f"{s['name']}: {s['pct']:.1f}% ({s['missed']} missed)")
         
else:
    print("No instructions found.")
