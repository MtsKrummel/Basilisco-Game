package motor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Entrada implements KeyListener, MouseListener, MouseMotionListener {

    // Constantes
    private static final int TECLAS = 1024;

    // Estado del teclado
    private final boolean[] abajo = new boolean[TECLAS];
    private final boolean[] nuevas = new boolean[TECLAS];

    // Estado del mouse
    private int mouseX;
    private int mouseY;
    private boolean botonAbajo;
    private boolean click;

    // Consultas de teclado
    public boolean estaPresionada(int codigo) {
        return codigo >= 0 && codigo < TECLAS && abajo[codigo];
    }

    public boolean fuePresionada(int codigo) {
        return codigo >= 0 && codigo < TECLAS && nuevas[codigo];
    }

    // Consultas de mouse
    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean mouseAbajo() {
        return botonAbajo;
    }

    public boolean huboClick() {
        return click;
    }

    // Eventos simulados (para pruebas sin ventana)
    public void presionarTecla(int codigo) {
        if (codigo >= 0 && codigo < TECLAS) {
            if (!abajo[codigo]) {
                nuevas[codigo] = true;
            }
            abajo[codigo] = true;
        }
    }

    public void soltarTecla(int codigo) {
        if (codigo >= 0 && codigo < TECLAS) {
            abajo[codigo] = false;
        }
    }

    public void moverMouse(int x, int y) {
        mouseX = x;
        mouseY = y;
    }

    public void apretarMouse() {
        if (!botonAbajo) {
            click = true;
        }
        botonAbajo = true;
    }

    public void soltarMouse() {
        botonAbajo = false;
    }

    // Fin de cada paso del motor
    public void terminarCuadro() {
        for (int i = 0; i < TECLAS; i++) {
            nuevas[i] = false;
        }
        click = false;
    }

    // Eventos de Swing: teclado
    @Override
    public void keyPressed(KeyEvent e) {
        presionarTecla(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        soltarTecla(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Eventos de Swing: mouse
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            moverMouse(e.getX(), e.getY());
            apretarMouse();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            soltarMouse();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        moverMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        moverMouse(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
