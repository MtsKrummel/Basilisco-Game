package juego.pantallas;

import motor.Escena;
import ui.Texto;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class FinEscena extends Escena {
    private int puntajeFinal;
    private boolean victoria;

    public FinEscena(int puntajeFinal, boolean victoria) {
        this.puntajeFinal = puntajeFinal;
        this.victoria = victoria;
    }

    @Override
    public void actualizar(double dt) {
        if (getContexto().getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new MenuEscena());
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        String titulo = victoria ? "¡VICTORIA!" : "GAME OVER";
        Color colorTitulo = victoria ? Color.GREEN : Color.RED;

        Texto.centrado(g, titulo, getContexto().getAncho() / 2, 150, 40, colorTitulo);
        Texto.centrado(g, "Puntaje Final: " + puntajeFinal, getContexto().getAncho() / 2, 240, 24, Color.WHITE);
        Texto.centrado(g, "Presiona ENTER para volver al menú", getContexto().getAncho() / 2, 320, 18, Color.GRAY);
    }
}