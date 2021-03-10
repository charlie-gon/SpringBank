package com.company.temp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.BankAPI;

@Controller
public class BankController {
	
	@Autowired BankAPI bankAPI;
	
	// API 로컬 테스트 페이지 참고 + 오픈뱅킹공동업무_API_명세서 p.14
	@RequestMapping("/auth") 
	public String auth() throws Exception {
		
		String reqURL = "https://testapi.openbanking.or.kr/oauth/2.0/authorize_account";
		
		// 아래와 같이 +로 필요한 값들을 엮으면 성능면에서 좀 떨어진다.
//		String param = 
//				"?response_type=code"
//			    + "&client_id=85bf2e88-ffb6-4387-b218-1f984ea8836e"
//			    + "&redirect_url=http://localhost:8880/html/callback.html" 
//			    + "&scope=login inquiry transfer"
//			    + "&state=01234567890123456789012345678901"
//			    + "&auth_type=0";
		
		String response_type = "code";
		String client_id = "85bf2e88-ffb6-4387-b218-1f984ea8836e";
		String redirect_uri="http://localhost/temp/callback";
		String scope="login inquiry transfer";
		String state="01234567890123456789012345678901";
		String auth_type="0";
		
		// StringBuilder를 이용하면 성능면에서 더 뛰어남
		StringBuilder qstr = new StringBuilder();
		qstr.append("response_type="+response_type)
		.append("&client_id="+client_id)
		.append("&redirect_uri="+redirect_uri)
		.append("&scope="+scope)
		.append("&state="+state)
		.append("&auth_type="+auth_type);
			    
		return "redirect:"+reqURL+"?"+ qstr.toString();	  
	}
	
	@RequestMapping("/callback")
	public String callback(@RequestParam Map<String, Object>map, HttpSession session) {
		System.out.println("코드값 =====>" + map.get("code"));
		String code = map.get("code").toString();
		
		//access_token 받기
		String access_token = bankAPI.getAccessToken(code);
		System.out.println("access_token =====> " + access_token);
		
		//session
		session.setAttribute("access_token", access_token);
		
		return "home";
	}
	
	@RequestMapping("/userinfo")
	public String userinfo(@RequestParam Map<String, Object>map, HttpServletRequest request) {
		
		//String access_token = request.getParameter("access_token");
		
		String access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTAwNzcwNTM3Iiwic2NvcGUiOlsiaW5xdWlyeSIsImxvZ2luIiwidHJhbnNmZXIiXSwiaXNzIjoiaHR0cHM6Ly93d3cub3BlbmJhbmtpbmcub3Iua3IiLCJleHAiOjE2MjMxNDExMjcsImp0aSI6IjhhYjM0M2E5LThjYjUtNGMyNy04NTk4LWQ2NTU1N2MxMGQ3ZiJ9._b6aj6RnnsMjt889qfPE66-Uq9jE3l7DLgN4xRPd9JE";
		String use_num = "1100770537";
		
		Map<String, Object> userinfo = bankAPI.getUserInfo(access_token, use_num);
		System.out.println("userinfo " + userinfo);
		
		return "home";
	}

}
