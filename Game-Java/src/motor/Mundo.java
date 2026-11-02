package motor;

import contratos.Actualizable;
import contratos.Colisionable;
import contratos.Dibujable;
import entidades.Entidad;
import estructuras.Lista;
import java.awt.Graphics2D;
import java.util.Iterator;

public class Mundo implements Actualizable, Dibujable, Iterable<Entidad> {

    // Atributos
    private final int ancho;
    private final int alto;
    private final Lista<Entidad> entidades;
    private Entidad[] cuadro = new Entidad[64];
    private int cantidad;
    private boolean sucio = true;

    // Constructor
    public Mundo(int ancho, int alto, Lista<Entidad> entidades) {
        if (ancho <= 0 || alto <= 0) {
            throw new IllegalArgumentException("dimensiones inválidas");
        }
        if (entidades == null) {
            throw new IllegalArgumentException("la lista no puede ser nula");
        }
        this.ancho = ancho;
        this.alto = alto;
        this.entidades = entidades;
    }

    // Consultas
    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public int cantidad() {
        return entidades.tamanio();
    }

    @Override
    public Iterator<Entidad> iterator() {
        return entidades.iterator();
    }

    // Altas y bajas
    public void agregar(Entidad entidad) {
        if (entidad == null) {
            throw new IllegalArgumentException("la entidad no puede ser nula");
        }
        entidades.agregar(entidad);
        sucio = true;
    }

    public void vaciar() {
        refrescar();
        for (int i = 0; i < cantidad; i++) {
            cuadro[i].destruir();
        }
    }

    // Ciclo de juego
    @Override
    public void actualizar(double dt) {
        refrescar();
        for (int i = 0; i < cantidad; i++) {
            Entidad e = cuadro[i];
            if (e.estaViva()) {
                e.actualizar(dt);
                e.ajustarALimites(ancho, alto);
            }
        }
        resolverColisiones();
        for (int i = 0; i < cantidad; i++) {
            if (cuadro[i].estaViva()) {
                cuadro[i].ajustarALimites(ancho, alto);
            }
        }
        limpiar();
    }

    @Override
    public void dibujar(Graphics2D g) {
        refrescar();
        for (int i = 0; i < cantidad; i++) {
            if (cuadro[i].estaViva()) {
                cuadro[i].dibujar(g);
            }
        }
    }

    // Colisiones entre pares
    private void resolverColisiones() {
        for (int i = 0; i < cantidad; i++) {
            if (!(cuadro[i] instanceof Colisionable)) {
                continue;
            }
            Colisionable a = (Colisionable) cuadro[i];
            for (int j = i + 1; j < cantidad; j++) {
                if (!(cuadro[j] instanceof Colisionable)) {
                    continue;
                }
                if (!cuadro[i].estaViva() || !cuadro[j].estaViva()) {
                    continue;
                }
                Colisionable b = (Colisionable) cuadro[j];
                if (a.getLimites().intersects(b.getLimites())) {
                    a.alColisionar(b);
                    b.alColisionar(a);
                }
            }
        }
    }

    // Eliminación de entidades destruidas
    private void limpiar() {
        boolean hayMuertas = false;
        for (int i = 0; i < cantidad; i++) {
            if (!cuadro[i].estaViva()) {
                hayMuertas = true;
                break;
            }
        }
        if (!hayMuertas) {
            return;
        }
        for (int k = entidades.tamanio() - 1; k >= 0; k--) {
            if (!entidades.obtener(k).estaViva()) {
                entidades.eliminar(k);
            }
        }
        sucio = true;
    }

    // Copia de trabajo del cuadro
    private void refrescar() {
        int n = entidades.tamanio();
        if (!sucio && n == cantidad) {
            return;
        }
        if (cuadro.length < n) {
            cuadro = new Entidad[Math.max(n, cuadro.length * 2)];
        }
        int i = 0;
        for (Entidad e : entidades) {
            if (i < cuadro.length) {
                cuadro[i++] = e;
            }
        }
        for (int k = i; k < cuadro.length; k++) {
            cuadro[k] = null;
        }
        cantidad = i;
        sucio = false;
    }
}
