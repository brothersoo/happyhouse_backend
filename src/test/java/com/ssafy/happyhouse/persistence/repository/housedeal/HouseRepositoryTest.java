package com.ssafy.happyhouse.persistence.repository.housedeal;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.House;
import com.ssafy.happyhouse.repository.house.HouseRepository;
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
public class HouseRepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  HouseRepository houseRepository;

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
  void findByUpmyundongIdTest() {
    Upmyundong upmyundong = persistBaseArea();

    for (int i = 0; i < 1; i++) {
      House house = House.builder().buildYear(1996).jibun("1")
          .aptName("GOOD_APT" + i).upmyundong(upmyundong).build();
      entityManager.persist(house);
    }

    List<House> res = houseRepository.findByUpmyundongId(upmyundong.getId());

    for (House house : res) {
      assertThat(house.getUpmyundong().getCode()).startsWith(upmyundong.getCode());
    }
  }

  @Test
  void batchInsertTest() {
    Upmyundong upmyundong = persistBaseArea();

    List<House> houses = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      House house = House.builder().aptName("A").jibun("1").buildYear(2000).upmyundong(upmyundong)
          .build();
      houses.add(house);
    }

    int insertedCount = houseRepository.batchInsert(houses);
    assertThat(insertedCount).isEqualTo(houses.size());

    List<House> searched = houseRepository.findByUpmyundongId(upmyundong.getId());
    assertThat(searched.size()).isEqualTo(houses.size());
  }
}
