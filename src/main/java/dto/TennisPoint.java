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
    SETWINNER(50);

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
                return SETWINNER;
            case SETWINNER:
                return ZERO;
            default: return this;
        }
    }
}

