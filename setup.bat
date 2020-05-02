@echo off
py scripts/mclib_retriever.py
echo remapping client jar
cd libs
del client-mapped.jar
cd ..
java -jar tiny-remapper.jar libs/client.jar libs/client-mapped.jar data/client.tiny proguard named
cd libs
del client.jar
rd /S /Q "client-sources/"
cd ..
echo decompiling minecraft
java -jar procyon.jar -jar libs/client-mapped.jar -o libs/client-sources/ --suppress-banner
