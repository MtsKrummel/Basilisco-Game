package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public final class Texto {

    // Sin instancias
    private Texto() {
    }

    // Alineado a la izquierda
    public static void izquierda(Graphics2D g, String texto, double x, double y, int tamanio, Color color) {
        preparar(g, tamanio, color);
        g.drawString(texto, (float) x, (float) y);
    }

    // Centrado en x
    public static void centrado(Graphics2D g, String texto, double x, double y, int tamanio, Color color) {
        preparar(g, tamanio, color);
        FontMetrics medidas = g.getFontMetrics();
        g.drawString(texto, (float) (x - medidas.stringWidth(texto) / 2.0), (float) y);
    }

    // Alineado a la derecha
    public static void derecha(Graphics2D g, String texto, double x, double y, int tamanio, Color color) {
        preparar(g, tamanio, color);
        FontMetrics medidas = g.getFontMetrics();
        g.drawString(texto, (float) (x - medidas.stringWidth(texto)), (float) y);
    }

    // Fuente y color
    private static void preparar(Graphics2D g, int tamanio, Color color) {
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, tamanio));
        g.setColor(color);
    }
}
