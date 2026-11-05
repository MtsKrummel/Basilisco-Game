package juego.entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

import juego.reglas.Nivel;

public class Cientifico extends Consumible {

    private static Image imagenCientifico;

    static {
        try {
            // Ajusta la ruta a tu archivo PNG del científico
            imagenCientifico = ImageIO.read(Cientifico.class.getResource("/juego/entidades/Sprites/cientifico.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Cientifico(int x, int y) {
        super(x, y, 30);
    }

    @Override
    public void aplicarEfecto(Snake serpiente) {
        serpiente.crecer();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int tam = Nivel.tamanioCelda; // 32 píxeles
        int posX = getX() * tam;
        int posY = getY() * tam;

        if (imagenCientifico != null) {
            g.drawImage(imagenCientifico, posX, posY, tam, tam, null);
        }
    }
}