// View/Ribbon.java
package View;

import Controller.DrawingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Ribbon {

    private static Canvas selectedIndicator = null;
    private static final Color SELECTED_BG   = Color.web("#CCE4FF");
    private static final Color UNSELECTED_BG = Color.web("#F5F5F5");
    private static final Color HOVER_BG      = Color.web("#E8F0FF");
    private static final Color BORDER_SEL    = Color.web("#3A8DFF");
    private static final Color BORDER_NORM   = Color.web("#CCCCCC");
    private static final Color ICON_COLOR    = Color.web("#222222");

    public static HBox getRibbon(DrawingController controller) {
    	
    	HBox ribbon = new HBox();

        // ── Top bar: color, size, clear ──────────────────────────────────────
        HBox topBar = new HBox(12);
        topBar.setPadding(new Insets(6, 10, 6, 10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #FAFAFA; -fx-border-color: #DDDDDD; -fx-border-width: 0 0 1 0;");

        // Pen toggle
        ToggleGroup freeGroup = new ToggleGroup();
        ToggleButton penBtn   = styledToggle("Pen",    freeGroup);
        ToggleButton eraserBtn= styledToggle("Eraser", freeGroup);
        penBtn.setSelected(true);
        // setOnAction for pen/eraser defined below, after spinners are created
        
        // Color Picker
        Label colorLabel = new Label("Color");
        colorLabel.setStyle("-fx-font-size:11px; -fx-text-fill:#666;");
        ColorPicker colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setPrefWidth(80);
        colorPicker.setOnAction(e -> controller.getModel().setCurrentColor(colorPicker.getValue()));

        // ── Brush size spinner (pen / shapes) ────────────────────────────────
        Label brushSizeLabel = new Label("Brush Size");
        brushSizeLabel.setStyle("-fx-font-size:11px; -fx-text-fill:#666;");
        Spinner<Integer> brushSpinner = new Spinner<>(1, 50, 5);
        brushSpinner.setPrefWidth(70);
        brushSpinner.setEditable(true);
        brushSpinner.valueProperty().addListener((obs, o, n) ->
            controller.getModel().setBrushSize(n.doubleValue()));
        VBox brushSizeBox = new VBox(2, brushSizeLabel, brushSpinner);
        brushSizeBox.setAlignment(Pos.CENTER_LEFT);

        // ── Eraser size spinner ───────────────────────────────────────────────
        Label eraserSizeLabel = new Label("Eraser Size");
        eraserSizeLabel.setStyle("-fx-font-size:11px; -fx-text-fill:#666;");
        Spinner<Integer> eraserSpinner = new Spinner<>(1, 100, 30);
        eraserSpinner.setPrefWidth(70);
        eraserSpinner.setEditable(true);
        eraserSpinner.valueProperty().addListener((obs, o, n) ->
            controller.getModel().setEraserSize(n.doubleValue()));
        VBox eraserSizeBox = new VBox(2, eraserSizeLabel, eraserSpinner);
        eraserSizeBox.setAlignment(Pos.CENTER_LEFT);
        eraserSizeBox.setVisible(false);   // hidden until eraser is active
        eraserSizeBox.setManaged(false);

        // ── Wire pen / eraser buttons to swap the visible spinner ─────────────
        penBtn.setOnAction(e -> {
            controller.setStrategy("pen");
            brushSizeBox.setVisible(true);
            brushSizeBox.setManaged(true);
            eraserSizeBox.setVisible(false);
            eraserSizeBox.setManaged(false);
        });
        eraserBtn.setOnAction(e -> {
            controller.setStrategy("eraser");
            eraserSizeBox.setVisible(true);
            eraserSizeBox.setManaged(true);
            brushSizeBox.setVisible(false);
            brushSizeBox.setManaged(false);
        });

        // Clear
        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #FF5252;-fx-text-fill: white;-fx-font-weight: bold;-fx-background-radius: 6;-fx-padding: 5 12 5 12;");
        clearBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Clear Canvas");
            confirm.setHeaderText("Clear the canvas?");
            confirm.setContentText("This will erase everything. Any unsaved changes will be lost.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.clearCanvas(
                        CanvasWindow.getGraphicsContext(),
                        CanvasWindow.getCanvasWidth(),
                        CanvasWindow.getCanvasHeight(),
                        CanvasWindow.getCanvas()
                    );
                }
            });
        });

        topBar.getChildren().addAll(
            penBtn, eraserBtn,
            new Separator(), colorLabel, colorPicker,
            new Separator(), brushSizeBox, eraserSizeBox,
            new Separator(), clearBtn
        );

        // ── Shape picker row ─────────────────────────────────────────────────
        HBox shapeBar = new HBox(6);
        shapeBar.setPadding(new Insets(6, 10, 6, 10));
        shapeBar.setAlignment(Pos.CENTER_LEFT);
        shapeBar.setStyle("-fx-background-color: #F0F0F0;");

        Label shapesLabel = new Label("SHAPES");
        shapesLabel.setStyle("-fx-font-size:10px; -fx-font-weight:bold; -fx-text-fill:#999; -fx-padding:0 8 0 0;");

        // Build each icon tile
        shapeBar.getChildren().add(shapesLabel);
        shapeBar.getChildren().addAll(
            makeShapeTile("Line",         "line",         controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Rectangle",    "rect",         controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Filled Rect",  "filledrect",   controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Circle",       "circle",       controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Filled Circle","filledcircle", controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Triangle",     "triangle",     controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Diamond",      "diamond",      controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Arrow",        "arrow",        controller, freeGroup, brushSizeBox, eraserSizeBox),
            makeShapeTile("Star",         "star",         controller, freeGroup, brushSizeBox, eraserSizeBox)
        );

        VBox layout = new VBox(topBar, shapeBar);
        ribbon.getChildren().add(layout);
        return ribbon;
    }

    // ── Helper: top-bar toggle button ────────────────────────────────────────
    private static ToggleButton styledToggle(String text, ToggleGroup group) {
        ToggleButton btn = new ToggleButton(text);
        btn.setToggleGroup(group);
        btn.setStyle("-fx-background-color: #EEEEEE;-fx-background-radius: 6;-fx-font-size: 12px;-fx-padding: 5 12 5 12;");
        btn.selectedProperty().addListener((obs, o, selected) ->
            btn.setStyle(selected
                ? "-fx-background-color:#CCE4FF; -fx-background-radius:6; -fx-font-size:12px; -fx-padding:5 12 5 12; -fx-border-color:#3A8DFF; -fx-border-radius:6; -fx-border-width:1.5;"
                : "-fx-background-color:#EEEEEE; -fx-background-radius:6; -fx-font-size:12px; -fx-padding:5 12 5 12;")
        );
        return btn;
    }

    // ── Helper: icon tile with canvas drawing + label ────────────────────────
    private static VBox makeShapeTile(String label, String tool,
                                      DrawingController controller,
                                      ToggleGroup freeGroup,
                                      VBox brushSizeBox, VBox eraserSizeBox) {
        Canvas icon = new Canvas(48, 40);
        drawIcon(icon.getGraphicsContext2D(), tool);

        Label lbl = new Label(label);
        lbl.setStyle("-fx-font-size:9px; -fx-text-fill:#444;");
        lbl.setMaxWidth(52);
        lbl.setAlignment(Pos.CENTER);

        VBox tile = new VBox(2, icon, lbl);
        tile.setAlignment(Pos.CENTER);
        tile.setPadding(new Insets(4));
        tile.setPrefWidth(58);
        tile.setStyle(buildTileStyle(false));
        tile.setCursor(javafx.scene.Cursor.HAND);

        // Hover
        tile.setOnMouseEntered(e -> {
            if (!tile.getStyle().contains("#CCE4FF"))
                tile.setStyle(buildTileStyle(false)
                    .replace("#F5F5F5", "#E8F0FF"));
        });
        tile.setOnMouseExited(e -> {
            if (!tile.getStyle().contains("#CCE4FF"))
                tile.setStyle(buildTileStyle(false));
        });

        // Click → select tool, deselect free tools, highlight tile
        tile.setOnMouseClicked(e -> {
        	controller.setStrategy(tool);
            freeGroup.selectToggle(null);           // deselect pen/eraser
            if (selectedIndicator != null) {
                // reset previous tile
                selectedIndicator.getParent().setStyle(buildTileStyle(false));
            }
            selectedIndicator = icon;
            tile.setStyle(buildTileStyle(true));
            // shapes use brush size
            brushSizeBox.setVisible(true);
            brushSizeBox.setManaged(true);
            eraserSizeBox.setVisible(false);
            eraserSizeBox.setManaged(false);
        });

        return tile;
    }

    private static String buildTileStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: #CCE4FF;-fx-border-color: #3A8DFF;-fx-border-width: 1.5;-fx-border-radius: 6;-fx-background-radius: 6;";
        }
        return "-fx-background-color: #F5F5F5;-fx-border-color: #CCCCCC;-fx-border-width: 1;-fx-border-radius: 6;-fx-background-radius: 6;";
    }

    // ── Draw a mini icon for each shape ──────────────────────────────────────
    private static void drawIcon(GraphicsContext gc, String tool) {
        gc.clearRect(0, 0, 48, 40);
        gc.setStroke(ICON_COLOR);
        gc.setFill(ICON_COLOR);
        gc.setLineWidth(1.8);

        switch (tool) {

            case "line":
                gc.strokeLine(6, 34, 42, 6);
                break;

            case "rect":
                gc.strokeRect(6, 8, 36, 24);
                break;

            case "filledrect":
                gc.setFill(ICON_COLOR);
                gc.fillRect(6, 8, 36, 24);
                break;

            case "circle":
                gc.strokeOval(6, 6, 36, 28);
                break;

            case "filledcircle":
                gc.setFill(ICON_COLOR);
                gc.fillOval(6, 6, 36, 28);
                break;

            case "triangle": {
                double[] x = {24, 42, 6};
                double[] y = {5,  35, 35};
                gc.strokePolygon(x, y, 3);
                break;
            }

            case "diamond": {
                double[] x = {24, 42, 24, 6};
                double[] y = {4,  20, 36, 20};
                gc.strokePolygon(x, y, 4);
                break;
            }

            case "arrow": {
                // shaft
                gc.strokeLine(5, 20, 36, 20);
                // head
                gc.strokeLine(36, 20, 26, 12);
                gc.strokeLine(36, 20, 26, 28);
                break;
            }

            case "star": {
                double cx = 24, cy = 20, outer = 16, inner = 7;
                double[] sx = new double[10];
                double[] sy = new double[10];
                for (int i = 0; i < 10; i++) {
                    double angle  = Math.toRadians(i * 36 - 90);
                    double radius = (i % 2 == 0) ? outer : inner;
                    sx[i] = cx + radius * Math.cos(angle);
                    sy[i] = cy + radius * Math.sin(angle);
                }
                gc.strokePolygon(sx, sy, 10);
                break;
            }
        }
    }
}