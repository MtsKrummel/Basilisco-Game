package fisica;

public final class Fisica {

    // Constantes
    public static final double GRAVEDAD = 900.0;
    public static final double UMBRAL_REPOSO = 12.0;
    public static final double ROZAMIENTO_PISO = 0.995;

    // Sin instancias
    private Fisica() {
    }

    // Distancia entre dos puntos
    public static double distancia(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Velocidad dirigida a un punto
    public static void dirigir(Cuerpo cuerpo, double objetivoX, double objetivoY, double rapidez) {
        double dx = objetivoX - cuerpo.getX();
        double dy = objetivoY - cuerpo.getY();
        double largo = Math.sqrt(dx * dx + dy * dy);
        if (largo < 1e-9) {
            cuerpo.setVelocidad(0, -rapidez);
            return;
        }
        cuerpo.setVelocidad(dx / largo * rapidez, dy / largo * rapidez);
    }

    // Rebote contra los bordes del mundo
    public static void rebotarEnLimites(Cuerpo c, double ancho, double alto) {
        double r = c.getRadio();
        double e = c.getRestitucion();
        double x = c.getX();
        double y = c.getY();
        double vx = c.getVx();
        double vy = c.getVy();

        if (x < r) {
            x = r;
            if (vx < 0) {
                vx = -vx * e;
            }
        } else if (x > ancho - r) {
            x = ancho - r;
            if (vx > 0) {
                vx = -vx * e;
            }
        }

        if (y < r) {
            y = r;
            if (vy < 0) {
                vy = -vy * e;
            }
        } else if (y >= alto - r) {
            y = alto - r;
            if (vy > 0) {
                vy = -vy * e;
                if (Math.abs(vy) < UMBRAL_REPOSO) {
                    vy = 0;
                }
            }
            if (vy == 0) {
                vx *= ROZAMIENTO_PISO;
            }
        }

        c.setPosicion(x, y);
        c.setVelocidad(vx, vy);
    }

    // Choque entre dos cuerpos circulares
    public static void resolverChoque(Cuerpo a, Cuerpo b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double distancia = Math.sqrt(dx * dx + dy * dy);
        double suma = a.getRadio() + b.getRadio();
        if (distancia >= suma) {
            return;
        }

        double nx = 1;
        double ny = 0;
        if (distancia > 1e-9) {
            nx = dx / distancia;
            ny = dy / distancia;
        }

        double inversaA = 1.0 / a.getMasa();
        double inversaB = 1.0 / b.getMasa();
        double total = inversaA + inversaB;

        // Separación proporcional a la masa
        double solape = suma - distancia;
        a.setPosicion(a.getX() - nx * solape * inversaA / total, a.getY() - ny * solape * inversaA / total);
        b.setPosicion(b.getX() + nx * solape * inversaB / total, b.getY() + ny * solape * inversaB / total);

        // Impulso
        double aproximacion = (a.getVx() - b.getVx()) * nx + (a.getVy() - b.getVy()) * ny;
        if (aproximacion <= 0) {
            return;
        }
        double e = Math.min(a.getRestitucion(), b.getRestitucion());
        double impulso = (1 + e) * aproximacion / total;
        a.setVelocidad(a.getVx() - impulso * inversaA * nx, a.getVy() - impulso * inversaA * ny);
        b.setVelocidad(b.getVx() + impulso * inversaB * nx, b.getVy() + impulso * inversaB * ny);
    }
}
