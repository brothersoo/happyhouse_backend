package com.ssafy.happyhouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.model.QnADto;
import com.ssafy.happyhouse.repository.QnAMapper;

@Service
public class QnAServiceImpl implements QnAService{

	private QnAMapper qnaMapper;

	public QnAServiceImpl(QnAMapper qnaMapper) {
		this.qnaMapper = qnaMapper;
	}

	@Override
	public boolean createQnA(QnADto qnaDto) {
		return qnaMapper.createQnA(qnaDto) == 1;
	}

	@Override
	public List<QnADto> listQnA() {
		return qnaMapper.listQnA();
	}

	@Override
	public QnADto searchById(Long id) {
		return qnaMapper.findById(id);
	}

	@Override
	public boolean modify(QnADto qnA) {
		return qnaMapper.update(qnA) == 1;
	}

	@Override
	public boolean removeById(Long id) {
		return qnaMapper.deleteById(id) == 1;
	}

}
