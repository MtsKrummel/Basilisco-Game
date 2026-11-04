package demo;

import juego.pantallas.PantallaMenu;
import motor.Ventana;

public class Main {

    // Punto de entrada: arranca en el menú; "Nuevo Juego" abre EscenaDemo
    public static void main(String[] args) {
        Ventana.abrir("Demo del motor", 900, 600, new PantallaMenu(EscenaDemo::new));
    }
}
