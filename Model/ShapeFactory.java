// Model/ShapeFactory.java
package Model;

import Model.Shapes.*;
import javafx.scene.paint.Color;

public class ShapeFactory {

    public static Drawable createShape(String tool,
                                       double startX, double startY,
                                       double endX,   double endY,
                                       Color color,   double strokeWidth) {

        double left   = Math.min(startX, endX);
        double top    = Math.min(startY, endY);
        double width  = Math.abs(endX - startX);
        double height = Math.abs(endY - startY);

        switch (tool) {

            case "line":
                return new Line(startX, startY, endX, endY, color, strokeWidth);

            case "rect":
                return new Rectangle(left, top, width, height, color, strokeWidth, false);

            case "filledrect":
                return new Rectangle(left, top, width, height, color, strokeWidth, true);

            case "circle":
                return new Circle(left, top, width, height, color, strokeWidth, false);

            case "filledcircle":
                return new Circle(left, top, width, height, color, strokeWidth, true);

            case "triangle":
                return new Triangle(left, top, width, height, color, strokeWidth);

            case "diamond":
                return new Diamond(left, top, width, height, color, strokeWidth);

            case "arrow":
                return new Arrow(left, top, width, height, color, strokeWidth);

            case "star":
                return new Star(
                    startX + width / 2,  // centerX
                    startY + height / 2, // centerY
                    width  / 2,          // outerRadius
                    width  / 4,          // innerRadius
                    5,                   // points
                    color, strokeWidth
                );

            default:
                throw new IllegalArgumentException("Unknown shape tool: " + tool);
        }
    }
}