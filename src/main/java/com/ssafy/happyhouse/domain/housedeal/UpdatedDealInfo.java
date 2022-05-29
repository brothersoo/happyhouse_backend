package com.ssafy.happyhouse.domain.housedeal;

import com.ssafy.happyhouse.domain.area.Sigugun;
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
@Table(name = "updated_deal_info")
@Entity
public class UpdatedDealInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "updated_deal_info_id")
  private Long id;

  private LocalDate date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sigugun_id")
  private Sigugun sigugun;

  @Builder
  public UpdatedDealInfo(LocalDate date, Sigugun sigugun) {
    this.date = date;
    this.sigugun = sigugun;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdatedDealInfo that = (UpdatedDealInfo) o;
    return Objects.equals(date, that.date) && Objects.equals(sigugun.getCode(),
        that.sigugun.getCode());
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, sigugun.getCode());
  }
}
