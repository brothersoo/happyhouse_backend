package com.ssafy.happyhouse.service.facade;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;

public interface AreaFacadeService {
  List<Sido> searchAllSidos();
  List<Sigugun> searchSigugunsInSido(String sidoCode);
  List<Upmyundong> searchUpmyundongsInSigugun(String sigugunCode);
  List<Upmyundong> searchUpmyundongsInSiguguns(List<String> codes);
  Upmyundong searchUpmyundongByName(String name);
}
