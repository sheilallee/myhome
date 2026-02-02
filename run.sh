#!/bin/bash

# Script de execução do MyHome (Multiplataforma via Maven Wrapper)
# Compila com Maven Wrapper e executa a aplicação

set -e

echo "╔════════════════════════════════════════════════════════╗"
echo "║              MyHome - Aplicação                        ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# Verificar se Maven Wrapper existe
if [ ! -f "./mvnw" ]; then
    echo "❌ Maven Wrapper não encontrado!"
    echo "   Execute: mvn wrapper:wrapper"
    exit 1
fi

# Dar permissão de execução ao wrapper (caso necessário)
chmod +x ./mvnw

# Compilar com Maven Wrapper
echo "🔨 Compilando com Maven Wrapper..."
./mvnw clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilação sucedida!"
    echo ""
    echo "🚀 Iniciando aplicação..."
    echo "════════════════════════════════════════════════════════"
    echo ""
    
    # Usar maven wrapper exec para incluir todas as dependências no classpath
    ./mvnw exec:java -Dexec.mainClass="com.myhome.Main" -q
else
    echo "❌ Erro na compilação!"
    exit 1
fi
