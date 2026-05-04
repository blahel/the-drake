package thedrake.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import thedrake.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.List;

/**
 * Main game board view - displays the game and handles all interactions
 */
public class BoardView extends BorderPane implements TileViewContext {

    private GameState gameState;
    private final TheDrakeApp application;
    private final GridPane gridPane;
    private TileView selectedTile;

    private final VBox blueSidePanel;
    private final VBox orangeSidePanel;
    private final Label turnIndicator;
    private final FlowPane blueStackPane;
    private final FlowPane orangeStackPane;
    private final FlowPane blueCapturedPane;
    private final FlowPane orangeCapturedPane;

    public BoardView(GameState gameState, TheDrakeApp application) {
        this.gameState = gameState;
        this.application = application;

        this.getStyleClass().add("game-root");

        // Initialize grid
        this.gridPane = new GridPane();
        gridPane.getStyleClass().add("game-board");
        gridPane.setHgap(2);
        gridPane.setVgap(2);
        gridPane.setAlignment(Pos.CENTER);

        // Initialize side panel components
        this.blueStackPane = new FlowPane(5, 5);
        this.orangeStackPane = new FlowPane(5, 5);
        this.blueCapturedPane = new FlowPane(5, 5);
        this.orangeCapturedPane = new FlowPane(5, 5);
        this.turnIndicator = new Label();

        // Create side panels
        this.blueSidePanel = createSidePanel("BLUE", PlayingSide.BLUE);
        this.orangeSidePanel = createSidePanel("ORANGE", PlayingSide.ORANGE);

        createLayout();
        updateView();
    }

