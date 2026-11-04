package fisica;

import contratos.Colisionable;
import entidades.Entidad;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public abstract class Cuerpo extends Entidad implements Colisionable {

    // Atributos
    private final Material material;
    private double radio;
    private double densidad;
    private double restitucion;
    private double gravedad = Fisica.GRAVEDAD;

    // Constructor
    protected Cuerpo(double x, double y, double radio, Material material) {
        super(x, y);
        if (material == null) {
            throw new IllegalArgumentException("el material no puede ser nulo");
        }
        this.material = material;
        setRadio(radio);
        setDensidad(material.getDensidad());
        setRestitucion(material.getRestitucion());
    }

    // Propiedades físicas
    public Material getMaterial() {
        return material;
    }

    public double getRadio() {
        return radio;
    }

    public final void setRadio(double radio) {
        if (!(radio > 0)) {
            throw new IllegalArgumentException("el radio debe ser positivo: " + radio);
        }
        this.radio = radio;
    }

    public double getDensidad() {
        return densidad;
    }

    public final void setDensidad(double densidad) {
        if (!(densidad > 0)) {
            throw new IllegalArgumentException("la densidad debe ser positiva: " + densidad);
        }
        this.densidad = densidad;
    }

    public double getRestitucion() {
        return restitucion;
    }

    public final void setRestitucion(double restitucion) {
        if (!(restitucion >= 0 && restitucion <= 1)) {
            throw new IllegalArgumentException("la restitución debe estar entre 0 y 1: " + restitucion);
        }
        this.restitucion = restitucion;
    }

    public double getGravedad() {
        return gravedad;
    }

    public void setGravedad(double gravedad) {
        if (!Double.isFinite(gravedad)) {
            throw new IllegalArgumentException("gravedad inválida: " + gravedad);
        }
        this.gravedad = gravedad;
    }

    public double getMasa() {
        return densidad * Math.PI * radio * radio;
    }

    // Geometría
    public Ellipse2D.Double getForma() {
        return new Ellipse2D.Double(getX() - radio, getY() - radio, 2 * radio, 2 * radio);
    }

    @Override
    public Rectangle2D getLimites() {
        return getForma().getBounds2D();
    }

    // Comportamiento por defecto
    @Override
    public void actualizar(double dt) {
        setVelocidad(getVx(), getVy() + gravedad * dt);
        mover(dt);
    }

    @Override
    public void ajustarALimites(double ancho, double alto) {
        Fisica.rebotarEnLimites(this, ancho, alto);
    }

    @Override
    public void alColisionar(Colisionable otro) {
        if (otro instanceof Cuerpo) {
            Fisica.resolverChoque(this, (Cuerpo) otro);
        }
    }
}
