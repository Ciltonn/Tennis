package service;

import model.CurrentMatch;

import java.util.UUID;

public interface OngoingMatchService {
    CurrentMatch getCurrentMatch(UUID matchId);
    void saveCurrentMatch(UUID match_id, CurrentMatch currentMatch);
}
