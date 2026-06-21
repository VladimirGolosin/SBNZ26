@echo off

echo =========================
echo BUILD STARTING
echo =========================

echo.
echo Building MODEL...
cd model
call mvn clean install
if errorlevel 1 goto error

cd ..

echo.
echo Building KJAR...
cd kjar
call mvn clean install
if errorlevel 1 goto error

cd ..

echo.
echo Building SERVICE...
cd service
call mvn clean install
if errorlevel 1 goto error

cd ..

echo.
echo =========================
echo DONE - all modules built successfully
echo =========================

goto end

:error
echo.
echo =========================
echo BUILD FAILED
echo =========================

:end
echo.
