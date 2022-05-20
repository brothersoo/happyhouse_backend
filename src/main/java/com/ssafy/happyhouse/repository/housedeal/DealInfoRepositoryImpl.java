package com.ssafy.happyhouse.repository.housedeal;

import static com.ssafy.happyhouse.domain.area.QUpmyundong.upmyundong;
import static com.ssafy.happyhouse.domain.housedeal.QDealInfo.dealInfo;
import static com.ssafy.happyhouse.domain.housedeal.QHouseInfo.houseInfo;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DealInfoRepositoryImpl implements DealInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<DealInfo> findByCodeYearMonthWithHouseInfo(String code, int dealYear, int dealMonth) {
    return queryFactory.selectFrom(dealInfo)
        .leftJoin(dealInfo.houseInfo, houseInfo)
        .leftJoin(houseInfo.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            dealInfo.dealYear.eq(dealYear),
            dealInfo.dealMonth.eq(dealMonth)).fetch();
  }

  @Override
  public List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(String code,
      long houseId, int fromYear, int toYear, int fromMonth, int toMonth, String type) {
    NumberPath<Integer> group;

    BooleanBuilder builder = new BooleanBuilder();
    builder.and(upmyundong.code.startsWith(code))
        .and(houseInfo.id.eq(houseId))
        .and(dealInfo.dealYear.between(fromYear, toYear));
    if (type.equals("month")) {
      builder.and(dealInfo.dealMonth.between(fromMonth, toMonth));
      group = dealInfo.dealMonth;
    } else {
      group = dealInfo.dealYear;
    }

    return queryFactory.select(Projections.fields(AveragePricePerUnit.class,
            houseInfo.aptName.as("name"), group.as("date"),
            dealInfo.price.avg().as("avgPrice")))
        .from(dealInfo)
        .leftJoin(dealInfo.houseInfo, houseInfo)
        .join(houseInfo.upmyundong, upmyundong)
        .where(builder)
        .groupBy(group)
        .orderBy()
        .fetch();
  }
}
