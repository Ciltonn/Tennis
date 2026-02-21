package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table (name = "matches")
@Check(constraints = "player1_id <> player2_id AND (winner_id = player1_id OR winner_id = player2_id)")
public class Match {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "player1_id", nullable = false, updatable = false)
    private Player playerFirst;

    @ManyToOne
    @JoinColumn (name = "player2_id", nullable = false, updatable = false)
    private Player playerSecond;

    @ManyToOne
    @JoinColumn (name = "winner_id", nullable = false, updatable = false)
    private Player winner;
}
