package com.ssafy.happyhouse.repository.housedeal;

import static com.ssafy.happyhouse.domain.area.QUpmyundong.upmyundong;
import static com.ssafy.happyhouse.domain.housedeal.QHouseDeal.houseDeal;
import static com.ssafy.happyhouse.domain.housedeal.QHouse.house;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class HouseDealRepositoryImpl implements HouseDealRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<HouseDeal> findByCodeAndYearMonthOfDate(String code, LocalDate date) {
    return queryFactory.selectFrom(houseDeal)
        .leftJoin(houseDeal.house, house)
        .leftJoin(house.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            houseDeal.dealDate.between(date.with(firstDayOfMonth()), date.with(lastDayOfMonth())))
        .fetch();
  }

  @Override
  public List<HouseDeal> findByCodeAndDateBetween(String code, LocalDate fromDate, LocalDate toDate) {
    return queryFactory.selectFrom(houseDeal)
        .leftJoin(houseDeal.house, house)
        .leftJoin(house.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            houseDeal.dealDate.between(fromDate, toDate))
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
          , houseDeal.dealDate
          , ConstantImpl.create("%Y-%m"));
    } else {
      formattedDate = Expressions.stringTemplate(
          "DATE_FORMAT({0}, {1})"
          , houseDeal.dealDate
          , ConstantImpl.create("%Y"));
    }

    return queryFactory.select(Projections.fields(AveragePricePerUnit.class,
            house.aptName.as("name"), formattedDate.as("date"),
            houseDeal.price.avg().as("avgPrice"), houseDeal.count().as("dealNumber")))
        .from(houseDeal)
        .leftJoin(houseDeal.house, house)
        .join(house.upmyundong, upmyundong)
        .where(upmyundong.code.startsWith(code),
            house.id.eq(houseId),
            houseDeal.dealDate.between(fromDate, toDate))
        .groupBy(formattedDate)
        .orderBy(formattedDate.asc())
        .fetch();
  }
}
