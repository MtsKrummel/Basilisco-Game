package estructuras;

public interface Pila<T> {

    // Operaciones
    void apilar(T elemento);

    T desapilar();

    T tope();

    // Consultas
    boolean estaVacia();

    int tamanio();

    void vaciar();
}
