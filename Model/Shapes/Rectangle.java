// Model/Shapes/RectangleShape.java
package Model.Shapes;

import Model.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Rectangle implements Drawable {

    private double x, y, width, height;
    private Color color;
    private double strokeWidth;
    private boolean filled;

    public Rectangle(double x, double y, double width, double height,
                          Color color, double strokeWidth, boolean filled) {
        this.x           = x;
        this.y           = y;
        this.width       = width;
        this.height      = height;
        this.color       = color;
        this.strokeWidth = strokeWidth;
        this.filled      = filled;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setLineWidth(strokeWidth);
        if (filled) {
            gc.setFill(color);
            gc.fillRect(x, y, width, height);
        } else {
            gc.setStroke(color);
            gc.strokeRect(x, y, width, height);
        }
    }
}