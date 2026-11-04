package estructuras;

public class ListaDoble<T> extends ListaBase<T> {
    private class Nodo {
        T dato;
        Nodo anterior;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
        }
    }

    private Nodo cabeza;
    private Nodo cola;
    private int cantidad;

    @Override
    public void agregar(T elemento) {
        Nodo nuevo = new Nodo(elemento);
        if (tamanio() == 0) {
            cabeza = nuevo;
            cola = nuevo;
        } else {
            cola.siguiente = nuevo;
            nuevo.anterior = cola;
            cola = nuevo;
        }
        cantidad++;
    }

    @Override
    public void insertar(int i, T elemento) {
        if (i < 0 || i > cantidad) throw new IndexOutOfBoundsException("Índice fuera de rango");
        if (i == cantidad) {
            agregar(elemento);
            return;
        }
        Nodo nuevo = new Nodo(elemento);
        if (i == 0) {
            nuevo.siguiente = cabeza;
            cabeza.anterior = nuevo;
            cabeza = nuevo;
        } else {
            Nodo actual = obtenerNodo(i);
            nuevo.siguiente = actual;
            nuevo.anterior = actual.anterior;
            actual.anterior.siguiente = nuevo;
            actual.anterior = nuevo;
        }
        cantidad++;
    }

    private Nodo obtenerNodo(int i) {
        if (i < 0 || i >= cantidad) throw new IndexOutOfBoundsException("Índice fuera de rango");
        Nodo actual;
        // Optimización: buscar desde la mitad más cercana
        if (i < cantidad / 2) {
            actual = cabeza;
            for (int j = 0; j < i; j++) actual = actual.siguiente;
        } else {
            actual = cola;
            for (int j = cantidad - 1; j > i; j--) actual = actual.anterior;
        }
        return actual;
    }

    @Override
    public T obtener(int i) {
        return obtenerNodo(i).dato;
    }

    @Override
    public T eliminar(int i) {
        Nodo aEliminar = obtenerNodo(i);
        if (aEliminar.anterior != null) {
            aEliminar.anterior.siguiente = aEliminar.siguiente;
        } else {
            cabeza = aEliminar.siguiente;
        }
        if (aEliminar.siguiente != null) {
            aEliminar.siguiente.anterior = aEliminar.anterior;
        } else {
            cola = aEliminar.anterior;
        }
        cantidad--;
        return aEliminar.dato;
    }

    @Override
    public int tamanio() {
        return cantidad;
    }

    @Override
    public void vaciar() {
        cabeza = null;
        cola = null;
        cantidad = 0;
    }
}