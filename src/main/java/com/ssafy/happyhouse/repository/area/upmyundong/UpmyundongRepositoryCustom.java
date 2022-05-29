package com.ssafy.happyhouse.repository.area.upmyundong;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;

public interface UpmyundongRepositoryCustom {

  List<Upmyundong> findByCodeInStartingWith(List<String> codes);
}
