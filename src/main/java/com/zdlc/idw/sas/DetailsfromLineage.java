package com.zdlc.idw.sas;

//This class reads L0 report and save all the data in a list

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zdlc.idw.sas.dto.LineageReportDTO;

public class DetailsfromLineage {

	LineageReportConvertor rl = new LineageReportConvertor();

	public List<LineageReportDTO> readlineage(File file) {
		List<LineageReportDTO> lineageReportList = new ArrayList<LineageReportDTO>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			while (rowIterator.hasNext()) {
				LineageReportDTO lineageReportDto = new LineageReportDTO();
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					row = rowIterator.next();
				}
				for (Cell cell : row) {

					if (cell.getColumnIndex() == 0) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setstepNumber("NOT_PROVIDED");
						}
						else{
						String stepNumber = cell.getStringCellValue();
						stepNumber=withoutSpecialCharacter(stepNumber);
						lineageReportDto.setstepNumber(stepNumber);
					}}
					// Fetch schema value from the L0 Report
					if (cell.getColumnIndex() == 4) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setSourceSchema("NOT_PROVIDED");
						}
						else{
						String schemaname = cell.getStringCellValue();
						schemaname=withoutSpecialCharacter(schemaname);
						lineageReportDto.setSourceSchema(schemaname);
					}}
					// Fetch source table value from the L0 Report
					if (cell.getColumnIndex() == 5) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setSourceTable("NOT_PROVIDED");
						}
						else{
						String sourceTablename = cell.getStringCellValue();
						sourceTablename=withoutSpecialCharacter(sourceTablename);
						lineageReportDto.setSourceTable(sourceTablename);
					}}
					// Fetch source column from the L0 Report
					if (cell.getColumnIndex() == 6) {
						try {
							if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
								lineageReportDto.setSourceColumn("NOT_PROVIDED");
							}
							else{
							String sourcecolumn = cell.getStringCellValue();
							String sourceCol=sourcecolumn.replaceAll("[^a-zA-Z0-9_]+","");
							if((sourceCol).length()>1){
							   sourcecolumn=withoutSpecialCharacter(sourcecolumn);
							}
							lineageReportDto.setSourceColumn(sourcecolumn);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}}
					// Fetch operation type from the L0 Report
					if (cell.getColumnIndex() == 7) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setOperationType("NOT_PROVIDED");
						}
						else{
						String operationtype = cell.getStringCellValue();
						operationtype=withoutSpecialCharacter(operationtype);
						lineageReportDto.setOperationType(operationtype);
					}}
					// Fetch statement type from the L0 Report
					if (cell.getColumnIndex() == 8) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setStatementType("NOT_PROVIDED");
						}
						else{
						String statementtype = cell.getStringCellValue();
						statementtype=withoutSpecialCharacter(statementtype);
						lineageReportDto.setStatementType(statementtype);
					}}
					// Fetch transformation logic from the L0 Report
					if (cell.getColumnIndex() == 9) {
					    String transformationlogic = cell.getStringCellValue();
						lineageReportDto.setTransformationLogic(transformationlogic);
					}
					// Fetch Target Column  from the L0 Report
					if (cell.getColumnIndex() == 10) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setTargetColumn("NOT_PROVIDED");
						}
						else{
						String targetcolumn = cell.getStringCellValue();
						targetcolumn=withoutSpecialCharacter(targetcolumn);
						lineageReportDto.setTargetColumn(targetcolumn);
					}}
					// Fetch Target Table  from the L0 Report
					if (cell.getColumnIndex() == 11) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setTargetTable("NOT_PROVIDED");
						}
						else{
						String targettable = cell.getStringCellValue();
						targettable=withoutSpecialCharacter(targettable);
						lineageReportDto.setTargetTable(targettable);
					}}
					// Fetch Target Schema  from the L0 Report
					if (cell.getColumnIndex() == 12) {
						if ((cell == null) || (cell.getCellType() == Cell.CELL_TYPE_BLANK)||(cell.getStringCellValue().isEmpty())) {
							lineageReportDto.setTargetSchema("NOT_PROVIDED");
						}
						else{
						String targetschema = cell.getStringCellValue();
						targetschema=withoutSpecialCharacter(targetschema);
						lineageReportDto.setTargetSchema(targetschema);
					}}
				}
				lineageReportList.add(lineageReportDto);
			}
			System.out.println("Lineage Report has read successfully");
            workbook.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return lineageReportList;
	}
	
	public static String withoutSpecialCharacter(String withSpecialCharacters){
		String withoutSpecialCharacters=withSpecialCharacters.trim();
		
		if(withoutSpecialCharacters.contains("[ _E")){
			System.out.println(" "  );
		}
		if(withoutSpecialCharacters.contains("[")){
		withoutSpecialCharacters=withoutSpecialCharacters.replace("[","");
		if(withoutSpecialCharacters.contains("]")){
		withoutSpecialCharacters=withoutSpecialCharacters.replace("]","");}}

	    withoutSpecialCharacters=withSpecialCharacters.replaceAll("[^a-zA-Z0-9_]+","_");
		 if (withoutSpecialCharacters.startsWith("_")){
			 withoutSpecialCharacters=withoutSpecialCharacters.replace("_","");
		 }
		return withoutSpecialCharacters;
	}
}