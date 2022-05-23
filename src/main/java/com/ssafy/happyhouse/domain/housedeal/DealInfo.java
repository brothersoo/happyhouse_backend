package com.ssafy.happyhouse.domain.housedeal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
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
@Table(name = "deal_info")
@Entity
public class DealInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String type;

  private int floor;

  private int price;

  private float exclusivePrivateArea;

  private LocalDate dealDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "house_info_id")
  private HouseInfo houseInfo;

  public void setPersistedHouseInfo(HouseInfo houseInfo) {
    this.houseInfo = houseInfo;
  }

  @Builder
  public DealInfo(Long id, String type, int floor, int price,float exclusivePrivateArea,
      LocalDate dealDate, HouseInfo houseInfo) {
    this.id = id;
    this.type = type;
    this.floor = floor;
    this.price = price;
    this.exclusivePrivateArea = exclusivePrivateArea;
    this.dealDate = dealDate;
    this.houseInfo = houseInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DealInfo dealInfo = (DealInfo) o;
    return floor == dealInfo.floor && price == dealInfo.price
        && Float.compare(dealInfo.exclusivePrivateArea, exclusivePrivateArea) == 0
        && Objects.equals(dealDate, dealInfo.dealDate) && Objects.equals(type, dealInfo.type)
        && Objects.equals(houseInfo.getId(), dealInfo.houseInfo.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, floor, price, exclusivePrivateArea, dealDate, houseInfo.getId());
  }
}
