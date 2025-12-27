@echo off
echo ========================================
echo ODIN Language Center - Compile Script
echo ========================================
echo.

REM Kiểm tra thư mục bin
if not exist "bin" (
    echo Creating bin directory...
    mkdir bin
)

REM Tìm MySQL Connector JAR trong thư mục lib
set MYSQL_JAR=
for %%f in (lib\mysql-connector*.jar) do (
    set MYSQL_JAR=%%f
    goto :found_mysql
)

:found_mysql

REM Find FlatLaf JAR
set FLATLAF_JAR=
for %%f in (lib\flatlaf*.jar) do (
    set FLATLAF_JAR=%%f
    goto :found_flatlaf
)

:found_flatlaf
if "%MYSQL_JAR%"=="" (
    echo ERROR: MySQL Connector JAR not found in lib folder!
    echo.
    echo Please download MySQL Connector/J and place it in the lib folder
    echo Download from: https://dev.mysql.com/downloads/connector/j/
    echo Or direct link: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
    echo.
    echo File name should be: mysql-connector-*.jar
    pause
    exit /b 1
)

echo Found MySQL Connector: %MYSQL_JAR%
echo Compiling Java files...
echo.

echo Found FlatLaf: %FLATLAF_JAR%
javac -cp ".;%MYSQL_JAR%;%FLATLAF_JAR%" -d bin -sourcepath src src\Main.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
    echo.
    echo To run the application, execute:
    echo .\run.bat  ^(in PowerShell^)
    echo or
    echo run.bat    ^(in Command Prompt^)
    echo.
    pause
    exit /b 0
) else (
    echo.
    echo ========================================
    echo Compilation failed!
    echo ========================================
    echo Please check the errors above
    echo.
    pause
    exit /b 1
)
