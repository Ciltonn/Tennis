package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public enum TennisPoint {
    ZERO("0"),
    FIFTEEN("15"),
    THIRTY("30"),
    FORTY("40"),
    ADVANTAGE("AD");

    private final String value;

    public TennisPoint next() {
       return switch (this) {
            case ZERO->FIFTEEN;
            case FIFTEEN->THIRTY;
            case THIRTY->FORTY;
            case FORTY->ADVANTAGE;
           case ADVANTAGE->throw new IllegalStateException("Has not next value for ADVANTAGE");
            };
    }
}


