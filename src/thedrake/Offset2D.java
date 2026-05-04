package thedrake;

public class Offset2D {
    public final int x;
    public final int y;

    public Offset2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        
        Offset2D offset2D = (Offset2D) other;
        return x == offset2D.x && y == offset2D.y;
    }

    public boolean equalsTo(int x, int y) {
        return this.x == x && this.y == y;
    }

    public Offset2D yFlipped() {
        return new Offset2D(x, -y);
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "Offset2D[" + x + ", " + y + "]";
    }
}
