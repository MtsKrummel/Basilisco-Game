package fisica;

import java.awt.Color;

public enum Material {

    // Materiales predefinidos
    GOMA(1.0, 0.90, new Color(230, 100, 40)),
    ESPUMA(0.15, 0.75, new Color(240, 215, 90)),
    MADERA(0.7, 0.50, new Color(160, 110, 60)),
    ACERO(7.8, 0.35, new Color(150, 160, 175)),
    PLASTILINA(1.5, 0.05, new Color(90, 170, 110));

    // Atributos
    private final double densidad;
    private final double restitucion;
    private final Color color;

    // Constructor
    Material(double densidad, double restitucion, Color color) {
        this.densidad = densidad;
        this.restitucion = restitucion;
        this.color = color;
    }

    // Accesores
    public double getDensidad() {
        return densidad;
    }

    public double getRestitucion() {
        return restitucion;
    }

    public Color getColor() {
        return color;
    }
}
