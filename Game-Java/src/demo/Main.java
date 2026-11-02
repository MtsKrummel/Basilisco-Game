package demo;

import motor.Ventana;

public class Main {

    // Punto de entrada
    public static void main(String[] args) {
        Ventana.abrir("Demo del motor", 900, 600, new EscenaDemo());
    }
}
