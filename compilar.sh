#!/bin/sh
# Compila el juego y copia los recursos (imagenes, fuentes) a out/
set -e
cd "$(dirname "$0")"

rm -rf out
mkdir out

# Lista de fuentes entre comillas (soporta espacios en las rutas)
find src -name "*.java" | sed 's/.*/"&"/' > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
rm -f fuentes.txt

# Copiar recursos (todo lo que no sea .java) respetando la estructura de carpetas
find src -type f ! -name "*.java" | while IFS= read -r f; do
    destino="out/${f#src/}"
    mkdir -p "$(dirname "$destino")"
    cp "$f" "$destino"
done

echo "Compilacion terminada. Ejecuta ./ejecutar.sh"
