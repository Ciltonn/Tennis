package service;

import dao.MatchImpl;
import dao.PlayerImpl;
import entity.Match;
import entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchesService {
    private final MatchImpl match;
    private final PlayerImpl player;

    public MatchesService(MatchImpl match, PlayerImpl player) {
        this.match = match;
        this.player = player;
    }

    public List<Match> getMatches(String playerName, int page, int pageSize) {

        if (playerName == null) {
            return match.findAllWithPagination(page, pageSize);
        }
        Optional<Player> playerForFind = player.findByName(playerName);
        if(playerForFind.isEmpty()) {
            return new ArrayList<>();
        }
        Long playerId = playerForFind.get().getId();
        return match.findAllByPlayerNameWithPagination(playerId, page, pageSize);
        }

    public Long getTotalMatchesCount() {
        return match.countMatchForPagination();
    }
  }
