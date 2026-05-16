// Model/Shapes/StarShape.java
package Model.Shapes;

import Model.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Star implements Drawable {

    private double centerX, centerY;
    private double outerRadius, innerRadius;
    private int points;
    private Color color;
    private double strokeWidth;

    public Star(double centerX, double centerY, double outerRadius,
                     double innerRadius, int points, Color color, double strokeWidth) {
        this.centerX     = centerX;
        this.centerY     = centerY;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.points      = points;
        this.color       = color;
        this.strokeWidth = strokeWidth;
    }

    @Override
    public void draw(GraphicsContext gc) {
        double[] xPoints = new double[points * 2];
        double[] yPoints = new double[points * 2];

        for (int i = 0; i < points * 2; i++) {
            double angle  = Math.toRadians(i * 180.0 / points - 90);
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + radius * Math.cos(angle);
            yPoints[i] = centerY + radius * Math.sin(angle);
        }

        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);
        gc.strokePolygon(xPoints, yPoints, points * 2);
    }
}