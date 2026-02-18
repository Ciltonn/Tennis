package service;

import dto.CurrentMatch;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchService {
    public static final Map<UUID, CurrentMatch> currentMatches = new ConcurrentHashMap<>();

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
