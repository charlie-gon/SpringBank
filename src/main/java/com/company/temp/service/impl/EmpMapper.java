package com.company.temp.service.impl;

import java.util.List;
import java.util.Map;

public interface EmpMapper {
	
	public List<Map<String, Object>> getEmpList();
	// 단건이면 Map만 사용하면 되지만, 여러건 조회 시 List 추가

}
