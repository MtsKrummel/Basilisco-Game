#!/bin/sh
# Uso: ./ejecutar.sh [paquete.Clase]   (por defecto: demo.Main)
cd "$(dirname "$0")"

if [ ! -d out ]; then
    echo "No existe la carpeta out/, compilando primero..."
    ./compilar.sh || exit 1
fi

java -cp out "${1:-demo.Main}"
