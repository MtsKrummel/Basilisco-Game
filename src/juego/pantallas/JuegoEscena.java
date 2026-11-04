package juego.pantallas;

import motor.Escena;
import motor.Temporizador;
import estructuras.ListaDoble;
import estructuras.PilaNodos;
import estructuras.ColaNodos;
import juego.entidades.*;
import juego.reglas.EstadoTurno;
import ui.Texto;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.Random;

public class JuegoEscena extends Escena {
    private int nivel;
    private int puntaje;
    private int metaPuntos;

    private int columnas;
    private int filas;
    private int tamanioCelda;

    private Snake serpiente;
    private Consumible consumibleActual;
    private String direccionActual;

    private ColaNodos<String> colaGiros;
    private PilaNodos<EstadoTurno> pilaDeshacer;
    private Temporizador timerPaso;
    private Temporizador timerConsumible;
    private Random random;

    public JuegoEscena() {
        this(1, 0);
    }

    public JuegoEscena(int nivel, int puntajeAcumulado) {
        this.nivel = nivel;
        this.puntaje = puntajeAcumulado;
        this.colaGiros = new ColaNodos<>();
        this.pilaDeshacer = new PilaNodos<>();
        this.random = new Random();
        this.direccionActual = "DERECHA";
        // Los consumibles desaparecen y cambian si no los comes en 6 segundos
        this.timerConsumible = new Temporizador(6.0);

        configurarNivel();

        // Inicializar serpiente con la ListaDoble propia
        this.serpiente = new Snake(new ListaDoble<>(), columnas / 2, filas / 2, 3);
        generarNuevoConsumible();
    }

    private void configurarNivel() {
        if (nivel == 1) {
            columnas = 15;
            filas = 15;
            metaPuntos = 50;
            timerPaso = new Temporizador(0.35);
        } else if (nivel == 2) {
            columnas = 22;
            filas = 22;
            metaPuntos = 150;
            timerPaso = new Temporizador(0.25);
        } else {
            columnas = 30;
            filas = 30;
            metaPuntos = 300;
            timerPaso = new Temporizador(0.18);
        }
    }

    @Override
    public void alEntrar() {
        this.tamanioCelda = getContexto().getAncho() / columnas;
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
            generarNuevoConsumible();
            timerConsumible.reiniciar(); // <-- Obliga al ítem a cambiar
        }
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
            getContexto().irA(new FinEscena(this.puntaje, false));
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
        boolean comioEnEsteTurno = consumibleActual.chocaCon(nuevoX, nuevoY);

        pilaDeshacer.apilar(new EstadoTurno(colaActual.getX(), colaActual.getY(), comioEnEsteTurno, puntaje, serpiente.getVidas()));

        // Mover cabeza
        serpiente.getCuerpo().insertar(0, new SegmentoSnake(nuevoX, nuevoY));

        // Interacción con consumible
        if (comioEnEsteTurno) {
            consumibleActual.alInteractuar(serpiente);
            puntaje += consumibleActual.getPuntos();
            generarNuevoConsumible();
            verificarProgresion();
        } else {
            serpiente.getCuerpo().eliminar(serpiente.getCuerpo().tamanio() - 1);
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

        this.puntaje = Math.max(0, previo.getPuntajePrevio() - 5); // Penalización de 5 puntos por usar Z
    }

    private void generarNuevoConsumible() {
        int rx = random.nextInt(columnas);
        int ry = random.nextInt(filas);
        int tipo = random.nextInt(10);

        if (tipo < 6) {
            consumibleActual = new Manzana(rx, ry);
        } else if (tipo < 8) {
            consumibleActual = new Cientifico(rx, ry);
        } else {
            consumibleActual = new Veneno(rx, ry);
        }
        if (timerConsumible != null) {
            timerConsumible.reiniciar();
        }
    }

    private void verificarProgresion() {
        if (this.puntaje >= metaPuntos) {
            if (nivel < 3) {
                getContexto().irA(new JuegoEscena(nivel + 1, puntaje));
            } else {
                getContexto().irA(new FinEscena(puntaje, true));
            }
        }
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Fondo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getContexto().getAncho(), getContexto().getAlto());

        // Dibujar Entidades
        if (consumibleActual != null) consumibleActual.dibujar(g);
        serpiente.dibujar(g);

        // HUD
        g.setColor(Color.WHITE);
        Texto.izquierda(g, "Nivel: " + nivel + " | Puntos: " + puntaje + " / " + metaPuntos + " | Vidas: " + serpiente.getVidas(), 10, 25, 18, Color.WHITE);
        Texto.derecha(g, "Z: Deshacer", getContexto().getAncho() - 10, 25, 16, Color.YELLOW);
    }
}