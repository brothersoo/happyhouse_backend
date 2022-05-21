package com.ssafy.happyhouse.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ssafy.happyhouse.model.QnADto;
import com.ssafy.happyhouse.repository.QnAMapper;

@RequiredArgsConstructor
@Service
public class QnAServiceImpl implements QnAService{

	private final QnAMapper qnaMapper;

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
