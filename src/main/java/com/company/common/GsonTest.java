package com.company.common;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.gson.Gson;

public class GsonTest { // 210311
	public static void main(String[] args) throws JsonProcessingException {
		
		Map<String, Object> map = new HashMap<>();
		map.put("name", "김찬곤");
		map.put("age", 20);
		
		// 자바 객체 -> String(json)
		Gson gson = new Gson();
		String str = gson.toJson(map); // toJson 메소드로 자바 객체를 JSON으로 변환
		System.out.println(str);
		
		str = "{\"name\":\"김찬곤\",\"age\":20, \"dept\":\"개발\"}";
		
		// String(JSON) -> java 객체
		map = gson.fromJson(str, Map.class);
		System.out.println(map.get("dept"));
		
		// jackson = spring json library @Requestbody @Responsebody
		// jackson을 통한 자바 객체 -> String(json)
		
		JsonMapper mapper = new JsonMapper();
		String str2 = mapper.writeValueAsString(map);
		System.out.println("jackson: " + str2);
		
		// jackson을 통한 String(JSON) -> java 객체
		map = mapper.readValue(str2, Map.class);
		System.out.println("jackson / String(json) -> java객체: " + map);
	}
}
