package com.ssafy.happyhouse.repository.housedeal;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.domain.housedeal.HouseInfo;
import com.ssafy.happyhouse.repository.area.UpmyundongRepository;
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
public class HouseInfoRepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  HouseInfoRepository houseInfoRepository;

  @Autowired
  UpmyundongRepository upmyundongRepository;

  @Test
  void findByUpmyundongIdTest() {
    Upmyundong upmyundong = upmyundongRepository.findByCodeStartingWith("11110").get(0);
    for (int i = 0; i < 1; i++) {
      HouseInfo houseInfo = HouseInfo.builder().buildYear(1996).jibun("1")
          .aptName("GOOD_APT" + i).upmyundong(upmyundong).build();
      entityManager.persist(houseInfo);
    }

    List<HouseInfo> res = houseInfoRepository.findByUpmyundongId(upmyundong.getId());

    for (HouseInfo houseInfo : res) {
      assertThat(houseInfo.getUpmyundong().getCode()).startsWith("11110");
    }
  }
}
