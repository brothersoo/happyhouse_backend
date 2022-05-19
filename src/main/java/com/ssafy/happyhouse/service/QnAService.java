package com.ssafy.happyhouse.service;

import java.util.List; 

import com.ssafy.happyhouse.model.QnADto;

public interface QnAService {
	
	boolean createQnA(QnADto qnaDto);
	List<QnADto> listQnA();
	QnADto searchById(Long id);
	boolean modify(QnADto qnA);
	boolean removeById(Long id);
}
