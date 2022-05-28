package com.ssafy.happyhouse.persistence.repository.area;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.repository.area.SigugunRepository;
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
class SigugunRepositoryTest {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  SigugunRepository sigugunRepository;

  @Test
  void findByCodeStartingWithSidoCodeTest() {
    Sido sido1 = Sido.builder().name("sido1").code("01").build();
    Sido sido2 = Sido.builder().name("sido2").code("02").build();
    entityManager.persist(sido1);
    entityManager.persist(sido2);

    int sigugunSeq = 1;
    Sigugun sigugun;
    for (int i = 0; i < 5; i++) {
      sigugun = Sigugun.builder().sido(sido1).code("01" + i).name("sigugun" + sigugunSeq++)
          .build();
      entityManager.persist(sigugun);
    }
    for (int i = 0; i < 3; i++) {
      sigugun = Sigugun.builder().sido(sido2).code("02" + i).name("sigugun" + sigugunSeq++)
          .build();
      entityManager.persist(sigugun);
    }

    List<Sigugun> siguguns01 = sigugunRepository.findByCodeStartingWith("01");

    assertThat(siguguns01.size()).isEqualTo(5);
  }

//  @Test
//  void findAllWithSidoTest() {
//    Sido sido1 = Sido.builder().name("sido1").code("01").build();
//    Sido sido2 = Sido.builder().name("sido2").code("02").build();
//    entityManager.persist(sido1);
//    entityManager.persist(sido2);
//
//    int sigugunSeq = 1;
//    Sigugun sigugun;
//    for (int i = 0; i < 5; i++) {
//      sigugun = Sigugun.builder().sido(sido1).code("01" + i).name("sigugun" + sigugunSeq++)
//          .build();
//      entityManager.persist(sigugun);
//    }
//    for (int i = 0; i < 3; i++) {
//      sigugun = Sigugun.builder().sido(sido2).code("02" + i).name("sigugun" + sigugunSeq++)
//          .build();
//      entityManager.persist(sigugun);
//    }
//
//    sigugunRepository.findAllWithSido();
//  }

}