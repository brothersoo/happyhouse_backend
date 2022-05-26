package com.ssafy.happyhouse.repository.housedeal;
import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface HouseDealRepository
    extends JpaRepository<HouseDeal, Long>, HouseDealRepositoryCustom {

  List<HouseDeal> findByHouseIdOrderByDealDateDesc(@Param("houseId") Long houseId);
}
