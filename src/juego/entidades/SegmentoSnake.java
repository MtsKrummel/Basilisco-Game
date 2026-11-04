package juego.entidades;

import contratos.Dibujable;
import java.awt.Color;
import java.awt.Graphics2D;

public class SegmentoSnake implements Dibujable, Interactuable {
    private int x;
    private int y;

    public SegmentoSnake(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() { return x; }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Coordenada X inválida.");
        }
        this.x = x;
    }

    public int getY() { return y; }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("Coordenada Y inválida.");
        }
        this.y = y;
    }

    @Override
    public boolean chocaCon(int posX, int posY) {
        return this.x == posX && this.y == posY;
    }

    @Override
    public void alInteractuar(Snake serpiente) {
        // Al chocar contra sí misma pierda una vida
        serpiente.quitarVida();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int tam = 20;
        g.setColor(Color.GREEN);
        g.fillRect(x * tam, y * tam, tam - 1, tam - 1);
    }
}