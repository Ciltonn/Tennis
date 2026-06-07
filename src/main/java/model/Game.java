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
            return firstPlayerPoints = firstPlayerPoints.next();
        } else return secondPlayerPoints = secondPlayerPoints.next();
    }

    public boolean isGameFinished() {
        int maxOrdinalToLoss = 2;
        if(firstPlayerPoints==Point.FORTY && secondPlayerPoints.ordinal()<=maxOrdinalToLoss) {
            return true;
        }
        if(secondPlayerPoints==Point.FORTY && firstPlayerPoints.ordinal()<=maxOrdinalToLoss) {
            return true;
        }
        if(firstPlayerPoints == Point.ADVANTAGE && secondPlayerPoints.ordinal()<=maxOrdinalToLoss) {
            return true;
        }
        if(secondPlayerPoints==Point.ADVANTAGE && firstPlayerPoints.ordinal()<=maxOrdinalToLoss) {
            return true;
        }
        return false;
    }
    public int getWinnerGame(){
        if(!isGameFinished()) {
            return 0;
        }
        if(firstPlayerPoints==Point.ADVANTAGE || firstPlayerPoints==Point.FORTY) {
            return 1;
        } else return 2;
    }
}
