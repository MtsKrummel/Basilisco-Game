package juego.pantallas;

import motor.Escena;
import ui.Texto;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class FinEscena extends Escena {
    private int puntajeFinal;
    private boolean victoria;
    private BufferedImage imgGameOver;

    public FinEscena(int puntajeFinal, boolean victoria) {
        this.puntajeFinal = puntajeFinal;
        this.victoria = victoria;

        // Cargamos la imagen solo si el jugador perdió
        if (!victoria) {
            // IMPORTANTE: Cambia "perdiste.png" por el nombre real de tu imagen
            imgGameOver = cargar("GameOverScreen.png");
        }
    }

    @Override
    public void actualizar(double dt) {
        if (getContexto().getEntrada().fuePresionada(KeyEvent.VK_ENTER)) {
            getContexto().irA(new PantallaMenu(() -> new JuegoEscena(1, 0)));
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        String titulo = victoria ? "¡VICTORIA!" : "GAME OVER";
        Color colorTitulo = victoria ? Color.GREEN : Color.RED;

        // Subí un poco los textos para darle espacio a la imagen abajo
        Texto.centrado(g, titulo, getContexto().getAncho() / 2, 100, 40, colorTitulo);
        Texto.centrado(g, "Puntaje Final: " + puntajeFinal, getContexto().getAncho() / 2, 170, 24, Color.WHITE);
        Texto.centrado(g, "Presiona ENTER para volver al menú", getContexto().getAncho() / 2, 220, 18, Color.GRAY);

        // Si perdimos y la imagen cargó bien, la dibujamos centrada
        // Si perdimos y la imagen cargó bien, la dibujamos centrada y achicada
        if (!victoria && imgGameOver != null) {
            int anchoDeseado = 350; // Puedes cambiar este número para hacerla más grande o más chica
            int altoDeseado = (imgGameOver.getHeight() * anchoDeseado) / imgGameOver.getWidth(); // Mantiene la proporción

            int x = (getContexto().getAncho() - anchoDeseado) / 2;
            int y = 260; // Posición vertical de la imagen

            g.drawImage(imgGameOver, x, y, anchoDeseado, altoDeseado, null);
        }
    }

    // Método interno seguro para cargar la imagen
    private BufferedImage cargar(String nombre) {
        try {
            return ImageIO.read(getClass().getResource("/juego/pantallas/" + nombre));
        } catch (Exception e) {
            System.out.println("Error al cargar la imagen: " + nombre);
            return null;
        }
    }
}