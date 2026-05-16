// Model/Shapes/DiamondShape.java
package Model.Shapes;

import Model.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Diamond implements Drawable {

    private double x, y, width, height;
    private Color color;
    private double strokeWidth;

    public Diamond(double x, double y, double width, double height,
                        Color color, double strokeWidth) {
        this.x           = x;
        this.y           = y;
        this.width       = width;
        this.height      = height;
        this.color       = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double midX = x + width  / 2;
        double midY = y + height / 2;

        double[] xPoints = { midX,        x + width, midX,   x };
        double[] yPoints = { y,           midY,      y + height, midY };

        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokePolygon(xPoints, yPoints, 4);
    }
}