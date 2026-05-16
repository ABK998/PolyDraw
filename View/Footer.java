// View/Footer.java
package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class Footer {

    private static Label   sizeLabel;
    private static Slider  zoomSlider;
    private static Spinner<Integer> zoomSpinner;

    // Prevents the slider and spinner from triggering each other in a loop
    private static boolean syncing = false;

    public static HBox getFooter() {
        HBox footer = new HBox(8);
        footer.setPadding(new Insets(4, 8, 4, 8));
        footer.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1 0 0 0;");
        footer.setAlignment(Pos.CENTER_LEFT);

        Label statusLabel = new Label("Ready");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        sizeLabel = new Label("900 x 600 px");
        sizeLabel.setStyle("-fx-text-fill: #555;");

        // ── Zoom slider (10% – 400%) ──────────────────────────────────────
        Label zoomIcon = new Label("🔍");

        zoomSlider = new Slider(10, 400, 100);
        zoomSlider.setPrefWidth(140);
        zoomSlider.setMajorTickUnit(100);
        zoomSlider.setSnapToTicks(false);

        // ── Zoom percentage spinner ───────────────────────────────────────
        zoomSpinner = new Spinner<>();
        zoomSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 400, 100));
        zoomSpinner.setPrefWidth(72);
        zoomSpinner.setEditable(true);

        Label percentLabel = new Label("%");
        percentLabel.setStyle("-fx-text-fill: #555;");

        // ── Keep slider ↔ spinner in sync ─────────────────────────────────
        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (syncing) return;
            syncing = true;
            int pct = newVal.intValue();
            zoomSpinner.getValueFactory().setValue(pct);
            CanvasWindow.setZoom(pct / 100.0);
            syncing = false;
        });

        zoomSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (syncing) return;
            syncing = true;
            zoomSlider.setValue(newVal);
            CanvasWindow.setZoom(newVal / 100.0);
            syncing = false;
        });

        // Commit typed value in spinner on Enter / focus loss
        zoomSpinner.getEditor().setOnAction(e -> zoomSpinner.commitValue());
        zoomSpinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) zoomSpinner.commitValue();
        });

        footer.getChildren().addAll(
            statusLabel,
            spacer,
            sizeLabel,
            new Region() {{ setPrefWidth(12); }},
            zoomIcon, zoomSlider, zoomSpinner, percentLabel
        );
        return footer;
    }

    public static void updateSize(double width, double height) {
        if (sizeLabel != null) {
            sizeLabel.setText((int) width + " x " + (int) height + " px");
        }
    }
}