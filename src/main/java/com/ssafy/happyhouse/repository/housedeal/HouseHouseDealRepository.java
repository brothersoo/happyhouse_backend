package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.HouseDeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseHouseDealRepository
    extends JpaRepository<HouseDeal, Long>, HouseDealRepositoryCustom { }
