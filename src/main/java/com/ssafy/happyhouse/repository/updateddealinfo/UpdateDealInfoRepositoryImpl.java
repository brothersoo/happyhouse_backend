package com.ssafy.happyhouse.repository.updateddealinfo;

import static com.ssafy.happyhouse.domain.housedeal.QUpdatedDealInfo.updatedDealInfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UpdateDealInfoRepositoryImpl implements UpdatedDealInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<UpdatedDealInfo> findByCodeInAndDateBetween(List<String> code, LocalDate fromDate, LocalDate toDate) {
    LocalDate.of(2022, 1, 0);
    return queryFactory
        .selectFrom(updatedDealInfo)
        .where(
            updatedDealInfo.code.in(code),
            updatedDealInfo.date.between(fromDate, toDate)
        )
        .fetch();
  }
}
