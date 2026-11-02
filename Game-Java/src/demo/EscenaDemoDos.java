package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Escena;
import ui.Texto;

public class EscenaDemoDos extends Escena {

    // Atributos
    private final int clicks;

    // Constructor
    public EscenaDemoDos(int clicks) {
        this.clicks = clicks;
    }

    // Actualización
    @Override
    public void actualizar(double dt) {
        if (getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaDemo());
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setColor(new Color(36, 24, 40));
        g.fillRect(0, 0, ancho, alto);
        Texto.centrado(g, "Segunda escena", ancho / 2.0, alto / 2.0 - 20, 40, Color.WHITE);
        Texto.centrado(g, "Hiciste " + clicks + " clicks en la anterior", ancho / 2.0, alto / 2.0 + 24, 20, Color.LIGHT_GRAY);
        Texto.centrado(g, "ENTER: volver", ancho / 2.0, alto / 2.0 + 64, 18, Color.GRAY);
    }
}
