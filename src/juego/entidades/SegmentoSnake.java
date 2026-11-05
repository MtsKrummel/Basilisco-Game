package juego.entidades;

import contratos.Dibujable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import juego.reglas.Nivel;

public class SegmentoSnake implements Dibujable, Interactuable {
    private int x;
    private int y;

    // Imágenes estáticas para cada parte
    private static Image imgCabezaArriba, imgCabezaAbajo, imgCabezaIzq, imgCabezaDer;
    private static Image imgCuerpoH, imgCuerpoV;
    private static Image imgGiroIzq, imgGiroDer;
    private static Image imgColaArriba, imgColaAbajo, imgColaIzq, imgColaDer; // Sprites de la cola

    static {
        try {
            imgCabezaArriba = cargarImg("CabezaArriba.png");
            imgCabezaAbajo  = cargarImg("CabezaAbajo.png");
            imgCabezaIzq    = cargarImg("CabezaIzq.png");
            imgCabezaDer    = cargarImg("CabezaDer.png");

            imgCuerpoH      = cargarImg("CuerpoH.png");
            imgCuerpoV      = cargarImg("CuerpoV.png");

            imgGiroIzq      = cargarImg("GiroIzq.png");
            imgGiroDer      = cargarImg("GiroDer.png");

            // Carga de los sprites de la cola (ajusta los nombres de tus archivos PNG)
            imgColaArriba   = cargarImg("ColaArriba.png");
            imgColaAbajo    = cargarImg("ColaAbajo.png");
            imgColaIzq      = cargarImg("ColaIzq.png");
            imgColaDer      = cargarImg("ColaDer.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Image cargarImg(String nombre) {
        URL url = SegmentoSnake.class.getResource("/juego/entidades/Sprites/" + nombre);
        if (url == null) return null;
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            return null;
        }
    }

    public SegmentoSnake(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() { return x; }
    public void setX(int x) {
        if (x < 0) throw new IllegalArgumentException("Coordenada X inválida.");
        this.x = x;
    }

    public int getY() { return y; }
    public void setY(int y) {
        if (y < 0) throw new IllegalArgumentException("Coordenada Y inválida.");
        this.y = y;
    }

    @Override
    public boolean chocaCon(int posX, int posY) {
        return this.x == posX && this.y == posY;
    }

    @Override
    public void alInteractuar(Snake serpiente) {
        serpiente.quitarVida();
    }

    public void dibujarCabeza(Graphics2D g, String direccion) {
        int tam = Nivel.tamanioCelda;
        String dir = (direccion != null) ? direccion : "DERECHA";
        Image img = switch (direccion) {
            case "ARRIBA" -> imgCabezaArriba;
            case "ABAJO" -> imgCabezaAbajo;
            case "IZQUIERDA" -> imgCabezaIzq;
            default -> imgCabezaDer;
        };
        dibujarImagen(g, img);
    }

    public void dibujarCola(Graphics2D g, SegmentoSnake penultimo) {
        int tam = Nivel.tamanioCelda;
        Image img = imgColaDer; // Por defecto

        if (penultimo != null) {
            // Evaluamos la posición del penúltimo respecto a la cola para saber hacia dónde apunta la punta
            if (penultimo.getX() > this.x) {
                img = imgColaIzq; // El cuerpo está a la derecha, la cola apunta a la izquierda
            } else if (penultimo.getX() < this.x) {
                img = imgColaDer; // El cuerpo está a la izquierda, la cola apunta a la derecha
            } else if (penultimo.getY() > this.y) {
                img = imgColaArriba; // El cuerpo está abajo, la cola apunta hacia arriba
            } else if (penultimo.getY() < this.y) {
                img = imgColaAbajo; // El cuerpo está arriba, la cola apunta hacia abajo
            }
        }
        dibujarImagen(g, img);
    }

    private static Image imgGiro= cargarImg("GiroIzq.png");

    public void dibujarSegmento(Graphics2D g, SegmentoSnake anterior, SegmentoSnake siguiente) {
        if (anterior == null || siguiente == null) { dibujarImagen(g, imgCuerpoH); return; }

        // Rectos
        if (anterior.getY() == siguiente.getY()) { dibujarImagen(g, imgCuerpoH); return; }
        if (anterior.getX() == siguiente.getX()) { dibujarImagen(g, imgCuerpoV); return; }

        // Curva: uno de los vecinos está en vertical y el otro en horizontal
        boolean arriba = anterior.getY() < y || siguiente.getY() < y;
        boolean derecha = anterior.getX() > x || siguiente.getX() > x;

        double grados;
        if (arriba && derecha)       grados = 0;    // └
        else if (!arriba && derecha) grados = 90;   // ┌
        else if (!arriba)            grados = 180;  // ┐
        else                         grados = 270;  // ┘

        grados +=180;

        int tam = Nivel.tamanioCelda;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.rotate(Math.toRadians(grados), x * tam + tam / 2.0, y * tam + tam / 2.0);
        if (imgGiro != null) g2.drawImage(imgGiro, x * tam, y * tam, tam, tam, null);
        g2.dispose();
    }

    private void dibujarImagen(Graphics2D g, Image img) {
        int tam = Nivel.tamanioCelda;
        int px = x * tam;
        int py = y * tam;
        if (img != null) {
            g.drawImage(img, px, py, tam, tam, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect(px, py, tam, tam);
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        dibujarSegmento(g, null, null);
    }
}