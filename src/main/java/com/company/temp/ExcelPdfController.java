package com.company.temp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.temp.service.impl.EmpMapper;

@Controller
public class ExcelPdfController {
	
	@Autowired EmpMapper empMapper;
	
	@RequestMapping("/getEmpExcel")
	public String getEmpExcel(Model model) {
		List<Map<String, Object>> list = empMapper.getEmpList();
		System.out.println(list.get(0));
		model.addAttribute("filename", "empList");
		
		// 컬럼명은 대문자로 사용하는게 default
		// mapper에서 alias 설정 하면 왼쪽과 같이 컬럼명 사용 가능
		model.addAttribute("headers", new String[] {"firstName", "salary", "job_id"}); 
		model.addAttribute("datas", list);
		return "commonExcelView";
	}
	
	// empList1
	@RequestMapping("/pdf/empList")
	public String getPdfEmpList(Model model) {
		model.addAttribute("filename", "/reports/empList.jasper");
		return "pdfView";
	}
	
	// empList2
	// 파라미터 포함한 pdf
	@RequestMapping("/pdf/empList2")
	public String getPdfEmpList2(Model model, @RequestParam int dept) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("P_DEPARTMENT_ID", dept);
		
		model.addAttribute("param", map);
		model.addAttribute("filename", "/reports/empList2.jasper");
		return "pdfView";
	}
	
	// empList3
	@RequestMapping("/pdf/empList3")
	public String empList3(Model model) {
		model.addAttribute("filename", "/reports/empListt3.jasper");
		return "pdfView";
	}
	
	
	
	
}
