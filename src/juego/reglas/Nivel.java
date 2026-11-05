package juego.reglas;

public final class Nivel {
    public static final int anchoPantalla = 960;
    public static final int altoPantalla = 720;

    public static final int altoUIArriba = 160;
    public static final int bandaColision = 75;
    public static final int altoUIAbajo = 165;

    public static final int tamanioCelda = 32;
    public static final int columnas = 30;
    public static final int filas = 10;
    public static final int areaX = 0;
    public static final int areaY = altoUIArriba + bandaColision;
    public static final int areaAncho = columnas * tamanioCelda;
    public static final int areaAlto = filas * tamanioCelda;
    public static final int UIInferiorY = areaY + areaAlto;

    private Nivel() {
    }
}
