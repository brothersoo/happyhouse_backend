package com.ssafy.happyhouse.repository.area;

import com.ssafy.happyhouse.domain.area.Sigugun;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SigugunRepository extends JpaRepository<Sigugun, Long> {

//  @Query(value = "select distinct s from sigugun s join fetch s.sido")
//  List<Sigugun> findAllWithSido();

  List<Sigugun> findByCodeStartingWith(String sidoCode);
}
