from pylib import *
import urllib.request
import platform
import os
import re
import time

systemName = platform.system()
if systemName == "Linux":
    osName = "linux"
elif systemName == "Darwin":
    osName = "osx"
elif systemName == "Windows":
    osName = "windows"
else:
    raise RuntimeError("Not sure which OS we're running on: " + systemName)
systemVersion = platform.release()
osVersion = systemVersion #Probably doesn't need tweaking like the OS name does

natives_type = "natives-" + osName

def download(file):
    loc = "./libs/" + file.split("/")[-1]
    if (not os.path.exists(loc)):
        urllib.request.urlretrieve(file, loc)
        print("Downloaded " + file)
    else:
        print("File Already Exists: " + loc)

data = jsonhelper.read_json("./data/manifest.json")
data_libs = data["libraries"]

if not os.path.exists("./libs"):
    os.makedirs("./libs")

download(data["downloads"]["client"]["url"])

def useLibrary(rules):
    if len(rules) == 0: return True

    for rule in reversed(rules):
        if "os" in rule: #Check if the rule is OS specific
            osFilter = rule["os"]

            if "name" in osFilter and osFilter["name"] != osName:
                continue #This rule isn't for us

            if "version" in osFilter and re.search(osFilter["version"], osVersion) == None:
                continue #This rule isn't for us

        return rule["action"] == "allow"

    return False

for lib_entry in data_libs:
    if "rules" in lib_entry and not useLibrary(lib_entry["rules"]):
        continue #Not a library for our OS

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
