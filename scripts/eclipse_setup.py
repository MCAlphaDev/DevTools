import os
import glob

def makeClasspathLine(jar):
    start = "\t<classpathentry kind=\"lib\" path=\"" + jar + '"'

    sources = jar[:-4] + "-sources.jar"
    if os.path.exists(sources):
        start += " sourcepath=\"" + sources + '"'

    return start + "/>"

if not os.path.exists("./.classpath"):
    with open("./data/classpath_base.txt", 'r') as file:
        file_data = file.read()

    libs = [os.path.normpath(jar).replace("\\", "/") for jar in glob.glob("./libs/*.jar")
             if os.path.basename(jar) != "client-mapped.jar" and not os.path.basename(jar).endswith("-sources.jar")]
    libs = "\n".join(map(makeClasspathLine, libs))
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
