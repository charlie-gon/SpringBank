package com.company.temp;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.temp.service.impl.EmpMapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Controller
public class ExcelPdfController {
	
	@Autowired EmpMapper empMapper;
	@Autowired DataSource dataSource;
	
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
	
	// empList3(pdf p.12)
	@RequestMapping("/pdf/empList3")
	public void empList3(HttpServletResponse response) throws Exception {
		Connection conn = dataSource.getConnection();
		//소스파일 컴파일하여 저장 : compileReportToFile
		 String jrxmlFile = getClass().getResource("/reports/empListt3.jrxml").getFile();
		String jasperFile = JasperCompileManager.compileReportToFile( jrxmlFile );
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile, null, conn);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}
	
	
	
	
}
