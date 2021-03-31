package com.zdlc.idw.sas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVWriter;
import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.DIWTemplateModel;
import com.zdlc.idw.sas.dto.SchemaDTO;
import com.zdlc.idw.sas.dto.TransformationDTO;
import com.zdlc.idw.sas.helper.TransformationLogicFormater;

public class DIWTemplateDataWriter extends IDWConstants {
	
	public void populateDIWTemplate(DIWTemplateModel diwTemplateModel, String outputfile) throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		DIWTemplateDataWriter templateWriter = new DIWTemplateDataWriter();
		FileInputStream inputStream = new FileInputStream(new File(outputfile));
		XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(inputStream);
		/*Map<String,List<LineageReportDTO>> stepwiseList = diwTemplateModel.getStepwiseList();
		Map<String,Set<String>> sqlOverideMap = new LinkedHashMap<String, Set<String>>();
		int counter = 0;*/
		
		

		
		//Populate Specification Sheet
		templateWriter.populateSpecificationsSheet(diwTemplateModel,workbook);
		
		//Populate System Entities sheet
		templateWriter.populateSystemEntities(diwTemplateModel,workbook);
		
		//populate System catalog sheet
				templateWriter.populateSystemCatalog(diwTemplateModel,workbook);

		//Populate Relational System Entities Definition sheet
		templateWriter.populateRelationalSystemEntityDefn(diwTemplateModel,workbook);
		
		//Populate the data mapping sheet
		templateWriter.populateDataMapping(diwTemplateModel,workbook);
			
		//Populate the Transformation sheet
		DIWTranformationConstructor diwTranformationConstructor = new DIWTranformationConstructor();
		Map<String,Map<String,List<TransformationDTO>>> srcMap = diwTranformationConstructor.populateTransformations(diwTemplateModel,workbook);
		diwTemplateModel.setSrcMap(srcMap);
		writeTransformation(diwTemplateModel, workbook);
		
		//Populate the Source To Target Mapping sheet
		DIWSourceToTargetConstructor sourceToTarget = new DIWSourceToTargetConstructor();
		sourceToTarget.populateSourceToTargetmapping(diwTemplateModel,workbook);
		
		//Populate Task sheet
		templateWriter.populateTask(diwTemplateModel, workbook);
		
		//Populate Workflow sheet
		templateWriter.populateWorkFlows(diwTemplateModel,workbook);
		
		//Populate Connection sheet
		templateWriter.populateConnections(diwTemplateModel,workbook);
		
