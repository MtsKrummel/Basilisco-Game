package juego.entidades;

import juego.reglas.Nivel;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D;

public class Manzana extends Consumible {
    private static Image imagenManzana;

    static {
        try {
            imagenManzana = ImageIO.read(Manzana.class.getResource("/juego/entidades/Sprites/Manzana.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Manzana(int x, int y) {
        super(x, y, 10);
    }

    @Override
    public void aplicarEfecto(Snake serpiente) {
        serpiente.crecer();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int tam = Nivel.tamanioCelda;
        int posX = getX() * tam;
        int posY = getY() * tam;

        if (imagenManzana != null) {
            g.drawImage(imagenManzana, posX, posY, tam, tam, null);
        }
    }
}