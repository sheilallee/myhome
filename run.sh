#!/bin/bash

# Script de execução do MyHome
# Compila com Maven (copia resources automaticamente)
# e executa direto sem problemas de Scanner

set -e

echo "╔════════════════════════════════════════════════════════╗"
echo "║              MyHome - Aplicação                        ║"
echo "╚════════════════════════════════════════════════════════╝"
echo ""

# Verificar se Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não encontrado! Instale com: sudo apt install maven"
    exit 1
fi

# Compilar com Maven
echo "🔨 Compilando com Maven..."
mvn clean compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilação sucedida!"
    echo ""
    echo "🚀 Iniciando aplicação..."
    echo "════════════════════════════════════════════════════════"
    echo ""
    
    # Executar direto (evita problemas do Maven com Scanner)
    java -cp target/classes com.myhome.Main
else
    echo "❌ Erro na compilação!"
    exit 1
fi
