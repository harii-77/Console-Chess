@echo off
setlocal
set JAR=junit-platform-console-standalone-1.10.2.jar
set JAR_URL=https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.2/junit-platform-console-standalone-1.10.2.jar
set OUT=target\classes
set OUT_TEST=target\test-classes

if not exist "%JAR%" (
  echo Downloading JUnit Console Standalone...
  powershell -NoProfile -Command "Invoke-WebRequest -Uri '%JAR_URL%' -OutFile '%JAR%'"
  if errorlevel 1 (
    echo Failed to download JUnit Console. Ensure internet access.
    exit /b 1
  )
)

if not exist "%OUT%" mkdir "%OUT%"
if not exist "%OUT_TEST%" mkdir "%OUT_TEST%"

echo Compiling main sources...
javac -d "%OUT%" src\main\java\com\consolechess\*.java
if errorlevel 1 (
  echo Main compilation failed.
  exit /b 1
)

echo Compiling test sources...
javac -cp "%JAR%;%OUT%" -d "%OUT_TEST%" src\test\java\com\consolechess\*.java
if errorlevel 1 (
  echo Test compilation failed.
  exit /b 1
)

echo Running tests...
java -jar "%JAR%" -cp "%OUT%;%OUT_TEST%" --scan-classpath
endlocal
