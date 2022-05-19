package com.ssafy.happyhouse.domain.housedeal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "house_info")
@Entity
public class HouseInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String aptName;

  private int buildYear;

  private String jibun;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "upmyundong_id")
  private Upmyundong upmyundong;

//  @OneToMany(mappedBy = "houseInfo")
//  private List<DealInfo> dealInfos;

  @Builder
  public HouseInfo(Long id, String aptName, int buildYear, String jibun,
      Upmyundong upmyundong, List<DealInfo> dealInfos) {
    this.id = id;
    this.aptName = aptName;
    this.buildYear = buildYear;
    this.jibun = jibun;
    this.upmyundong = upmyundong;
//    this.dealInfos = dealInfos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HouseInfo houseInfo = (HouseInfo) o;
    return aptName.equals(houseInfo.aptName) && upmyundong.getId().equals(houseInfo.upmyundong.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(aptName, upmyundong.getId());
  }
}
