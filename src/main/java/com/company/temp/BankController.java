package com.company.temp;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.company.common.BankAPI;
import com.company.common.BankVO;

@Controller
public class BankController {
	
	@Autowired BankAPI bankAPI;
	
	String access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIxMTAwNzcwNTM3Iiwic2NvcGUiOlsiaW5xdWlyeSIsImxvZ2luIiwidHJhbnNmZXIiXSwiaXNzIjoiaHR0cHM6Ly93d3cub3BlbmJhbmtpbmcub3Iua3IiLCJleHAiOjE2MjMyMDY2MTEsImp0aSI6ImQ0NzU0Yjk4LTM4ZGEtNDVkMi04Y2E1LTNkNGYxZWMyZjc0MiJ9.ldyO_HdD-h9Yd2_YSn_A_AJKZwf3xjqLjwqa5MK2ZtY";
	String use_num = "1100770537";
	
	// API 로컬 테스트 페이지 참고 + 오픈뱅킹공동업무_API_명세서 p.26(계좌등록확인)
	// 참고 https://developers.kftc.or.kr/dev/doc/open-banking#doclist_2_1
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
	
	// 사용자 토큰발급
	// 참고 https://developers.kftc.or.kr/dev/doc/open-banking#doclist_2_1
	@RequestMapping("/callback")
	public String callback(@RequestParam Map<String, Object>map, HttpSession session) {
		System.out.println("코드값 =====>" + map.get("code")); // 사용자인증 API를 통해 Authorization Code 획득!(사용자의 동의를 받았다)
		String code = map.get("code").toString();
		
		//access_token 받기
		String access_token = bankAPI.getAccessToken(code); // Authorization Code를 통해 Access Token 받기!
		System.out.println("access_token =====> " + access_token);
		
		//session
		session.setAttribute("access_token", access_token);
		
		return "home";
	}
	
	// 사용자 토큰발급
	@RequestMapping("/getOrgAuthorize")
	public String getAccessToken() {
		Map<String, Object> map = bankAPI.getOrgAccessToken();
		System.out.println("access_token =====> " + map.get("access_token"));
		
		return "home";
	}
	
	
	@RequestMapping("/getAccountList")
	public String userinfo(HttpServletRequest request, Model model) {
		
		//String access_token = request.getParameter("access_token");
		
		Map<String, Object> map = bankAPI.getAccountList(access_token, use_num);
		System.out.println("map =====> " + map);
		model.addAttribute("list", map);
		
		return "bank/getAccountList";
	}
	
	@RequestMapping("/getBalance")
	public String getBalance(BankVO vo, Model model) {
		vo.setAccess_token(access_token);
		Map<String, Object>map = bankAPI.getBalance(vo);
		System.out.println("잔액 =====> " + map);
		model.addAttribute("balance", map);
		return "bank/getBalance";
	}
	
	@ResponseBody // ajax 호출!
	@RequestMapping("/ajaxGetBalance")
	public Map<String, Object> ajaxGetBalance(BankVO vo, Model model){
		vo.setAccess_token(access_token);
		Map<String, Object>map = bankAPI.getBalance(vo);
		System.out.println("잔액 =====> " + map);
		return map; // <== Responsebody에 의해 데이터 값이 json으로 넘어간다
	}
}
