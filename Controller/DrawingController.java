package Controller;

import Model.DrawingModel;
import Model.Strategies.*;
import Model.Command.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class DrawingController {
    private final DrawingModel model;
    private DrawingStrategy currentStrategy;
    private CommandController commandController;
    
    private WritableImage undoSnapshot; // Holds the state before drawing

    public DrawingController(DrawingModel model, CommandController commandController) {
        this.model = model;
        this.commandController = commandController;
        setStrategy("pen"); 
    }

    public void setStrategy(String tool) {
        model.setCurrentTool(tool);
        switch (tool.toLowerCase()) {
        case "pen":    { currentStrategy = new PenStrategy();   break; }
        case "eraser": { currentStrategy = new EraserStrategy(); break; }
        default:       { currentStrategy = new ShapeStrategy();          }
        }
    }

    public void onMousePressed(double x, double y, GraphicsContext gc, Canvas canvas) {
    	gc.setGlobalAlpha(1.0); 
        // 1. Capture the canvas state BEFORE drawing begins
        undoSnapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, undoSnapshot);

        // 2. Let the strategy handle the actual drawing/preview
        currentStrategy.onPress(x, y, gc, canvas, model);
    }

    public void onMouseDragged(double x, double y, GraphicsContext gc) {
    	gc.setGlobalAlpha(1.0); 
        currentStrategy.onDrag(x, y, gc, model);
    }

    // Notice we added 'Canvas canvas' as a parameter here so we can take the final snapshot
    public void onMouseReleased(Canvas canvas) {
        currentStrategy.onRelease();

        // 3. Capture the state AFTER drawing is finished
        WritableImage afterSnapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, afterSnapshot);

        // 4. Save this action to the Undo stack
        commandController.getCommandManager().addCommand(new ImageCommand(canvas, undoSnapshot, afterSnapshot));
        undoSnapshot = null; // Free up memory
    }

    public void clearCanvas(GraphicsContext gc, double width, double height, Canvas canvas) {
        // Save state before clearing so users can undo an accidental clear
        WritableImage beforeClear = new WritableImage((int) width, (int) height);
        canvas.snapshot(null, beforeClear);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        WritableImage afterClear = new WritableImage((int) width, (int) height);
        canvas.snapshot(null, afterClear);

        commandController.getCommandManager().addCommand(new ImageCommand(canvas, beforeClear, afterClear));
    }

    public DrawingModel getModel() { return model; }
    public CommandController getcommandController() { return commandController; }
    public DrawingStrategy getStrategy() { return currentStrategy; }
}