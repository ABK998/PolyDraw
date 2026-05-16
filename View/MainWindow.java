package View;

import Controller.CommandController;
import Controller.DrawingController;
import Controller.SaveController;
import Model.DrawingModel;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class MainWindow {
    public static Scene getScene() {
        DrawingModel      model      = new DrawingModel();
        CommandController commandController = new CommandController();
        DrawingController controller = new DrawingController(model, commandController);

        VBox root   = new VBox();
        
        // Pass the controller to the UI components that need it
        HBox  navbar = Navbar.getNavbar(controller, commandController);
        HBox  ribbon = Ribbon.getRibbon(controller);
        VBox  canvas = CanvasWindow.getCanvas(controller);
        HBox  footer = Footer.getFooter();

        VBox.setVgrow(canvas, Priority.ALWAYS);  // canvas takes all remaining space
        root.getChildren().addAll(navbar, ribbon, canvas, footer);

        // No fixed width/height — window is freely resizable
        Scene scene = new Scene(root, 1000, 600);

        // Global Keyboard Shortcuts
        scene.setOnKeyPressed(e -> {
            // Save (Ctrl+S)
            if (e.isControlDown() && e.getCode() == javafx.scene.input.KeyCode.S) {
                SaveController.save(CanvasWindow.getCanvas(), scene.getWindow());
            }
            // Undo (Ctrl+Z)
            if (e.isControlDown() && e.getCode() == javafx.scene.input.KeyCode.Z) {
                commandController.getCommandManager().undo();
            }
            // Redo (Ctrl+Y)
            if (e.isControlDown() && e.getCode() == javafx.scene.input.KeyCode.Y) {
                commandController.getCommandManager().redo();
            }
        });

        return scene;
    }
}