package motor;

public class Temporizador {

    // Atributos
    private final double intervalo;
    private double acumulado;

    // Constructor
    public Temporizador(double intervalo) {
        if (!(intervalo > 0)) {
            throw new IllegalArgumentException("el intervalo debe ser positivo: " + intervalo);
        }
        this.intervalo = intervalo;
    }

    // Avance
    public boolean avanzar(double dt) {
        acumulado += dt;
        if (acumulado + 1e-9 >= intervalo) {
            acumulado -= intervalo;
            return true;
        }
        return false;
    }

    // Consultas
    public double getProgreso() {
        return acumulado / intervalo;
    }

    public double getIntervalo() {
        return intervalo;
    }

    // Reinicio
    public void reiniciar() {
        acumulado = 0;
    }
}
