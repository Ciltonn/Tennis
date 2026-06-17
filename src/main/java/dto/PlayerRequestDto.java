package dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record PlayerRequestDto(
        @NotBlank(message = "Player name cannot be blank.")
        @Size(min = 2, max = 30, message = "Player name must be between {min} and {max} characters.")
    String firstPlayer,
    @NotBlank(message = "Player name cannot be blank.")
    @Size(min = 2, max = 30, message = "Player name must be between {min} and {max} characters.")
    String secondPlayer
) {
        public String getFirstPlayer() {
        return firstPlayer;
    }

        public String getSecondPlayer() {
        return secondPlayer;
    }
}
