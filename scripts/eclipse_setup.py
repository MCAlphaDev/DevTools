import os

file_data = ""
with open("./data/classpath_base.txt", 'r') as file:
    file_data = file.read()

with open(".classpath", 'w+') as file:
    file.write(file_data)

with open("./data/project_base.txt", 'r') as file:
    file_data = file.read()

with open(".project", 'w+') as file:
    file.write(file_data)

if not os.path.exists("./bin"):
    os.makedirs("./bin")

if not os.path.exists("./src"):
    os.makedirs("./src")
