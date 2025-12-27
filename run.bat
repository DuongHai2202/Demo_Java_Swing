@echo off
echo ========================================
echo ODIN Language Center Management System
echo ========================================
echo.

REM Kiểm tra bin folder
if not exist "bin" (
    echo ERROR: Compiled classes not found!
    echo Please run compile.bat first
    pause
    exit /b 1
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
    echo Please run compile.bat first or download MySQL Connector
    pause
    exit /b 1
)

echo Found MySQL Connector: %MYSQL_JAR%
echo Starting application...
echo.

echo Found FlatLaf: %FLATLAF_JAR%
java -cp "bin;%MYSQL_JAR%;%FLATLAF_JAR%" Main

pause
