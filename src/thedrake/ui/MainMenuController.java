package thedrake.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Button twoPlayersButton;

    @FXML
    private Button vsComputerButton;

    @FXML
    private Button onlineButton;
    
    @FXML
    private Button exitButton;
    
    private TheDrakeApp application;
    
    public void setApplication(TheDrakeApp app) {
        this.application = app;
    }
    
    @FXML
    private void onTwoPlayers() {
        if (application != null) {
            application.startTwoPlayerGame();
        }
    }

    @FXML
    private void onVsComputer() {

    }

    @FXML
    private void onOnline() {

    }
    
    @FXML
    private void onExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
