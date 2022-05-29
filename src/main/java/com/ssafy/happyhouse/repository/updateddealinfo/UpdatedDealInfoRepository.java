package com.ssafy.happyhouse.repository.updateddealinfo;

import com.ssafy.happyhouse.domain.housedeal.UpdatedDealInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdatedDealInfoRepository
    extends UpdatedDealInfoRepositoryCustom, JpaRepository<UpdatedDealInfo, Long> {

}
