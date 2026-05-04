package thedrake.ui;

import javafx.scene.image.Image;
import thedrake.PlayingSide;
import thedrake.TroopFace;

import java.io.InputStream;

/**
 * Helper class to load troop images
 */
public class TroopImageSet {
    private final Image troopFrontB;
    private final Image troopBackB;
    private final Image troopFrontO;
    private final Image troopBackO;

    public TroopImageSet(String troopName, PlayingSide side) {
        try {
            troopFrontB = new Image(assetFromJAR("front" + troopName + "B"));
            troopBackB = new Image(assetFromJAR("back" + troopName + "B"));
            troopFrontO = new Image(assetFromJAR("front" + troopName + "O"));
            troopBackO = new Image(assetFromJAR("back" + troopName + "O"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load troop images for " + troopName, e);
        }
    }

    private InputStream assetFromJAR(String fileName) {
        return getClass().getResourceAsStream("/assets/" + fileName + ".png");
    }

    public Image get(PlayingSide side, TroopFace face) {
        if (side == PlayingSide.BLUE) {
            if (face == TroopFace.AVERS) {
                return troopFrontB;
            } else {
                return troopBackB;
            }
        } else {
            if (face == TroopFace.AVERS) {
                return troopFrontO;
            } else {
                return troopBackO;
            }
        }
    }

    /**
     * Get the front image (for stack display)
     */
    public Image getFront(PlayingSide side) {
        return side == PlayingSide.BLUE ? troopFrontB : troopFrontO;
    }
}
