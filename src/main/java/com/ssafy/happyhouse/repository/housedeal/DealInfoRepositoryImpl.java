package com.ssafy.happyhouse.repository.housedeal;

import static com.ssafy.happyhouse.domain.area.QUpmyundong.upmyundong;
import static com.ssafy.happyhouse.domain.housedeal.QDealInfo.dealInfo;
import static com.ssafy.happyhouse.domain.housedeal.QHouseInfo.houseInfo;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DealInfoRepositoryImpl implements DealInfoRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<DealInfo> findByCodeAndYearMonthOfDate(String code, LocalDate date) {
    return queryFactory.selectFrom(dealInfo)
        .leftJoin(dealInfo.houseInfo, houseInfo)
        .leftJoin(houseInfo.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            dealInfo.dealDate.between(date.with(firstDayOfMonth()), date.with(lastDayOfMonth())))
        .fetch();
  }

  @Override
  public List<DealInfo> findByCodeAndDateBetween(String code, LocalDate fromDate, LocalDate toDate) {
    return queryFactory.selectFrom(dealInfo)
        .leftJoin(dealInfo.houseInfo, houseInfo)
        .leftJoin(houseInfo.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            dealInfo.dealDate.between(fromDate, toDate))
        .fetch();
  }

  @Override
  public List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(String code,
      long houseId, LocalDate fromDate, LocalDate toDate, String type) {

    NumberPath<Integer> group;

    StringTemplate formattedDate;
    if (type.equals("month")) {
      formattedDate = Expressions.stringTemplate(
          "DATE_FORMAT({0}, {1})"
          , dealInfo.dealDate
          , ConstantImpl.create("%Y-%m"));
    } else {
      formattedDate = Expressions.stringTemplate(
          "DATE_FORMAT({0}, {1})"
          , dealInfo.dealDate
          , ConstantImpl.create("%Y"));
    }

    return queryFactory.select(Projections.fields(AveragePricePerUnit.class,
            houseInfo.aptName.as("name"), formattedDate.as("date"),
            dealInfo.price.avg().as("avgPrice"), dealInfo.count().as("dealNumber")))
        .from(dealInfo)
        .leftJoin(dealInfo.houseInfo, houseInfo)
        .join(houseInfo.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            houseInfo.id.eq(houseId),
            dealInfo.dealDate.between(fromDate, toDate))
        .groupBy(formattedDate)
        .orderBy(formattedDate.asc())
        .fetch();
  }
}
