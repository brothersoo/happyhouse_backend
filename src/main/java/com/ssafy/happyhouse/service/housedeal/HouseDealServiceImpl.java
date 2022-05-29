package com.ssafy.happyhouse.service.housedeal;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import com.ssafy.happyhouse.repository.housedeal.HouseDealRepository;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class HouseDealServiceImpl implements HouseDealService {

  private final HouseDealRepository houseDealRepository;

  @Override
  public List<HouseDeal> getDealsOfHouse(Long houseId) {
    return houseDealRepository.findByHouseIdOrderByDealDateDesc(houseId);
  }

  @Override
  public Set<HouseDeal> getHouseDealSetInSigugunBetweenDate(List<String> codes, LocalDate fromDate,
      LocalDate toDate) {
    return new HashSet<>(houseDealRepository.findByCodeInAndDateBetween(codes, fromDate, toDate));
  }

  @Override
  @Transactional
  public List<HouseDeal> saveAll(Collection<HouseDeal> houseDeals) {
    return houseDealRepository.saveAll(houseDeals);
  }

  @Override
  public List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(List<Long> houseIds,
      LocalDate fromDate, LocalDate toDate, String type) {
    return houseDealRepository.findHouseAveragePriceByCodeAndDateRange(
        houseIds, fromDate, toDate, type);
  }

  @Override
  @Transactional
  public int batchInsert(List<HouseDeal> houseDeals) {
    return houseDealRepository.batchInsert(houseDeals);
  }
}
