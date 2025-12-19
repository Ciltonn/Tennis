package dto;

import entity.Player;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor

public class NewMatch {

    private Long idPlayer1;
    private Long idPlayer2;
    private int sets;
    private int games;
    private int points;
}
