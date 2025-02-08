package huan.gui;

import huan.logic.HuanLogic;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private HuanLogic huan = new HuanLogic();

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image huanImage = new Image(this.getClass().getResourceAsStream("/images/DaHuan.png"));

    /**
     * The initialise method is called automatically by JavaFX.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        // Combine them (with line breaks) plus a welcome line
        String greeting = "Hello! I'm Huan! \n"
                + "What can I do for you?";

        // Show the ASCII art in the GUI, e.g., as the bot's first message
        dialogContainer.getChildren().add(
                DialogBox.getHuanDialog(greeting, huanImage)
        );
    }

    /**
     * Handles the user input event.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = huan.getResponse(input);
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));

        if (input.equalsIgnoreCase("pspsps")) {
            response = "Meow! 🐱";
            huanImage = new Image(this.getClass().getResourceAsStream("/images/DaCat.png"));
        }

        dialogContainer.getChildren().add(DialogBox.getHuanDialog(response, huanImage));
        userInput.clear();
    }
}