package juego.entidades;

import estructuras.Lista;
import contratos.Dibujable;
import estructuras.ListaDoble;

import java.awt.Graphics2D;

public class Snake implements Dibujable {
    private Lista<SegmentoSnake> cuerpo;
    private int puntos;
    private int vidas;
    private boolean viva;
    private boolean debeCrecer;

    public Snake(Lista<SegmentoSnake> listaVacia, int xInicial, int yInicial, int vidasIniciales) {
        if (listaVacia == null) {
            throw new IllegalArgumentException("La lista del cuerpo no puede ser nula.");
        }
        setVidas(vidasIniciales);
        this.cuerpo = listaVacia;
        this.cuerpo.agregar(new SegmentoSnake(xInicial, yInicial));
        this.puntos = 0;
        this.viva = true;
        this.debeCrecer = false;
    }

    public Lista<SegmentoSnake> getCuerpo() { return cuerpo; }
    public int getPuntos() { return puntos; }
    public int getVidas() { return vidas; }
    public boolean estaViva() { return viva; }

    public void setVidas(int vidas) {
        if (vidas <= 0) {
            throw new IllegalArgumentException("Las vidas iniciales deben ser mayores a 0.");
        }
        this.vidas = vidas;
    }

    public void sumarPuntos(int cantidad) {
        this.puntos += cantidad;
        if (this.puntos < 0) {
            this.puntos = 0; // Evita puntajes negativos si consume veneno acumulado
        }
    }

    public void quitarVida() {
        this.vidas--;
        if (this.vidas <= 0) {
            this.vidas = 0;
            this.viva = false;
        }
    }

    public void crecer() {
        this.debeCrecer = true;
    }

    public void reducir() {
        if (cuerpo.tamanio() > 1) {
            cuerpo.eliminar(cuerpo.tamanio() - 1);
        }
    }

    public SegmentoSnake getCabeza() {
        return cuerpo.obtener(0);
    }

    public boolean debeCrecerYResetear() {
        boolean estado = debeCrecer;
        debeCrecer = false;
        return estado;
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Por defecto llamamos al dibujo pasando "DERECHA" como orientación base
        dibujar(g, "DERECHA");
    }


    public void dibujar(Graphics2D g, String direccionActual) {
        int tamano = cuerpo.tamanio();

        for (int i = 0; i < tamano; i++) {
            SegmentoSnake actual = cuerpo.obtener(i);

            if (i == 0) {
                actual.dibujarCabeza(g, direccionActual);
            }
            else if (i == tamano - 1) {
                // 2. Es la COLA (último elemento)
                SegmentoSnake penultimo = (tamano > 1) ? cuerpo.obtener(i - 1) : null;
                actual.dibujarCola(g, penultimo);
            }
            else {
                // 3. Es el CUERPO intermedio
                SegmentoSnake anterior = cuerpo.obtener(i - 1);
                SegmentoSnake siguiente = cuerpo.obtener(i + 1);
                actual.dibujarSegmento(g, anterior, siguiente);
            }
        }
    }
}