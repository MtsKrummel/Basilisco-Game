package motor;

import contratos.Actualizable;
import contratos.Dibujable;

public abstract class Escena implements Actualizable, Dibujable {

    // Atributos
    private Contexto contexto;

    // Conexión con el motor
    public final void conectar(Contexto contexto) {
        this.contexto = contexto;
    }

    protected Contexto getContexto() {
        return contexto;
    }

    protected Entrada getEntrada() {
        return contexto.getEntrada();
    }

    // Ganchos del ciclo de vida
    public void alEntrar() {
    }

    public void alSalir() {
    }
}
