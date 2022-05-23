package com.ssafy.happyhouse.domain.housedeal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
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
@Table(name = "house")
@Entity
public class House {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "house_id")
  private Long id;

  private String aptName;

  private int buildYear;

  private String jibun;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "upmyundong_id")
  private Upmyundong upmyundong;

  @Builder
  public House(Long id, String aptName, int buildYear, String jibun,
      Upmyundong upmyundong) {
    this.id = id;
    this.aptName = aptName;
    this.buildYear = buildYear;
    this.jibun = jibun;
    this.upmyundong = upmyundong;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    House house = (House) o;
    return aptName.equals(house.aptName) && upmyundong.getId().equals(house.upmyundong.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(aptName, upmyundong.getId());
  }
}
