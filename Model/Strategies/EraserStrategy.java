package Model.Strategies;

import Model.DrawingModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EraserStrategy extends Strategy implements DrawingStrategy {
	public EraserStrategy() {
		this.name = "eraser";
	}
	
    @Override
    public void onPress(double x, double y, GraphicsContext gc, Canvas canvas, DrawingModel model) {
        gc.setStroke(Color.WHITE); // Eraser is just a white pen
        gc.setLineWidth(model.getEraserSize());
        gc.beginPath();
        gc.moveTo(x, y);
        gc.stroke();
    }

    @Override
    public void onDrag(double x, double y, GraphicsContext gc, DrawingModel model) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(model.getEraserSize());
        gc.lineTo(x, y);
        gc.stroke();
    }

    @Override
    public void onRelease() {
        // Nothing to clean up for the eraser
    }
    
}