package com.ssafy.happyhouse.service.updateddealinfo;

import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import com.ssafy.happyhouse.repository.updateddealinfo.UpdatedDealInfoRepository;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UpdatedDealInfoServiceImpl implements UpdatedDealInfoService {

  private final UpdatedDealInfoRepository updatedDealInfoRepository;

  @Override
  public Set<UpdatedDealInfo> findByCodeInAndDateBetweenSet(List<String> codes, LocalDate fromDate,
      LocalDate toDate) {
    return new HashSet<>(
        updatedDealInfoRepository.findBySigugunCodeInAndDateBetween(codes, fromDate, toDate));
  }

  @Override
  @Transactional
  public List<UpdatedDealInfo> saveAll(List<UpdatedDealInfo> updatedDealInfo) {
    return updatedDealInfoRepository.saveAll(updatedDealInfo);
  }
}
