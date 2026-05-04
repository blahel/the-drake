package thedrake.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.*;
import thedrake.*;

/**
 * Visual representation of a single tile on the board
 */
public class TileView extends StackPane {

    private BoardPos position;
    private Tile tile;
    private TileViewContext context;
    private Move move;
    private boolean selected;

    public TileView(BoardPos position, Tile tile, TileViewContext context) {
        this.position = position;
        this.tile = tile;
        this.context = context;
        this.selected = false;

        setPrefSize(80, 80);
        getStyleClass().add("tile");

        update(tile);

        setOnMouseClicked(e -> onClick());
    }

    /**
     * Update the tile's appearance based on its state
     */
    public void update(Tile tile) {
        this.tile = tile;

        // Set background based on tile type
        Image background = TileBackgrounds.get(tile);
        if (background != null) {
            setBackground(new Background(new BackgroundImage(
                    background,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            )));
        } else {
            // Empty tile - light gray
            setBackground(new Background(new BackgroundFill(
                    javafx.scene.paint.Color.rgb(220, 220, 220),
                    null, null
            )));
        }
    }

    /**
     * Mark this tile as selected
     */
    public void select() {
        selected = true;
        getStyleClass().add("tile-selected");
    }

    /**
     * Unmark this tile as selected
     */
    public void unselect() {
        selected = false;
        getStyleClass().remove("tile-selected");
    }

    /**
     * Set a move that can be executed from this tile
     */
    public void setMove(Move move) {
        this.move = move;

        // Show move indicator (arrow)
        if (move != null) {
            try {
                Image moveImage = new Image(getClass().getResourceAsStream("/assets/move.png"));
                BackgroundImage bgImage = new BackgroundImage(
                        moveImage,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(30, 30, false, false, false, false)
                );

                // Combine with existing background
                Background currentBg = getBackground();
                if (currentBg != null && !currentBg.getImages().isEmpty()) {
                    setBackground(new Background(currentBg.getImages().get(0), bgImage));
                } else {
                    setBackground(new Background(bgImage));
                }
            } catch (Exception e) {
                // If move.png not found, just skip the arrow
            }
        }
    }

    /**
     * Clear any move indicator
     */
    public void clearMove() {
        this.move = null;
        update(tile);
    }

    /**
     * Handle mouse click on this tile
     */
    private void onClick() {
        if (move != null) {
            // There's a move set - execute it
            context.executeMove(move);
        } else if (tile.hasTroop()) {
            // Tile has a troop - select it
            select();
            context.tileSelected(this);
        }
    }

    public BoardPos position() {
        return position;
    }

    public Tile tile() {
        return tile;
    }
}
