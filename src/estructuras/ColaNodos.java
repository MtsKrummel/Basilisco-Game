package estructuras;

public class ColaNodos<T> implements Cola<T> {
    private class Nodo {
        T elemento;
        Nodo siguiente;

        Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    private Nodo frente;
    private Nodo fin;
    private int cantidad;

    @Override
    public void encolar(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        if (estaVacia()) {
            frente = nuevo;
        } else {
            fin.siguiente = nuevo;
        }
        fin = nuevo;
        cantidad++;
    }

    @Override
    public T desencolar() {
        if (estaVacia()) {
            throw new IllegalStateException("No se puede desencolar de una cola vacía");
        }
        T dato = frente.elemento;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        cantidad--;
        return dato;
    }

    @Override
    public T frente() {
        if (estaVacia()) {
            throw new IllegalStateException("La cola está vacía");
        }
        return frente.elemento;
    }

    @Override
    public boolean estaVacia() {
        return frente == null;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        frente = null;
        fin = null;
        cantidad = 0;
    }
}