    /**
     * Create the overall layout with board and side panels
     */
    private void createLayout() {
        // Populate grid with tiles
        int dimension = gameState.board().dimension();
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++) {
                BoardPos pos = new BoardPos(dimension, i, j);
                TileView tileView = new TileView(pos, gameState.tileAt(pos), this);
                gridPane.add(tileView, i, j);
            }
        }

        setCenter(gridPane);

        // Set side panels
        setLeft(blueSidePanel);
        setRight(orangeSidePanel);

        // Top panel with turn indicator and menu button
        createTopPanel();
    }

    /**
     * Create top panel with turn indicator and buttons
     */
    private void createTopPanel() {
        HBox topPanel = new HBox(20);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(15));
        topPanel.getStyleClass().add("info-panel");

        turnIndicator.getStyleClass().add("turn-indicator");
        turnIndicator.setFont(new Font(18));

        Button menuButton = new Button("Main Menu");
        menuButton.getStyleClass().add("game-button");
        menuButton.setOnAction(e -> application.showMainMenu());

        topPanel.getChildren().addAll(turnIndicator, menuButton);
        setTop(topPanel);
    }

    /**
     * Create a side panel for one player
     */
    private VBox createSidePanel(String title, PlayingSide side) {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setMinWidth(200);
        panel.setMaxWidth(200);
        panel.getStyleClass().add("info-panel");

        // Title
        Label titleLabel = new Label(title + " Player");
        titleLabel.getStyleClass().add("info-label");
        titleLabel.setFont(new Font(18));

        // Stack label
        Label stackLabel = new Label("Stack:");
        stackLabel.getStyleClass().add("info-label");

        FlowPane stackPane = (side == PlayingSide.BLUE) ? blueStackPane : orangeStackPane;
        stackPane.setPrefWrapLength(180);

        // Captured label
        Label capturedLabel = new Label("Captured:");
        capturedLabel.getStyleClass().add("info-label");

        FlowPane capturedPane = (side == PlayingSide.BLUE) ? blueCapturedPane : orangeCapturedPane;
        capturedPane.setPrefWrapLength(180);

        panel.getChildren().addAll(titleLabel, stackLabel, stackPane, capturedLabel, capturedPane);
        return panel;
    }

    /**
     * Update the entire view after a move
     */
    private void updateView() {
        // Update board tiles
        int dimension = gameState.board().dimension();
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++) {
                BoardPos pos = new BoardPos(dimension, i, j);
                TileView tileView = (TileView) gridPane.getChildren().get(j * dimension + i);
                tileView.update(gameState.tileAt(pos));
                tileView.clearMove();
            }
        }

        // Update stacks
        updateStack(PlayingSide.BLUE);
        updateStack(PlayingSide.ORANGE);

        // Update captured units
        updateCaptured(PlayingSide.BLUE);
        updateCaptured(PlayingSide.ORANGE);

        // Update turn indicator
        updateTurnIndicator();

        // Check for game over
        checkGameOver();
    }

    /**
     * Update stack display for one player
     */
    private void updateStack(PlayingSide side) {
        FlowPane stackPane = (side == PlayingSide.BLUE) ? blueStackPane : orangeStackPane;
        stackPane.getChildren().clear();

        List<Troop> stack = gameState.army(side).stack();

        for (int i = 0; i < stack.size(); i++) {
            Troop troop = stack.get(i);

            StackPane troopPane = new StackPane();
            troopPane.setPrefSize(50, 50);
            troopPane.setMinSize(50, 50);
            troopPane.setMaxSize(50, 50);
            troopPane.getStyleClass().add("stack-tile");

            try {
                TroopImageSet imageSet = new TroopImageSet(troop.name(), side);
                javafx.scene.image.Image troopImage = imageSet.getFront(side);

                if (troopImage != null) {
                    // I used ImageView instead of BackgroundImage for better scaling and error-free loading
                    javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(troopImage);
                    imageView.setFitWidth(45);
                    imageView.setFitHeight(45);
                    imageView.setPreserveRatio(true);

                    troopPane.getChildren().add(imageView);
                }
            } catch (Exception e) {
                System.out.println("Error loading troop image: " + e.getMessage());
            }

            // Only first troop is clickable
            if (i == 0) {
                final int index = i;
                troopPane.setOnMouseClicked(e -> onStackClick(side, index));
            } else {
                troopPane.setOpacity(0.6);
            }

            stackPane.getChildren().add(troopPane);
        }
    }
    /**
     * Update captured units display for one player
     */
    private void updateCaptured(PlayingSide side) {
        FlowPane capturedPane = (side == PlayingSide.BLUE) ? blueCapturedPane : orangeCapturedPane;
        capturedPane.getChildren().clear();

        List<Troop> captured = gameState.army(side).captured();
        PlayingSide opponentSide = (side == PlayingSide.BLUE) ? PlayingSide.ORANGE : PlayingSide.BLUE;

        for (Troop troop : captured) {
            StackPane troopPane = new StackPane();
            troopPane.setPrefSize(40, 40);
            troopPane.setMinSize(40, 40);
            troopPane.setMaxSize(40, 40);
            troopPane.getStyleClass().add("captured-tile");

            try {
                TroopImageSet imageSet = new TroopImageSet(troop.name(), opponentSide);
                javafx.scene.image.Image troopImage = imageSet.getFront(opponentSide);

                if (troopImage != null) {
                    javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(troopImage);
                    imageView.setFitWidth(35);
                    imageView.setFitHeight(35);
                    imageView.setPreserveRatio(true);

                    troopPane.getChildren().add(imageView);
                }
            } catch (Exception e) {
                System.out.println("Error loading captured troop image: " + e.getMessage());
            }

            capturedPane.getChildren().add(troopPane);
        }
    }

    /**
     * Update turn indicator text
     */
    private void updateTurnIndicator() {
        String text = gameState.sideOnTurn() == PlayingSide.BLUE ? "BLUE Player's Turn" : "ORANGE Player's Turn";
        turnIndicator.setText(text);

        turnIndicator.getStyleClass().removeAll("blue-turn", "orange-turn");
        turnIndicator.getStyleClass().add(gameState.sideOnTurn() == PlayingSide.BLUE ? "blue-turn" : "orange-turn");
    }

    /**
     * Handle click on stack troop
     */
    private void onStackClick(PlayingSide side, int index) {
        if (side != gameState.sideOnTurn()) {
            return; // Not this player's turn
        }

        if (index != 0) {
            return; // Only top troop is clickable
        }

        if (gameState.army(side).stack().isEmpty()) {
            return; // No troops in stack
        }

        // Clear previous selection
        if (selectedTile != null) {
            selectedTile.unselect();
            selectedTile = null;
        }

        // Show valid placement positions
        clearMoves();
        showPlacementMoves();
    }

    /**
     * Show valid positions where troops can be placed from stack
     */
    private void showPlacementMoves() {
        ValidMoves validMoves = new ValidMoves(gameState);
        List<Move> moves = validMoves.movesFromStack();

        for (Move move : moves) {
            TileView targetView = getTileView(move.target());
            targetView.setMove(move);
        }
    }

    @Override
    public void tileSelected(TileView tileView) {
        if (selectedTile != null && selectedTile != tileView) {
            selectedTile.unselect();
        }

        selectedTile = tileView;
        clearMoves();

        // Show valid moves from this tile
        List<Move> moves = tileView.tile().movesFrom(tileView.position(), gameState);
        for (Move move : moves) {
            TileView targetView = getTileView(move.target());
            targetView.setMove(move);
        }
    }

    @Override
    public void executeMove(Move move) {
        if (selectedTile != null) {
            selectedTile.unselect();
            selectedTile = null;
        }

        // Execute the move
        gameState = move.execute(gameState);

        clearMoves();
        updateView();
    }

    /**
     * Clear all move indicators
     */
    private void clearMoves() {
        int dimension = gameState.board().dimension();
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++) {
                BoardPos pos = new BoardPos(dimension, i, j);
                TileView tileView = getTileView(pos);
                tileView.clearMove();
            }
        }
    }

    /**
     * Get TileView at a specific position
     */
    private TileView getTileView(BoardPos pos) {
        int dimension = gameState.board().dimension();
        int index = pos.j() * dimension + pos.i();
        return (TileView) gridPane.getChildren().get(index);
    }

    /**
     * Check if game is over
     */
    private void checkGameOver() {
        if (gameState.result() != GameResult.IN_PLAY) {
            showVictoryDialog();
            return;
        }

        // Check if current player has no valid moves
        if (!hasAnyValidMoves()) {
            showNoMovesDialog();
        }
    }

    /**
     * Check if current player has any valid moves
     */
    private boolean hasAnyValidMoves() {
        ValidMoves validMoves = new ValidMoves(gameState);
        return !validMoves.allMoves().isEmpty();
    }

    /**
     * Show victory dialog
     */
    private void showVictoryDialog() {
        String winner = gameState.sideOnTurn() == PlayingSide.BLUE ? "ORANGE" : "BLUE";
        showGameOverDialog(winner + " Player Wins!");
    }

    /**
     * Show no valid moves dialog
     */
    private void showNoMovesDialog() {
        String loser = gameState.sideOnTurn() == PlayingSide.BLUE ? "BLUE" : "ORANGE";
        String winner = gameState.sideOnTurn() == PlayingSide.BLUE ? "ORANGE" : "BLUE";
        showGameOverDialog(winner + " Player Wins!\n(" + loser + " has no valid moves)");
    }

    /**
     * Show game over dialog with options
     */
    private void showGameOverDialog(String message) {
        VBox dialog = new VBox(20);
        dialog.setAlignment(Pos.CENTER);
        dialog.setPadding(new Insets(30));
        dialog.getStyleClass().add("victory-dialog");

        Text victoryText = new Text(message);
        victoryText.getStyleClass().add("victory-text");

        Button newGameButton = new Button("New Game");
        newGameButton.getStyleClass().add("menu-button");
        newGameButton.getStyleClass().add("active-button");
        newGameButton.setOnAction(e -> application.startTwoPlayerGame());

        Button menuButton = new Button("Main Menu");
        menuButton.getStyleClass().add("menu-button");
        menuButton.getStyleClass().add("active-button");
        menuButton.setOnAction(e -> application.showMainMenu());

        HBox buttons = new HBox(15, newGameButton, menuButton);
        buttons.setAlignment(Pos.CENTER);

        dialog.getChildren().addAll(victoryText, buttons);

        StackPane overlay = new StackPane(dialog);
        overlay.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.7), null, null)));

        setCenter(overlay);
    }
}