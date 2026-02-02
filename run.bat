@echo off
REM Script de execução do MyHome (Multiplataforma via Maven Wrapper)
REM Compila com Maven Wrapper e executa a aplicação

setlocal enabledelayedexpansion

echo ╔════════════════════════════════════════════════════════╗
echo ║              MyHome - Aplicação                        ║
echo ╚════════════════════════════════════════════════════════╝
echo.

REM Verificar se Maven Wrapper existe
if not exist "mvnw.cmd" (
    echo ❌ Maven Wrapper não encontrado!
    echo    Execute: mvn wrapper:wrapper
    pause
    exit /b 1
)

REM Compilar com Maven Wrapper
echo 🔨 Compilando com Maven Wrapper...
call mvnw.cmd clean compile -q

if %errorlevel% equ 0 (
    echo ✅ Compilação sucedida!
    echo.
    echo 🚀 Iniciando aplicação...
    echo ════════════════════════════════════════════════════════
    echo.
    
    REM Executar a aplicação
    call mvnw.cmd exec:java -Dexec.mainClass="com.myhome.Main" -q
) else (
    echo ❌ Erro na compilação!
    pause
    exit /b 1
)
