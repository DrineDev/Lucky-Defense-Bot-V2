package Home;

import java.util.Random;
public class Coordinates {
    private int X;
    private int Y;

    public Coordinates() {
        X = 0;
        Y = 0;
    }

    public Coordinates(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public static Coordinates makeRandomCoordinate(Coordinates topLeft, Coordinates bottomRight) {
        Coordinates temp = new Coordinates();
        Random random = new Random();

        temp.X = random.nextInt(bottomRight.X - topLeft.X + 1) + topLeft.X;
        temp.Y = random.nextInt(bottomRight.Y - topLeft.Y + 1) + topLeft.Y;

        return temp;
    }

    public  int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    @Override
    public String toString() {
        String result;
        result = Integer.toString(X) + " " + Integer.toString(Y);

        return result;
    }
}
