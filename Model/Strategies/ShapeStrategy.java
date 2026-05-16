package Model.Strategies;

import Model.Drawable;
import Model.DrawingModel;
import Model.ShapeFactory;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;

public class ShapeStrategy extends Strategy implements DrawingStrategy {
    private double startX, startY;
    private WritableImage snapshot;
    
    public ShapeStrategy() {
    	this.name = "shape";
    }

    @Override
    public void onPress(double x, double y, GraphicsContext gc, Canvas canvas, DrawingModel model) {
        startX = x;
        startY = y;
        // Take a snapshot of the canvas before we start dragging the shape
        snapshot = canvas.snapshot(null, null);
    }

    @Override
    public void onDrag(double x, double y, GraphicsContext gc, DrawingModel model) {
        // Restore the clean snapshot
    	gc.setGlobalAlpha(1.0);
        gc.drawImage(snapshot, 0, 0);
        
        // Draw the new shape over it (Live Preview)
        Drawable shape = ShapeFactory.createShape(
            model.getCurrentTool(), 
            startX, startY, 
            x, y, 
            model.getCurrentColor(), 
            model.getBrushSize()
        );
        
        if (shape != null) {
            shape.draw(gc);
        }
    }

    @Override
    public void onRelease() {
        // Clear the snapshot from memory once the mouse is released
        snapshot = null;
    }
}