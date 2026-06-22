package service;

import dto.MatchDto;
import entity.TennisMatch;

import java.util.List;

public interface MatchService {
    List<MatchDto> getMatches(String filterByName, int page, int size);
    long getTotalMatchesCount(String filterByName);
}
