package dto;

import entity.Player;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class FinishedMatch {
    private Player player1;
    private Player player2;
    private Player winner;


}
