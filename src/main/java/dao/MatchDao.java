package dao;

import entity.TennisMatch;

import java.util.List;
import java.util.Optional;

public interface MatchDao {
    Optional<TennisMatch> findById(Long id);
    TennisMatch save(TennisMatch match);
    List<TennisMatch> findAllWithPagination(int page, int pageSize);
    List<TennisMatch> findAllByPlayerNameWithPagination(Long playerId, int page, int pageSize);
    Long countMatchForPagination();
    Long countMatchesForPlayerForPagination(Long playerId);
}
