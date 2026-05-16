// Model/DrawingModel.java
package Model;

import javafx.scene.paint.Color;

public class DrawingModel {
    private String currentTool = "pen";
    private Color currentColor = Color.BLACK;
    private double brushSize = 5;
    private double eraserSize = 30;

    public String getCurrentTool() { return currentTool; }
    public void setCurrentTool(String tool) { this.currentTool = tool; }

    public Color getCurrentColor() { return currentColor; }
    public void setCurrentColor(Color color) { this.currentColor = color; }

    public double getBrushSize() { return brushSize; }
    public void setBrushSize(double size) { this.brushSize = size; }
    
    public double getEraserSize() { return eraserSize; }
    public void setEraserSize(double size) { this.eraserSize = size; }
}