package com.ssafy.happyhouse.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.happyhouse.model.QnADto;
import com.ssafy.happyhouse.service.QnAService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/qna")
public class QnAController {
	
	private static final Logger logger = LoggerFactory.getLogger(QnAController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	private QnAService qnaService;

	public QnAController(QnAService qnaService) {
		this.qnaService = qnaService;
	}
	
	@ApiOperation(value = "모든 게시글의 정보를 반환한다.", response = List.class)
	@GetMapping
	public ResponseEntity<List<QnADto>> listQnA() throws Exception {
		logger.debug("listQnA - 호출");
		return new ResponseEntity<List<QnADto>>(qnaService.listQnA(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "새로운 게시글 정보를 입력한다. 그리고 DB입력 성공여부에 따라 'success' 또는 'fail' 문자열을 반환한다.", response = String.class)
	@PostMapping
	public ResponseEntity<String> createQnA(@RequestBody QnADto qnaDto) {
		logger.debug("createQnA - 호출");
		logger.debug(qnaDto.toString());
		if (qnaService.createQnA(qnaDto)) {
			return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
		}
		return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value="ID를 통해 QnA의 상세 정보를 반환합니다.", response= QnADto.class)
	@GetMapping("/{id}")
	public ResponseEntity<?> searchById(@PathVariable("id") long id) {
		try {
			QnADto qna = qnaService.searchById(id);
			return new ResponseEntity<>(qna, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value="ID를 통해 QnA를 수정합니다.", response=Integer.class)
	@PutMapping()
	public ResponseEntity<?> modify(@RequestBody QnADto qnaDto) {
		try {
			logger.debug("qnaDto info : {}", qnaDto);
			if (qnaService.modify(qnaDto)) {
				return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
			}
		} catch(Exception e) {
			return new ResponseEntity<String>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value="ID를 통해 QnA를 삭제합니다.", response=Integer.class)
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeById(@PathVariable("id") long id) {
		try {
			if (qnaService.removeById(id)) {
				return new ResponseEntity<>(SUCCESS, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>(FAIL, HttpStatus.NO_CONTENT);
			}
		} catch(Exception e) {
			return new ResponseEntity<String>("Sorry: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
