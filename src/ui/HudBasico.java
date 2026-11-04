package ui;

import contratos.Dibujable;
import contratos.Estadisticas;
import java.awt.Color;
import java.awt.Graphics2D;

public class HudBasico implements Dibujable {

    // Atributos
    private final Estadisticas estadisticas;
    private final int ancho;

    // Constructor
    public HudBasico(Estadisticas estadisticas, int ancho) {
        if (estadisticas == null) {
            throw new IllegalArgumentException("las estadísticas no pueden ser nulas");
        }
        this.estadisticas = estadisticas;
        this.ancho = ancho;
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        Texto.izquierda(g, "Puntaje: " + estadisticas.getPuntaje(), 16, 28, 18, Color.WHITE);
        Texto.centrado(g, estadisticas.getMensaje(), ancho / 2.0, 28, 16, Color.LIGHT_GRAY);
        String derecha = "Vidas: " + estadisticas.getVidas() + "   Nivel: " + estadisticas.getNivel();
        Texto.derecha(g, derecha, ancho - 16, 28, 18, Color.WHITE);
    }
}
