package View;

import Controller.DrawingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CanvasWindow {

    private static Canvas          canvas;
    private static GraphicsContext gc;
    
    // The transparent overlay for the brush/eraser cursor
    private static Canvas          cursorCanvas;
    private static GraphicsContext cursorGc;

    private static double canvasW = 900;
    private static double canvasH = 600;

    // Drag state
    private static double dragStartScreenX, dragStartScreenY;
    private static double dragStartW,       dragStartH;

    // Key layout nodes that must update on resize
    private static StackPane canvasBorder;
    private static StackPane shadowWrapper; 
    private static Pane      handleLayer;
    private static StackPane canvasStack;
    private static StackPane innerArea;

    private static double zoomScale = 1.0; // current zoom, 1.0 = 100%

    public static VBox getCanvas(DrawingController controller) {

        // ── Canvas ─────────────────────────────────────────────────────────
        canvas = new Canvas(canvasW, canvasH);
        gc     = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false); 
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasW, canvasH);
        canvas.setCursor(Cursor.CROSSHAIR); // Looks more professional for drawing

        // ── Cursor Overlay (Ghost layer that sits on top of canvas) ──────────
        cursorCanvas = new Canvas(canvasW, canvasH);
        cursorGc = cursorCanvas.getGraphicsContext2D();
        cursorCanvas.setMouseTransparent(true); // Lets clicks pass through to the real canvas!

        // ── Shadow ─────────────────────────────────────────────────────────
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.color(0, 0, 0, 0.35));
        shadow.setRadius(10);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);

        // ── Blue border wrapper (Holds BOTH canvases now) ────────────────────
        canvasBorder = new StackPane(canvas, cursorCanvas);
        canvasBorder.setStyle("-fx-background-color: white; -fx-border-color: #3A8DFF; -fx-border-width: 2;");
        syncBorderSize();

        // ── Shadow wrapper (sits outside canvasBorder so it never touches gc) ──
        shadowWrapper = new StackPane(canvasBorder);
        shadowWrapper.setEffect(shadow);
        shadowWrapper.setPickOnBounds(false);
        syncShadowWrapper();

        // ── Handle layer (sits on top, same size as canvas) ────────────────
        handleLayer = new Pane();
        handleLayer.setPickOnBounds(false);
        syncHandleLayer();

        // Add the 3 active handles
        handleLayer.getChildren().addAll(
            makeHandle("rc", Cursor.E_RESIZE),   // right-center  → width
            makeHandle("bc", Cursor.S_RESIZE),   // bottom-center → height
            makeHandle("br", Cursor.SE_RESIZE)   // bottom-right  → width + height
        );
        positionHandles();

        // ── Stack: shadow wrapper + handles ───────────────────────────────
        canvasStack = new StackPane(shadowWrapper, handleLayer);
        canvasStack.setAlignment(Pos.TOP_LEFT);
        syncStackSize();

        // ── Scrollable grey surround ───────────────────────────────────────
        innerArea = new StackPane(canvasStack);
        innerArea.setAlignment(Pos.CENTER);
        innerArea.setPadding(new Insets(40));
        innerArea.setStyle("-fx-background-color: #C0C0C0;");
        syncInnerArea();

        ScrollPane scroll = new ScrollPane(innerArea);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background: #C0C0C0; -fx-background-color: #C0C0C0;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // ── Canvas mouse events ────────────────────────────────────────────
        canvas.setOnMousePressed(e  -> controller.onMousePressed(e.getX(), e.getY(), gc, canvas));
        
        canvas.setOnMouseDragged(e  -> {
            controller.onMouseDragged(e.getX(), e.getY(), gc);
            updateCursor(e.getX(), e.getY(), controller); // Move cursor while dragging
        });
        
        canvas.setOnMouseReleased(e -> controller.onMouseReleased(canvas));

        // ── Cursor hover events ────────────────────────────────────────────
        canvas.setOnMouseMoved(e -> updateCursor(e.getX(), e.getY(), controller));
        canvas.setOnMouseExited(e -> cursorGc.clearRect(0, 0, canvasW, canvasH)); // Hide cursor when leaving

        Footer.updateSize(canvasW, canvasH);

        VBox root = new VBox(scroll);
        VBox.setVgrow(root, Priority.ALWAYS);
        return root;
    }

    // ── Draws the circular ghost cursor for Pen and Eraser ─────────────────
    private static void updateCursor(double x, double y, DrawingController controller) {
        // Clear the ghost layer every time the mouse moves
        cursorGc.clearRect(0, 0, canvasW, canvasH);
        
        String tool = controller.getModel().getCurrentTool().toLowerCase();
        
        // Only draw the circle if using pen or eraser
        if (tool.equals("pen") || tool.equals("eraser")) {
            double size = tool.equals("pen") ? 
                          controller.getModel().getBrushSize() : 
                          controller.getModel().getEraserSize();

            // Draw a black circle with a white border so it is visible on ANY background
            cursorGc.setStroke(Color.WHITE);
            cursorGc.setLineWidth(2);
            cursorGc.strokeOval(x - size / 2, y - size / 2, size, size);

            cursorGc.setStroke(Color.BLACK);
            cursorGc.setLineWidth(1);
            cursorGc.strokeOval(x - size / 2, y - size / 2, size, size);
        }
    }

    // ── Build one resize handle ───────────────────────────────────────────────
    private static Pane makeHandle(String id, Cursor cursor) {
        Pane h = new Pane();
        h.setId(id);
        h.setPrefSize(10, 10);
        h.setCursor(cursor);
        h.setStyle("-fx-background-color: #3A8DFF; -fx-border-color: white; -fx-border-width: 1;");

        h.setOnMouseEntered(e -> h.setStyle("-fx-background-color: #0057D9; -fx-border-color: white; -fx-border-width: 1;"));
        h.setOnMouseExited(e  -> h.setStyle("-fx-background-color: #3A8DFF; -fx-border-color: white; -fx-border-width: 1;"));

        h.setOnMousePressed(e -> {
            dragStartScreenX = e.getScreenX();
            dragStartScreenY = e.getScreenY();
            dragStartW       = canvasW;
            dragStartH       = canvasH;
            e.consume();
        });

        h.setOnMouseDragged(e -> {
            double dx = e.getScreenX() - dragStartScreenX;
            double dy = e.getScreenY() - dragStartScreenY;

            double newW = canvasW;
            double newH = canvasH;

            switch (id) {
                case "rc": {newW = Math.max(100, dragStartW + dx); break;}
                case "bc": {newH = Math.max(100, dragStartH + dy); break;}
                case "br": { newW = Math.max(100, dragStartW + dx); newH = Math.max(100, dragStartH + dy); break; }
            }

            doResize(newW, newH);
            e.consume();
        });

        return h;
    }

    // ── Public zoom: called from Footer slider / spinner ─────────────────────
    public static void setZoom(double scale) {
        zoomScale = scale;
        canvasStack.setScaleX(zoomScale);
        canvasStack.setScaleY(zoomScale);
        syncInnerArea();   // grow/shrink scroll area so scrollbars stay accurate
    }

    // ── Public resize: called externally (e.g. OpenController) ───────────────
    public static void resize(double newW, double newH) {
        doResize(newW, newH);
    }

    // ── Core resize: snapshot → resize → restore ──────────────────────────────
    private static void doResize(double newW, double newH) {

        // 1. Snapshot current drawing
        WritableImage snap = new WritableImage(
            (int) Math.max(canvasW, 1),
            (int) Math.max(canvasH, 1)
        );
        canvas.snapshot(null, snap);

        // 2. Update tracked size
        canvasW = newW;
        canvasH = newH;

        // 3. Resize the canvas node itself
        canvas.setWidth(canvasW);
        canvas.setHeight(canvasH);
        
        // Resize the ghost canvas so it doesn't get left behind!
        cursorCanvas.setWidth(canvasW);
        cursorCanvas.setHeight(canvasH);

        // 4. Re-fetch gc and restore the previous drawing onto the white background
        gc = canvas.getGraphicsContext2D();
        gc.setGlobalAlpha(1.0);
        gc.setImageSmoothing(false); 
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvasW, canvasH);
        gc.drawImage(snap, 0, 0);

        // 5. Update ALL wrapper sizes so nothing stays locked
        syncBorderSize();
        syncShadowWrapper();
        syncHandleLayer();
        syncStackSize();
        syncInnerArea();
        positionHandles();

        Footer.updateSize(canvasW, canvasH);
    }

    // ── Sync helpers — keep every wrapper in step with canvasW / canvasH ──────
    private static void syncBorderSize() {
        canvasBorder.setMinSize(canvasW, canvasH);
        canvasBorder.setMaxSize(canvasW, canvasH);
        canvasBorder.setPrefSize(canvasW, canvasH);
    }

    private static void syncShadowWrapper() {
        shadowWrapper.setMinSize(canvasW, canvasH);
        shadowWrapper.setMaxSize(canvasW, canvasH);
        shadowWrapper.setPrefSize(canvasW, canvasH);
    }

    private static void syncHandleLayer() {
        handleLayer.setMinSize(canvasW, canvasH);
        handleLayer.setMaxSize(canvasW, canvasH);
        handleLayer.setPrefSize(canvasW, canvasH);
    }

    private static void syncStackSize() {
        canvasStack.setMinSize(canvasW + 10, canvasH + 10);
        canvasStack.setMaxSize(canvasW + 10, canvasH + 10);
        canvasStack.setPrefSize(canvasW + 10, canvasH + 10);
    }

    private static void syncInnerArea() {
        double scaledW = (canvasW + 10) * zoomScale;
        double scaledH = (canvasH + 10) * zoomScale;
        innerArea.setMinSize(scaledW + 100, scaledH + 100);
        innerArea.setPrefSize(scaledW + 100, scaledH + 100);
    }

    // ── Reposition handles after every resize ─────────────────────────────────
    private static void positionHandles() {
        for (Node node : handleLayer.getChildren()) {
            if (!(node instanceof Pane)) continue;
            Pane h = (Pane) node;
            switch (h.getId()) {
                case "rc": { h.setLayoutX(canvasW - 5); h.setLayoutY(canvasH / 2 - 5); break; }
                case "bc": { h.setLayoutX(canvasW / 2 - 5); h.setLayoutY(canvasH - 5); break; }
                case "br": { h.setLayoutX(canvasW - 5); h.setLayoutY(canvasH - 5); break; } 
            }
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public static GraphicsContext getGraphicsContext() { return gc; }
    public static Canvas          getCanvas()          { return canvas; }
    public static double          getCanvasWidth()     { return canvasW; }
    public static double          getCanvasHeight()    { return canvasH; }
}