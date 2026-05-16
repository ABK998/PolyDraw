// Model/Shapes/TriangleShape.java
package Model.Shapes;

import Model.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Triangle implements Drawable {

    private double x, y, width, height;
    private Color color;
    private double strokeWidth;

    public Triangle(double x, double y, double width, double height,
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
        double[] xPoints = { x + width / 2, x + width, x };
        double[] yPoints = { y, y + height, y + height };

        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokePolygon(xPoints, yPoints, 3);
    }
}