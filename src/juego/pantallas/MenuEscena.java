package juego.pantallas;

import motor.Escena;
import ui.Texto;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MenuEscena extends Escena {
    @Override
    public void actualizar(double dt) {
        if (getContexto().getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new JuegoEscena());
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        Texto.centrado(g, "SNAKE CON DESHACER", getContexto().getAncho() / 2, 150, 36, Color.GREEN);
        Texto.centrado(g, "Presiona ENTER para jugar", getContexto().getAncho() / 2, 250, 20, Color.WHITE);
        Texto.centrado(g, "Usa Z para deshacer movimientos", getContexto().getAncho() / 2, 300, 16, Color.YELLOW);
    }
}