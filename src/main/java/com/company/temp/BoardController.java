package com.company.temp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.common.BoardVO;

@Controller
public class BoardController {
	
	// 파라미터를 VO에 담아 결과 페이지에 title, writer 출력하는 핸들러 작성
	@RequestMapping("/insertBoard")
	public String insertBoard(BoardVO vo) { // vo는 자동으로 Model에 저장됨(첫글자 소문자 형태) = boardVO
		return "insertBoardResult";
	}
	
	// 파라미터를 VO에 담아 vo 자체를 응답하는 핸들러 작성
	@RequestMapping("/ajaxinsertBoard")
	public @ResponseBody BoardVO ajaxinsertBoard(BoardVO vo) {
		return vo;
	}
	
	// 
	

}
