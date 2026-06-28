package model;

import lombok.Getter;

@Getter
public class Game {
    private Point firstPlayerPoints;
    private Point secondPlayerPoints;
    private boolean finished;

    public Game() {
        this.firstPlayerPoints = Point.ZERO;
        this.secondPlayerPoints = Point.ZERO;
        this.finished = false;
    }

    public Point addPoint(int player) {
        if (firstPlayerPoints == Point.ADVANTAGE || secondPlayerPoints == Point.ADVANTAGE) {
            return advantageGame(player);
        }
        return regularGame(player);
    }

    protected Point advantageGame(int player) {
        if (player == 1) {
            if (firstPlayerPoints == Point.ADVANTAGE) {
                finished = true;
                return firstPlayerPoints;
            }
            if (secondPlayerPoints == Point.ADVANTAGE) {
                returnDeuce();
                return secondPlayerPoints;
            }
        } else {
            if (secondPlayerPoints == Point.ADVANTAGE) {
                finished = true;
                return secondPlayerPoints;
            }
            if (firstPlayerPoints == Point.ADVANTAGE) {
                returnDeuce();
                return firstPlayerPoints;
            }
        }
        throw new IllegalStateException("Unexpected state in advantageGame");
    }

    protected Point regularGame(int player) {
        if (player == 1) {
            if (firstPlayerPoints == Point.FORTY) {
                if (secondPlayerPoints == Point.FORTY) {
                    firstPlayerPoints = Point.ADVANTAGE;
                    return firstPlayerPoints;
                }
                finished = true;
                return firstPlayerPoints;
            }
            firstPlayerPoints = firstPlayerPoints.next();
            return firstPlayerPoints;
        } else {
            if (secondPlayerPoints == Point.FORTY) {
                if (firstPlayerPoints == Point.FORTY) {
                    secondPlayerPoints = Point.ADVANTAGE;
                    return secondPlayerPoints;
                }
                finished = true;
                return secondPlayerPoints;
            }
            secondPlayerPoints = secondPlayerPoints.next();
            return secondPlayerPoints;
        }
    }

    protected void returnDeuce() {
        firstPlayerPoints = Point.FORTY;
        secondPlayerPoints = Point.FORTY;
        finished = false;
    }

    public boolean isGameFinished() {
        return finished;
    }

    public int getWinnerGame() {
        if (!finished) {
            return 0;
        }
        if (firstPlayerPoints == Point.FORTY || firstPlayerPoints == Point.ADVANTAGE) {
            return 1;
        } else {
            return 2;
        }
    }
}