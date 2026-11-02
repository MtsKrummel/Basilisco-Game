package provisorio;

import estructuras.Pila;

public class PilaProvisoria<T> implements Pila<T> {

    // Atributos
    private Object[] datos = new Object[16];
    private int tamanio;

    // Operaciones
    @Override
    public void apilar(T elemento) {
        if (tamanio == datos.length) {
            Object[] nuevo = new Object[datos.length * 2];
            System.arraycopy(datos, 0, nuevo, 0, tamanio);
            datos = nuevo;
        }
        datos[tamanio++] = elemento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T desapilar() {
        if (tamanio == 0) {
            throw new IllegalStateException("la pila está vacía");
        }
        T elemento = (T) datos[--tamanio];
        datos[tamanio] = null;
        return elemento;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T tope() {
        if (tamanio == 0) {
            throw new IllegalStateException("la pila está vacía");
        }
        return (T) datos[tamanio - 1];
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
        for (int i = 0; i < tamanio; i++) {
            datos[i] = null;
        }
        tamanio = 0;
    }
}
