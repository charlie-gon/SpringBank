package com.company.temp;

import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CrawlingController {
	
	// 크롤링 = API 지원하지 않을 시, 전체 페이지 가져와서 세부 정보 조회
	
	// 페이지 이동
	@GetMapping("/getBiz")
	public String getBizForm() {
		return "bank/getBiz";
	}
	
	// 크롤링 결과 조회
	@PostMapping("/getBiz")
	public String getBiz(@RequestParam String bizno, Model model) { // @RequestParam에서 사용되는 변수 이름은 프론트 페이지에서 넘어오는 변수명과 일치해야 한다!
		// 크롤링
		
		String url = "https://bizno.net/?query="+bizno;
		Document doc = null;
		
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		org.jsoup.select.Elements element = doc.select("div.titles > a > h4"); // web에서는 document.querySelector("div.titles > a > h4").innerHTML로 조회 가능
		Elements element2 = doc.select("div.titles > a > h5");
		
		String bizName = element.text();
		String category = element2.text();
		
		// 회사명 추출 -> Model에 저장
		model.addAttribute("bizname", bizName);
		model.addAttribute("category", category);
		return "bank/getBiz";
	}

}
