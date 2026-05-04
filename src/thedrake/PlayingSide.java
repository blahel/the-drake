package thedrake;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable {
    ORANGE, BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"");
        writer.print(this.name());
        writer.print("\"");
    }
}
