javac tests/Tests.java
jar cvfm Tests.jar tests/manifest.txt .
del /f /s /q *.class
java -jar Tests.jar