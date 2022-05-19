package com.ssafy.happyhouse.domain.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "sigugun")
@Entity
public class Sigugun {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sigugun_id")
  private Long id;

  private String code;

  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sido_id")
  private Sido sido;

//  @OneToMany(mappedBy = "sigugun")
//  private List<Upmyundong> upmyundongs;

  @Builder
  public Sigugun(Long id, String code, String name, Sido sido, List<Upmyundong> upmyundongs) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.sido = sido;
//    this.upmyundongs = upmyundongs;
  }
}
