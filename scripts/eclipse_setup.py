from pylib import jsonhelper
from mclib_retriever import downloadTo
import os
import glob

def makeClasspathLine(jar):
    start = "\t<classpathentry kind=\"lib\" path=\"" + jar + '"'

    sources = jar[:-4] + "-sources.jar"
    if os.path.exists(sources):
        start += " sourcepath=\"" + sources + '"'

    native = os.path.abspath("./libs/" + os.path.basename(jar)[:-4])
    if os.path.isdir(native):
        return start + ">\n\t\t<attributes>\n\t\t\t<attribute name=\"org.eclipse.jdt.launching.CLASSPATH_ATTR_LIBRARY_PATH_ENTRY\" value=\""\
                    + native.replace("\\", "/") + "\"/>\n\t\t</attributes>\n\t</classpathentry>"
    else:
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

if not os.path.exists("./assets"):
    os.makedirs("./assets")

    manifest = jsonhelper.read_json("./data/manifest.json")
    assetURL = manifest["assetIndex"]["url"]
    downloadTo(assetURL, "./assets/assetIndex.json")

    assets = jsonhelper.read_json("./assets/assetIndex.json")
    for name, info in assets["objects"].items():
        destination = "./assets/" + name

        if not os.path.exists(os.path.dirname(destination)):
            os.makedirs(os.path.dirname(destination))

        hash = info["hash"]
        downloadTo("http://resources.download.minecraft.net/" + hash[0:2] + "/" + hash, destination)