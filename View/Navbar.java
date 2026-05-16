package View;

import Controller.CommandController;
import Controller.DrawingController;
import Controller.OpenController;
import Controller.SaveController;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;

public class Navbar {

    public static HBox getNavbar(DrawingController controller, CommandController commandController) {
        HBox navbar = new HBox();
        MenuBar menuBar = new MenuBar();

        OpenController openController = new OpenController(controller);

        // ── File Menu ────────────────────────────────────────────────────────
        Menu fileMenu = new Menu("File");
        MenuItem newItem  = new MenuItem("New");
        MenuItem openItem = new MenuItem("Open...");
        MenuItem saveItem = new MenuItem("Save");
        MenuItem saveAs   = new MenuItem("Save As...");
        MenuItem exitItem = new MenuItem("Exit");

        // Keyboard shortcuts
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        newItem.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("New Drawing");
            confirm.setHeaderText("Start a new drawing?");
            confirm.setContentText("Any unsaved changes will be lost.");
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

        openItem.setOnAction(e -> openController.open(menuBar.getScene().getWindow()));

        saveItem.setOnAction(e -> SaveController.save(CanvasWindow.getCanvas(), menuBar.getScene().getWindow()));
        saveAs.setOnAction(e -> SaveController.saveAs(CanvasWindow.getCanvas(), menuBar.getScene().getWindow()));

        exitItem.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Exit");
            confirm.setHeaderText("Exit Paint?");
            confirm.setContentText("Any unsaved changes will be lost.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) javafx.application.Platform.exit();
            });
        });

        fileMenu.getItems().addAll(newItem, openItem, new SeparatorMenuItem(), saveItem, saveAs, new SeparatorMenuItem(), exitItem);

        // ── Edit Menu ────────────────────────────────────────────────────────
        Menu editMenu = new Menu("Edit");

        MenuItem undoItem = new MenuItem("Undo");
        undoItem.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        undoItem.setOnAction(e -> commandController.getCommandManager().undo());

        MenuItem redoItem = new MenuItem("Redo");
        redoItem.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
        redoItem.setOnAction(e -> commandController.getCommandManager().redo());

        MenuItem clearItem = new MenuItem("Clear Canvas");
        clearItem.setOnAction(e -> {
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

        editMenu.getItems().addAll(undoItem, redoItem, new SeparatorMenuItem(), clearItem);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        navbar.getChildren().add(menuBar);

        return navbar;
    }
}