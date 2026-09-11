@echo off
echo ==============================================
echo  Online Examination System Compiler and Runner
echo ==============================================
echo.

if not exist "bin" mkdir bin

echo Compiling source files...
javac -d bin -sourcepath src src/app/MainFrame.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)

echo Compilation successful.
echo Launching application...
start javaw -cp bin app.MainFrame
exit /b 0
