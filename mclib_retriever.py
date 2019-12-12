from pylib import *
import urllib.request
import os
import time

#todo other natives, detect os
natives_type = "natives-windows"

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
    try:
        lib_entry_url = lib_entry["downloads"]["artifact"]["url"]
        download(lib_entry_url)
    except:
        pass
    try:
        lib_natives_url = lib_entry["downloads"]["classifiers"][natives_type]["url"]
        download(lib_natives_url)
    except:
        pass

time.sleep(1)
