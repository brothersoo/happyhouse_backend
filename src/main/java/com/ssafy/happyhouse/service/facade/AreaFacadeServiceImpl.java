package com.ssafy.happyhouse.service.facade;

import com.ssafy.happyhouse.domain.area.Sido;
import com.ssafy.happyhouse.domain.area.Sigugun;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import com.ssafy.happyhouse.repository.area.SidoRepository;
import com.ssafy.happyhouse.repository.area.SigugunRepository;
import com.ssafy.happyhouse.repository.area.upmyundong.UpmyundongRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AreaFacadeServiceImpl implements AreaFacadeService {

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

  public List<Upmyundong> searchUpmyundongsInSiguguns(List<String> codes) {
    return upmyundongRepository.findByCodeInStartingWith(codes);
  }

  public Upmyundong searchUpmyundongByName(String name) {
    return upmyundongRepository.findFirstByName(name);
  }
}
