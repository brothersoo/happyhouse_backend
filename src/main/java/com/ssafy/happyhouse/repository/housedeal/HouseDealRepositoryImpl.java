package com.ssafy.happyhouse.repository.housedeal;

import static com.ssafy.happyhouse.domain.area.QUpmyundong.upmyundong;
import static com.ssafy.happyhouse.domain.housedeal.QHouseDeal.houseDeal;
import static com.ssafy.happyhouse.domain.housedeal.QHouse.house;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.dto.response.AveragePricePerUnit;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HouseDealRepositoryImpl implements HouseDealRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private final JdbcTemplate jdbcTemplate;

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
  public List<HouseDeal> findByCodeInAndDateBetween(
      List<String> codes, LocalDate fromDate, LocalDate toDate) {
    return queryFactory
        .selectFrom(houseDeal)
        .join(houseDeal.house, house)
        .fetchJoin()
        .join(house.upmyundong, upmyundong)
        .fetchJoin()
        .where(
            upmyundong.code.substring(0, 5).in(codes),
            houseDeal.dealDate.between(fromDate, toDate)
        )
        .fetch();
  }

  @Override
  public List<AveragePricePerUnit> findHouseAveragePriceByCodeAndDateRange(List<Long> houseIds,
      LocalDate fromDate, LocalDate toDate, String type) {

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
        .where(house.id.in(houseIds),
            houseDeal.dealDate.between(fromDate, toDate))
        .groupBy(house.aptName, formattedDate)
        .orderBy(formattedDate.asc())
        .fetch();
  }

  public int batchInsert(List<HouseDeal> deals) {
    if (deals.isEmpty()) {
      return 0;
    }

    String sql = "INSERT INTO house_deal(type, floor, price, "
        + "exclusive_private_area, deal_date, house_id) VALUES(?, ?, ?, ?, ?, ?)";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    int[] res = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        HouseDeal deal = deals.get(i);
        ps.setString(1, deal.getType());
        ps.setInt(2, deal.getFloor());
        ps.setInt(3, deal.getPrice());
        ps.setFloat(4, deal.getExclusivePrivateArea());
        ps.setString(5, deal.getDealDate().format(formatter));
        ps.setLong(6, deal.getHouse().getId());
      }

      @Override
      public int getBatchSize() {
        return deals.size();
      }
    });

    return res.length;
  }
}
