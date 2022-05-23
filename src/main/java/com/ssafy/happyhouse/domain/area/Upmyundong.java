package com.ssafy.happyhouse.domain.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "upmyundong")
@Entity
public class Upmyundong {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "upmyundong_id")
  private Long id;

  private String code;

  private String name;

  private Float lat;

  private Float lng;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="sigugun_id")
  private Sigugun sigugun;

  @Builder
  public Upmyundong(Long id, String code, String name, Float lat, Float lng,
      Sigugun sigugun) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.lat = lat;
    this.lng = lng;
    this.sigugun = sigugun;
  }
}
