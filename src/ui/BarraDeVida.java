package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public final class BarraDeVida {

    private static final Image CORAZON = cargar();

    // El PNG mide 125x118 pero el corazón ocupa solo esta zona; el resto es transparente
    private static final int SX1 = 36, SY1 = 37, SX2 = 88, SY2 = 82;
    private static final int ANCHO = SX2 - SX1;   // 52
    private static final int ALTO = SY2 - SY1;    // 45
    private static final int SEPARACION = 6;

    private BarraDeVida() {
    }

    private static Image cargar() {
        URL url = BarraDeVida.class.getResource("/juego/entidades/Sprites/Vida.png");
        if (url == null) {
            System.err.println("FALTA SPRITE: Vida.png");
            return null;
        }
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Dibuja un corazón por cada vida. (x, y) es la esquina superior izquierda del primero.
    public static void dibujar(Graphics2D g, int vidas, int x, int y) {
        if (CORAZON == null) {
            Texto.izquierda(g, String.valueOf(vidas), x, y + ALTO, 24, Color.WHITE);
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        for (int i = 0; i < vidas; i++) {
            int dx = x + i * (ANCHO + SEPARACION);
            g2.drawImage(CORAZON, dx, y, dx + ANCHO, y + ALTO, SX1, SY1, SX2, SY2, null);
        }
        g2.dispose();
    }
}