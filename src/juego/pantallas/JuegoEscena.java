package juego.pantallas;

import estructuras.Lista;
import motor.Escena;
import motor.Temporizador;
import estructuras.ListaDoble;
import estructuras.PilaNodos;
import estructuras.ColaNodos;
import juego.entidades.*;
import juego.reglas.EstadoTurno;
import ui.BarraDeVida;
import ui.Texto;
import estructuras.Lista;
import ui.BarraDeVida;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.util.Random;
import juego.reglas.Nivel;


public class JuegoEscena extends Escena {
    private int nivel;
    private int puntaje;
    private int puntajeAcumulado;
    private int metaPuntos;

    private int columnas;
    private int filas;
    private int tamanioCelda;

    private Snake serpiente;
    private Consumible manzanaActual;
    private Consumible itemEspecialActual;
    private String direccionActual;

    private ColaNodos<String> colaGiros;
    private PilaNodos<EstadoTurno> pilaDeshacer;
    private Temporizador timerPaso;
    private Temporizador timerConsumible;
    private Random random;
    private Lista<Coordenada> obstaculosDelNivel;

    private BufferedImage fondoUI;

    private static BufferedImage cargar(String nombre){
        URL url = JuegoEscena.class.getResource("/juego/pantallas/" + nombre);
        if (url == null) {
            throw new IllegalStateException("No se encontró la imagen: " + nombre);
        }
        try {
            BufferedImage imagen = ImageIO.read(url);
            if (imagen == null) {
                throw new IllegalStateException("Formato no soportado: " + nombre);
            }
            return imagen;
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer la imagen: " + nombre, e);
        }
    }



    public JuegoEscena() {
        this(1, 0);
    }

    public JuegoEscena(int nivel, int puntajeAcumulado) {
        this.nivel = nivel;
        this.puntajeAcumulado = puntajeAcumulado;
        this.puntaje = 0;
        this.colaGiros = new ColaNodos<>();
        this.pilaDeshacer = new PilaNodos<>();
        this.random = new Random();
        this.direccionActual = "DERECHA";
        // Los consumibles desaparecen y cambian si no los comes en 6 segundos
        this.timerConsumible = new Temporizador(6.0);

        fondoUI = cargar("Nivel" + Math.min(nivel, 3) + "/FondoUI.png");

        configurarNivel();
        cargarObstaculosDelNivel(nivel);

        // Inicializar serpiente con la ListaDoble propia
        int xInicial = 5;
        int yInicial = 5;

        this.serpiente = new Snake(new ListaDoble<>(), columnas / 2, filas / 2, 3);
        generarManzana();
        generarItemEspecial();
    }

    private void configurarNivel() {
        columnas = Nivel.columnas;
        filas = Nivel.filas;
        tamanioCelda = Nivel.tamanioCelda;

        if (nivel == 1) {
            metaPuntos = 150;
            timerPaso = new Temporizador(0.25);
        } else if (nivel == 2) {
            metaPuntos = 250;
            timerPaso = new Temporizador(0.20);
        } else {
            metaPuntos = 350;
            timerPaso = new Temporizador(0.15);
        }
    }


    @Override
    public void actualizar(double dt) {
        capturarTeclas();

        if (getContexto().getEntrada().fuePresionada(KeyEvent.VK_Z)) {
            aplicarDeshacer();
        }

        if (timerPaso.avanzar(dt)) {
            avanzarTurno();
            timerPaso.reiniciar(); // <-- Frena a la serpiente
        }

        if (timerConsumible != null && timerConsumible.avanzar(dt)) {
            generarItemEspecial();
            timerConsumible.reiniciar(); // <-- Obliga al ítem a cambiar
        }
    }


    private void generarManzana() {
        int rx, ry;
        do {
            rx = random.nextInt(Nivel.columnas);
            ry = random.nextInt(Nivel.filas);
        } while (!celdaLibre(rx, ry));
        manzanaActual = new Manzana(rx, ry);
    }

    private void generarItemEspecial() {
        int rx, ry;
        do {
            rx = random.nextInt(Nivel.columnas);
            ry = random.nextInt(Nivel.filas);
        } while (!celdaLibre(rx, ry));

        if (random.nextBoolean()) {
            itemEspecialActual = new Cientifico(rx, ry);
        } else {
            itemEspecialActual = new Veneno(rx, ry);
        }
        if (timerConsumible != null) timerConsumible.reiniciar();
    }



