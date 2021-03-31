package com.zdlc.idw.sas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.DIWTemplateModel;
import com.zdlc.idw.sas.dto.DateFormats;
import com.zdlc.idw.sas.dto.LineageReportDTO;

public class LineageReportConvertor extends IDWConstants {

	

	public List<File> generateDIWReport(String csvPath,String outputpath) throws Exception {
		
		Properties prop = new Properties();
		
		File childFile = new File("../conf/DIWconfig.config");
		System.out.println(childFile);
		FileInputStream input = new FileInputStream(childFile);
		prop.load(input);
		String diw_template_file_path = getAbsolutePaths(prop.getProperty("DIW_TEMPLATE_FILE_Path"));// Template for DIW
		String otherinputpath = getAbsolutePaths(prop.getProperty("otherInputFilePath"));// config file
		
		Map<String, String> otherinputs = otherInputs(otherinputpath);
		File diw_format = new File(diw_template_file_path);
		File inputFileDir = new File(csvPath);
		List<File> outFiles =new ArrayList<File>();
		if (inputFileDir != null && inputFileDir.exists() && inputFileDir.isDirectory()) {
			File L0reports[] = inputFileDir.listFiles();
			for (File lin_report : L0reports) {
				String L0reportName = lin_report.getName();
				if (L0reportName != null && !L0reportName.isEmpty() && !L0reportName.startsWith("~$")) {
					File outputDir = new File(outputpath);
					if (outputDir == null || !outputDir.exists() || outputDir.isDirectory()) {
						outputDir.mkdir();
					}
					System.out.println("Converting L0 to DIW for " + L0reportName);
					File outputfilename = new File(outputpath + "\\"+ L0reportName.substring(0, L0reportName.lastIndexOf(DOT)) + "_" + "DIW format" + ".XLSM");
					String outputfile = outputpath + "\\" + L0reportName.substring(0, L0reportName.lastIndexOf(DOT))+ "_" + "DIW format" + ".XLSM";
					FileUtils.copyFile(diw_format, outputfilename);
					LineageReportToCSV lineageDetails = new LineageReportToCSV();
					List<LineageReportDTO> lineageReportList = lineageDetails.readCSVFile(lin_report);// List contains all the rows in L0
					
					LineageReportConvertor readL0 = new LineageReportConvertor();
					DIWTemplateModel diwTemplateModel =  readL0.segregateLineageReportData(lineageReportList, outputfilename,otherinputs);
					DIWTemplateDataWriter dataWriter = new DIWTemplateDataWriter();
					dataWriter.populateDIWTemplate(diwTemplateModel,outputfile);
					outFiles.add(new File(outputfile));
					System.out.println("DIW Format Excel Produced for " + L0reportName + " in " + outputfile);

				}
			}
		}
		FileUtils.deleteDirectory(inputFileDir);
		return outFiles;

	}
	
	
	/**
	 * This code is responsible for getting the absolute path.
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String getAbsolutePaths(String path) throws IOException{
		File f= new File(path);
		File outputPath = new File(f.getCanonicalPath());
		return outputPath.toString();
	} 

	public DIWTemplateModel segregateLineageReportData(List<LineageReportDTO> lineageReportList, File L0ReportName,Map<String, String> otherinputs) {
		
		String programName = manipulateFileName(L0ReportName.getName());

		
		//DIWTemplatePopulationHelper populationHelper = new DIWTemplatePopulationHelper();
		//Map<String, List<DIWDataDTO>> targetDIWDTO  = populationHelper.groupsourceToTarget(lineageReportList);
		Map<String,List<String>> tableColumnList = new LinkedHashMap<String,List<String>>();
		Set<String> srcToTgtMappingValues = new HashSet<String>();
		List<LineageReportDTO> srcToTgtList =  new ArrayList<LineageReportDTO>();
		List<LineageReportDTO> targetVariablesList =  new ArrayList<LineageReportDTO>();
		Map<String,List<String>> groupByList = new HashMap<String,List<String>>();
		Map<String,Map<String,List<LineageReportDTO>>> multiJoinerMap = new LinkedHashMap<String,Map<String,List<LineageReportDTO>>>();
		Map<String,Map<String,List<LineageReportDTO>>> ifConditionMap = new LinkedHashMap<String,Map<String,List<LineageReportDTO>>>();
		Map<String,Map<String,List<LineageReportDTO>>> caseWhenMap = new LinkedHashMap<String,Map<String,List<LineageReportDTO>>>();
		Map<String,List<LineageReportDTO>> stepwiseList = new LinkedHashMap<String, List<LineageReportDTO>>();
		Map<String,String> codeStatement = new LinkedHashMap<String, String>();
		Set<String> multiJoinerUniqueSet = new HashSet<String>();
		Map<String,Map<String,Set<String>>> tableDBMap = new LinkedHashMap<String,Map<String,Set<String>>>();
		String skipStepNo = "";
		String stepNumber="";
		String format="";
		Map<String, String> targetTableOfWhereandIntnx=RetriveTargetTablebyStep.retriveTarget(lineageReportList);
		
		for (LineageReportDTO dto : lineageReportList) {
			
			if(dto.getstepNumber().contains("_SUB_")) {
				String stepNo = dto.getstepNumber().substring(0,dto.getstepNumber().indexOf("SUB")-1);
				dto.setstepNumber(stepNo.trim());
			}
			
			if(dto.getStatementType().contains("DELETE_STATEMENT")) {
				skipStepNo=dto.getstepNumber();
			}
			
			if(dto.getstepNumber().equalsIgnoreCase(skipStepNo)) {
				continue;
			}
			
			if(!stepwiseList.containsKey(dto.getstepNumber())) {
				List<LineageReportDTO> list = new ArrayList<LineageReportDTO>();
				list.add(dto);
				stepwiseList.put(dto.getstepNumber(), list);
			} else {
				stepwiseList.get(dto.getstepNumber()).add(dto);
				
			}
			
			if(dto.getStatementType().equalsIgnoreCase("CODE_STATEMENT_DEFINITION")) {
				codeStatement.put(dto.getstepNumber(), dto.getTransformationLogic());
			}
			
			
			if ((!(dto.getTargetTable()).equals("CONSTANT")) && (!(dto.getSourceTable()).equals("PARAMETER"))
					&& (!(dto.getSourceTable()).equals("PARAMATER")) && (!(dto.getSourceTable()).equals("VARIABLE"))
					&& (!(dto.getSourceTable()).equals("ISNULL")) && (!(dto.getSourceTable()).contains("SUBQUERY"))
					&& (!(dto.getSourceTable()).equals("NOTAVAILABLE"))
					&& (!(dto.getSourceTable()).equals("NOT_AVAILABLE"))
					&& (!(dto.getSourceTable()).equals("NOTAPPLICABLE"))
					&& (!(dto.getSourceTable()).equals("NOT_APPLICABLE"))
					&& (!(dto.getSourceTable()).equals("CONSTANT"))
					&& (!(dto.getSourceTable()).equals("INSUFFICIENTDATA"))
					&& (!(dto.getSourceTable()).equals("INSUFFICIENT_DATA"))
					&& (!(dto.getSourceSchema()).equals("WORK"))) {
			
				if(!tableDBMap.containsKey(dto.getstepNumber())) {
					Map<String,Set<String>> tableDB =  new LinkedHashMap<String, Set<String>>();
					Set<String> table = new HashSet<String>();
					table.add(dto.getSourceTable());
					tableDB.put(dto.getSourcedataBase(), table);
					tableDBMap.put(dto.getstepNumber(), tableDB);
				} else if(!tableDBMap.get(dto.getstepNumber()).containsKey(dto.getSourcedataBase())) {
					Set<String> table = new HashSet<String>();
					table.add(dto.getSourceTable());
					tableDBMap.get(dto.getstepNumber()).put(dto.getSourcedataBase(), table);
				} else {
					tableDBMap.get(dto.getstepNumber()).get(dto.getSourcedataBase()).add(dto.getSourceTable());
				}
			}
			
			if ((!(dto.getTargetTable()).equals("CONSTANT")) && (!(dto.getSourceTable()).equals("PARAMETER"))
					&& (!(dto.getSourceTable()).equals("PARAMATER")) && (!(dto.getSourceTable()).equals("VARIABLE"))
					&& (!(dto.getSourceTable()).equals("ISNULL")) && (!(dto.getSourceTable()).contains("SUBQUERY"))
					&& (!(dto.getTargetTable()).contains("SUBQUERY")) && (!(dto.getTargetTable()).equals("PARAMETER"))
					&& (!(dto.getTargetTable()).equals("PARAMATER")) && (!(dto.getTargetTable()).equals("VARIABLE"))
					&& (!(dto.getTargetTable()).equals("ISNULL")) && (!(dto.getTargetTable()).equals("NOTAPPLICABLE"))
					&& (!(dto.getTargetTable()).equals("NOT_APPLICABLE"))
					&& (!(dto.getTargetTable()).equals("INSUFFICIENT_DATA"))
					&& (!(dto.getTargetTable()).contains("ResultSet_"))
					&& (!(dto.getTargetTable()).equals("NOTAVAILABLE"))
					&& (!(dto.getSourceTable()).equals("NOTAVAILABLE"))
					&& (!(dto.getTargetTable()).equals("NOT_AVAILABLE"))
					&& (!(dto.getSourceTable()).equals("NOT_AVAILABLE"))
					&& (!(dto.getSourceTable()).equals("NOTAPPLICABLE"))
					&& (!(dto.getSourceTable()).equals("NOT_APPLICABLE"))
					&& (!(dto.getSourceTable()).equals("CONSTANT"))
					&& (!(dto.getSourceTable()).equals("INSUFFICIENTDATA"))
					&& (!(dto.getSourceTable()).equals("INSUFFICIENT_DATA"))) {
				
				if(!srcToTgtMappingValues.contains(dto.getSourceTable()+dto.getSourceColumn()+dto.getTargetTable()+dto.getTargetColumn())) {
					srcToTgtMappingValues.add(dto.getSourceTable()+dto.getSourceColumn()+dto.getTargetTable()+dto.getTargetColumn());
					srcToTgtList.add(dto);
				}
				
				
				
				//Logic to populate table list with column
				if(!tableColumnList.containsKey(dto.getSourceSchema()+PIPE+dto.getSourceTable())) {
					List<String> srcColumnList = new ArrayList<String>();
					srcColumnList.add(dto.getSourceColumn());
					tableColumnList.put(dto.getSourceSchema()+PIPE+dto.getSourceTable(), srcColumnList);
				} else {
					if(!tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).contains(dto.getSourceColumn())) {
						tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).add(dto.getSourceColumn());
					}
				}
				
				if(!tableColumnList.containsKey(dto.getTargetSchema()+PIPE+dto.getTargetTable())) {
					List<String> tgtColumnList = new ArrayList<String>();
					tgtColumnList.add(dto.getTargetColumn());
					tableColumnList.put(dto.getTargetSchema()+PIPE+dto.getTargetTable(), tgtColumnList);
				} else {
					if(!tableColumnList.get(dto.getTargetSchema()+PIPE+dto.getTargetTable()).contains(dto.getTargetColumn())) {
						tableColumnList.get(dto.getTargetSchema()+PIPE+dto.getTargetTable()).add(dto.getTargetColumn());
					}
				}
				
				
			}  else if (dto.getStatementType().contains(DATA_STATEMENT_DEFINITION)&& dto.getTransformationLogic().contains("FORMAT ")){
				 stepNumber=dto.getstepNumber();
				 format=getFormatFromExpression(dto);
			} else if(stepNumber.equals(dto.getstepNumber())&&(dto.getTransformationLogic().contains("INTNX (")|| dto.getTransformationLogic().contains("INTNX(")) &&
					!dto.getSourceTable().contains("CONSTANT") && !dto.getTargetTable().contains("VARIABLE") &&
					!dto.getTargetTable().contains(NOT_APPLICABLE)&& !dto.getSourceColumn().contains(NOT_APPLICABLE)) {
				String formatLogic="";
				if(format!=null) {					
					 formatLogic=dto.getTransformationLogic()+",["+format+"]";
				}
				String src = dto.getSourceTable();
				src = src.replace("VARIABLE", "DUMMY");
				dto.setSourceTable(src);
				dto.setSourceColumn("X");
				dto.setTransformationLogic(formatLogic);
				srcToTgtList.add(dto);
			}/*else if((dto.getTransformationLogic().contains("SYMPUT (")|| dto.getTransformationLogic().contains("SYMPUT(")) &&
					!dto.getSourceTable().contains("CONSTANT") && !dto.getTargetTable().contains("CONSTANT") &&
					!dto.getTargetTable().contains(NOT_APPLICABLE)&& !dto.getSourceColumn().contains(NOT_APPLICABLE)) {
				
				srcToTgtList.add(dto);
			}  */

