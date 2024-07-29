package organization;

public class Coordinates {
    private Integer x;
    private float y;

    public Coordinates(Integer x, float y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    @Override
    public String toString() {
        return "x=" + x + "; y=" + y;
    }
}