package provisorio;

import estructuras.Cola;

public class ColaProvisoria<T> implements Cola<T> {

    // Atributos
    private Object[] datos = new Object[16];
    private int inicio;
    private int tamanio;

    // Operaciones
    @Override
    public void encolar(T elemento) {
        if (tamanio == datos.length) {
            crecer();
        }
        datos[(inicio + tamanio) % datos.length] = elemento;
        tamanio++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T desencolar() {
        if (tamanio == 0) {
            throw new IllegalStateException("la cola está vacía");
        }
        T elemento = (T) datos[inicio];
        datos[inicio] = null;
        inicio = (inicio + 1) % datos.length;
        tamanio--;
        return elemento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T frente() {
        if (tamanio == 0) {
            throw new IllegalStateException("la cola está vacía");
        }
        return (T) datos[inicio];
    }

    // Consultas
    @Override
    public boolean estaVacia() {
        return tamanio == 0;
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    @Override
    public void vaciar() {
        for (int i = 0; i < datos.length; i++) {
            datos[i] = null;
        }
        inicio = 0;
        tamanio = 0;
    }

    // Crecimiento
    private void crecer() {
        Object[] nuevo = new Object[datos.length * 2];
        for (int i = 0; i < tamanio; i++) {
            nuevo[i] = datos[(inicio + i) % datos.length];
        }
        datos = nuevo;
        inicio = 0;
    }
}
