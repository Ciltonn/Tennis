package entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "players")

public class Player {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 30, unique = true)
    private String name;

    public Player(String name) {
        this.name = name;
    }
}
