package estructuras;

public interface Cola<T> {

    // Operaciones
    void encolar(T elemento);

    T desencolar();

    T frente();

    // Consultas
    boolean estaVacia();

    int tamanio();

    void vaciar();
}
