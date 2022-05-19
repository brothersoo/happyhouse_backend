package com.ssafy.happyhouse.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.happyhouse.model.QnADto;

@Mapper
public interface QnAMapper {
	int createQnA(QnADto qnaDto);
	List<QnADto> listQnA();
	QnADto findById(Long id);
	int update(QnADto qnA);
	int deleteById(Long id);
}
