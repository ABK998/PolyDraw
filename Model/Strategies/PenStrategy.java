package Model.Strategies;

import Model.DrawingModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class PenStrategy extends Strategy implements DrawingStrategy {
	public PenStrategy() {
		this.name = "pen";
	}
	
    @Override
    public void onPress(double x, double y, GraphicsContext gc, Canvas canvas, DrawingModel model) {
        gc.setStroke(model.getCurrentColor());
        gc.setLineWidth(model.getBrushSize());
        gc.beginPath();
        gc.moveTo(x, y);
        gc.stroke();
    }

    @Override
    public void onDrag(double x, double y, GraphicsContext gc, DrawingModel model) {
        gc.setStroke(model.getCurrentColor());
        gc.setLineWidth(model.getBrushSize());
        gc.lineTo(x, y);
        gc.stroke();
    }

    @Override
    public void onRelease() {
        // Nothing to clean up for the pen
    }
}