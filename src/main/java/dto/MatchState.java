package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MatchState {
    boolean isMatchOver = false;
    boolean tieBreak = false;
    boolean isDeuce = false;
    boolean isPlayer1Advantage = false;
    boolean isPlayer2Advantage = false;
}
