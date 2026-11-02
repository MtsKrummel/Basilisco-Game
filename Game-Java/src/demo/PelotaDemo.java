package demo;

import fisica.Cuerpo;
import fisica.Material;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class PelotaDemo extends Cuerpo {

    // Constructor
    public PelotaDemo(double x, double y, double radio, Material material) {
        super(x, y, radio, material);
    }

    // Dibujo
    @Override
    public void dibujar(Graphics2D g) {
        g.setColor(getMaterial().getColor());
        g.fill(getForma());
        g.setColor(new Color(0, 0, 0, 120));
        g.setStroke(new BasicStroke(2f));
        g.draw(getForma());
    }
}
