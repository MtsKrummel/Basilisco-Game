package provisorio;

import estructuras.ListaBase;

public class ListaProvisoria<T> extends ListaBase<T> {

    // Atributos
    private Object[] datos = new Object[16];
    private int tamanio;

    // Altas
    @Override
    public void agregar(T elemento) {
        asegurarCapacidad();
        datos[tamanio++] = elemento;
    }

    @Override
    public void insertar(int indice, T elemento) {
        if (indice < 0 || indice > tamanio) {
            throw new IndexOutOfBoundsException("índice " + indice + ", tamaño " + tamanio);
        }
        asegurarCapacidad();
        System.arraycopy(datos, indice, datos, indice + 1, tamanio - indice);
        datos[indice] = elemento;
        tamanio++;
    }

    // Consultas
    @Override
    @SuppressWarnings("unchecked")
    public T obtener(int indice) {
        validar(indice);
        return (T) datos[indice];
    }

    @Override
    public int tamanio() {
        return tamanio;
    }

    // Bajas
    @Override
    @SuppressWarnings("unchecked")
    public T eliminar(int indice) {
        validar(indice);
        T elemento = (T) datos[indice];
        System.arraycopy(datos, indice + 1, datos, indice, tamanio - indice - 1);
        datos[--tamanio] = null;
        return elemento;
    }

    @Override
    public void vaciar() {
        for (int i = 0; i < tamanio; i++) {
            datos[i] = null;
        }
        tamanio = 0;
    }

    // Auxiliares
    private void validar(int indice) {
        if (indice < 0 || indice >= tamanio) {
            throw new IndexOutOfBoundsException("índice " + indice + ", tamaño " + tamanio);
        }
    }

    private void asegurarCapacidad() {
        if (tamanio == datos.length) {
            Object[] nuevo = new Object[datos.length * 2];
            System.arraycopy(datos, 0, nuevo, 0, tamanio);
            datos = nuevo;
        }
    }
}
