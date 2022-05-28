package com.ssafy.happyhouse.persistence.repository.area;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.javafaker.Faker;
import com.ssafy.happyhouse.config.QueryDslConfig;
import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.repository.area.SidoRepository;
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
class SidoRepositoryTest {

  @Autowired
  private SidoRepository sidoRepository;

  @PersistenceContext
  private EntityManager entityManager;

  static final Faker faker = new Faker();

  @Test
  void findAllSidos() {
    // given

    // when
    List<Sido> allSidos = sidoRepository.findAll();

    // then
    for (Sido sido : allSidos) {
      assertThat(sido).isInstanceOf(Sido.class);
    }
  }
}