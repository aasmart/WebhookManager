package guiComponents;

import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;

/**
 * A {@link AbstractBorder} for creating a rounded border
 */
public class RoundedBorder extends AbstractBorder {
    private final Color color;
    private final int thickness;
    private final int radii;
    private final Insets insets;
    private final BasicStroke stroke;
    private final int strokePad;
    RenderingHints hints;

    /**
     * Creates the rounded border
     *
     * @param color The color of the border outline
     * @param thickness The thickness of the border outline
     * @param radii The radius of the rounded border
     */
    public RoundedBorder(Color color, int thickness, int radii) {
        this.thickness = thickness;
        this.radii = radii;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(strokePad, strokePad, width - thickness, bottomLineY, radii, radii);

        Area area = new Area(bubble);

        g2.setRenderingHints(hints);

        // Paint the background color of the parent
        Component parent  = c.getParent();
        if (parent != null) {
            Graphics2D g2d = (Graphics2D)g2.create();
            Color background = parent.getBackground();
            Rectangle rect = g2d.getClip().getBounds();
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2d.setClip(borderRegion);
            g2d.setColor(background);
            g2d.fillRect(0, 0, width, height);
            g2d.dispose();
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}
