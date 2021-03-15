package com.company.temp;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.company.common.TestVO;

@Controller
public class TestController { // 210315. Spring 총정리
	
	// Boomerang 활용
	
	// get방식. 커맨드 객체에 parameter 담기
	@RequestMapping("/getTest1")
	public String getTest1(TestVO vo) {
		System.out.println(vo);
		return "home";
	}
	
	// @RequestParam 활용( = request.getParameter("")와 동일한 개념)
	// VO가 없지만 parameter 값을 전송해야 할 때
	@RequestMapping("/getTest2")
	public String getTest2(@RequestParam String firstName, String salary) { 
		System.out.println(firstName + " / " + salary);
		return "home";
	}
	
	// 배열로 값 받기( = request.getParameterValues()와 동일)
	@RequestMapping("/getTest3")
	public String getTest3(@RequestParam("hobby") String[] hobbies) { 
		// 전달하는 parameter 이름과 Controller에서 받는 이름이 다른 경우 RequestParam("")과 같이 원래 parameter 이름을 지정해주면 OK
		
		// List 형식으로 출력
		System.out.println("취미: " + Arrays.asList(hobbies));
		
		// 반복문으로 출력
		for(String result : hobbies) {
			System.out.println("취미: " + result);
		}
		return "home";
	}
	
	// JSON
	@RequestMapping("/getTest4")
	//public String getTest4(@RequestBody TestVO vo) { // 넘어온 데이터를 json 형태로 변환 -> vo에 담는다
	public String getTest4(@RequestBody List<TestVO> vo) { // 배열 형태로 값 전달할 경우 List/Array/ 사용
		// 배열 형식 => [{"firstName" : "ChanGon", "salary" : "1234"}, {"firstName" : "GilDong", "salary" : "4567"}]
	//public String getTest4(@RequestBody List<Map> vo) { // Map 형식으로 값을 받아도 OK
		
		System.out.println(vo);
		return "home";
	}
	
	// PathVariable
	@RequestMapping("/getTest5/{firstName}/{salary}") // 요청 URL 뒤에 정의된 객체 사용
	//public String getTest5(@PathVariable String firstName, @PathVariable int salary) {
	//public String getTest5(@PathVariable String firstName, @PathVariable String salary, TestVO vo, Model model) {
	public String getTest5(@PathVariable String firstName, @PathVariable String salary, @ModelAttribute("tvo") TestVO vo, Model model) {
		// PathVariable 뒤에 VO객체 삽입하면, 아래와 같이 전달받은 값을 VO에 담아 활용할 수 있음.
		// Note) VO에 값이 담길 때 Model에도 값이 저장된다. view 페이지에서 VO값을 보고싶을 때 첫 글자 소문자 형태로 출력하면 됨(testVO)
		// Model에 저장된 VO 이름을 바꾸고 싶을 때 ModelAttribute 사용(교재 p.286)
		
		vo.setFirstName(firstName);
		vo.setSalary(salary);
		System.out.println(vo);
		model.addAttribute("firstName", firstName);
		return "test";
	}
	
	// ModelAndView
	@RequestMapping("/getTest6/{firstName}/{salary}")
	public ModelAndView getTest6(@PathVariable String firstName, @PathVariable String salary, 
												@ModelAttribute("tvo") TestVO vo, Model model) {
		
		vo.setFirstName(firstName);
		vo.setSalary(salary);
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("firstName", firstName);
//		mv.setViewName("test");
		return new ModelAndView("test", "firstName", firstName);
	}
	
	// 응답 결과를 JSON으로 넘기기
	@RequestMapping("/getTest7/{firstName}/{salary}")
	public @ResponseBody TestVO getTest7(@PathVariable String firstName, @PathVariable String salary, TestVO vo) {
		
		vo.setFirstName(firstName);
		vo.setSalary(salary);
		return vo;
	}
	
	// List 활용한 JSON parsing
	@RequestMapping("/getTest8")
	@ResponseBody // json 구조로 parsing
	public List<Map> getTest8(){
		List list = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "choi");
		map.put("sal", "1000");
		list.add(map);
		
		map.put("name", "kim");
		map.put("sal", "7000");
		list.add(map);
		
		return list;
	}
	
}
