package estructuras;

public class PilaNodos<T> implements Pila<T> {
    private class Nodo {
        T elemento;
        Nodo siguiente;

        Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    private Nodo tope;
    private int cantidad;

    @Override
    public void apilar(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        nuevo.siguiente = tope;
        tope = nuevo;
        cantidad++;
    }

    @Override
    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desapilar de una pila vacía");
        }
        T dato = tope.elemento;
        tope = tope.siguiente;
        cantidad--;
        return dato;
    }

    @Override
    public T tope() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return tope.elemento;
    }

    @Override
    public boolean estaVacia() {
        return tope == null;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        tope = null;
        cantidad = 0;
    }
}