    private void capturarTeclas() {
        var entrada = getContexto().getEntrada();
        if (entrada.fuePresionada(KeyEvent.VK_UP) || entrada.fuePresionada(KeyEvent.VK_W)) colaGiros.encolar("ARRIBA");
        if (entrada.fuePresionada(KeyEvent.VK_DOWN) || entrada.fuePresionada(KeyEvent.VK_S)) colaGiros.encolar("ABAJO");
        if (entrada.fuePresionada(KeyEvent.VK_LEFT) || entrada.fuePresionada(KeyEvent.VK_A)) colaGiros.encolar("IZQUIERDA");
        if (entrada.fuePresionada(KeyEvent.VK_RIGHT) || entrada.fuePresionada(KeyEvent.VK_D)) colaGiros.encolar("DERECHA");
    }

    private void reiniciarPosicion() {
        // Guarda las vidas que le quedan y recrea la serpiente en el centro
        int vidasRestantes = serpiente.getVidas();
        this.serpiente = new Snake(new ListaDoble<>(), columnas / 2, filas / 2, vidasRestantes);
        this.direccionActual = "DERECHA";
        this.colaGiros.vaciar();
        this.pilaDeshacer.vaciar();
        if (timerPaso != null) timerPaso.reiniciar();
    }

    private void avanzarTurno() {
        if (!serpiente.estaViva()) {
            getContexto().irA(new FinEscena(puntaje + puntajeAcumulado, false));
            return;
        }



        // Obtener siguiente dirección válida de la Cola
        if (!colaGiros.estaVacia()) {
            String sig = colaGiros.desencolar();
            if (esDireccionValida(sig)) {
                direccionActual = sig;
            }
        }

        SegmentoSnake cabeza = serpiente.getCabeza();
        int nuevoX = cabeza.getX();
        int nuevoY = cabeza.getY();

        switch (direccionActual) {
            case "ARRIBA" -> nuevoY--;
            case "ABAJO" -> nuevoY++;
            case "IZQUIERDA" -> nuevoX--;
            case "DERECHA" -> nuevoX++;
        }

        // Detección de paredes del tablero
        // Detección de choque con la pared
        if (nuevoX < 0 || nuevoX >= columnas || nuevoY < 0 || nuevoY >= filas) {
            serpiente.quitarVida();
            if (serpiente.estaViva()) reiniciarPosicion();
            return;
        }

        // Detección de choque contra su propio cuerpo
        for (int i = 0; i < serpiente.getCuerpo().tamanio(); i++) {
            if (serpiente.getCuerpo().obtener(i).chocaCon(nuevoX, nuevoY)) {
                serpiente.quitarVida();
                if (serpiente.estaViva()) reiniciarPosicion();
                return;
            }
        }

        // Guardar estado en Pila antes de modificar el cuerpo
        SegmentoSnake colaActual = serpiente.getCuerpo().obtener(serpiente.getCuerpo().tamanio() - 1);

        boolean comioManzana = manzanaActual != null && manzanaActual.chocaCon(nuevoX, nuevoY);
        boolean comioEspecial = itemEspecialActual != null && itemEspecialActual.chocaCon(nuevoX, nuevoY);
        boolean comioEnEsteTurno = comioManzana || comioEspecial;

        pilaDeshacer.apilar(new EstadoTurno(colaActual.getX(), colaActual.getY(), comioEnEsteTurno, puntaje, serpiente.getVidas()));

        // Mover cabeza
        serpiente.getCuerpo().insertar(0, new SegmentoSnake(nuevoX, nuevoY));
        if(comioManzana) {
            manzanaActual.alInteractuar(serpiente);
            puntaje+= manzanaActual.getPuntos();
            generarManzana();
            verificarProgresion();
        }

        if (comioEspecial) {
            itemEspecialActual.alInteractuar(serpiente);
            puntaje = Math.max(0, puntaje + itemEspecialActual.getPuntos());
            itemEspecialActual = null;
            verificarProgresion();
        }

        // Interacción con consumible
        if (!comioEnEsteTurno) {
            serpiente.getCuerpo().eliminar(serpiente.getCuerpo().tamanio() - 1);
        }

        if (hayObstaculo(nuevoX, nuevoY)) {
            serpiente.quitarVida();
            if (serpiente.estaViva()) reiniciarPosicion();
            return;
        }
    }

