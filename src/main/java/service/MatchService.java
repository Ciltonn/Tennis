package service;

import dao.MatchImpl;
import dao.PlayerDaoImpl;
import entity.TennisMatch;

import java.util.Collections;
import java.util.List;

public class MatchService {
    private MatchImpl matchDaoImpl;
    private PlayerDaoImpl playerDaoImpl;

    public MatchService(MatchImpl matchDaoImpl, PlayerDaoImpl playerDaoImpl) {
        this.matchDaoImpl = matchDaoImpl;
        this.playerDaoImpl = playerDaoImpl;
    }
    public List<TennisMatch> getMatches(String filterByName, int page, int size) {
        if(filterByName != null && !filterByName.trim().isEmpty()) {
            return playerDaoImpl.findByName(filterByName.trim())
                    .map(player->matchDaoImpl.findAllByPlayerNameWithPagination(player.getId(), page, size))
                            .orElse(Collections.emptyList());


        } else {
            return matchDaoImpl.findAllWithPagination(page, size, offset);
        }
    }
    public long getTotalMatchesCount (String filterByName) {
        if(filterByName !=null && !filterByName.trim().isEmpty()) {
            return playerDaoImpl.findByName(filterByName.trim())
                    .map(player -> matchDaoImpl.countMatchesForPlayerForPagination(player.getId()))
                    .orElse(0L);
        } else {
            Long count = matchDaoImpl.countMatchForPagination();
            return count !=null ? count : 0L;
        }
    }
}
