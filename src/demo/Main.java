package demo;

import motor.Ventana;
import juego.pantallas.PantallaMenu;
import juego.pantallas.JuegoEscena;

public class Main {
    public static void main(String[] args) {
        Ventana.abrir("Snake con Deshacer", 600, 600, new PantallaMenu(() -> new JuegoEscena(1, 0)));
    }
}