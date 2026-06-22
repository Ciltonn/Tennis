package model;

public class Game {
    private Point firstPlayerPoints;
    private Point secondPlayerPoints;

    public Game() {
        this.firstPlayerPoints = Point.ZERO;
        this.secondPlayerPoints = Point.ZERO;
    }

    public Point addPoint(int player) {
        if (player == 1) {
            if (secondPlayerPoints == Point.ADVANTAGE) {
                secondPlayerPoints = Point.FORTY;
                return firstPlayerPoints;
            }
            return firstPlayerPoints = firstPlayerPoints.next();
        } else {
            if (firstPlayerPoints == Point.ADVANTAGE) {
                firstPlayerPoints = Point.FORTY;
                return secondPlayerPoints;
            }
            return secondPlayerPoints = secondPlayerPoints.next();
        }
    }

    public boolean isGameFinished() {
        int firstPlayerOrdinal = firstPlayerPoints.ordinal();
        int secondPlayerOrdinal = secondPlayerPoints.ordinal();
        if (firstPlayerOrdinal >= 4 && firstPlayerOrdinal - secondPlayerOrdinal >= 2) {
            return true;
        }
        if (secondPlayerOrdinal >= 4 && secondPlayerOrdinal - firstPlayerOrdinal >= 2) {
            return true;
        }
        return false;
    }

    public int getWinnerGame() {
        if (!isGameFinished()) {
            return 0;
        }
        if (firstPlayerPoints == Point.ADVANTAGE || firstPlayerPoints == Point.FORTY) {
            return 1;
        } else return 2;
    }

    public String getPointDisplay(int player) {
        Point point = (player == 1) ? firstPlayerPoints : secondPlayerPoints;
        if (firstPlayerPoints == Point.FORTY && secondPlayerPoints == Point.FORTY) {
            return "40";
        }
        return point.getValue();
    }
}
