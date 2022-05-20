package com.ssafy.happyhouse.repository.area;

import com.ssafy.happyhouse.domain.area.Sido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SidoRepository extends JpaRepository<Sido, Long> {

  List<Sido> findByCode(@Param("") String code);
}
