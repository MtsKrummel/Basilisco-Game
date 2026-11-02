package motor;

public interface Contexto {

    // Servicios que el motor ofrece a cada escena
    Entrada getEntrada();

    int getAncho();

    int getAlto();

    void irA(Escena escena);

    void salir();
}
