package com.ssafy.happyhouse.repository.area;

import com.ssafy.happyhouse.domain.area.Sigugun;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface SigugunRepository extends JpaRepository<Sigugun, Long> {

//  @Query(value = "select distinct s from sigugun s join fetch s.sido")
//  List<Sigugun> findAllWithSido();

  List<Sigugun> findByCodeStartingWith(@Param("") String sidoCode);

  List<Sigugun> findByCodeIn(@Param("") List<String> codes);
}
