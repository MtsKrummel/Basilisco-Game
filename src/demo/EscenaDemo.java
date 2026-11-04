package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import motor.Entrada;
import motor.Escena;
import motor.Temporizador;
import ui.Texto;

public class EscenaDemo extends Escena {

    // Atributos
    private final Temporizador parpadeo = new Temporizador(0.5);
    private double x;
    private double y;
    private boolean azul;
    private boolean visible;
    private int clicks;

    // Entrada a la escena
    @Override
    public void alEntrar() {
        x = getContexto().getAncho() / 2.0;
        y = getContexto().getAlto() / 2.0;
        azul = true;
        visible = true;
        clicks = 0;
        parpadeo.reiniciar();
    }

    // Actualización
    @Override
    public void actualizar(double dt) {
        Entrada entrada = getEntrada();
        double velocidad = 260;

        if (entrada.estaPresionada(KeyEvent.VK_LEFT)) {
            x -= velocidad * dt;
        }
        if (entrada.estaPresionada(KeyEvent.VK_RIGHT)) {
            x += velocidad * dt;
        }
        if (entrada.estaPresionada(KeyEvent.VK_UP)) {
            y -= velocidad * dt;
        }
        if (entrada.estaPresionada(KeyEvent.VK_DOWN)) {
            y += velocidad * dt;
        }
        x = Math.max(20, Math.min(getContexto().getAncho() - 20, x));
        y = Math.max(20, Math.min(getContexto().getAlto() - 20, y));

        if (entrada.huboClick()) {
            clicks++;
            azul = !azul;
        }
        if (parpadeo.avanzar(dt)) {
            visible = !visible;
        }
        if (entrada.fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new EscenaDemoDos(clicks));
        }
        if (entrada.fuePresionada(KeyEvent.VK_ESCAPE)) {
            getContexto().salir();
        }
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setColor(new Color(20, 24, 36));
        g.fillRect(0, 0, ancho, alto);

        g.setColor(azul ? new Color(70, 140, 230) : new Color(230, 110, 60));
        g.fillRect((int) x - 20, (int) y - 20, 40, 40);

        Texto.izquierda(g, "Clicks: " + clicks, 16, 28, 18, Color.WHITE);
        if (visible) {
            Texto.centrado(g, "Flechas: mover   Click: cambiar color   ENTER: siguiente escena   ESC: salir",
                    ancho / 2.0, alto - 20, 14, Color.LIGHT_GRAY);
        }
    }
}
