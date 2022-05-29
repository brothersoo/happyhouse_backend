package com.ssafy.happyhouse.service.area.sigugun;

import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.repository.area.SigugunRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SigugunServiceImpl implements SigugunService {

  private final SigugunRepository sigugunRepository;

  @Override
  public Map<String, Sigugun> getCodeSigugunMap(List<String> codes) {
    Map<String, Sigugun> sigugunMap = new HashMap<>();
    for (Sigugun sigugun : sigugunRepository.findByCodeIn(codes)) {
      sigugunMap.putIfAbsent(sigugun.getCode(), sigugun);
    }
    return sigugunMap;
  }
}
