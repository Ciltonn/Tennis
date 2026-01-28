package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TennisPoint {
    ZERO(0),
    FIFTEEN(15),
    THIRTY(30),
    FORTY(40),
    GAME(50),
    ADVANTAGE(60),
    NOTADVANTAGE(70),
    ZEROGAME(0),
    ONEGAME(1),
    TWOGAME(2),
    THREEGAME(3),
    FOURGAME(4),
    FIVEGAME(5),
    SIXGAME(6),
    SEVENGAME(7);


    private final int value;

    public String getValuePoints() {
        switch (this) {
            case ZERO:
                return "0";
            case FIFTEEN:
                return "15";
            case THIRTY:
                return "30";
            case FORTY:
                return "40";
            case ADVANTAGE:
                return "AD";
            case NOTADVANTAGE:
                return "  ";
            case GAME:
                return "GAME";
            case ZEROGAME:
                return "0";
            case ONEGAME:
                return "1";
            case TWOGAME:
                return "2";
            case THREEGAME:
                return "3";
            case FOURGAME:
                return "4";
            case FIVEGAME:
                return "5";
            case SIXGAME:
                return "6";
            case SEVENGAME:
                return "7";
            default:
                return String.valueOf(value);
        }
    }

    public TennisPoint getNextValuePoints() {
        switch (this) {
            case ZERO:
                return FIFTEEN;
            case FIFTEEN:
                return THIRTY;
            case THIRTY:
                return FORTY;
            case FORTY:
                return GAME;
            default:
                return this;
        }
    }
    public TennisPoint getNextValueGame() {
        switch (this) {
            case ZEROGAME:
                return ONEGAME;
            case ONEGAME:
                return TWOGAME;
            case TWOGAME:
                return THREEGAME;
            case THREEGAME:
                return FOURGAME;
            case FOURGAME:
                return FIVEGAME;
            case FIVEGAME:
                return SIXGAME;
            case SIXGAME:
                return SEVENGAME;
            default:
                return this;
        }
    }
}

