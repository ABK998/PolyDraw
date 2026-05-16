// Model/Shapes/ArrowShape.java
package Model.Shapes;

import Model.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Arrow implements Drawable {

    private double x, y, width, height;
    private Color color;
    private double strokeWidth;

    public Arrow(double x, double y, double width, double height,
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
        double midY      = y + height / 2;
        double arrowHead = width * 0.35; // head takes 35% of width
        double shaftTop  = y + height * 0.3;
        double shaftBot  = y + height * 0.7;

        double[] xPoints = {
            x,                      // shaft left top
            x + width - arrowHead,  // shaft right top
            x + width - arrowHead,  // head top
            x + width,              // tip
            x + width - arrowHead,  // head bottom
            x + width - arrowHead,  // shaft right bottom
            x                       // shaft left bottom
        };

        double[] yPoints = {
            shaftTop,
            shaftTop,
            y,
            midY,
            y + height,
            shaftBot,
            shaftBot
        };

        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokePolygon(xPoints, yPoints, 7);
    }
}