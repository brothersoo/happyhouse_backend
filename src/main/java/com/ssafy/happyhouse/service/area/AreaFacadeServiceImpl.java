package com.ssafy.happyhouse.service.area;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.repository.area.SidoRepository;
import com.ssafy.happyhouse.repository.area.SigugunRepository;
import com.ssafy.happyhouse.repository.area.UpmyundongRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AreaFacadeServiceImpl {

  private final SidoRepository sidoRepository;
  private final SigugunRepository sigugunRepository;
  private final UpmyundongRepository upmyundongRepository;

  public List<Sido> searchAllSidos() {
    return sidoRepository.findAll();
  }

  public List<Sigugun> searchSigugunsInSido(String sidoCode) {
    return sigugunRepository.findByCodeStartingWith(sidoCode);
  }

  public List<Upmyundong> searchUpmyundongsInSigugun(String sigugunCode) {
    return upmyundongRepository.findByCodeStartingWith(sigugunCode);
  }
}
