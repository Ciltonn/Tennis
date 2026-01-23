package service;

import dto.CurrentMatch;

import java.util.HashMap;
import java.util.UUID;

public class OngoingMatchService {
    public static final HashMap<UUID, CurrentMatch> currentMatches = new HashMap<>();

    public CurrentMatch getCurrentMatch(UUID match_id) {
        return currentMatches.get(match_id);
    }
    public void saveCurrentMatch(UUID match_id, CurrentMatch currentMatch) {
        currentMatches.put(match_id, currentMatch);
    }
    public void deleteCurrentMatch(UUID uuid) {
        currentMatches.remove(uuid);
    }

}
