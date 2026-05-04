package thedrake.ui;

import javafx.scene.image.Image;
import thedrake.*;

/**
 * Helper class to get background images for tiles
 */
public class TileBackgrounds {

    /**
     * Get the appropriate background image for a tile
     */
    public static Image get(Tile tile) {
        if (tile.hasTroop()) {
            TroopTile armyTile = ((TroopTile) tile);
            return getTroop(armyTile.troop(), armyTile.side(), armyTile.face());
        }

        if (tile == BoardTile.MOUNTAIN) {
            try {
                return new Image(TileBackgrounds.class.getResourceAsStream("/assets/mountain.png"));
            } catch (Exception e) {
                return null;
            }
        }

        return null; // Empty tile
    }

    /**
     * Get the troop image
     */
    public static Image getTroop(Troop info, PlayingSide side, TroopFace face) {
        TroopImageSet images = new TroopImageSet(info.name(), side);
        return images.get(side, face);
    }
}
