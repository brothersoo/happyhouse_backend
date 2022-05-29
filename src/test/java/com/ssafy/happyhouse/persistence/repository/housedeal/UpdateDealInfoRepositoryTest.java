package com.ssafy.happyhouse.persistence.repository.housedeal;

import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import com.ssafy.happyhouse.repository.updateddealinfo.UpdatedDealInfoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
class UpdateDealInfoRepositoryTest {

  @Autowired
  UpdatedDealInfoRepository updatedDealInfoRepository;
  @PersistenceContext
  EntityManager em;

  @Test
  @DisplayName("코드, 연도, 월로 갱신된지 확인")
  void findByCodeInAndYearAndMonthTest() {
    Sigugun abc = Sigugun.builder().code("abc").build();
    em.persist(UpdatedDealInfo.builder()
        .sigugun(abc).date(LocalDate.of(2021, 12, 31)).build());
    em.persist(UpdatedDealInfo.builder()
        .sigugun(abc).date(LocalDate.of(2022, 1, 1)).build());
    em.persist(UpdatedDealInfo.builder()
        .sigugun(abc).date(LocalDate.of(2022, 2, 1)).build());
    em.persist(UpdatedDealInfo.builder()
        .sigugun(abc).date(LocalDate.of(2022, 3, 1)).build());

    List<UpdatedDealInfo> updatedDealInfos = updatedDealInfoRepository.findBySigugunCodeInAndDateBetween(
        Arrays.asList("abc"), LocalDate.of(2022, 1, 1), LocalDate.of(2022, 4, 1));

    Assertions.assertThat(updatedDealInfos).hasSize(3);
  }
}
