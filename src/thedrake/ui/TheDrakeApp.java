package thedrake.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;

import java.io.InputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import thedrake.*;

import java.io.IOException;

public class TheDrakeApp extends Application {

    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene gameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("The Drake");

        // Load main menu
        showMainMenu();

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.show();
    }

    public void showMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setApplication(this);

            // Load background image and set it
            InputStream imageStream = getClass().getResourceAsStream("/assets/welcomescreen.jpg");
            if (imageStream != null) {
                Image bgImage = new Image(imageStream);
                BackgroundImage backgroundImage = new BackgroundImage(
                        bgImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
                );

                // Set background on the root
                if (root instanceof StackPane) {
                    ((StackPane) root).setBackground(new Background(backgroundImage));
                }
            }

            mainMenuScene = new Scene(root, 800, 600);
            primaryStage.setScene(mainMenuScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startTwoPlayerGame() {
        // Create initial game state
        Board board = new Board(4);
        PositionFactory pf = board.positionFactory();

        // Add mountains at b2 and c3
        board = board.withTiles(
                new Board.TileAt(pf.pos("b2"), BoardTile.MOUNTAIN),
                new Board.TileAt(pf.pos("c3"), BoardTile.MOUNTAIN)
        );

        StandardDrakeSetup setup = new StandardDrakeSetup();
        GameState gameState = setup.startState(board);

        // Create game view
        BoardView boardView = new BoardView(gameState, this);
        gameScene = new Scene(boardView, 1000, 700);
        gameScene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(gameScene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
    }
}
