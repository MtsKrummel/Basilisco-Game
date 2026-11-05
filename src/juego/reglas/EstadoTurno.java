package juego.reglas;



public class EstadoTurno {
    private int colaX;
    private int colaY;
    private boolean habiaComido;
    private int puntajePrevio;
    private int vidasPrevias;

    public EstadoTurno(int colaX, int colaY, boolean habiaComido, int puntajePrevio, int vidasPrevias) {
        this.colaX = colaX;
        this.colaY = colaY;
        this.habiaComido = habiaComido;
        this.puntajePrevio = puntajePrevio;
        this.vidasPrevias = vidasPrevias;
    }

    public int getColaX() { return colaX; }
    public int getColaY() { return colaY; }
    public boolean habiaComido() { return habiaComido; }
    public int getPuntajePrevio() { return puntajePrevio; }
    public int getVidasPrevias() { return vidasPrevias; }
}