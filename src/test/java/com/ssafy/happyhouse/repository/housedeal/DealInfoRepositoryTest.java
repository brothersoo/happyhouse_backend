package com.ssafy.happyhouse.repository.housedeal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
import com.ssafy.happyhouse.repository.area.UpmyundongRepository;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig.class)
class DealInfoRepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  DealInfoRepository dealInfoRepository;

  @Autowired
  UpmyundongRepository upmyundongRepository;

  Upmyundong persistBaseArea() {
    Sido sido = Sido.builder().name("sido").code("11").build();
    entityManager.persist(sido);
    Sigugun sigugun = Sigugun.builder().name("sigugun1")
        .code(sido.getCode() + "110").sido(sido).build();
    entityManager.persist(sigugun);
    Upmyundong upmyundong = Upmyundong.builder().sigugun(sigugun).code(sigugun.getCode() + "777")
        .name("sigugun").lat(0.1F).lng(01.F).build();
    entityManager.persist(upmyundong);
    return upmyundong;
  }

  @Test
  void saveTest() {
    String code = "11110";
    Upmyundong upmyundong = upmyundongRepository.findByCodeStartingWith(code).get(0);

    HouseInfo houseInfo = HouseInfo.builder().buildYear(1996)
        .aptName("GREAT_APT").jibun("1").upmyundong(upmyundong).build();
    entityManager.persist(houseInfo);
    DealInfo dealInfo = DealInfo.builder().dealDate(LocalDate.of(2000,6,10))
        .type("A").price(1).exclusivePrivateArea(1F).floor(1).houseInfo(houseInfo).build();

    DealInfo persistedHouseInfo = dealInfoRepository.save(dealInfo);

    assertThat(persistedHouseInfo).isEqualTo(dealInfo);
  }

  @Test
  void findByCodeAndDealYearAndDealMonthWithHouseInfo() {
    String code = "11110";
    Upmyundong upmyundong = upmyundongRepository.findByCodeStartingWith(code).get(0);

    HouseInfo houseInfo = HouseInfo.builder().aptName("aptName").buildYear(2000).jibun("1")
        .upmyundong(upmyundong).build();
    entityManager.persist(houseInfo);
    for (int i = 0; i < 5; i++) {
      DealInfo dealInfo = DealInfo.builder().dealDate(LocalDate.of(1996,6,10))
          .floor(1).price(1).exclusivePrivateArea(1).type("A").houseInfo(houseInfo).build();
      entityManager.persist(dealInfo);
    }
    for (int i = 0; i < 3; i++) {
      DealInfo dealInfo = DealInfo.builder().dealDate(LocalDate.of(1997,6,10))
          .floor(1).price(1).exclusivePrivateArea(1).type("A").houseInfo(houseInfo).build();
      entityManager.persist(dealInfo);
    }

    List<DealInfo> res = dealInfoRepository.findByCodeAndYearMonthOfDate(code, LocalDate.of(1996, 6, 1));
    assertThat(res.size()).isEqualTo(5);

    for (DealInfo dealInfo : res) {
      assertThat(dealInfo.getHouseInfo().getAptName()).isEqualTo("aptName");
    }
  }
}