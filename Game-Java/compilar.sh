#!/bin/sh
rm -rf out
mkdir out
javac -encoding UTF-8 -d out $(find src -name "*.java")
mkdir -p out/juego/pantallas
cp src/juego/pantallas/*.png out/juego/pantallas/
