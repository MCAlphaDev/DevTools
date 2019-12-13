@echo off
py scripts/mclib_retriever.py
java -jar tiny-remapper.jar libs/client.jar libs/client-mapped.jar data/client.tiny proguard named
cd libs
del client.jar
cd ..