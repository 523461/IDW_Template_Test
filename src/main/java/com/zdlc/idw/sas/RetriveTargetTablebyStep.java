package com.zdlc.idw.sas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.LineageReportDTO;

public class RetriveTargetTablebyStep extends IDWConstants {

	public static Map<String, String> retriveTarget(List<LineageReportDTO> normalListDto) {
		Map<String, String> targetMap = new LinkedHashMap<String, String>();
		Map<String, List<LineageReportDTO>> stepwiseList = getStepwiseListDto(normalListDto);
		for (Entry<String, List<LineageReportDTO>> map : stepwiseList.entrySet()) {
			String key=map.getKey();
			List<LineageReportDTO> list = map.getValue();
			for (LineageReportDTO dto : list) {

					if (!dto.getTargetTable().contains(NOT_APPLICABLE)&&!targetMap.containsKey(key)) {
							targetMap.put(key, dto.getTargetTable());
					}
				}
			}

		return targetMap;
	}
	
	public static Map<String,List<LineageReportDTO>> getStepwiseListDto(List<LineageReportDTO> normalListDto) {
		Map<String,List<LineageReportDTO>> stepwiseList = new LinkedHashMap<>();
		for (LineageReportDTO lineageReportDTO : normalListDto) {
			if(!stepwiseList.containsKey(lineageReportDTO.getstepNumber())) {
				List<LineageReportDTO> list = new ArrayList<LineageReportDTO>();
				list.add(lineageReportDTO);
				if(lineageReportDTO.getStatementType().equalsIgnoreCase("PROC_EXPORT_STATEMENT"))
				stepwiseList.put(lineageReportDTO.getstepNumber(), list);
				
			} else {
				if(lineageReportDTO.getStatementType().equalsIgnoreCase("PROC_EXPORT_STATEMENT"))
				stepwiseList.get(lineageReportDTO.getstepNumber()).add(lineageReportDTO);
				
			}
		}
		return stepwiseList;
	}
	
}
