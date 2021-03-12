package com.company.temp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.common.MovieAPI;

@Controller
public class MovieController {
	
	@Autowired MovieAPI movieAPI;
	
	/*
	 * @ResponseBody 
 		- 자바 객체를 HTTP 응답 몸체로 전송함
 		- 자바 객체를 HTTP 요청의 body 내용으로 매핑하는 역할
		출처: https://lee-mandu.tistory.com/242 [개발/일상_Mr.lee]
	 */

	// 영화 정보에서 영화 제목만
	@RequestMapping("/boxOffice")
	@ResponseBody
	public List<String> boxOffice(){
		List<String> list = new ArrayList<>();
		Map map = movieAPI.getBoxOffice();
		Map boxOfficeResult = (Map) map.get("boxOfficeResult");
		List<Map> dailyBoxOfficeList = (List<Map>) boxOfficeResult.get("dailyBoxOfficeList");
		for(Map movie : dailyBoxOfficeList) {
			list.add((String) movie.get("movieNm") + ":" + (String)movie.get("movieCd"));
		}
		
		return list;
	}
	// 영화 정보에서 배우 이름만
	@RequestMapping("/movieActor")
	@ResponseBody
	public List<String> movieActor(){
		List<String> list = new ArrayList<>();
		Map map = movieAPI.getMovieInfo();
		Map movieInfoResult = (Map) map.get("movieInfoResult");
		Map movieInfo = (Map) movieInfoResult.get("movieInfo");
		List<Map> actors = (List<Map>) movieInfo.get("actors");
		
		for(Map peopleNm : actors) {
			list.add((String) peopleNm.get("peopleNm"));
		}
		return list;
	}
}
	
	