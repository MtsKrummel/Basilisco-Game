package motor;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Ventana {

    // Constantes
    private static final int MAXIMO_PASOS = 12;

    // Atributos
    private final Motor motor;
    private final JFrame marco;
    private final JPanel panel;
    private final Timer reloj;
    private long ultimo;
    private double acumulado;

    // Constructor
    public Ventana(String titulo, int ancho, int alto) {
        motor = new Motor(ancho, alto);
        marco = new JFrame(titulo);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                motor.dibujar(g2);
                g2.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(ancho, alto));
        panel.setFocusable(true);
        panel.addKeyListener(motor.getEntrada());
        panel.addMouseListener(motor.getEntrada());
        panel.addMouseMotionListener(motor.getEntrada());

        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setResizable(false);
        marco.add(panel);
        marco.pack();
        marco.setLocationRelativeTo(null);

        reloj = new Timer(8, e -> tick());
    }

    // Apertura desde main
    public static void abrir(String titulo, int ancho, int alto, Escena inicial) {
        SwingUtilities.invokeLater(() -> {
            Ventana ventana = new Ventana(titulo, ancho, alto);
            ventana.iniciar(inicial);
        });
    }

    // Arranque
    public void iniciar(Escena inicial) {
        motor.iniciar(inicial);
        marco.setVisible(true);
        panel.requestFocusInWindow();
        ultimo = System.nanoTime();
        reloj.start();
    }

    // Cuadro
    private void tick() {
        long ahora = System.nanoTime();
        double delta = (ahora - ultimo) / 1e9;
        ultimo = ahora;
        acumulado += Math.min(delta, 0.25);

        int pasos = 0;
        while (acumulado >= Motor.PASO && pasos < MAXIMO_PASOS) {
            motor.paso();
            acumulado -= Motor.PASO;
            pasos++;
        }
        if (pasos == MAXIMO_PASOS) {
            acumulado = 0;
        }

        if (motor.estaTerminado()) {
            reloj.stop();
            marco.dispose();
            System.exit(0);
        }
        panel.repaint();
    }
}
