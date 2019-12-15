@echo off
py scripts/mclib_retriever.py
echo remapping client jar
java -jar tiny-remapper.jar libs/client.jar libs/client-mapped.jar data/client.tiny proguard named
cd libs
del client.jar
cd ..
echo decompiling minecraft
java -jar procyon.jar -jar libs/client-mapped.jar -o libs/client-sources/ --suppress-banner
