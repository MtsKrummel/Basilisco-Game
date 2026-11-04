package juego.entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Cientifico extends Consumible {

    public Cientifico(int x, int y) {
        super(x, y, 50); // Otorga muchos más puntos
    }

    @Override
    public void aplicarEfecto(Snake serpiente) {
        serpiente.crecer();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int tam = 20;
        g.setColor(Color.CYAN); // Se distingue por su color celeste/azul
        g.fillRect(getX() * tam + 3, getY() * tam + 3, tam - 6, tam - 6);
    }
}