package juego.entidades;

import contratos.Dibujable;

public abstract class Consumible implements Dibujable, Interactuable {
    private int x;
    private int y;
    private int puntos;

    public Consumible(int x, int y, int puntos) {
        setX(x);
        setY(y);
        setPuntos(puntos);
    }

    public int getX() { return x; }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("La posición X no puede ser negativa.");
        }
        this.x = x;
    }

    public int getY() { return y; }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("La posición Y no puede ser negativa.");
        }
        this.y = y;
    }

    public int getPuntos() { return puntos; }

    public void setPuntos(int puntos) {
        this.puntos = puntos; // Puede ser negativo si es un consumible dañino
    }

    @Override
    public boolean chocaCon(int posX, int posY) {
        return this.x == posX && this.y == posY;
    }

    @Override
    public void alInteractuar(Snake serpiente) {
        serpiente.sumarPuntos(this.puntos);
        aplicarEfecto(serpiente);
    }

    // Método abstracto que fuerza el polimorfismo en las 3 subclases
    public abstract void aplicarEfecto(Snake serpiente);
}