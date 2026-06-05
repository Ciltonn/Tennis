package entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table (name = "matches", check = @CheckConstraint(name="match_players_and_winner_chek",
        constraint = "first_player_id <> second_player_id AND (winner_id = first_player_id OR winner_id = second_player_id)"))

public class TennisMatch {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch=FetchType.LAZY, optional = false)
    @JoinColumn (name = "first_player_id", nullable = false, updatable = false)
    private Player firstPlayer;

    @ManyToOne (fetch=FetchType.LAZY, optional = false)
    @JoinColumn (name = "second_player_id", nullable = false, updatable = false)
    private Player secondPlayer;

    @ManyToOne (fetch=FetchType.LAZY, optional = false)
    @JoinColumn (name = "winner_id", nullable = false, updatable = false)
    private Player winner;

    public TennisMatch(Player firstPlayer, Player secondPlayer, Player winner) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.winner = winner;
    }
}
