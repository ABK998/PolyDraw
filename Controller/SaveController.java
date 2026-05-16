// Controller/SaveManager.java
package Controller;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SaveController {

    // Remembers last saved path for quick Ctrl+S
    private static File lastSavedFile = null;

    // ── Save (uses last path if available, else opens dialog) ────────────────
    public static void save(Canvas canvas, Window ownerWindow) {
        if (lastSavedFile != null) {
            writeToFile(canvas, lastSavedFile);
        } else {
            saveAs(canvas, ownerWindow);
        }
    }

    // ── Save As (always opens file chooser) ──────────────────────────────────
    public static void saveAs(Canvas canvas, Window ownerWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image As");
        fileChooser.setInitialFileName(
            lastSavedFile != null ? lastSavedFile.getName() : "drawing"
        );
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG Image",  "*.png"),
            new FileChooser.ExtensionFilter("JPEG Image", "*.jpg")
        );

        File file = fileChooser.showSaveDialog(ownerWindow);
        if (file != null) {
            lastSavedFile = file;
            writeToFile(canvas, file);
        }
    }

    // ── Clear canvas to white ─────────────────────────────────────────────────
    public static void clearCanvas(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // ── Core write logic ──────────────────────────────────────────────────────
    private static void writeToFile(Canvas canvas, File file) {
        WritableImage image = new WritableImage(
            (int) canvas.getWidth(),
            (int) canvas.getHeight()
        );
        canvas.snapshot(null, image);

        String name   = file.getName().toLowerCase();
        String format = name.endsWith(".jpg") || name.endsWith(".jpeg") ? "jpg" : "png";

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), format, file);
            showInfo("Saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to save: " + e.getMessage());
        }
    }

    // ── Dialogs ───────────────────────────────────────────────────────────────
    private static void showInfo(String message) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION
            );
            alert.setTitle("Saved");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private static void showError(String message) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Save Error");
            alert.setHeaderText("Could not save image");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}