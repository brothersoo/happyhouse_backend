package com.ssafy.happyhouse.repository.area.upmyundong;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UpmyundongRepository
    extends UpmyundongRepositoryCustom, JpaRepository<Upmyundong, Long> {

  List<Upmyundong> findByCodeStartingWith(@Param("") String sigugunCode);

  Upmyundong findFirstByName(@Param("") String name);
}