    private boolean celdaLibre(int x, int y) {
        if (hayObstaculo(x, y)) return false;
        for (int i = 0; i < serpiente.getCuerpo().tamanio(); i++) {
            if (serpiente.getCuerpo().obtener(i).chocaCon(x, y)) return false;
        }
        if (manzanaActual != null && manzanaActual.chocaCon(x, y)) return false;
        if (itemEspecialActual != null && itemEspecialActual.chocaCon(x, y)) return false;
        return true;
    }


    private void agregarFrasco(int x, int y, int ancho, int alto) {
        for (int dx = 0; dx < ancho; dx++) {
            for (int dy = 0; dy < alto; dy++) {
                obstaculosDelNivel.agregar(new Coordenada(x + dx, y + dy));
            }
        }
    }

    private boolean hayObstaculo(int cx, int cy) {
        for (int i = 0; i < obstaculosDelNivel.tamanio(); i++) {
            Coordenada c = obstaculosDelNivel.obtener(i);
            if (c.getX() == cx && c.getY() == cy) return true;
        }
        return false;
    }

    private void cargarObstaculosDelNivel(int nivel) {
        obstaculosDelNivel = new ListaDoble<>();

        switch (Math.min(nivel, 3)) {
            case 1:
                agregarFrasco(17, 2, 2, 3);
                agregarFrasco(10, 6, 2, 3);
                agregarFrasco(27, 3, 3, 4);
                break;
            case 2:
                agregarFrasco(11, 1, 2, 3);
                agregarFrasco(18, 1, 2, 3);
                agregarFrasco(4, 2, 2, 3);
                agregarFrasco(25, 2, 2, 3);
                agregarFrasco(7, 5, 2, 3);
                agregarFrasco(22, 5, 2, 3);
                agregarFrasco(15, 7, 2, 3);
                break;
            case 3:
                agregarFrasco(6, 0, 2, 3);
                agregarFrasco(23, 0, 2, 3);
                agregarFrasco(12, 1, 2, 3);
                agregarFrasco(17, 1, 2, 3);
                agregarFrasco(2, 2, 2, 3);
                agregarFrasco(27, 2, 2, 3);
                agregarFrasco(8, 3, 2, 3);
                agregarFrasco(21, 3, 2, 3);
                agregarFrasco(4, 5, 2, 3);
                agregarFrasco(25, 5, 2, 3);
                agregarFrasco(11, 7, 2, 3);
                agregarFrasco(17, 7, 2, 3);
                break;
        }
    }

    private boolean esDireccionValida(String nueva) {
        if (direccionActual.equals("ARRIBA") && nueva.equals("ABAJO")) return false;
        if (direccionActual.equals("ABAJO") && nueva.equals("ARRIBA")) return false;
        if (direccionActual.equals("IZQUIERDA") && nueva.equals("DERECHA")) return false;
        if (direccionActual.equals("DERECHA") && nueva.equals("IZQUIERDA")) return false;
        return true;
    }

    private void aplicarDeshacer() {
        if (pilaDeshacer.estaVacia()) return;

        EstadoTurno previo = pilaDeshacer.desapilar();

        // Quitar la cabeza agregada en el último paso
        if (serpiente.getCuerpo().tamanio() > 1) {
            serpiente.getCuerpo().eliminar(0);
        }

        // Si no había comido, restaurar la cola previa
        if (!previo.habiaComido()) {
            serpiente.getCuerpo().agregar(new SegmentoSnake(previo.getColaX(), previo.getColaY()));
        }

        this.puntaje = Math.max(0, previo.getPuntajePrevio() - 5);
    }


    private void verificarProgresion() {
        if (this.puntaje >= metaPuntos) {
            if (nivel < 3) {
                getContexto().irA(new JuegoEscena(nivel + 1, puntajeAcumulado + puntaje));
            } else {
                getContexto().irA(new FinEscena(puntajeAcumulado + puntaje, true));
            }
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Fondo
        g.drawImage(fondoUI, 0, 0, null);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(Nivel.areaX,Nivel.areaY);


        if (manzanaActual != null) manzanaActual.dibujar(g2d);
        if (itemEspecialActual != null) itemEspecialActual.dibujar(g2d);
        serpiente.dibujar(g2d, direccionActual);

        g2d.dispose();

        // HUD
        Color verdeClaro = new Color(156, 238, 194); // mismo verde del tablero
        Texto.centrado(g, puntaje + " / " + metaPuntos, 735, 95, 64, verdeClaro);
        BarraDeVida.dibujar(g, serpiente.getVidas(), 198, 48);

    }
}