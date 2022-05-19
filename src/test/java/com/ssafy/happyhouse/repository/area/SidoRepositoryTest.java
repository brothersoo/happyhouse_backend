package com.ssafy.happyhouse.repository.area;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import com.ssafy.happyhouse.domain.area.Sido;
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
class SidoRepositoryTest {

  @Autowired
  private SidoRepository sidoRepository;

  @PersistenceContext
  private EntityManager entityManager;

  static final Faker faker = new Faker();

  @Test
  void findAllSidos() {
    // given
    int code = 1;
    for (int i = 0; i < 10; i++) {
      Sido sido = Sido.builder().code(String.valueOf(code)).name(faker.address().state() + code).build();
      code++;
      entityManager.persist(sido);
    }

    // when
    List<Sido> allSidos = sidoRepository.findAll();

    // then
    assertThat(allSidos.size()).isEqualTo(10);
  }
}