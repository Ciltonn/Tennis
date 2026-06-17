package service;

import dao.MatchDaoImpl;
import dao.PlayerDaoImpl;
import entity.TennisMatch;

import java.util.Collections;
import java.util.List;

public class MatchService {
    private MatchDaoImpl matchDaoImpl;
    private PlayerDaoImpl playerDaoImpl;

    public MatchService(MatchDaoImpl matchDaoImpl, PlayerDaoImpl playerDaoImpl) {
        this.matchDaoImpl = matchDaoImpl;
        this.playerDaoImpl = playerDaoImpl;
    }
    public List<TennisMatch> getMatches(String filterByName, int page, int size) {
        int offset = (page - 1) * size;
        int limit = size;
        if(filterByName != null && !filterByName.trim().isEmpty()) {
            return playerDaoImpl.findByName(filterByName.trim())
                    .map(player->matchDaoImpl.findAllByPlayerNameWithPagination(player.getId(), offset, limit))
                            .orElse(Collections.emptyList());


        } else {

            return matchDaoImpl.findAllWithPagination(offset, limit);
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
