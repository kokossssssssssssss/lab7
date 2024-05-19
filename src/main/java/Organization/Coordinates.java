package Organization;

public class Coordinates {
    private Integer x;
    private double y;

    public Coordinates(Integer x, double y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "x=" + x + "; y=" + y;
    }
}