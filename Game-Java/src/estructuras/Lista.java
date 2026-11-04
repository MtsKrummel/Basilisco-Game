package estructuras;

public interface Lista<T> extends Iterable<T> {

    // Altas
    void agregar(T elemento);

    void insertar(int indice, T elemento);

    // Consultas
    T obtener(int indice);

    int indiceDe(T elemento);

    boolean contiene(T elemento);

    int tamanio();

    boolean estaVacia();

    // Bajas
    T eliminar(int indice);

    boolean quitar(T elemento);

    void vaciar();
}
