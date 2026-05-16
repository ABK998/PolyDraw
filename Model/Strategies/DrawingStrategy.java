package Model.Strategies;

import Model.DrawingModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public interface DrawingStrategy {
    void onPress(double x, double y, GraphicsContext gc, Canvas canvas, DrawingModel model);
    void onDrag(double x, double y, GraphicsContext gc, DrawingModel model);
    void onRelease();
    String getName();
}