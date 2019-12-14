import os
import glob

if not os.path.exists("./.classpath"):
    with open("./data/classpath_base.txt", 'r') as file:
        file_data = file.read()

    libs = [os.path.normpath(jar).replace("\\", "/") for jar in glob.glob("./libs/*.jar")
             if os.path.basename(jar) != "client-mapped.jar"]
    libs = "\n".join(map(lambda jar: "\t<classpathentry kind=\"lib\" path=\"" + jar + "\"/>", libs))
    file_data = file_data.replace("**LIB CLASSPATH**", libs, 1)

    with open(".classpath", 'w+') as file:
        file.write(file_data)

if not os.path.exists("./.project"):
    with open("./data/project_base.txt", 'r') as file:
        file_data = file.read()

    with open(".project", 'w+') as file:
        file.write(file_data)

if not os.path.exists("./bin"):
    os.makedirs("./bin")

if not os.path.exists("./src"):
    os.makedirs("./src")
