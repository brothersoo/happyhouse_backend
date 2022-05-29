package com.ssafy.happyhouse.repository.area.upmyundong;

import static com.ssafy.happyhouse.domain.area.QSigugun.sigugun;
import static com.ssafy.happyhouse.domain.area.QUpmyundong.upmyundong;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.happyhouse.domain.area.Upmyundong;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UpmyundongRepositoryImpl implements UpmyundongRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Upmyundong> findByCodeInStartingWith(List<String> codes) {
    return queryFactory
        .selectFrom(upmyundong)
        .join(upmyundong.sigugun, sigugun)
        .fetchJoin()
        .where(sigugun.code.in(codes))
        .fetch();
  }
}
