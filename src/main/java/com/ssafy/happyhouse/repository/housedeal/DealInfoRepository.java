package com.ssafy.happyhouse.repository.housedeal;

import com.ssafy.happyhouse.domain.housedeal.DealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealInfoRepository
    extends JpaRepository<DealInfo, Long>, DealInfoRepositoryCustom { }
