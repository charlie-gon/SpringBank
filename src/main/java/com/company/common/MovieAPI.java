package com.company.common;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MovieAPI {
	
	public Map<String, Object> getBoxOffice(){
		
		// 데이터 출처: 영화진흥위원회
		// https://www.kobis.or.kr/kobisopenapi/homepg/apiservice/searchServiceInfo.do
		
		String reqURL = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=f5eef3421c602c6cb7ea224104795888&targetDt=20210311";
		
		// 참고 https://sjh836.tistory.com/141
		RestTemplate restTemp = new RestTemplate();
		return restTemp.getForObject(reqURL, Map.class); // 자동으로 url 요청 및 map에 데이터 담는다
	
	}
	
	public Map getMovieInfo() {
		
		String movieCd = "20205144";
		String reqURL = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=f5eef3421c602c6cb7ea224104795888&movieCd="+movieCd;
		
		RestTemplate restTemp = new RestTemplate();
		return restTemp.getForObject(reqURL, Map.class);
	}
}