			//code added for Variable assignement stared
			
			else if (dto.getTargetTable().contains("VARIABLE") && !dto.getTransformationLogic().contains("DIRECT") &&
					!dto.getSourceTable().contains("CONSTANT")&& !dto.getSourceTable().contains("VARIABLE")&&!dto.getSourceTable().contains("NOT_APPLICABLE")) {			
					
				targetVariablesList.add(dto);
					
				}else if(dto.getSourceTable().contains("VARIABLE") && !dto.getTransformationLogic().contains("DIRECT")&& 
						!dto.getTargetTable().contains("VARIABLE")&& !dto.getTargetTable().contains("CONSTANT")&&!dto.getTargetTable().equals("NOT_APPLICABLE")) {
					
					 getVariableTarget(targetVariablesList,dto,srcToTgtList);
						
				}
			//code added for Variable assignement ended
			
			
			else if(dto.getStatementType().equalsIgnoreCase("ORDER_BY_STATEMENT") || /*dto.getStatementType().equalsIgnoreCase("WHERE_CONDITION_STATEMENT") ||*/ 
					dto.getStatementType().equalsIgnoreCase("HAVING_STATEMENT")) {
				
				if(!srcToTgtMappingValues.contains(dto.getSourceTable()+dto.getSourceColumn()+dto.getTargetTable()+dto.getTargetColumn())) {
					srcToTgtMappingValues.add(dto.getSourceTable()+dto.getSourceColumn()+dto.getTargetTable()+dto.getTargetColumn());
				//	System.out.println("dto.getstepNumber()----"+dto.getstepNumber());
					srcToTgtList.add(dto);
				}
				
				if(!tableColumnList.containsKey(dto.getSourceSchema()+PIPE+dto.getSourceTable())) {
					List<String> srcColumnList = new ArrayList<String>();
					srcColumnList.add(dto.getSourceColumn());
					tableColumnList.put(dto.getSourceSchema()+PIPE+dto.getSourceTable(), srcColumnList);
				} else {
					if(!tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).contains(dto.getSourceColumn())) {
						tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).add(dto.getSourceColumn());
					}
				}
				
			} else if(dto.getOperationType().equalsIgnoreCase("JOIN")) {
				//if(!multiJoinerUniqueSet.contains(dto.getTransformationLogic())) {
					multiJoinerUniqueSet.add(dto.getTransformationLogic());
					if(!multiJoinerMap.containsKey(dto.getstepNumber())) {
						Map<String,List<LineageReportDTO>> map = new LinkedHashMap<String,List<LineageReportDTO>>();
						List<LineageReportDTO> dtos = new ArrayList<LineageReportDTO>();
						dtos.add(dto);
						map.put(dto.getSourceTable(),dtos);
						multiJoinerMap.put(dto.getstepNumber(), map);
					} else if(!multiJoinerMap.get(dto.getstepNumber()).containsKey(dto.getSourceTable())) {
						List<LineageReportDTO> dtos = new ArrayList<LineageReportDTO>();
						dtos.add(dto);
						multiJoinerMap.get(dto.getstepNumber()).put(dto.getSourceTable(),dtos);
					} else {
						multiJoinerMap.get(dto.getstepNumber()).get(dto.getSourceTable()).add(dto);
					}
				//}
				
				srcToTgtList.add(dto);
				
				if(!tableColumnList.containsKey(dto.getSourceSchema()+PIPE+dto.getSourceTable())) {
					List<String> srcColumnList = new ArrayList<String>();
					srcColumnList.add(dto.getSourceColumn());
					tableColumnList.put(dto.getSourceSchema()+PIPE+dto.getSourceTable(), srcColumnList);
				} else {
					if(!tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).contains(dto.getSourceColumn())) {
						tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).add(dto.getSourceColumn());
					}
				}
				
			} else if(dto.getStatementType().equalsIgnoreCase("GROUP_BY_STATEMENT")) {
				
				if(groupByList.containsKey(dto.getstepNumber())) {
				groupByList.get(dto.getstepNumber()).add(dto.getSourceTable()+DOT+dto.getSourceColumn());
				} else {
					 List<String> grpList = new ArrayList<String>();
					 grpList.add(dto.getSourceTable()+DOT+dto.getSourceColumn());
					 groupByList.put(dto.getstepNumber(), grpList);
				}
				
				if(!tableColumnList.containsKey(dto.getSourceSchema()+PIPE+dto.getSourceTable())) {
					List<String> srcColumnList = new ArrayList<String>();
					srcColumnList.add(dto.getSourceColumn());
					tableColumnList.put(dto.getSourceSchema()+PIPE+dto.getSourceTable(), srcColumnList);
				} else {
					if(!tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).contains(dto.getSourceColumn())) {
						tableColumnList.get(dto.getSourceSchema()+PIPE+dto.getSourceTable()).add(dto.getSourceColumn());
					}
				}
			} 
			
			
			if(dto.getTransformationLogic().contains("CASE WHEN") && !dto.getStatementType().equalsIgnoreCase("CODE_STATEMENT_DEFINITION")) {
					if(!caseWhenMap.containsKey(dto.getstepNumber())) {
						Map<String,List<LineageReportDTO>> map = new LinkedHashMap<String,List<LineageReportDTO>>();
						List<LineageReportDTO> dtos = new ArrayList<LineageReportDTO>();
						dtos.add(dto);
						//Add once for IF condition
						srcToTgtList.add(dto);
						map.put(dto.getTransformationLogic(),dtos);
						caseWhenMap.put(dto.getstepNumber(), map);
					} else if(!caseWhenMap.get(dto.getstepNumber()).containsKey(dto.getTransformationLogic())) {
						List<LineageReportDTO> dtos = new ArrayList<LineageReportDTO>();
						dtos.add(dto);
						caseWhenMap.get(dto.getstepNumber()).put(dto.getTransformationLogic(),dtos);
					} else {
						caseWhenMap.get(dto.getstepNumber()).get(dto.getTransformationLogic()).add(dto);
					}
			}
			
			if((dto.getStatementType().equalsIgnoreCase("CONDITION_STATEMENT") || dto.getStatementType().equalsIgnoreCase("DATA_STATEMENT") ) 
					&& dto.getTransformationLogic().contains("IF") && dto.getTransformationLogic().contains("THEN")) {
				
				if(!ifConditionMap.containsKey(dto.getstepNumber())) {
					Map<String,List<LineageReportDTO>> map = new LinkedHashMap<String,List<LineageReportDTO>>();
					List<LineageReportDTO> dtos = new ArrayList<LineageReportDTO>();
					dtos.add(dto);
					//Add once for IF condition
					srcToTgtList.add(dto);
					map.put(dto.getTransformationLogic(),dtos);
					ifConditionMap.put(dto.getstepNumber(), map);
				} else if(!ifConditionMap.get(dto.getstepNumber()).containsKey(dto.getTransformationLogic())) {
					List<LineageReportDTO> dtos = new ArrayList<LineageReportDTO>();
					dtos.add(dto);
					ifConditionMap.get(dto.getstepNumber()).put(dto.getTransformationLogic(),dtos);
				} else {
					ifConditionMap.get(dto.getstepNumber()).get(dto.getTransformationLogic()).add(dto);
				}
			} 
			
			if (dto.getStatementType().equalsIgnoreCase("WHERE_CONDITION_STATEMENT")
					&& dto.getTransformationLogic().contains("INTNX(")) {
				if (dto.getTargetColumn().contains(NOT_APPLICABLE) && dto.getTargetTable().contains(NOT_APPLICABLE)&&
						!dto.getSourceTable().contains("CONSTANT")&&!dto.getSourceTable().contains("VARIABLE")&&!dto.getSourceTable().contains(NOT_APPLICABLE)) {
					for (Entry<String, String> entry : targetTableOfWhereandIntnx.entrySet()) {
						if (entry.getKey().contains(dto.getstepNumber())) {
							dto.setTargetColumn(dto.getSourceColumn());
							dto.setTargetTable(entry.getValue());
							srcToTgtList.add(dto);
						}
					}
				}
			}
		}
		
		DIWTemplateModel diwTemplateModel = new DIWTemplateModel();
		diwTemplateModel.setDataMappingName(programName);
		diwTemplateModel.setGroupByList(groupByList);
		diwTemplateModel.setMultiJoinerMap(multiJoinerMap);
		diwTemplateModel.setSrcToTgtList(srcToTgtList);
		diwTemplateModel.setSrcToTgtMappingValues(srcToTgtMappingValues);
		diwTemplateModel.setTableColumnList(tableColumnList);
		diwTemplateModel.setOtherinputs(otherinputs);
		String sqlOverrideConfig = otherinputs.get("sqloverride");
		if(sqlOverrideConfig!=null && sqlOverrideConfig.equalsIgnoreCase("NA")) {
			diwTemplateModel.setSQLOverrideAvailable(false);
		} else if(!sqlOverrideConfig.equalsIgnoreCase("NA")) {
			if(!stepwiseList.containsKey(sqlOverrideConfig)) {
				diwTemplateModel.setSQLOverrideAvailable(false);
			} else {
				diwTemplateModel.setSQLOverrideAvailable(true);
			}
		}
		//diwTemplateModel.setSQLOverrideAvailable(false);
		SchemaToIDW schemaToIDW = new SchemaToIDW();
		diwTemplateModel.setSchemaMap(schemaToIDW.getSchemaDetails());
		diwTemplateModel.setIfConditionMap(ifConditionMap);
		diwTemplateModel.setCaseWhenMap(caseWhenMap);
		diwTemplateModel.setStepwiseList(stepwiseList);
		diwTemplateModel.setCodeStatement(codeStatement);
		diwTemplateModel.setTableDBMap(tableDBMap);
		return diwTemplateModel;
	}
	
	private void getVariableTarget(List<LineageReportDTO> variableList,LineageReportDTO lto,List<LineageReportDTO> srcToTgtList) {
		LineageReportDTO reportDTO= null;
		for (LineageReportDTO lDTO : variableList) {
			if(lDTO.getTargetTable().equalsIgnoreCase(lto.getSourceTable())&&lDTO.getTransformationLogic().equalsIgnoreCase(lto.getTransformationLogic())
					&& lDTO.getTargetColumn().equalsIgnoreCase(lto.getSourceColumn())) {
				reportDTO=lDTO;			
				reportDTO.setTargetColumn(lto.getTargetColumn());
				reportDTO.setTargetTable(lto.getTargetTable());
				reportDTO.setTargetSchema(lto.getTargetSchema());
				srcToTgtList.add(reportDTO);
			}
		}
	}
	

	private static String manipulateFileName(String L0ReportName) {
		String programName="";
		
		if(L0ReportName.contains(DOT)){
		programName=L0ReportName.replace(DOT, "_");
		programName=programName.substring(0,programName.length()-5);
		if(programName.endsWith("_L0")){
			programName.substring(0, programName.length()-3);}
		}
		else{programName=L0ReportName;}
		programName=DetailsfromLineage.withoutSpecialCharacter(programName);
		return programName;
	}

	/***
	 * 
	 * @param otherinputpath
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> otherInputs(String otherinputpath) throws IOException {

		FileReader fr = new FileReader(new File(otherinputpath));// read the input file save all inputs as map
		BufferedReader br = new BufferedReader(fr);
		Map<String, String> otherInput = new LinkedHashMap<String, String>();
		String currentLine;

		while ((currentLine = br.readLine()) != null) {
			if (currentLine.contains("=")) {
				String[] inputline = currentLine.split("=");
				String inputtype = inputline[0];
				String inputname = inputline[1];
				otherInput.put(inputtype, inputname);
			}
		}
		br.close();

		return otherInput;

	}
	public static String getFormatFromExpression(LineageReportDTO dto) {
		String logic=dto.getTransformationLogic();
		
		if(logic!=null&&logic.toUpperCase().contains(DateFormats.YYQ6+".")) {
			logic=DateFormats.YYQ6+"";
		}else if(logic!=null&&logic.toUpperCase().contains(DateFormats.YYMMDD10+".")) {
			logic=DateFormats.YYMMDD10+"";
		}
		
		return logic;
		
	}
}