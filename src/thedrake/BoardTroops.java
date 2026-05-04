package thedrake;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BoardTroops implements JSONSerializable {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;

    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        this.troopMap = Collections.emptyMap();
        this.leaderPosition = TilePos.OFF_BOARD;
        this.guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        return Optional.ofNullable(troopMap.get(pos));
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return leaderPosition != TilePos.OFF_BOARD;
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {
        if (troopMap.containsKey(target)) {
            throw new IllegalArgumentException();
        }
        
        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
        
        if (!isLeaderPlaced()) {
            return new BoardTroops(playingSide, newTroopMap, target, guards);
        }
        
        if (isPlacingGuards()) {
            return new BoardTroops(playingSide, newTroopMap, leaderPosition, guards + 1);
        }
        
        return new BoardTroops(playingSide, newTroopMap, leaderPosition, guards);
    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }
        
        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }
        
        if (!troopMap.containsKey(origin)) {
            throw new IllegalArgumentException();
        }
        
        if (troopMap.containsKey(target)) {
            throw new IllegalArgumentException();
        }
        
        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        TroopTile tile = newTroopMap.remove(origin);
        newTroopMap.put(target, tile.flipped());
        
        TilePos newLeaderPosition = leaderPosition;
        if (origin.equals(leaderPosition)) {
            newLeaderPosition = target;
        }
        
        return new BoardTroops(playingSide, newTroopMap, newLeaderPosition, guards);
    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot remove troops before the leader is placed.");
        }
        
        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot remove troops before guards are placed.");
        }
        
        if (!troopMap.containsKey(target)) {
            throw new IllegalArgumentException();
        }
        
        Map<BoardPos, TroopTile> newTroopMap = new HashMap<>(troopMap);
        newTroopMap.remove(target);
        
        TilePos newLeaderPosition = leaderPosition;
        if (target.equals(leaderPosition)) {
            newLeaderPosition = TilePos.OFF_BOARD;
        }
        
        return new BoardTroops(playingSide, newTroopMap, newLeaderPosition, guards);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");
        writer.print("\"side\":");
        playingSide.toJSON(writer);
        writer.print(",");
        writer.print("\"leaderPosition\":");
        writer.print("\"");
        writer.print(leaderPosition.toString());
        writer.print("\"");
        writer.print(",");
        writer.print("\"guards\":");
        writer.print(guards);
        writer.print(",");
        writer.print("\"troopMap\":{");
        
        List<BoardPos> positions = new ArrayList<>(troopMap.keySet());
        positions.sort((a, b) -> {
            if (a.i() != b.i()) {
                return Integer.compare(a.i(), b.i());
            }
            return Integer.compare(a.j(), b.j());
        });
        
        for (int i = 0; i < positions.size(); i++) {
            BoardPos pos = positions.get(i);
            writer.print("\"");
            writer.print(pos.toString());
            writer.print("\":");
            troopMap.get(pos).toJSON(writer);
            if (i < positions.size() - 1) {
                writer.print(",");
            }
        }
        
        writer.print("}");
        writer.print("}");
    }
}
