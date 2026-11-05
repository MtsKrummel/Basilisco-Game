package demo;

import juego.reglas.Nivel;
import motor.Ventana;
import juego.pantallas.PantallaMenu;
import juego.pantallas.JuegoEscena;

public class Main {
    public static void main(String[] args) {
        Ventana.abrir("Basilisco", Nivel.anchoPantalla, Nivel.altoPantalla, new PantallaMenu(() -> new JuegoEscena(1, 0)));
    }
}