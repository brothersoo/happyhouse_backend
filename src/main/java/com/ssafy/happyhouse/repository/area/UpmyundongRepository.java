package com.ssafy.happyhouse.repository.area;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpmyundongRepository extends JpaRepository<Upmyundong, Long> {

  List<Upmyundong> findByCodeStartingWith(String sigugunCode);
}
