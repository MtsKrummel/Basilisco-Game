package verificador;

import estructuras.ListaDoble;
import estructuras.PilaNodos;
import estructuras.ColaNodos;

public class PruebaMisEstructuras {

    public static void main(String[] args) {

        System.out.println("--- Probando Lista ---");
        VerificadorEstructuras.verificarLista(new ListaDoble<Integer>());

        System.out.println("\n--- Probando Pila ---");
        VerificadorEstructuras.verificarPila(new PilaNodos<Integer>());

        System.out.println("\n--- Probando Cola ---");
        VerificadorEstructuras.verificarCola(new ColaNodos<Integer>());

    }
}