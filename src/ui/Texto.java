package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;

public final class Texto {

    private static final Font FUENTE_BASE = cargarFuente();

    private Texto() {
    }

    private static Font cargarFuente() {
        try (InputStream in = Texto.class.getResourceAsStream("/juego/Fuente/GrapeSoda.ttf")) {
            if (in == null) {
                System.err.println("FALTA FUENTE: /juego/Fuente/GrapeSoda.ttf");
                return new Font(Font.SANS_SERIF, Font.BOLD, 12);
            }
            return Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font(Font.SANS_SERIF, Font.BOLD, 12);
        }
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

    private static void preparar(Graphics2D g, int tamanio, Color color) {
        g.setFont(FUENTE_BASE.deriveFont((float) tamanio));
        g.setColor(color);
    }
}