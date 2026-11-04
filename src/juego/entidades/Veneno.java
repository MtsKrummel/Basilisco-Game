package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Veneno extends Consumible {

    public Veneno(int x, int y) {
        super(x, y, -20); // Resta puntos al puntaje total
    }

    @Override
    public void aplicarEfecto(Snake serpiente) {
        serpiente.quitarVida();
        serpiente.reducir();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int tam = 20;
        g.setColor(new Color(128, 0, 128)); // Color morado
        g.fillOval(getX() * tam + 3, getY() * tam + 3, tam - 6, tam - 6);
    }
}