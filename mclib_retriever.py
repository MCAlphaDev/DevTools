from pylib import *
import urllib.request
import os

def download(file):
    loc = "./libs/" + file.split("/")[-1]
    if (not os.path.exists(loc)):
        urllib.request.urlretrieve(file, loc)
        print("Downloaded " + file)
    else:
        print("File Already Exists: " + loc)

data = jsonhelper.read_json("./manifest.json")
data_libs = data["libraries"]

if not os.path.exists("./libs"):
    os.makedirs("./libs")

download(data["downloads"]["client"]["url"])

for lib_entry in data_libs:
    lib_entry_url = lib_entry["downloads"]["artifact"]["url"]
    download(lib_entry_url)
    try:
        lib_natives_url = lib_entry["downloads"]["classifiers"]["natives-windows"]["url"]
        download(lib_natives_url)
    except:
        print("Either natives do not exist for: " + lib_entry_url.split("/")[-1] + ", or there was an error parsing the json")

