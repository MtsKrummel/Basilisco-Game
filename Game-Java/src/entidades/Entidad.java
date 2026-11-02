package entidades;

import contratos.Actualizable;
import contratos.Dibujable;

public abstract class Entidad implements Actualizable, Dibujable {

    // Atributos
    private double x;
    private double y;
    private double vx;
    private double vy;
    private boolean viva = true;

    // Constructor
    protected Entidad(double x, double y) {
        setPosicion(x, y);
    }

    // Posición
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public final void setPosicion(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("posición inválida: " + x + ", " + y);
        }
        this.x = x;
        this.y = y;
    }

    // Velocidad
    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVelocidad(double vx, double vy) {
        if (!Double.isFinite(vx) || !Double.isFinite(vy)) {
            throw new IllegalArgumentException("velocidad inválida: " + vx + ", " + vy);
        }
        this.vx = vx;
        this.vy = vy;
    }

    // Ciclo de vida
    public boolean estaViva() {
        return viva;
    }

    public void destruir() {
        viva = false;
    }

    // Movimiento
    protected void mover(double dt) {
        setPosicion(x + vx * dt, y + vy * dt);
    }

    // Gancho para los límites del mundo
    public void ajustarALimites(double ancho, double alto) {
    }
}