		inputStream.close();
		FileOutputStream outputStream = new FileOutputStream(outputfile);
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	/**
	 * Method to populate the specifications sheet
	 * 
	 * @param otherinputs
	 * @param workbook
	 */
	public void populateSpecificationsSheet(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {

			
			Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			Sheet requiredSheet = workbook.getSheet("Specification");
			Row row = requiredSheet.getRow(4);
			
			setCellData(row, 4, otherinputs.get("specificationName"));
			Row row1 = requiredSheet.getRow(5);
			setCellData(row1, 4, otherinputs.get("description"));
			Row row2 = requiredSheet.getRow(6);
			setCellData(row2, 4, otherinputs.get("longname_label"));
			Row row3 = requiredSheet.getRow(7);
			setCellData(row3, 4, (otherinputs.get("versionNumber")));
			Row row4 = requiredSheet.getRow(8);
			setCellData(row4, 4, otherinputs.get("schemaversion"));
			Row row5 = requiredSheet.getRow(11);
			setCellData(row5, 4, otherinputs.get("definition"));
			Row row6 = requiredSheet.getRow(12);
			setCellData(row6, 4, otherinputs.get("functionalScope"));
			Row row7 = requiredSheet.getRow(13);
			setCellData(row7, 4, otherinputs.get("functionalDescription"));
			Row row8 = requiredSheet.getRow(14);
			setCellData(row8, 4, otherinputs.get("designDecision"));
			Row row9 = requiredSheet.getRow(18);
			setCellData(row9, 4, "");
			Row row10 = requiredSheet.getRow(19);
			setCellData(row10, 4, otherinputs.get("businessContactName"));
			Row row11 = requiredSheet.getRow(20);
			setCellData(row11, 4, otherinputs.get("businessContactRole"));
			Row row12 = requiredSheet.getRow(21);
			setCellData(row12, 4, otherinputs.get("businessContactEmailAddress"));
			Row row13 = requiredSheet.getRow(22);
			setCellData(row13, 4, "");
			Row row14 = requiredSheet.getRow(23);
			setCellData(row14, 4, "");
			Row row15 = requiredSheet.getRow(24);
			setCellData(row15, 4, "");
			Row row16 = requiredSheet.getRow(25);
			setCellData(row16, 4, "");
			Row row17 = requiredSheet.getRow(28);
			setCellData(row17, 4, "");
			Row row18 = requiredSheet.getRow(29);
			setCellData(row18, 4, otherinputs.get("technicalContactName"));
			Row row19 = requiredSheet.getRow(30);
			setCellData(row19, 4, otherinputs.get("technicalContactRole"));
			Row row20 = requiredSheet.getRow(31);
			setCellData(row20, 4, otherinputs.get("technicalContactEmailAddress"));
			Row row21 = requiredSheet.getRow(32);
			setCellData(row21, 4, "");
			Row row22 = requiredSheet.getRow(33);
			setCellData(row22, 4, otherinputs.get("technicalSecondaryContactName"));
			Row row23 = requiredSheet.getRow(34);
			setCellData(row23, 4, otherinputs.get("technicalSecondaryContactRole"));
			Row row24 = requiredSheet.getRow(35);
			setCellData(row24, 4, otherinputs.get("technicalSecondaryContactEmailAddress"));
		}



	/**
	 * Method to populate the system catalog sheet
	 * @param populateSourceTargetCatalog
	 * @param outputpath
	 * @param otherinputs
	 * @param workbook
	 */
	public void populateSystemCatalog(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {
		
		    Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			Sheet requiredSheet = workbook.getSheet("SystemCatalog");
			int rowCount = 4;
			int connectionNumber = 0;
			Row row = requiredSheet.getRow(++rowCount);
			setCellData(row, 1, CATALOG_NAME);
			setCellData(row, 2, otherinputs.get("sourceConnectionType"));
			setCellData(row, 3, "Connection" + connectionNumber);
			if(diwTemplateModel.isFileCatalog()) {
				Row row1 = requiredSheet.getRow(++rowCount);
				setCellData(row1, 1, CATALOG_FILE);
			}
	}

	
	/**
	 * Method to populate the system entities sheet
	 * @param tableColumnList
	 * @param otherinputs
	 * @param workbook
	 */
	public void populateSystemEntities(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {
		
		    Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			Sheet requiredSheet = workbook.getSheet("SystemEntities");
			int rowCount = 5;
			boolean isFileCatalog = false;
			Map<String, Map<String, Set<String>>> dbTableMap = new LinkedHashMap<String, Map<String,Set<String>>>();
			Map<String, Set<String>> map = new LinkedHashMap<String, Set<String>>();
			Set<String> overrideSet = new HashSet<String>();
			List<String> overrideList = new ArrayList<String>();
			Map<String, Map<String, Set<String>>> overrideSetValues = new LinkedHashMap<String, Map<String,Set<String>>>();
			Set<String> filterDuplicateTables = new HashSet<String>();
			
			if(diwTemplateModel.isSQLOverrideAvailable()) {
				Map<String, Set<String>> overrideName = new LinkedHashMap<String, Set<String>>();
				dbTableMap = diwTemplateModel.getTableDBMap();
				if (diwTemplateModel.isSQLOverrideAvailable()) {
					String overrideStep = otherinputs.get("sqloverride");
					String step[] = overrideStep.split("[|]");
					for (int i = 0; i < step.length; i++) {
						map = dbTableMap.get(otherinputs.get("sqloverride"));
						try {
							for (String db : map.keySet()) {
								overrideSet.addAll(map.get(db));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						overrideList.add("SQL_OVERRIDE" + i);
						overrideName.put("SQL_OVERRIDE" + i, overrideSet);
						overrideSetValues.put(step[i], overrideName);
						Row row = requiredSheet.getRow(++rowCount);
						setCellData(row, 1, CATALOG_NAME);
						setCellData(row, 2, "SQL_OVERRIDE" + i);
						setCellData(row, 5, otherinputs.get("sourceFeedType"));
						setCellData(row, 18, "TERADATA");
						setCellData(row, 6, otherinputs.get("dataAcquisitionMethod"));
						setCellData(row, 9, "No");
						setCellData(row, 11, otherinputs.get("isShortcut"));
					} 
				}
				diwTemplateModel.setOverrideSet(overrideSet);
			}
			
			//diwTemplateModel.getTableColumnList().put("TERADATA|SQLOVERRIDE", overrideList);
			
			for (String key : diwTemplateModel.getTableColumnList().keySet()) {
				String table[] = key.split("[|]");
				if(table[1].equalsIgnoreCase(NOT_APPLICABLE) || table[1].startsWith(SUBQUERY) || table[1].equalsIgnoreCase(VARIABLE) || table[1].equalsIgnoreCase("CONSTANT") || table[1].equalsIgnoreCase("INSUFFICIENT_DATA") ) {
					continue;
				}
				
				if(!table[0].equalsIgnoreCase(WORK) && !overrideSet.contains(table[1])) {
				if (!filterDuplicateTables.contains(table[1])) {
					filterDuplicateTables.add(table[1]);
					Row row = requiredSheet.getRow(++rowCount);
					setCellData(row, 1, CATALOG_NAME);
					setCellData(row, 2, table[1]);
					if (table[0].contains("xlsx")) {
						setCellData(row, 1, CATALOG_FILE);
						setCellData(row, 5, "Files - Delimited");
						setCellData(row, 12, "Yes");
						setCellData(row, 21, table[0]);
						setCellData(row, 22, ".csv");
						setCellData(row, 23, "1");
						setCellData(row, 25, ",");
						String fileName[] = table[0].split("[\\\\]");
						setCellData(row, 39, fileName[fileName.length - 1]);
						isFileCatalog = true;
					} else if (table[1].endsWith("_txt")) {
						setCellData(row, 1, CATALOG_FILE);
						setCellData(row, 2, table[1].replace("_txt", ""));
						setCellData(row, 5, "Files - Delimited");
						setCellData(row, 12, "Yes");
						setCellData(row, 21, table[0]);
						setCellData(row, 22, ".txt");
						setCellData(row, 23, "1");
						setCellData(row, 25, ",");
						String fileName[] = table[0].split("[\\\\]");
						setCellData(row, 39, fileName[fileName.length - 1]);
						isFileCatalog = true;
					} else {
						setCellData(row, 5, otherinputs.get("sourceFeedType"));
						setCellData(row, 18, table[0]);
					}
					setCellData(row, 6, otherinputs.get("dataAcquisitionMethod"));
					setCellData(row, 9, "No");
					setCellData(row, 11, otherinputs.get("isShortcut"));
				}
				}
				
			}
			diwTemplateModel.setFileCatalog(isFileCatalog);
			diwTemplateModel.setOverrideSetValues(overrideSetValues);
			
	}
	
	


	/**
	 * Method to populate Relational System Entity Definition
	 * 
	 * @param tableColumnList
	 * @param otherinputs
	 * @param workbook
	 */
	public void populateRelationalSystemEntityDefn(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {
			Sheet requiredSheet = workbook.getSheet("RelationalSystemEntityDefn");
			
			//SchemaToIDW schemaToIDW = new SchemaToIDW();
			//Map<String, List<SchemaDTO>> map = schemaToIDW.getSchemaDetails(diwTemplateModel.getTableColumnList());
			Map<String, List<String>> tableMap = diwTemplateModel.getTableColumnList();
			Map<String,SchemaDTO> schemaMap =  diwTemplateModel.getSchemaMap();
			Set<String> filterTableCol =  new HashSet<String>();
			int rowCount = 5;
			
			Set<String> overrideSet = new HashSet<String>();
			overrideSet.add("DUMMYTEST");
			if(diwTemplateModel.isSQLOverrideAvailable()) {
				overrideSet = diwTemplateModel.getOverrideSet();
			}
			
			//Set<String> sqlColumnSet = new HashSet<String>();
			
			int colPosition = 1;
			String [] arry = null;
			for (String key : tableMap.keySet()) {
				String table[] = key.split("[|]");
				if(!table[0].equalsIgnoreCase(WORK)) {
					
					List<String> columnList = tableMap.get(key);
					String catalogName="";
					if (table[0].contains("xlsx") || table[1].endsWith("_txt")) {
						catalogName = CATALOG_FILE;
						table[1] = table[1].replace("_txt", "");
					} else {
						catalogName = CATALOG_NAME;
					}
					for (String colName : columnList) {
					
						
						if(table[1].contains("SUBQUERY") || table[1].equalsIgnoreCase("VARIABLE") || table[1].equalsIgnoreCase("CONSTANT")) {
							continue;
						}
						if (colName.equalsIgnoreCase(ALL_FIELDS)) {
							continue;
						}
						String overrideTable = "";
						if(overrideSet.contains(table[1])) {
							overrideTable="SQL_OVERRIDE0";
						} else {
							overrideTable=table[1];
						}
						
					
					if (!filterTableCol.contains(overrideTable+colName)) {
						filterTableCol.add(overrideTable+colName);
						Row row = requiredSheet.getRow(++rowCount);
						setCellData(row, 1, catalogName);
						setCellData(row, 2, overrideTable);
						setCellData(row, 3, colName);
						SchemaDTO srcColumn = schemaMap.get((table[1]+colName).toUpperCase());
						if (srcColumn != null) {
							if (srcColumn.getDataElementName().equalsIgnoreCase(ALL_FIELDS)) {
								continue;
							}
							setCellData(row, 6, srcColumn.getDatatype());
							setCellData(row, 7, srcColumn.getLength());
							setCellData(row, 10, srcColumn.getNullable());
							setCellData(row, 13, srcColumn.getKeyType());
							if(overrideSet.contains(table[1])) {
								setCellData(row, 12, String.valueOf(colPosition++));
							} else {
								setCellData(row, 12, srcColumn.getColumnPosition());
							}
							
						} else {
							 arry= new String[] {"Missing Table ", table[0]+"."+table[1],  " Column " , colName};
							System.out.println("Missing Table " +table[0]+"."+table[1] + " Column " + colName);
						} 
					}
					} 
				}
				}
			try { 
				CSVWriter writer = new CSVWriter(new FileWriter("C:\\ZDLC\\file-demo\\Files\\OUT\\output.csv"));
				if(arry.length>1)
				writer.writeNext(arry);
			      writer.flush();
			      System.out.println("Data entered");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}

	/**
	 * Method to populate the data mapping sheet
	 * 
	 * @param otherinputs
	 * @param dataMappingName
	 * @param workbook
	 */
	public void populateDataMapping(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {

		    Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			Sheet requiredSheet = workbook.getSheet("DataMappings");
			int rowCount = 4;
			Row row = requiredSheet.getRow(++rowCount);
			setCellData(row, 1, diwTemplateModel.getDataMappingName());
			setCellData(row, 2, otherinputs.get("datamappingdescription"));
			setCellData(row, 3, otherinputs.get("isabstract"));
			setCellData(row, 4, otherinputs.get("pdooption"));
			setCellData(row, 6, otherinputs.get("errorthreshold"));
			setCellData(row, 7, otherinputs.get("errorlogging"));
			setCellData(row, 8, otherinputs.get("rejecthandling"));
			setCellData(row, 9, otherinputs.get("rejectreprocessing"));
	}
	
	
	public void writeTransformation(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {
		
		Sheet requiredSheet = workbook.getSheet("Transformations");
		int rowCount = 5;
		int targetLoadOrder1 = 1;
		Map<String,Map<String,List<TransformationDTO>>> sourceMap = diwTemplateModel.getSrcMap();
		Set<String> filterDuplicateComp =  new HashSet<String>();
		for (String step : sourceMap.keySet()) {
			Map<String, List<TransformationDTO>> map = sourceMap.get(step);
			for(String table : map.keySet()) {
				List<TransformationDTO> dtos = map.get(table);
				for (TransformationDTO dto : dtos) {
					if(dto.getComponentName().endsWith("_txt_TGT")) {
						dto.setComponentName(dto.getComponentName().replace("_txt_TGT", "_TGT"));
					}
					
					if(dto.getComponentName().endsWith("_txt_SRC")) {
						dto.setComponentName(dto.getComponentName().replace("_txt_SRC", "_SRC"));
					}
					
					if(dto.getComponentName().equalsIgnoreCase("EXP_TEMP_TERAERR")) {
						dto.setPreviousComponentName("SQL_OVERRIDE0_SRC");
					} 
					
					if (!filterDuplicateComp.contains(dto.getComponentName())) {
						Row row = requiredSheet.getRow(++rowCount);
						setCellData(row, 1, dto.getDataMappingName());
						if (dto.getComponentName().contains(EXP_TEMP)) {
							setCellData(row, 2, EXPRESSION);
							dto.setTransformationType(EXPRESSION);
							dto.setTransformationDetails("NA");
						} else {
							setCellData(row, 2, dto.getTransformationType());
						}
						setCellData(row, 4, "-");
						if (dto.getTransformationType().equals(TARGET)) {
						
							setCellData(row, 5, TransformationLogicFormater.formatTargetTransformation(
									dto.getTransformationDetails(), dto.getCatalogName(), targetLoadOrder1++));
							
						} else {
							setCellData(row, 5, dto.getTransformationDetails());
						}
						setCellData(row, 6, dto.getComponentName());
						if (!dto.getTransformationType().equals(CATALOG_NAME)) {
							setCellData(row, 7, dto.getPreviousComponentName());
						}
						setCellData(row, 8, dto.getNextComponentName());
						setCellData(row, 10, "Ignore");
						
						filterDuplicateComp.add(dto.getComponentName());
					}
				}
			}
		}
	}
	
	

	/**
	 * Method to populate workflow
	 * @param otherinputs
	 * @param programName
	 * @param workbook
	 */
	public void populateWorkFlows(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {

			Sheet requiredSheet = workbook.getSheet("Workflows");
			Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			int rowCount = 5;
			Row row = requiredSheet.getRow(++rowCount);
			setCellData(row, 1, "Workflow1");
			setCellData(row, 2, otherinputs.get("workflowdescription"));
			setCellData(row, 6, otherinputs.get("run_option"));
	}

	/**
	 * Method to populate the task sheet
	 * @param otherinputs
	 * @param dataMappingName
	 * @param workbook
	 */
	public void populateTask(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {

			Sheet requiredSheet = workbook.getSheet("Tasks");
			Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			int rowCount = 5;
			Row row = requiredSheet.getRow(++rowCount);
			setCellData(row, 1, "WorkFlow1");
			setCellData(row, 3, otherinputs.get("task_type"));
			setCellData(row, 5, "Task");
			setCellData(row, 6, otherinputs.get("task_description"));
			setCellData(row, 8, diwTemplateModel.getDataMappingName());
			//setCellData(row, 27, "Task");
	}

	/**
	 * Method to populate Connection sheet
	 * 
	 * @param otherinputs
	 * @param workbook
	 */
	public void populateConnections(DIWTemplateModel diwTemplateModel,XSSFWorkbook workbook) {

			Sheet requiredSheet = workbook.getSheet("Connections");
			Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			Row row = requiredSheet.getRow(5);
			setCellData(row, 3, otherinputs.get("connectionName"));
			Row row1 = requiredSheet.getRow(6);
			setCellData(row1, 3, otherinputs.get("connectionType"));
			Row row2 = requiredSheet.getRow(8);
			setCellData(row2, 3, otherinputs.get("hostname"));
			Row row3 = requiredSheet.getRow(9);
			setCellData(row3, 3, otherinputs.get("ipaddress"));
			Row row4 = requiredSheet.getRow(10);
			setCellData(row4, 3, otherinputs.get("port"));
			Row row5 = requiredSheet.getRow(12);
			setCellData(row5, 3, otherinputs.get("username"));
			Row row6 = requiredSheet.getRow(13);
			setCellData(row6, 3, otherinputs.get("password"));
			Row row7 = requiredSheet.getRow(22);
			setCellData(row7, 3, "");
			Row row8 = requiredSheet.getRow(23);
			setCellData(row8, 3, "");
			Row row9 = requiredSheet.getRow(24);
			setCellData(row9, 3, otherinputs.get("enableparallelmode"));
			Row row10 = requiredSheet.getRow(27);
			setCellData(row10, 3, otherinputs.get("connectionretryperiod"));

	}

	public void setCellData(Row row, int columnNum, String value) {
		Cell cell;
		try {
			cell = row.getCell(columnNum);
			cell.setCellValue(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
