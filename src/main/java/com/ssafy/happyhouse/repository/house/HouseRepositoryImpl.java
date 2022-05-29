package com.ssafy.happyhouse.repository.house;

import static com.ssafy.happyhouse.domain.area.QUpmyundong.upmyundong;
import static com.ssafy.happyhouse.domain.housedeal.QHouse.house;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.housedeal.House;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class HouseRepositoryImpl implements HouseRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final JdbcTemplate jdbcTemplate;

  public List<House> findByCodeStartingWithIn(List<String> codes) {
    return queryFactory
        .selectFrom(house)
        .join(house.upmyundong, upmyundong)
        .fetchJoin()
        .where(upmyundong.code.substring(0, 5).in(codes))
        .fetch();
  }

  public int batchInsert(List<House> houses) {
    if (houses.isEmpty()) {
      return 0;
    }

    String sql = "INSERT INTO house(apt_name, build_year, jibun, upmyundong_id) VALUES(?, ?, ?, ?)";

    int[] res = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        House house = houses.get(i);
        ps.setString(1, house.getAptName());
        ps.setInt(2, house.getBuildYear());
        ps.setString(3, house.getJibun());
        ps.setLong(4, house.getUpmyundong().getId());
      }

      @Override
      public int getBatchSize() {
        return houses.size();
      }
    });

    return res.length;
  }
}
