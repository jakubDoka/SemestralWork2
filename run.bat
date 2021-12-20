@echo off
javac github/com/jakubDoka/directions/Main.java
jar cvfm Main.jar github/com/jakubDoka/directions/manifest.txt .
del /f /s /q *.class
java -jar Main.jar