// Model/Shapes/LineShape.java
package Model.Shapes;

import Model.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Line implements Drawable {

    private double x1, y1, x2, y2;
    private Color color;
    private double strokeWidth;

    public Line(double x1, double y1, double x2, double y2,
                     Color color, double strokeWidth) {
        this.x1          = x1;
        this.y1          = y1;
        this.x2          = x2;
        this.y2          = y2;
        this.color       = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokeLine(x1, y1, x2, y2);
    }
}