package contratos;

import java.awt.geom.Rectangle2D;

public interface Colisionable {

    // Caja que ocupa en el mundo
    Rectangle2D getLimites();

    // Reacción al tocar a otro objeto
    void alColisionar(Colisionable otro);
}
