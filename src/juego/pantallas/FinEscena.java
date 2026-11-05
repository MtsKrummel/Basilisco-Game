package juego.pantallas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import motor.Entrada;
import motor.Escena;
import ui.Texto;

public class FinEscena extends Escena {

    // <-- AJUSTAR al nombre real de tu imagen (carpeta junto a Nivel1, Nivel2, Nivel3)
    private static final String FONDO_DERROTA = "/juego/pantallas/GameOver/GameOver.png";

    private static final int BOTON_Y = 585;
    private static final int BOTON_IZQ_X = 61;
    private static final int BOTON_DER_X = 534;
    private static final int PUNTAJE_X = 61;
    private static final int PUNTAJE_Y = 560;
    private static final double SEGUNDOS_ACTIVO = 0.3;
    private static final int NINGUNO = -1;
    private static final Color VERDE_CLARO = new Color(156, 238, 194);

    private final int puntajeFinal;
    private final boolean victoria;
    private final BufferedImage fondo;           // null en victoria
    private final PantallaMenu.Boton[] botones;
    private int seleccionado = 0;
    private int elegido = NINGUNO;
    private double restante;
    private int ultimoX = Integer.MIN_VALUE;
    private int ultimoY = Integer.MIN_VALUE;

    public FinEscena(int puntajeFinal, boolean victoria) {
        this.puntajeFinal = puntajeFinal;
        this.victoria = victoria;
        this.fondo = victoria ? null : cargar(FONDO_DERROTA);
        this.botones = new PantallaMenu.Boton[] {
                new PantallaMenu.Boton("nuevoJuegoNormal.png", "nuevoJuegoActivo.png", "nuevoJuegoHold.png",
                        () -> getContexto().irA(new JuegoEscena(1, 0))),
                new PantallaMenu.Boton("salirNormal.png", "salirActivo.png", "salirHold.png",
                        () -> getContexto().salir())
        };
    }

    @Override
    public void actualizar(double dt) {
        if (elegido != NINGUNO) {
            restante -= dt;
            if (restante <= 0) {
                int indice = elegido;
                elegido = NINGUNO;
                botones[indice].accion.run();
            }
            return;
        }

        Entrada entrada = getEntrada();

        if (entrada.fuePresionada(KeyEvent.VK_LEFT)) seleccionado = Math.max(0, seleccionado - 1);
        if (entrada.fuePresionada(KeyEvent.VK_RIGHT)) seleccionado = Math.min(botones.length - 1, seleccionado + 1);

        int mx = entrada.getMouseX();
        int my = entrada.getMouseY();
        int bajo = botonEn(mx, my);
        if ((mx != ultimoX || my != ultimoY) && bajo != NINGUNO) {
            seleccionado = bajo;
        }
        ultimoX = mx;
        ultimoY = my;

        if (entrada.huboClick() && bajo != NINGUNO) {
            seleccionado = bajo;
            elegir(bajo);
        } else if (entrada.fuePresionada(KeyEvent.VK_ENTER)) {
            elegir(seleccionado);
        }
    }

    private void elegir(int indice) {
        elegido = indice;
        restante = SEGUNDOS_ACTIVO;
    }

    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (fondo != null) {
            g.drawImage(fondo, 0, 0, ancho, alto, null);
        } else {
            // Victoria: todavía sin imagen propia
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, ancho, alto);
            Texto.centrado(g, "¡VICTORIA!", ancho / 2.0, 200, 64, VERDE_CLARO);
        }

        Texto.izquierda(g, "Puntaje: " + puntajeFinal, PUNTAJE_X, PUNTAJE_Y, 32, VERDE_CLARO);

        for (int i = 0; i < botones.length; i++) {
            Rectangle r = areaBoton(i);
            g.drawImage(imagenBoton(i), r.x, r.y, r.width, r.height, null);
        }
    }

    private BufferedImage imagenBoton(int indice) {
        PantallaMenu.Boton b = botones[indice];
        if (indice == elegido) return b.activo;
        return indice == seleccionado ? b.hold : b.normal;
    }

    // Tamaño original del botón; el 0 va a la izquierda y el 1 a la derecha
    private Rectangle areaBoton(int indice) {
        BufferedImage img = botones[indice].normal;
        int x = (indice == 0) ? BOTON_IZQ_X : BOTON_DER_X;
        return new Rectangle(x, BOTON_Y, img.getWidth(), img.getHeight());
    }

    private int botonEn(int x, int y) {
        for (int i = 0; i < botones.length; i++) {
            if (areaBoton(i).contains(x, y)) return i;
        }
        return NINGUNO;
    }

    private static BufferedImage cargar(String ruta) {
        URL url = FinEscena.class.getResource(ruta);
        if (url == null) {
            throw new IllegalStateException("No se encontró la imagen: " + ruta);
        }
        try {
            BufferedImage imagen = ImageIO.read(url);
            if (imagen == null) {
                throw new IllegalStateException("Formato no soportado: " + ruta);
            }
            return imagen;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer la imagen: " + ruta, e);
        }
    }
}