package util;

import dto.PlayerRequestDto;
import entity.Player;
import exception.InvalidParameterException;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\s\\-'.]+$");

    public static void validate(PlayerRequestDto playerRequest) {
        if (playerRequest.getFirstPlayer() == null) {
            throw new InvalidParameterException("Missing parameter - name");
        }
        if (playerRequest.getSecondPlayer() == null) {
            throw new InvalidParameterException("Missing parameter - name");
        }
        if (!NAME_PATTERN.matcher(playerRequest.getFirstPlayer()).matches()) {
            throw new InvalidParameterException("\"Use only English or Russian letters, spaces, hyphens( - ), apostrophes( ' ), and dots( . )");
        }
        if (!NAME_PATTERN.matcher(playerRequest.getSecondPlayer()).matches()) {
            throw new InvalidParameterException("\"Use only English or Russian letters, spaces, hyphens( - ), apostrophes( ' ), and dots( . )");
        }
        if (!playerRequest.getFirstPlayer().matches(".*[a-zA-Zа-яА-ЯёЁ].*")) {
            throw new InvalidParameterException("Player name must contain at least one letter");
        }
        if (!playerRequest.getSecondPlayer().matches(".*[a-zA-Zа-яА-ЯёЁ].*")) {
            throw new InvalidParameterException("Player name must contain at least one letter");
        }
        if (playerRequest.getFirstPlayer().isEmpty() || playerRequest.getSecondPlayer().isEmpty()) {
            throw new InvalidParameterException("Player requests cannot be null");
        }
        if (playerRequest.getFirstPlayer().equalsIgnoreCase(playerRequest.getSecondPlayer())) {
            throw new InvalidParameterException("The player cannot play by himself");
        }
    }
}
