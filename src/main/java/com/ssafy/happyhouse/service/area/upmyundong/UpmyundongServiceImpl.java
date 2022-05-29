package com.ssafy.happyhouse.service.area.upmyundong;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.repository.area.upmyundong.UpmyundongRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpmyundongServiceImpl implements UpmyundongService {

  private final UpmyundongRepository upmyundongRepository;

  @Override
  public Map<String, Upmyundong> getNameUpmyundongMap(List<String> codes) {
    Map<String, Upmyundong> upmyundongMap = new HashMap<>();
    for (Upmyundong upmyundong : upmyundongRepository.findByCodeInStartingWith(codes)) {
      upmyundongMap.putIfAbsent(upmyundong.getName(), upmyundong);
    }
    return upmyundongMap;
  }
}
