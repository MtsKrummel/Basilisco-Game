package juego.entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import juego.reglas.Nivel;

public class Veneno extends Consumible {

    private static Image imagenVeneno;

    static {
        try {
            imagenVeneno = ImageIO.read(Veneno.class.getResource("/juego/entidades/Sprites/Veneno.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        int tam = Nivel.tamanioCelda; // 32 píxeles exactos
        int posX = getX() * tam;
        int posY = getY() * tam;

        if (imagenVeneno != null) {
            g.drawImage(imagenVeneno, posX, posY, tam, tam, null);
        }
    }
}