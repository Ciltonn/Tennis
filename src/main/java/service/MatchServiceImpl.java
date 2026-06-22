package service;

import dao.MatchDao;
import dto.MatchDto;
import entity.TennisMatch;

import java.util.List;
import java.util.stream.Collectors;

public class MatchServiceImpl implements MatchService {
    private final MatchDao matchDao;

    public MatchServiceImpl(MatchDao matchDao) {
        this.matchDao = matchDao;
    }

    @Override
    public List<MatchDto> getMatches(String filterByName, int page, int size) {
        int offset = (page - 1) * size;
        int limit = size;
        List<TennisMatch> matches;
        if (filterByName != null && !filterByName.trim().isEmpty()) {
            matches = matchDao.findByPlayerName(filterByName.trim(), offset, limit);
        } else {
            matches = matchDao.findAllWithPagination(offset, limit);
        }
        return matches.stream().map(this::toDto).collect(Collectors.toList());
    }
@Override
    public long getTotalMatchesCount(String filterByName) {
        if (filterByName != null && !filterByName.trim().isEmpty()) {
            return matchDao.countByPlayerName(filterByName.trim());
        } else {
            Long count = matchDao.countMatchForPagination();
            return count != null ? count : 0L;
        }
    }
    private MatchDto toDto (TennisMatch match){
        return new MatchDto(
                match.getFirstPlayer().getName(),
                match.getSecondPlayer().getName(),
                match.getWinner().getName());
    }
}
