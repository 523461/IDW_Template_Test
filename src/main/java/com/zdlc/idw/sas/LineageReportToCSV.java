package com.zdlc.idw.sas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVWriter;
import com.zdlc.idw.sas.dto.LineageReportDTO;

public class LineageReportToCSV {
	
	private static final int stepNumber_Index =0; 
	private static final int sourceDatabase_Index=3;
	private static final int sourceSchema_Index=4;
	private static final int sourceTable_Index=5;
	private static final int sourceColumn_Index=6;
	private static final int operationType_Index=7;
	private static final int statementType_Index=8;
	private static final int transformationLogic_Index=9;
	private static final int targetColumn_Index=10;
	private static final int targetTable_Index=11;
	private static final int targetSchema_Index=12;
	

	public static void main(String[] args) throws Exception {
		       
		File file = new File("C:\\Users\\523641\\Documents\\L0 reports\\L0 reports\\");
		if (file.isDirectory()) {
			File listOfFiles[] = file.listFiles();
			for (File oneByone : listOfFiles) {
				//convertToCSV(oneByone);
			}
			//readCSVFile(oneByone+".csv");
		}
		File file1 = new File("C:\\Users\\523641\\Documents\\L0 reports\\");
		if (file1.isDirectory()) {
			File listOfFiles[] = file1.listFiles();
			for (File oneByone : listOfFiles) {
				System.out.println(oneByone);
				//readCSVFile(oneByone);
			}
		}

	}

	public static void convertToCSV(File oneByone, String OutputPath) throws Exception {

		FileInputStream input_document = new FileInputStream(oneByone);
		XSSFWorkbook my_xls_workbook = new XSSFWorkbook(input_document);
		XSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0);
		Iterator<Row> rowIterator = my_worksheet.iterator();
		FileWriter my_csv = new FileWriter(
				OutputPath + oneByone.getName().replace(".xlsx", ".csv"));
		CSVWriter my_csv_output = new CSVWriter(my_csv);
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			int i = 0;
			String[] csvdata = new String[15];
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					csvdata[i] = cell.getStringCellValue().replaceAll("\n", " ");
					break;
				}
				i = i + 1;
			}
			my_csv_output.writeNext(csvdata);
		}
		System.out.println(oneByone.getName()+":: File Converted to CSV Format Sucessfully");
		my_csv_output.close();
		input_document.close();
		my_xls_workbook.close();
	}
	
	public List<LineageReportDTO> readCSVFile(File FileName) {
		BufferedReader reader= null;
		List<LineageReportDTO> lineageReportDTOs = new ArrayList<LineageReportDTO>();
		try {
			String line = "";
			reader= new BufferedReader( new FileReader(FileName));
			reader.readLine();
			while((line=reader.readLine())!=null) {
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				LineageReportDTO reportDTO= new LineageReportDTO();
				if(tokens.length>0) {
					if(tokens[stepNumber_Index].isEmpty()) {
						reportDTO.setstepNumber("NOT_PROVIDED");
					}else {
						reportDTO.setstepNumber(withoutSpecialCharacter(tokens[stepNumber_Index]));
					}
					
					// Fetch database value from the CSV Report
					try {
						if(tokens[sourceDatabase_Index].isEmpty()) {
							reportDTO.setSourcedataBase("NOT_PROVIDED");
						}else {
							reportDTO.setSourcedataBase(removeDoubleQuotes(tokens[sourceDatabase_Index]));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// Fetch schema value from the CSV Report
					if(tokens[sourceSchema_Index].isEmpty()) {
						reportDTO.setSourceSchema("NOT_PROVIDED");
					}else {
						reportDTO.setSourceSchema(removeDoubleQuotes(tokens[sourceSchema_Index]));
					}
					// Fetch source table value from the CSV Report
					if(tokens[sourceTable_Index].isEmpty()) {
						reportDTO.setSourceTable("NOT_PROVIDED");
					}else {
						reportDTO.setSourceTable(withoutSpecialCharacter(tokens[sourceTable_Index]));
					}
					// Fetch source column from the CSV Report
					if(tokens[sourceColumn_Index].isEmpty()) {
						reportDTO.setSourceColumn("NOT_PROVIDED");
					}else {
						String sourceCol=tokens[sourceColumn_Index].replaceAll("[^a-zA-Z0-9_]+","");
						if((sourceCol).length()>1){
						   reportDTO.setSourceColumn(withoutSpecialCharacter(tokens[sourceColumn_Index]));
						}else {
							reportDTO.setSourceColumn(tokens[sourceColumn_Index]);
						}
					}
					// Fetch operation type from the CSV Report
					if(tokens[operationType_Index].isEmpty()) {
						reportDTO.setOperationType("NOT_PROVIDED");
					}else {
						reportDTO.setOperationType(withoutSpecialCharacter(tokens[operationType_Index]));
					}
					// Fetch statement type from the CSV Report
					if(tokens[statementType_Index].isEmpty()) {
						reportDTO.setStatementType("NOT_PROVIDED");
					}else {
						reportDTO.setStatementType(withoutSpecialCharacter(tokens[statementType_Index]));
					}
					
					// Fetch transformation logic from the CSV Report
					reportDTO.setTransformationLogic(removeDoubleQuotes(tokens[transformationLogic_Index]));
					
					// Fetch Target Column  from the CSV Report
					if(tokens[targetColumn_Index].isEmpty()) {
						reportDTO.setTargetColumn("NOT_PROVIDED");
					}else {
						reportDTO.setTargetColumn(withoutSpecialCharacter(tokens[targetColumn_Index]));
					}
					// Fetch Target Table  from the CSV Report
					if(tokens[targetTable_Index].isEmpty()) {
						reportDTO.setTargetTable("NOT_PROVIDED");
					}else {
						reportDTO.setTargetTable(withoutSpecialCharacter(tokens[targetTable_Index]));
					}
					// Fetch Target Schema  from the CSV Report
					if(tokens[targetSchema_Index].isEmpty()) {
						reportDTO.setTargetSchema("NOT_PROVIDED");
					}else {
						reportDTO.setTargetSchema(removeDoubleQuotes(tokens[targetSchema_Index]));
					}
				}
				lineageReportDTOs.add(reportDTO);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lineageReportDTOs;
		
	}
	
	private String removeDoubleQuotes(String value) {
		String logic = value.substring(1, value.length()-1);
		return logic;
	}
	
	private String withoutSpecialCharacter(String withSpecialCharacters){
		withSpecialCharacters=withSpecialCharacters.replace("\"", "").trim();
		String withoutSpecialCharacters=withSpecialCharacters;
		
		if(withoutSpecialCharacters.contains("[ _E")){
			System.out.println(" "  );
		}
		if(withoutSpecialCharacters.contains("[")){
		withoutSpecialCharacters=withoutSpecialCharacters.replace("[","");
		if(withoutSpecialCharacters.contains("]")){
		withoutSpecialCharacters=withoutSpecialCharacters.replace("]","");}}

	    withoutSpecialCharacters=withSpecialCharacters.replaceAll("[^a-zA-Z0-9_]+","_");
	    //withoutSpecialCharacters=withSpecialCharacters.replaceAll("[^a-zA-Z0-9_]+","_");
		 if (withoutSpecialCharacters.startsWith("_")){
			 withoutSpecialCharacters=withoutSpecialCharacters.replace("_","");
		 }
		return withoutSpecialCharacters;
	}

	

}
