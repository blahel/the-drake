package thedrake.ui;

import thedrake.Move;

/**
 * Interface for communication between TileView and BoardView
 */
public interface TileViewContext {
    /**
     * Called when a tile is selected by the user
     */
    void tileSelected(TileView tileView);

    /**
     * Called when a move should be executed
     */
    void executeMove(Move move);
}
