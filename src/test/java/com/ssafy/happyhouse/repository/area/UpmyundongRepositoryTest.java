package com.ssafy.happyhouse.repository.area;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UpmyundongRepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  UpmyundongRepository upmyundongRepository;

  @Test
  void findByCodeStartingWithSidoCodeTest() {
    Sido sido = Sido.builder().name("sido1").code("01").build();
    entityManager.persist(sido);
    Sigugun sigugun1 = Sigugun.builder().name("sigugun1")
        .code(sido.getCode() + "01").sido(sido).build();
    Sigugun sigugun2 = Sigugun.builder().name("sigugun2")
        .code(sido.getCode() + "02").sido(sido).build();
    entityManager.persist(sigugun1);
    entityManager.persist(sigugun2);

    int upmyundongSeq = 1;
    Upmyundong upmyundong;
    for (int i = 0; i < 5; i++) {
      upmyundong = Upmyundong.builder().sigugun(sigugun1).code(sigugun1.getCode() + i)
          .name("sigugun" + upmyundongSeq++).lat(0.1F).lng(01.F).build();
      entityManager.persist(upmyundong);
    }
    for (int i = 0; i < 3; i++) {
      upmyundong = Upmyundong.builder().sigugun(sigugun2).code(sigugun2.getCode() + i)
          .name("sigugun" + upmyundongSeq++).lat(0.1F).lng(01.F).build();
      entityManager.persist(upmyundong);
    }

    List<Upmyundong> upmyundongs = upmyundongRepository.findByCodeStartingWith(sigugun1.getCode());

    assertThat(upmyundongs.size()).isEqualTo(5);
  }
}