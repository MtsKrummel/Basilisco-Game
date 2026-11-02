package estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class ListaBase<T> implements Lista<T> {

    // Operaciones derivadas
    @Override
    public boolean estaVacia() {
        return tamanio() == 0;
    }

    @Override
    public int indiceDe(T elemento) {
        for (int i = 0; i < tamanio(); i++) {
            T actual = obtener(i);
            boolean iguales = actual == null ? elemento == null : actual.equals(elemento);
            if (iguales) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contiene(T elemento) {
        return indiceDe(elemento) >= 0;
    }

    @Override
    public boolean quitar(T elemento) {
        int indice = indiceDe(elemento);
        if (indice < 0) {
            return false;
        }
        eliminar(indice);
        return true;
    }

    // Recorrido con for-each
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int actual = 0;

            @Override
            public boolean hasNext() {
                return actual < tamanio();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return obtener(actual++);
            }
        };
    }
}
