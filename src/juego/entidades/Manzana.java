package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Manzana extends Consumible {

    public Manzana(int x, int y) {
        super(x, y, 10);
    }

    @Override
    public void aplicarEfecto(Snake serpiente) {
        serpiente.crecer();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int tam = 20;
        g.setColor(Color.RED);
        g.fillOval(getX() * tam + 2, getY() * tam + 2, tam - 4, tam - 4);
    }
}