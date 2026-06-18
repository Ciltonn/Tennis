package service;

import dao.MatchDaoImpl;
import entity.TennisMatch;

import java.util.List;

public class MatchServiceImpl implements MatchService {
    private final MatchDaoImpl matchDaoImpl;

    public MatchServiceImpl(MatchDaoImpl matchDaoImpl) {
        this.matchDaoImpl = matchDaoImpl;
    }

    @Override
    public List<TennisMatch> getMatches(String filterByName, int page, int size) {
        int offset = (page - 1) * size;
        int limit = size;
        if (filterByName != null && !filterByName.trim().isEmpty()) {
            return matchDaoImpl.findByPlayerName(filterByName.trim(), offset, limit);
        } else {
            return matchDaoImpl.findAllWithPagination(offset, limit);
        }
    }
@Override
    public long getTotalMatchesCount(String filterByName) {
        if (filterByName != null && !filterByName.trim().isEmpty()) {
            return matchDaoImpl.countByPlayerName(filterByName.trim());
        } else {
            Long count = matchDaoImpl.countMatchForPagination();
            return count != null ? count : 0L;
        }
    }
}
