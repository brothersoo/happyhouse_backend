package com.ssafy.happyhouse.service.area.upmyundong;

import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import java.util.Map;

public interface UpmyundongService {

  Map<String, Upmyundong> getNameUpmyundongMap(List<String> codes);
}
