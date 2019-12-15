from pylib import *
import urllib.request
import platform
import zipfile
import os
import re
import time

def download(file):
    loc = "./libs/" + file.split("/")[-1]
    downloadTo(file, loc)    

def downloadTo(file, loc):
    if (not os.path.exists(loc)):
        urllib.request.urlretrieve(file, loc)
        print("Downloaded " + file)
    else:
        print("File Already Exists: " + loc)

def useLibrary(rules):
    if len(rules) == 0: return True

    for rule in reversed(rules):
        if "os" in rule: # Check if the rule is OS specific
            osFilter = rule["os"]

            if "name" in osFilter and osFilter["name"] != osName:
                continue # This rule isn't for us

            if "version" in osFilter and re.search(osFilter["version"], osVersion) == None:
                continue # This rule isn't for us

        return rule["action"] == "allow"

    return False


if __name__ == "__main__":    
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
    osVersion = systemVersion # Probably doesn't need tweaking like the OS name does

    natives_type = "natives-" + osName


    data = jsonhelper.read_json("./data/manifest.json")
    data_libs = data["libraries"]

    if not os.path.exists("./libs"):
        os.makedirs("./libs")

    download(data["downloads"]["client"]["url"])

    for lib_entry in data_libs:
        if "rules" in lib_entry and not useLibrary(lib_entry["rules"]):
            continue # Not a library for our OS

        try:
            lib_entry_url = lib_entry["downloads"]["artifact"]["url"]
            download(lib_entry_url)

            lib_source_url = lib_entry_url[:-4] + "-sources.jar"
            download(lib_source_url)
        except:
            pass
        if "classifiers" in lib_entry["downloads"] and natives_type in lib_entry["downloads"]["classifiers"]:
            lib_natives_url = lib_entry["downloads"]["classifiers"][natives_type]["url"]
            lib_natives_name = "-".join(lib_entry["name"].split(":")[1:3])
            
            if "-platform" not in lib_natives_name:
                raise RuntimeError("Unexpected native library name: " + lib_natives_name)

            lib_natives = "./libs/" + lib_natives_name.replace("-platform", "")
            if not os.path.exists(lib_natives):
                os.makedirs(lib_natives)

            lib_natives_jar = lib_natives + "/native.jar"
            downloadTo(lib_natives_url, lib_natives_jar)

            exclusions = lib_entry["extract"]["exclude"]
            with zipfile.ZipFile(lib_natives_jar, "r") as zip:
                extractions = [name for name in zip.namelist() 
                                if not any([name.startswith(exclusion) for exclusion in exclusions])]
                zip.extractall(lib_natives, extractions)

    time.sleep(1)
