package com.ssafy.happyhouse.domain.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "sido")
@Entity
public class Sido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sido_id")
  private Long id;

  private String code;

  private String name;

//  @OneToMany(mappedBy = "sido")
//  private List<Sigugun> siguguns;

  @Builder
  public Sido(Long id, String code, String name, List<Sigugun> siguguns) {
    this.id = id;
    this.code = code;
    this.name = name;
//    this.siguguns = siguguns;
  }
}
