package Model.Command;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;

public class ImageCommand implements Command {
    private final Canvas canvas;
    private final WritableImage beforeState;
    private final WritableImage afterState;

    public ImageCommand(Canvas canvas, WritableImage beforeState, WritableImage afterState) {
        this.canvas = canvas;
        this.beforeState = beforeState;
        this.afterState = afterState;
    }

    @Override
    public void undo() {
        canvas.getGraphicsContext2D().drawImage(beforeState, 0, 0);
    }

    @Override
    public void redo() {
        canvas.getGraphicsContext2D().drawImage(afterState, 0, 0);
    }
}