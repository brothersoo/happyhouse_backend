package com.ssafy.happyhouse.persistence.repository.housedeal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import com.ssafy.happyhouse.repository.housedeal.HouseDealRepository;
import java.time.LocalDate;
import java.util.ArrayList;
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
class HouseDealRepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  HouseDealRepository houseDealRepository;

  Upmyundong persistBaseArea() {
    Sido sido = Sido.builder().name("sido").code("SIDO_").build();
    entityManager.persist(sido);
    Sigugun sigugun = Sigugun.builder().name("sigugun")
        .code(sido.getCode() + "SIGUGUN_").sido(sido).build();
    entityManager.persist(sigugun);
    Upmyundong upmyundong = Upmyundong.builder().sigugun(sigugun).code(sigugun.getCode() + "UPMYUNDONG_")
        .name("sigugun").lat(0.1F).lng(01.F).build();
    entityManager.persist(upmyundong);
    return upmyundong;
  }

  @Test
  void saveTest() {
    Upmyundong upmyundong = persistBaseArea();

    House house = House.builder().buildYear(1996)
        .aptName("GREAT_APT").jibun("1").upmyundong(upmyundong).build();
    entityManager.persist(house);
    HouseDeal houseDeal = HouseDeal.builder().dealDate(LocalDate.of(2000,6,10))
        .type("A").price(1).exclusivePrivateArea(1F).floor(1).house(house).build();

    HouseDeal persistedHouse = houseDealRepository.save(houseDeal);

    assertThat(persistedHouse).isEqualTo(houseDeal);
  }

  @Test
  void findByCodeAndDealYearAndDealMonthWithHouse() {
    Upmyundong upmyundong = persistBaseArea();

    House house = House.builder().aptName("aptName").buildYear(2000).jibun("1")
        .upmyundong(upmyundong).build();
    entityManager.persist(house);
    for (int i = 0; i < 5; i++) {
      HouseDeal houseDeal = HouseDeal.builder().dealDate(LocalDate.of(1996,6,10))
          .floor(1).price(1).exclusivePrivateArea(1).type("A").house(house).build();
      entityManager.persist(houseDeal);
    }
    for (int i = 0; i < 3; i++) {
      HouseDeal houseDeal = HouseDeal.builder().dealDate(LocalDate.of(1997,6,10))
          .floor(1).price(1).exclusivePrivateArea(1).type("A").house(house).build();
      entityManager.persist(houseDeal);
    }

    List<HouseDeal> res = houseDealRepository.findByCodeAndYearMonthOfDate(upmyundong.getCode(), LocalDate.of(1996, 6, 1));
    assertThat(res.size()).isEqualTo(5);

    for (HouseDeal houseDeal : res) {
      assertThat(houseDeal.getHouse().getAptName()).isEqualTo("aptName");
    }
  }

  @Test
  void batchInsertTest() {
    Upmyundong upmyundong = persistBaseArea();

    House house = House.builder().buildYear(2000).jibun("1")
        .aptName("apt").upmyundong(upmyundong).build();
    entityManager.persist(house);

    List<HouseDeal> houseDeals = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      HouseDeal houseDeal = HouseDeal.builder()
          .dealDate(LocalDate.of(2000, 1, 1))
          .exclusivePrivateArea(1).price(100).floor(1)
          .type("A").house(house).build();
      houseDeals.add(houseDeal);
    }

    int res = houseDealRepository.batchSave(houseDeals);

    assertThat(res).isEqualTo(houseDeals.size());

    List<HouseDeal> list = houseDealRepository.findByHouseIdOrderByDealDateDesc(house.getId());
    assertThat(list.size()).isEqualTo(houseDeals.size());
  }
}