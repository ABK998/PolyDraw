package Controller;

import View.CanvasWindow;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;

public class OpenController {

    private final DrawingController drawingController;

    public OpenController(DrawingController drawingController) {
        this.drawingController = drawingController;
    }

    // ── Public entry point called from Navbar ─────────────────────────────────
    public void open(Window owner) {
        File file = promptForFile(owner);
        if (file == null) return;           // user cancelled

        Image image = loadImage(file);
        if (image == null) return;          // error already shown

        renderToCanvas(image);
    }

    // ── Show file chooser filtered to PNG / JPG ───────────────────────────────
    private File promptForFile(Window owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files (PNG, JPG)", "*.png", "*.jpg", "*.jpeg")
        );
        return fileChooser.showOpenDialog(owner);
    }

    // ── Load image from disk, show error dialog on failure ────────────────────
    private Image loadImage(File file) {
        try {
            return new Image(new FileInputStream(file));
        } catch (Exception ex) {
            showError("Could not open image", ex.getMessage());
            return null;
        }
    }

    private static final double CANVAS_PADDING = 40; // extra space around the image

    // ── Resize canvas to fit image (+ padding), then draw at natural size ─────
    private void renderToCanvas(Image image) {
        double imgW = image.getWidth();
        double imgH = image.getHeight();

        CanvasWindow.resize(imgW + CANVAS_PADDING, imgH + CANVAS_PADDING);

        // Draw the image at its natural size, offset by half the padding on each side
        double offsetX = CANVAS_PADDING / 2;
        double offsetY = CANVAS_PADDING / 2;
        CanvasWindow.getGraphicsContext().drawImage(image, offsetX, offsetY, imgW, imgH);
    }

    // ── Generic error dialog ──────────────────────────────────────────────────
    private void showError(String header, String message) {
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("Open Failed");
        error.setHeaderText(header);
        error.setContentText(message);
        error.showAndWait();
    }
}