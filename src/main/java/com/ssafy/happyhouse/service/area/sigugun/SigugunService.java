package com.ssafy.happyhouse.service.area.sigugun;

import com.ssafy.happyhouse.domain.area.Sigugun;
import java.util.List;
import java.util.Map;

public interface SigugunService {
  Map<String, Sigugun> getCodeSigugunMap(List<String> codes);
}
