package juego.pantallas;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import motor.Entrada;
import motor.Escena;

public class PantallaMenu extends Escena {

    // Constantes
    private static final String CARPETA = "/juego/pantallas/";
    private static final double FRACCION_LOGO = 0.60;
    private static final double FRACCION_BOTON = 0.42;
    private static final double POSICION_LOGO = 0.12;
    private static final double POSICION_BOTONES = 0.58;
    private static final double SEPARACION_BOTONES = 0.03;
    private static final double SEGUNDOS_ACTIVO = 0.3;
    private static final int NINGUNO = -1;

    // Atributos
    private final Supplier<Escena> nuevoJuego;
    private final BufferedImage fondo;
    private final BufferedImage logo;
    private final Boton[] botones;
    private int seleccionado = 0;
    private int elegido = NINGUNO;
    private double restante;
    private int ultimoX = Integer.MIN_VALUE;
    private int ultimoY = Integer.MIN_VALUE;

    // Constructor
    public PantallaMenu(Supplier<Escena> nuevoJuego) {
        this.nuevoJuego = nuevoJuego;
        fondo = cargar("fondoSplash.png");
        logo = cargar("logoSplash.png");
        botones = new Boton[] {
            new Boton("nuevoJuegoNormal.png", "nuevoJuegoActivo.png", "nuevoJuegoHold.png",
                    () -> getContexto().irA(this.nuevoJuego.get())),
            new Boton("salirNormal.png", "salirActivo.png", "salirHold.png",
                    () -> getContexto().salir())
        };
    }

    // Actualización
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

        // Teclado: flechas mueven la selección, ENTER elige
        if (entrada.fuePresionada(KeyEvent.VK_UP)) {
            seleccionado = Math.max(0, seleccionado - 1);
        }
        if (entrada.fuePresionada(KeyEvent.VK_DOWN)) {
            seleccionado = Math.min(botones.length - 1, seleccionado + 1);
        }

        // Mouse: solo selecciona si el mouse se movió sobre un botón
        int mx = entrada.getMouseX();
        int my = entrada.getMouseY();
        int bajo = botonEn(mx, my);
        if ((mx != ultimoX || my != ultimoY) && bajo != NINGUNO) {
            seleccionado = bajo;
        }
        ultimoX = mx;
        ultimoY = my;

        // Click sobre un botón lo selecciona y lo elige
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

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        dibujarFondo(g, ancho, alto);

        Rectangle area = areaLogo();
        g.drawImage(logo, area.x, area.y, area.width, area.height, null);

        for (int i = 0; i < botones.length; i++) {
            Rectangle r = areaBoton(i);
            g.drawImage(imagenBoton(i), r.x, r.y, r.width, r.height, null);
        }
    }

    // Imagen del botón: activo si fue elegido, hold si está seleccionado, normal en otro caso
    private BufferedImage imagenBoton(int indice) {
        Boton b = botones[indice];
        if (indice == elegido) {
            return b.activo;
        }
        return indice == seleccionado ? b.hold : b.normal;
    }

    // Fondo escalado para cubrir toda la ventana (recorte centrado)
    private void dibujarFondo(Graphics2D g, int ancho, int alto) {
        double escala = Math.max(ancho / (double) fondo.getWidth(), alto / (double) fondo.getHeight());
        int w = (int) Math.round(fondo.getWidth() * escala);
        int h = (int) Math.round(fondo.getHeight() * escala);
        g.drawImage(fondo, (ancho - w) / 2, (alto - h) / 2, w, h, null);
    }

    // Disposición
    private Rectangle areaLogo() {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        int w = (int) (ancho * FRACCION_LOGO);
        int h = (int) Math.round(w * (double) logo.getHeight() / logo.getWidth());
        return new Rectangle((ancho - w) / 2, (int) (alto * POSICION_LOGO), w, h);
    }

    private Rectangle areaBoton(int indice) {
        int ancho = getContexto().getAncho();
        int alto = getContexto().getAlto();
        BufferedImage normal = botones[indice].normal;
        int w = (int) (ancho * FRACCION_BOTON);
        int h = (int) Math.round(w * (double) normal.getHeight() / normal.getWidth());
        int y = (int) (alto * POSICION_BOTONES) + indice * (h + (int) (alto * SEPARACION_BOTONES));
        return new Rectangle((ancho - w) / 2, y, w, h);
    }

    private int botonEn(int x, int y) {
        for (int i = 0; i < botones.length; i++) {
            if (areaBoton(i).contains(x, y)) {
                return i;
            }
        }
        return NINGUNO;
    }

    // Carga de recursos
    private static BufferedImage cargar(String nombre) {
        URL url = PantallaMenu.class.getResource(CARPETA + nombre);
        if (url == null) {
            throw new IllegalStateException("No se encontró la imagen: " + nombre);
        }
        try {
            BufferedImage imagen = ImageIO.read(url);
            if (imagen == null) {
                throw new IllegalStateException("Formato no soportado: " + nombre);
            }
            return imagen;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer la imagen: " + nombre, e);
        }
    }

    // Recorta el margen transparente, con el mismo recuadro para todas las imágenes
    private static BufferedImage[] recortarJuntas(BufferedImage... imagenes) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = -1;
        int maxY = -1;
        for (BufferedImage img : imagenes) {
            for (int y = 0; y < img.getHeight(); y++) {
                for (int x = 0; x < img.getWidth(); x++) {
                    if ((img.getRGB(x, y) >>> 24) != 0) {
                        minX = Math.min(minX, x);
                        minY = Math.min(minY, y);
                        maxX = Math.max(maxX, x);
                        maxY = Math.max(maxY, y);
                    }
                }
            }
        }
        if (maxX < 0) {
            return imagenes;
        }
        BufferedImage[] recortadas = new BufferedImage[imagenes.length];
        for (int i = 0; i < imagenes.length; i++) {
            recortadas[i] = imagenes[i].getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
        return recortadas;
    }

    // Botón con sus tres estados visuales y su acción
    private static final class Boton {
        private final BufferedImage normal;
        private final BufferedImage activo;
        private final BufferedImage hold;
        private final Runnable accion;

        Boton(String normal, String activo, String hold, Runnable accion) {
            BufferedImage[] imagenes = recortarJuntas(cargar(normal), cargar(activo), cargar(hold));
            this.normal = imagenes[0];
            this.activo = imagenes[1];
            this.hold = imagenes[2];
            this.accion = accion;
        }
    }
}
