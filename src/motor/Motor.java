package motor;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class Motor implements Contexto {

    // Constantes
    public static final double PASO = 1.0 / 120.0;

    // Atributos
    private final int ancho;
    private final int alto;
    private final Entrada entrada = new Entrada();
    private Escena actual;
    private Escena pendiente;
    private boolean terminado;

    // Constructor
    public Motor(int ancho, int alto) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("dimensiones inválidas");
        }
        this.ancho = ancho;
        this.alto = alto;
    }

    // Arranque
    public void iniciar(Escena inicial) {
        cambiar(inicial);
    }

    // Avance
    public void paso() {
        if (pendiente != null) {
            Escena nueva = pendiente;
            pendiente = null;
            cambiar(nueva);
        }
        if (actual != null) {
            actual.actualizar(PASO);
        }
        entrada.terminarCuadro();
    }

    public void simular(double segundos) {
        long pasos = Math.round(segundos / PASO);
        for (long i = 0; i < pasos; i++) {
            paso();
        }
    }

    // Dibujo
    public void dibujar(Graphics2D g) {
        if (actual != null) {
            actual.dibujar(g);
        }
    }

    public BufferedImage capturar() {
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        dibujar(g);
        g.dispose();
        return imagen;
    }

    // Consultas
    public Escena getEscenaActual() {
        return actual;
    }

    public boolean estaTerminado() {
        return terminado;
    }

    // Contexto
    @Override
    public Entrada getEntrada() {
        return entrada;
    }

    @Override
    public int getAncho() {
        return ancho;
    }

    @Override
    public int getAlto() {
        return alto;
    }

    @Override
    public void irA(Escena escena) {
        if (escena == null) {
            throw new IllegalArgumentException("la escena no puede ser nula");
        }
        pendiente = escena;
    }

    @Override
    public void salir() {
        terminado = true;
    }

    // Cambio de escena
    private void cambiar(Escena nueva) {
        if (actual != null) {
            actual.alSalir();
        }
        actual = nueva;
        actual.conectar(this);
        actual.alEntrar();
    }
}
