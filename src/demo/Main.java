package demo;

import motor.Ventana;
import juego.pantallas.MenuEscena;

public class Main {
    public static void main(String[] args) {
        Ventana.abrir("Snake con Deshacer", 600, 600, new MenuEscena());
    }
}