package estructuras;

public interface Lista<T> {

    // Altas
    void agregar(T elemento);

    void insertar(int indice, T elemento);

    // Consultas
    T obtener(int indice);

    int indiceDe(T elemento);

    int tamanio();

    boolean estaVacia();

    // Bajas
    T eliminar(int indice);

    void vaciar();
}
