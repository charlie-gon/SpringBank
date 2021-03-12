package com.company.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Component
public class BankAPI {
	
	String host = "https://testapi.openbanking.or.kr";
	String client_id="85bf2e88-ffb6-4387-b218-1f984ea8836e";
	String client_secret="e4ff075e-e2f6-4fd5-921f-a76a49fe9234";
	String redirect_uri="http://localhost/temp/callback";
	String bank_tran_id = "M202111671"; // 이용기관코드
	String org_access_token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJNMjAyMTExNjcxIiwic2NvcGUiOlsib29iIl0sImlzcyI6Imh0dHBzOi8vd3d3Lm9wZW5iYW5raW5nLm9yLmtyIiwiZXhwIjoxNjIzMzA2NjAzLCJqdGkiOiI5OWM1NWIxMS1jNDkyLTQ1MTYtYTBiZC04YmNmYWJlYmRkZGMifQ.klLob1u6UFoPZgZ-Ie8_3LaIiiXvktKMjB8ojw_knmM";
	
	
	// 사용자 토큰발급 API (3-legged)
		public String getAccessToken (String authorize_code) {

	        String access_Token = "";
	        String refresh_Token = "";
	        String reqURL = host + "/oauth/2.0/token";
	        
	        try {
	            URL url = new URL(reqURL);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            
	            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
	            conn.setRequestMethod("POST");
	            conn.setDoOutput(true);
	            
	            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
	            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
	            StringBuilder sb = new StringBuilder();
	            sb.append("code=").append(authorize_code);
	            sb.append("&client_id=").append(client_id);
	            sb.append("&client_secret=").append(client_secret);
	            sb.append("&redirect_uri=").append(redirect_uri);
	            sb.append("&grant_type=authorization_code");
	            bw.write(sb.toString());
	            bw.flush();
	            
	            //    결과 코드가 200이라면 성공
	            int responseCode = conn.getResponseCode();
	            System.out.println("responseCode : " + responseCode);
	 
	            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line = "";
	            String result = "";
	            
	            while ((line = br.readLine()) != null) {
	                result += line;
	            }
	            System.out.println("response body : " + result);
	            
	            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
	            JsonParser parser = new JsonParser();
	            JsonElement element = parser.parse(result);
	            
	            access_Token = element.getAsJsonObject().get("access_token").getAsString();
	            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
	            
	            System.out.println("access_token : " + access_Token);
	            System.out.println("refresh_token : " + refresh_Token);
	            
	            br.close();
	            bw.close();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } 
	        
	        return access_Token;
	    }
		
		// 기관인증
		public Map<String, Object> getOrgAccessToken() {
			Map<String, Object> map = new HashMap(); // 하나 이상의 파라미터 값을 다루기 위해 Map 사용
			String reqURL = host + "/oauth/2.0/token";

			try {
				URL url = new URL(reqURL);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();

				// POST 요청을 위해 기본값이 false인 setDoOutput을 true로
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);

				// header
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

				// POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("client_id=").append(client_id);
				sb.append("&client_secret=").append(client_secret);
				sb.append("&scope=oob");
				sb.append("&grant_type=client_credentials");
				bw.write(sb.toString());
				bw.flush();

				// 결과 코드가 200이라면 성공
				int responseCode = conn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				  Gson gson = new Gson();
			      map = gson.fromJson(result, HashMap.class);

				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return map;
		} 
		
		// 등록 계좌 조회(pdf p.36)
		public HashMap<String, Object> getAccountList (String access_token, String use_num) {
		    // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
		    HashMap<String, Object> map = new HashMap<>();
		    String reqURL = "https://testapi.openbanking.or.kr/v2.0/account/list";
		    String user_seq_no = use_num;
		    String include_cancel_yn = "Y";
		    String sort_order = "D";
		    
		    StringBuilder qstr = new StringBuilder();
			qstr.append("user_seq_no="+user_seq_no)
			.append("&include_cancel_yn="+include_cancel_yn)
			.append("&sort_order="+sort_order);
		
		    try {
		        URL url = new URL(reqURL + "?" + qstr);
		        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		        conn.setRequestMethod("GET");
		        
		        // 요청에 필요한 Header에 포함될 내용
		        conn.setRequestProperty("Authorization", "Bearer " + access_token);
		        // 출력되는 값이 200이면 정상작동
		        int responseCode = conn.getResponseCode();
		        System.out.println("responseCode : " + responseCode);
		        
		        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		        
		        String line = "";
		        String result = "";
		        
		        while ((line = br.readLine()) != null) {
		            result += line;
		        }
		        System.out.println("response body : " + result);
		        
		        // String값을 map에 담아서 리턴
		        Gson gson = new Gson();
		        map = gson.fromJson(result, HashMap.class);
		        
		        
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }
		    
		    return map;
		}
		
		public String getDate() { // 요청일시 설정
			String str = "";
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
			str = format.format(date);
			return str;
		}
		
		// System.currentTimeMills
		// 1970년 1월1일부터 경과한 시간을 long값이자 밀리세컨(1/1000초)값으로 리턴.
		// 총 13자리 숫자로 리턴.
		
		public String getRand() { // 9자리 난수 생성
			long time = System.currentTimeMillis();
			String str = Long.toString(time);
			return Long.toString(time).substring(str.length()-9); 
			// currentTimeMills가 반환하는 총 13자리 숫자 중 9자리 제거 = 4
			// Long.toString(time).substring(4) => 13자리 숫자에 substr(4) 적용되어 인덱스 4부터 시작하면(index[4,5,6,7,8,9,10,11,12] => 9자리 숫자 리턴
		}
		
		public String getRand32() { // 32자리 난수 생성
			long time = System.currentTimeMillis();
			String str = Long.toString(time);
			return Long.toString(time).substring((str.length()*2)-4); // (13*2)-4 = 32니까?
		}
		
		// 잔액조회
		public HashMap<String, Object> getBalance(BankVO vo) {
			// 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
			HashMap<String, Object> map = new HashMap<>();
			String reqURL = host + "/v2.0/account/balance/fin_num";

			StringBuilder qstr = new StringBuilder();
			qstr.append("bank_tran_id=").append(bank_tran_id + "U" + getRand())
			.append("&fintech_use_num=").append(vo.getFintech_use_num())
			.append("&tran_dtime=").append(getDate());

			try {
				URL url = new URL(reqURL + "?" + qstr);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");

				// 요청에 필요한 Header에 포함될 내용
				conn.setRequestProperty("Authorization", "Bearer " + vo.getAccess_token());
				// 출력되는 값이 200이면 정상작동
				int responseCode = conn.getResponseCode();
				System.out.println("responseCode : " + responseCode);

				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

				String line = "";
				String result = "";

				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);

				// String값을 map에 담아서 리턴
				Gson gson = new Gson();
				map = gson.fromJson(result, HashMap.class);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return map;
		}
		
		// 기관인증을 RestTemplate으로!
		public Map<String, Object> getOrgAccessTokenRestTemplate() {
			String reqURL = host + "/oauth/2.0/token";   
			
	        MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
	        param.add("client_id", client_id);
	        param.add("client_secret", client_secret);
	        param.add("scope", "oob");
	        param.add("grant_type", "client_credentials");

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

	        HttpEntity<MultiValueMap<String, String>> request = 
	        		new HttpEntity<MultiValueMap<String, String>>( param, headers);
	        
	        RestTemplate restTemplate = new RestTemplate();
	        Map map = restTemplate.postForObject(
	        		reqURL,
			        request,
			        Map.class		);
	        return map;
		}
}
