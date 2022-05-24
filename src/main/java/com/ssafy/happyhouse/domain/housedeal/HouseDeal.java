package com.ssafy.happyhouse.domain.housedeal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
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
@Table(name = "house_deal")
@Entity
public class HouseDeal {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "house_deal_id")
  private Long id;

  private String type;

  private int floor;

  private int price;

  private float exclusivePrivateArea;

  private LocalDate dealDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "house_id")
  private House house;

  public void setPersistedHouse(House house) {
    this.house = house;
  }

  @Builder
  public HouseDeal(Long id, String type, int floor, int price,float exclusivePrivateArea,
      LocalDate dealDate, House house) {
    this.id = id;
    this.type = type;
    this.floor = floor;
    this.price = price;
    this.exclusivePrivateArea = exclusivePrivateArea;
    this.dealDate = dealDate;
    this.house = house;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HouseDeal houseDeal = (HouseDeal) o;
    return floor == houseDeal.floor && price == houseDeal.price
        && Float.compare(houseDeal.exclusivePrivateArea, exclusivePrivateArea) == 0
        && Objects.equals(dealDate, houseDeal.dealDate) && Objects.equals(type, houseDeal.type)
        && Objects.equals(house.getId(), houseDeal.house.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, floor, price, exclusivePrivateArea, dealDate, house.getId());
  }
}
