package util;

import dto.PlayerRequest;
import exception.InvalidParameterException;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Zа-яА-ЯёЁ\\s\\-'.]+$");
    public static void validate(PlayerRequest playerRequest) {
                String playerName = playerRequest.getName();
        if (playerName == null) {
            throw new InvalidParameterException("Missing parameter - name");
        }
        if (playerName.length() < 2 || playerName.length() > 40) {
            throw new InvalidParameterException("Player name cannot be less 2 characters and more 50 characters");
        }
        if(!NAME_PATTERN.matcher(playerName).matches()) {
            throw new InvalidParameterException("Use only English or Russian letters");
        }
    }
    public static void validatePlayers(PlayerRequest playerRequest1, PlayerRequest playerRequest2) {
        String playerName1 = playerRequest1.getName().trim();
        String playerName2 = playerRequest2.getName().trim();
        if(playerName1.equalsIgnoreCase(playerName2)) {
            throw new InvalidParameterException("The player cannot play by himself");
        }
    }
}
