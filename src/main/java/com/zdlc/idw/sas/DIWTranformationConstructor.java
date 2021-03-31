package com.zdlc.idw.sas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.DIWTemplateModel;
import com.zdlc.idw.sas.dto.LineageReportDTO;
import com.zdlc.idw.sas.dto.TransformationDTO;
import com.zdlc.idw.sas.helper.IDWTemplatePopulationHelper;
import com.zdlc.idw.sas.helper.TransformationLogicFormater;

public class DIWTranformationConstructor extends IDWConstants {
	
	

	/**
	 * Method to populate transformation sheet
	 * 
	 * @param lineageReportList
	 * @param dataMappingName
	 * @param workbook
	 */
	public Map<String,Map<String,List<TransformationDTO>>> populateTransformations(DIWTemplateModel diwTemplateModel, XSSFWorkbook workbook) {
		
		String expTemp="",compName="";
		
			List<LineageReportDTO> lineageReportList = diwTemplateModel.getSrcToTgtList();
			Map<String,Map<String,List<LineageReportDTO>>> multiJoinerMap  = diwTemplateModel.getMultiJoinerMap();
			//String dataMappingName = diwTemplateModel.getDataMappingName();
			
			Map<String,TransformationDTO> filterUniqueComp =  new LinkedHashMap<String,TransformationDTO>();
			Map<String,Map<String,List<TransformationDTO>>> sourceMap = new LinkedHashMap<String, Map<String,List<TransformationDTO>>>();
			Map<String,List<TransformationDTO>> transfromationMap = new LinkedHashMap<String,List<TransformationDTO>>();
			Set<String> filterMap = new HashSet<String>();
			Map<String,String> expressionMap = new HashMap<String,String>();
			List<LineageReportDTO> orderByList = new ArrayList<LineageReportDTO>();
			String previousStepNo = "";
			int listSize = 0;
			int experssionCounter = 1;
			int aggregatorCounter = 1;
			int filterCounter = 1;
			int sorterCounter =1;
			String dataMappingName = diwTemplateModel.getDataMappingName();
			Map<String,String> componentLink = new HashMap<String,String>();
			Map<String, String> otherinputs = diwTemplateModel.getOtherinputs();
			Map<String, String> codeSatement = diwTemplateModel.getCodeStatement();
			Set<String> overrideSet = diwTemplateModel.getOverrideSet();
			String overrideStep = otherinputs.get("sqloverride");
			//String step[] = overrideStep.split("[|]");
			boolean isSQLcreated=false;
			for (LineageReportDTO dto : lineageReportList) {
				listSize++;
				if(diwTemplateModel.isSQLOverrideAvailable()) {
					if(overrideStep.contains(dto.getstepNumber())) {
						if (!isSQLcreated) {
							isSQLcreated=true;
							String code = codeSatement.get(dto.getstepNumber());
							TransformationDTO transformationDTO2 = new TransformationDTO(); 
							transformationDTO2.setDataMappingName(dataMappingName);
							transformationDTO2.setComponentName("SQL_OVERRIDE0_SRC");
							transformationDTO2.setPreviousComponentName("");
							transformationDTO2.setTransformationType(SOURCE);
							transformationDTO2.setTransformationDetails(TransformationLogicFormater.formatSQLOverideSource(code, CATALOG_NAME, overrideSet));
							List<TransformationDTO> transDtos = new ArrayList<TransformationDTO>();
							transDtos.add(transformationDTO2);
							transfromationMap.put(dto.getstepNumber(), transDtos);
						}
						continue;
					}
				}
				
				if(previousStepNo.isEmpty()) {
					previousStepNo=dto.getstepNumber();
				}
				//String dataMappingName = diwTemplateModel.getDataMappingName()+previousStepNo;
				
				
				if(!previousStepNo.equals(dto.getstepNumber())) {
					
					if (multiJoinerMap.get(previousStepNo)!=null) {
						List<TransformationDTO> list = createMutliJoiner(multiJoinerMap.get(previousStepNo),componentLink,expressionMap,dataMappingName);
						if (list != null && list.size() > 0) {
							transfromationMap.put(MULTI_JOINER, list);
						}
						createJoiner(transfromationMap, componentLink,expressionMap, dataMappingName,
								multiJoinerMap.get(previousStepNo));
					}
					
					if(orderByList!=null && !orderByList.isEmpty()) {
						transfromationMap.put(SORTER, createSorter(orderByList,dataMappingName,sorterCounter++));
						orderByList = new ArrayList<LineageReportDTO>();
					}
					

					setSourcePreviousCompName(setJoinerPreviousCompName(setExpressionPreviousCompName(setAggregatorPreviousCompName(
							setFilterPreviousCompName(setSorterPreviousCompName(setTargetPreviousCompName(transfromationMap,diwTemplateModel,previousStepNo)))))));
					sourceMap.put(previousStepNo, transfromationMap);
					transfromationMap = new LinkedHashMap<String,List<TransformationDTO>>();
					filterUniqueComp = new LinkedHashMap<String,TransformationDTO>();
					previousStepNo = dto.getstepNumber();
				}
				
			
					if (!dto.getSourceSchema().equalsIgnoreCase(WORK)) {
						
						if ((!(dto.getSourceTable()).equals("PARAMETER"))
								&& (!(dto.getSourceTable()).equals("PARAMATER")) && (!(dto.getSourceTable()).equals("VARIABLE"))
								&& (!(dto.getSourceTable()).equals("ISNULL")) && (!(dto.getSourceTable()).contains("SUBQUERY"))
								&& (!(dto.getSourceTable()).equals("NOTAVAILABLE")) && (!(dto.getSourceTable()).equals("NOT_AVAILABLE"))
								&& (!(dto.getSourceTable()).equals("NOTAPPLICABLE")) && (!(dto.getSourceTable()).equals("NOT_APPLICABLE"))
								&& (!(dto.getSourceTable()).equals("CONSTANT")) && (!(dto.getSourceTable()).equals("INSUFFICIENT_DATA"))) {
						
								if (!filterUniqueComp.containsKey(dto.getSourceTable() + SRC)) {
									
									TransformationDTO transformationDTO = new TransformationDTO();
									transformationDTO.setDataMappingName(dataMappingName);
									transformationDTO.setComponentName(dto.getSourceTable() + SRC);
									if (dto.getTargetSchema().equalsIgnoreCase(WORK)) {
										transformationDTO.setPreviousComponentName(EXP_TEMP + dto.getTargetTable());
									} else {
										transformationDTO.setPreviousComponentName(dto.getTargetTable() + TGT);
									}
									String catalogName="";
									if(dto.getSourceSchema().contains("xlsx") || dto.getSourceTable().endsWith("_txt")) {
										transformationDTO.setCatalogName(CATALOG_NAME);
										catalogName = CATALOG_FILE;
									} else {
										transformationDTO.setCatalogName(CATALOG_NAME);
										catalogName = CATALOG_NAME;
									}
									transformationDTO.setTransformationDetails(TransformationLogicFormater
											.formatSourceTransformation(dto.getSourceTable(), catalogName));
									transformationDTO.setTransformationType(SOURCE);
									if (transfromationMap.get(SOURCE) == null) {
										List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
										transDto.add(transformationDTO);
										transfromationMap.put(SOURCE, transDto);
									} else {
										transfromationMap.get(SOURCE).add(transformationDTO);
									}
									componentLink.put(dto.getSourceTable() + SRC, dto.getTargetTable() + TGT);
									filterUniqueComp.put(dto.getSourceTable() + SRC,transformationDTO);
								}
					}
					} else {
						if (!filterUniqueComp.containsKey(EXP_TEMP + dto.getSourceTable())) {
							
							TransformationDTO transformationDTO2 = new TransformationDTO();
							transformationDTO2.setDataMappingName(dataMappingName);
							transformationDTO2.setComponentName(EXP_TEMP + dto.getSourceTable());
							if(dto.getSourceTable().equalsIgnoreCase("TERAERR")) {
								transformationDTO2.setPreviousComponentName("SQL_OVERRIDE0_SRC");
							} else {
								transformationDTO2.setPreviousComponentName("");
								}
							transformationDTO2.setTransformationDetails("NA");
							transformationDTO2.setTransformationType(EXPRESSION);
							if (transfromationMap.get(SOURCE) == null) {
								List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
								transDto.add(transformationDTO2);
								transfromationMap.put(SOURCE, transDto);
							} else {
								transfromationMap.get(SOURCE).add(transformationDTO2);
							}
							expressionMap.put(dto.getSourceTable(), EXP_TEMP + dto.getSourceTable());
							filterUniqueComp.put(EXP_TEMP + dto.getSourceTable(),transformationDTO2);
						} else {

						}
					} 
				
				if (!dto.getTargetSchema().equalsIgnoreCase(WORK)) {
					if (!(dto.getTargetTable().contains(NOT_APPLICABLE)||dto.getTargetTable().contains("NOTAPPLICABLE"))) {
						if (!filterUniqueComp.containsKey(dto.getTargetTable() + TGT)) {
							
							TransformationDTO transformationDTO1 = new TransformationDTO();
							transformationDTO1.setDataMappingName(dataMappingName);
							transformationDTO1.setComponentName(dto.getTargetTable() + TGT);
							if (dto.getSourceSchema().equalsIgnoreCase(WORK)) {
								transformationDTO1.setPreviousComponentName(EXP_TEMP+dto.getSourceTable());
							} else {
								transformationDTO1.setPreviousComponentName(dto.getSourceTable() + SRC);
							}
							
							if(dto.getTargetSchema().contains("xlsx") || dto.getTargetTable().endsWith("_txt")) {
								transformationDTO1.setCatalogName(CATALOG_FILE);
							} else {
								transformationDTO1.setCatalogName(CATALOG_NAME);
							}
							
							transformationDTO1.setTransformationDetails(dto.getTargetTable());
							transformationDTO1.setTransformationType(TARGET);
							if(transfromationMap.get(TARGET)==null) {
								List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
								transDto.add(transformationDTO1);
								transfromationMap.put(TARGET, transDto);
							} else {
								transfromationMap.get(TARGET).add(transformationDTO1);
							}
							componentLink.put(dto.getSourceTable() + SRC, dto.getTargetTable() + TGT);
							filterUniqueComp.put(dto.getTargetTable() + TGT,transformationDTO1);
						} else {
							//Write logic for same table occured again
						}
				
					} 
				} else {
					if(!filterUniqueComp.containsKey(EXP_TEMP+dto.getTargetTable())) {
						
						TransformationDTO transformationDTO2 = new TransformationDTO(); 
						transformationDTO2.setDataMappingName(dataMappingName );
						transformationDTO2.setComponentName(EXP_TEMP+dto.getTargetTable());
						if (dto.getSourceSchema().equalsIgnoreCase(WORK)) {
							transformationDTO2.setPreviousComponentName(EXP_TEMP+dto.getSourceTable());
						} else {
							transformationDTO2.setPreviousComponentName(dto.getSourceTable() + SRC);
						}
						transformationDTO2.setTransformationDetails("NA");
						transformationDTO2.setTransformationType(TARGET);
						if(transfromationMap.get(TARGET)==null) {
							List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
							transDto.add(transformationDTO2);
							transfromationMap.put(TARGET, transDto);
						} else {
							transfromationMap.get(TARGET).add(transformationDTO2);
						}
						expressionMap.put(dto.getTargetTable(),EXP_TEMP+dto.getTargetTable());
						filterUniqueComp.put(EXP_TEMP+dto.getTargetTable(),transformationDTO2);
					} else {

					}
				}
				//}
				
				if(dto.getStatementType().equalsIgnoreCase("CONDITION_STATEMENT") && dto.getTransformationLogic().contains("IF")) {
					//Create Entry for Format
					TransformationDTO transDto = new TransformationDTO();
					transDto.setDataMappingName(dataMappingName);
					transDto.setTransformationType(OTHER_TRANSFORMATION);
					transDto.setComponentName("If_Condition_"+dto.getstepNumber());
					transDto.setTransformationDetails(TransformationLogicFormater.formatMappletTransformation());
					transfromationMap.put(OTHER_TRANSFORMATION, new ArrayList<TransformationDTO>());
					transfromationMap.get(OTHER_TRANSFORMATION).add(transDto);
				}
				
				if(dto.getStatementType().equalsIgnoreCase("DATA_STATEMENT_DEFINITION") && dto.getTransformationLogic().contains("INTNX(")) {
					//Create Entry for Format
					TransformationDTO transDto = new TransformationDTO();
					transDto.setDataMappingName(dataMappingName);
					transDto.setTransformationType(OTHER_TRANSFORMATION);
					transDto.setComponentName("INTNX_Function_"+dto.getstepNumber());
					transDto.setTransformationDetails(TransformationLogicFormater.formatMappletTransformation());
					transfromationMap.put(OTHER_TRANSFORMATION, new ArrayList<TransformationDTO>());
					transfromationMap.get(OTHER_TRANSFORMATION).add(transDto);
				}
				
				if(dto.getTransformationLogic().contains("CATS (")) {
					//Create Entry for Format
					TransformationDTO transDto = new TransformationDTO();
					transDto.setDataMappingName(dataMappingName);
					transDto.setTransformationType(OTHER_TRANSFORMATION);
					transDto.setComponentName("CATS_Statement_"+dto.getstepNumber());
					transDto.setTransformationDetails(TransformationLogicFormater.formatMappletTransformation());
					transfromationMap.put(OTHER_TRANSFORMATION, new ArrayList<TransformationDTO>());
					transfromationMap.get(OTHER_TRANSFORMATION).add(transDto);
				}
				
				if(dto.getTransformationLogic().contains("CASE WHEN")) {
					//Create Entry for Format
					TransformationDTO transDto = new TransformationDTO();
					transDto.setDataMappingName(dataMappingName);
					transDto.setTransformationType(OTHER_TRANSFORMATION);
					transDto.setComponentName("Case_When_Statement_"+dto.getstepNumber());
					transDto.setTransformationDetails(TransformationLogicFormater.formatMappletTransformation());
					transfromationMap.put(OTHER_TRANSFORMATION, new ArrayList<TransformationDTO>());
					transfromationMap.get(OTHER_TRANSFORMATION).add(transDto);
				}
				
				
				
				
				if(dto.getTransformationLogic().toLowerCase().contains("substr") || dto.getTransformationLogic().toLowerCase().contains("datepart") || 
						dto.getTransformationLogic().toLowerCase().contains("strip (") ||
						dto.getTransformationLogic().toLowerCase().contains("strip(") ||
						dto.getTransformationLogic().contains("TRIMN(") ||dto.getTransformationLogic().contains("TRIMN (") ||
						dto.getTransformationLogic().toLowerCase().contains("round(") ||dto.getTransformationLogic().toLowerCase().contains("lowcase(") ||
						dto.getTransformationLogic().toLowerCase().contains("lowcase (") ||dto.getTransformationLogic().toLowerCase().contains("upcase (") ||
						dto.getTransformationLogic().toLowerCase().contains("upcase(") ||dto.getTransformationLogic().toLowerCase().contains("round (")||
						dto.getTransformationLogic().contains("TRIM(")||dto.getTransformationLogic().contains("TRIM (")||
						dto.getTransformationLogic().contains("TRANSLATE(")||dto.getTransformationLogic().contains("TRANSLATE (") ||
						dto.getTransformationLogic().contains("INTNX(")||dto.getTransformationLogic().contains("INTNX (")||
						dto.getTransformationLogic().contains("CATS(")||dto.getTransformationLogic().contains("CATS (")||
						dto.getTransformationLogic().contains("SYMPUT (")|| dto.getTransformationLogic().contains("SYMPUT(")||
						(dto.getStatementType().equalsIgnoreCase("CONDITION_STATEMENT") && dto.getTransformationLogic().contains("IF"))) {
					//write logic for Expression
					if(transfromationMap.get(EXPRESSION)==null) {
						TransformationDTO transformationDTO2 = new TransformationDTO(); 
						
						transformationDTO2.setDataMappingName(dataMappingName);
						//transformationDTO2.setComponentName("Exp_"+dto.getSourceTable()+dto.getTargetTable());
						transformationDTO2.setComponentName("Expression_"+experssionCounter++);
						transformationDTO2.setPreviousComponentName("");
						transformationDTO2.setTransformationDetails("NA");
						transformationDTO2.setTransformationType(EXPRESSION);
						
						List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
						transDto.add(transformationDTO2);
						transfromationMap.put(EXPRESSION, transDto);
					}
				}
				
				if(dto.getTransformationLogic().toLowerCase().contains("sum(")||dto.getTransformationLogic().toLowerCase().contains("sum (")||
						dto.getTransformationLogic().toLowerCase().contains("max(")|| dto.getTransformationLogic().toLowerCase().contains("max (")||
						dto.getTransformationLogic().toLowerCase().contains("min(")|dto.getTransformationLogic().toLowerCase().contains("min (") ||
						dto.getTransformationLogic().toLowerCase().contains("count(")|dto.getTransformationLogic().toLowerCase().contains("count (")) {
					TransformationDTO transformationDTO2 = new TransformationDTO(); 
					transformationDTO2.setDataMappingName(dataMappingName);
					transformationDTO2.setComponentName("Aggregator_"+aggregatorCounter++);
					transformationDTO2.setPreviousComponentName("");
					transformationDTO2.setTransformationDetails("NA");
					transformationDTO2.setTransformationType(AGGREGATOR);
					
					if(transfromationMap.get(AGGREGATOR)==null) {
						List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
						transDto.add(transformationDTO2);
						transfromationMap.put(AGGREGATOR, transDto);
					} 
				}
				

				
				if(dto.getStatementType().equalsIgnoreCase("WHERE_CONDITION_STATEMENT") || dto.getStatementType().equalsIgnoreCase("HAVING_STATEMENT") || dto.getOperationType().equalsIgnoreCase("JOIN")) {
					//added step number into filterMap for the same transformation logic contains different step number with different target table
					if (!filterMap.contains(dto.getTransformationLogic()+"_"+dto.getstepNumber())) {
						//String expTemp="";
						//if(dto.getTransformationLogic().contains("INTNX")) {
							filterMap.add(dto.getTransformationLogic()+"_"+dto.getstepNumber());
							TransformationDTO transformationDTO2 = new TransformationDTO();
							transformationDTO2.setDataMappingName(dataMappingName);
							transformationDTO2.setComponentName("Filter_"+filterCounter++);
							transformationDTO2.setPreviousComponentName("");
							
							if(dto.getSourceSchema().contains("WORK")) {
								expTemp=EXP_TEMP + dto.getSourceTable() + DOT;
							}else {
								expTemp=dto.getSourceTable() + SRC + DOT ;
							}
							if(dto.getStatementType().contains("WHERE_CONDITION_STATEMENT") && dto.getTransformationLogic().contains("INTNX")) {
								 compName="Expression_"+--experssionCounter;
								 transformationDTO2.setTransformationDetails(TransformationLogicFormater.formatIntnxFilterTransformation(dto,expTemp,compName));
								compName="Expression_"+experssionCounter++;
							}else {
								transformationDTO2.setTransformationDetails(TransformationLogicFormater.formatFilterTransformation(dto));
							}
							
							transformationDTO2.setTransformationType(FILTER);
							if (transfromationMap.get(FILTER) == null) {
								List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
								transDto.add(transformationDTO2);
								transfromationMap.put(FILTER, transDto);
							} else {
								transfromationMap.get(FILTER).add(transformationDTO2);
							}
						//}
					} 
				}
				
				
			if (dto.getOperationType().equalsIgnoreCase("JOIN")) {
				if (dto.getTransformationLogic().contains("<") || dto.getTransformationLogic().contains(">")
						|| dto.getTransformationLogic().toLowerCase().contains("like")) {
					if (!filterMap.contains(dto.getTransformationLogic()+"_"+dto.getstepNumber())) {
						filterMap.add(dto.getTransformationLogic()+"_"+dto.getstepNumber());
						TransformationDTO transformationDTO2 = new TransformationDTO();
						transformationDTO2.setDataMappingName(dataMappingName);
						transformationDTO2.setComponentName("Filter_" + filterCounter++);
						transformationDTO2.setPreviousComponentName("");
						transformationDTO2.setTransformationDetails(TransformationLogicFormater.formatFilterTransformation(dto));
						transformationDTO2.setTransformationType(FILTER);
						if (transfromationMap.get(FILTER) == null) {
							List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
							transDto.add(transformationDTO2);
							transfromationMap.put(FILTER, transDto);
						} else {
							transfromationMap.get(FILTER).add(transformationDTO2);
						}
					}
				}
			}
				
				if(dto.getStatementType().equalsIgnoreCase("ORDER_BY_STATEMENT")) {
					orderByList.add(dto);
				}
				
				
				if(lineageReportList.size()==listSize) {
					if (multiJoinerMap.get(previousStepNo)!=null) {
						List<TransformationDTO> list = createMutliJoiner(multiJoinerMap.get(previousStepNo),componentLink,expressionMap,dataMappingName);
						if(list!=null && list.size()>0) {
							transfromationMap.put(MULTI_JOINER, list);
						}
						createJoiner(transfromationMap, componentLink,expressionMap,dataMappingName,multiJoinerMap.get(previousStepNo));
						
						if(orderByList!=null && !orderByList.isEmpty()) {
							transfromationMap.put(SORTER, createSorter(orderByList,dataMappingName,sorterCounter++));
							orderByList = new ArrayList<LineageReportDTO>();
						}
					}
					//setTargetPreviousCompName(transfromationMap);
					setSourcePreviousCompName(setJoinerPreviousCompName(setExpressionPreviousCompName(setAggregatorPreviousCompName(
							setFilterPreviousCompName(setSorterPreviousCompName(setTargetPreviousCompName(transfromationMap,diwTemplateModel,previousStepNo)))))));
					
					
					sourceMap.put(previousStepNo, transfromationMap);
					
					transfromationMap = null;
					filterUniqueComp = new LinkedHashMap<String,TransformationDTO>();
				}
			
			}
			diwTemplateModel.setExpressionMap(expressionMap);
			
			return sourceMap; 
		}



	private List<TransformationDTO> createSorter(List<LineageReportDTO> orderByList,String dataMappingName,int sorterCounter) {
		// TODO Auto-generated method stub
		TransformationDTO transformationDTO2 = new TransformationDTO(); 
		transformationDTO2.setDataMappingName(dataMappingName );
		transformationDTO2.setComponentName("Sorter_"+sorterCounter);
		transformationDTO2.setPreviousComponentName("");
		transformationDTO2.setTransformationType(SORTER);
		transformationDTO2.setTransformationDetails(TransformationLogicFormater.formatSorterTransformation(orderByList));
			List<TransformationDTO> transDtos = new ArrayList<TransformationDTO>();
			transDtos.add(transformationDTO2);
		return transDtos;
	}
	
	

	/**
	 * Method to populate the multi-joiner condition
	 * @param map
	 * @param componentLink 
	 * @param expressionMap 
	 * @param dataMappingName
	 * @return
	 */
	private List<TransformationDTO> createMutliJoiner(Map<String, List<LineageReportDTO>> map,Map<String, String> componentLink, Map<String, String> expressionMap, String dataMappingName) {
		
		List<TransformationDTO> transList =  new ArrayList<TransformationDTO>();
		Set<String> multiJoinerFilter =  new HashSet<String>();
		StringBuffer joinerFilter = new StringBuffer("");
		int conditionCounter = 1;
		for (String srcTable : map.keySet()) {
			if (map.get(srcTable).size()>1) {

				StringBuffer sb = new StringBuffer(fetchTemporaryTable(expressionMap,srcTable.trim()));
				for (LineageReportDTO dto : map.get(srcTable)) {
					if(!multiJoinerFilter.contains(dto.getTransformationLogic())) {
						multiJoinerFilter.add(dto.getTransformationLogic());
						TransformationDTO transformationDTO = new TransformationDTO();
						transformationDTO.setDataMappingName(dataMappingName);
						transformationDTO.setComponentName("MultiJoiner" + srcTable);
						transformationDTO.setTransformationType(MULTI_JOINER);
						String join[] = dto.getTransformationLogic().split(" AND | OR ");
						for(int i=0;i<join.length;i++) {
								multiJoinerFilter.add(dto.getTransformationLogic());
								if(!join[i].contains("<") && !join[i].contains(">")) {
									String name[] = join[i].split("=");
									String rightTableColumn[] = name[1].split("[.]");
									String table = fetchTemporaryTable(expressionMap,rightTableColumn[0].trim());
									if(!sb.toString().contains(table)) {
										sb.append(";" + table);
									}
									conditionCounter++;
								} else {
									//filter logic captured here
									joinerFilter.append(join[i]);
								}
							}	
						transformationDTO.setPreviousComponentName(sb.toString());
						transformationDTO.setTransformationDetails(
								TransformationLogicFormater.formatMultiJoinerTransformation(fetchTemporaryTable(expressionMap,srcTable.trim()), map.get(srcTable),expressionMap));
						if(conditionCounter>=2) {
							transList.add(transformationDTO);
						}
					}
				}
			}
		}
		
		return transList;
	}
	
	
	private void createJoiner(Map<String,List<TransformationDTO>> transfromationMap,Map<String,String> componentLink,Map<String, String> expressionMap, String dataMappingName, Map<String, List<LineageReportDTO>> map) {
		
		int joinCounter = 0;
		String joiner = "";
		Map<String,String> joinerList = new HashMap<String,String>();
		for (String key : map.keySet()) {
			
		if (map.get(key).size()<=1) {
			List<LineageReportDTO> dtos = map.get(key);
			for (LineageReportDTO dto : dtos) {
				if (!joinerList.containsKey(dto.getTransformationLogic())) {
					joinCounter++;
					TransformationDTO transformationDTO2 = new TransformationDTO();
					transformationDTO2.setDataMappingName(dataMappingName);
					String transformationLogic = dto.getTransformationLogic();
					String name[] = transformationLogic.split("=");
					String leftTableColumn[] = name[0].split("[.]");
					String rightTableColumn[] = name[1].split("[.]");
					transformationDTO2.setComponentName(
							"JNR" + joinCounter + "_" + dto.getSourceTable() + "_" + dto.getSourceColumn());
					if (joiner != null && !joiner.isEmpty()) {
						transformationDTO2.setPreviousComponentName(
								joiner + ";" + fetchTemporaryTable(expressionMap,dto.getSourceTable()) + ";" + fetchTemporaryTable(expressionMap,rightTableColumn[0].trim()));
					} else {
						transformationDTO2
								.setPreviousComponentName(fetchTemporaryTable(expressionMap,dto.getSourceTable())+ ";" + fetchTemporaryTable(expressionMap,rightTableColumn[0].trim()));
					}

					transformationDTO2.setTransformationDetails(TransformationLogicFormater.formatJoinerTransformation(
							IDWTemplatePopulationHelper.setJoinType(dto.getStatementType()), fetchTemporaryTable(expressionMap,leftTableColumn[0].trim()),
							leftTableColumn[1], fetchTemporaryTable(expressionMap,rightTableColumn[0].trim()), rightTableColumn[1]));
					transformationDTO2.setTransformationType(JOINER);
					joinerList.put(dto.getTransformationLogic(), dataMappingName);

					//sourceMap1.put("Joiner"+joinCounter,transformationDTO2);
					if (transfromationMap.get(JOINER) == null) {
						List<TransformationDTO> transDto = new ArrayList<TransformationDTO>();
						transDto.add(transformationDTO2);
						transfromationMap.put(JOINER, transDto);
					} else {
						transfromationMap.get(JOINER).add(transformationDTO2);
					}

					if (!componentLink.containsKey(JOINER)) {
						componentLink.put(JOINER,
								"JNR" + joinCounter + "_" + dto.getSourceTable() + "_" + dto.getSourceColumn());
					} else {
						String s = componentLink.get(JOINER);
						componentLink.put(JOINER, s + "~" + "JNR" + joinCounter + "_" + dto.getSourceTable() + "_"
								+ dto.getSourceColumn());
					}
					joiner = "JNR" + joinCounter + "_" + dto.getSourceTable() + "_" + dto.getSourceColumn();
				}
			} 
		}
		}
	}
	

	private String fetchTemporaryTable(Map<String, String> componentLink, String tableName) {
		String compName = "";
		if(componentLink.containsKey(tableName)) {
			compName = componentLink.get(tableName);
		} else {
			compName = tableName+SRC;
		}
		return compName;
	}
	

	private Map<String, List<TransformationDTO>> setTargetPreviousCompName(Map<String, List<TransformationDTO>> sourceMap1,DIWTemplateModel model, String previousStepNo) {
		
		if(sourceMap1.get(TARGET) != null ) {
		List<TransformationDTO> tgtDto = sourceMap1.get(TARGET);
		
		if(sourceMap1.get(SORTER)!=null) {
			tgtDto.get(0).setPreviousComponentName(sourceMap1.get(SORTER).get(0).getComponentName());
		} else if(sourceMap1.get(FILTER)!=null) {
			try {
				int size = sourceMap1.get(FILTER).size();
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(FILTER).get(0).getComponentName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (sourceMap1.get(AGGREGATOR)!=null) {
			tgtDto.get(0).setPreviousComponentName(sourceMap1.get(AGGREGATOR).get(0).getComponentName());
		} else if (sourceMap1.get(EXPRESSION)!= null) {
			tgtDto.get(0).setPreviousComponentName(sourceMap1.get(EXPRESSION).get(0).getComponentName());
		} else if (sourceMap1.get(JOINER)!= null) {
			List<TransformationDTO> joinerDtos = sourceMap1.get(JOINER);
			try {
				tgtDto.get(0).setPreviousComponentName(joinerDtos.get(joinerDtos.size()-1).getComponentName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (sourceMap1.get(MULTI_JOINER) != null) {
			List<TransformationDTO> multiJoinerDtos = sourceMap1.get(MULTI_JOINER);
			String prevCompName="";
			try {
				prevCompName = tgtDto.get(tgtDto.size() - 1).getPreviousComponentName();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			tgtDto.get(tgtDto.size() - 1).setPreviousComponentName(prevCompName+","+multiJoinerDtos.get(0).getComponentName());
		} else if (sourceMap1.get(SOURCE)!= null) {
			List<TransformationDTO> list = sourceMap1.get(SOURCE);
			if(list.size()>1) {
				List<TransformationDTO> unionList = createUnioun(list);
				unionList.get(0).setNextComponentName(tgtDto.get(0).getComponentName());
				tgtDto.get(0).setPreviousComponentName(unionList.get(0).getComponentName());
				sourceMap1.put(SET_OPERATION, unionList);
				if(model.getUnionSetOperation()!=null && model.getUnionSetOperation().containsKey(previousStepNo)) {
					model.getUnionSetOperation().get(previousStepNo).addAll(unionList);
				} else {
					Map<String, List<TransformationDTO>> unionSetOperation= new HashMap<String, List<TransformationDTO>>();
					unionSetOperation.put(previousStepNo, unionList);
					model.setUnionSetOperation(unionSetOperation);
				}
			} else {
				tgtDto.get(0).setPreviousComponentName(list.get(0).getComponentName());
			}
		}
		//setSorterPreviousCompName(sourceMap1);
		}
		return sourceMap1;
	}
	
	
	private List<TransformationDTO> createUnioun(List<TransformationDTO> list) {
		
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		int count=1;
		sb.append("<Type: Union ALL/>");
		for (TransformationDTO dto : list) {
			sb.append("<"+dto.getComponentName().replace(SRC, "")+":"+dto.getComponentName()+"/>");
			if(count != list.size()) {
				sb1.append(dto.getComponentName()+";");
			} else {
				sb1.append(dto.getComponentName());
			}
			count++;
		}
		
		TransformationDTO transformationDTO2 = new TransformationDTO();
		transformationDTO2.setDataMappingName(list.get(0).getDataMappingName() );
		transformationDTO2.setPreviousComponentName(sb1.toString());
		transformationDTO2.setComponentName("Union_"+sb1.toString().replace(";", "_"));
		transformationDTO2.setTransformationType(SET_OPERATION);
		transformationDTO2.setTransformationDetails(sb.toString());
		List<TransformationDTO> transDtos = new ArrayList<TransformationDTO>();
		transDtos.add(transformationDTO2);
		return transDtos;
	}
	
	
	private Map<String, List<TransformationDTO>> setSorterPreviousCompName(Map<String, List<TransformationDTO>> sourceMap1) {
		  
		  if (sourceMap1.get(SORTER) != null) {
			List<TransformationDTO> tgtDto = sourceMap1.get(SORTER);
			if (sourceMap1.get(FILTER) != null) {
				int size = sourceMap1.get(FILTER).size();
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(FILTER).get(size-1).getComponentName());
			} else if (sourceMap1.get(AGGREGATOR) != null) {
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(AGGREGATOR).get(0).getComponentName());
			} else if (sourceMap1.get(EXPRESSION) != null) {
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(EXPRESSION).get(0).getComponentName());
			} else if (sourceMap1.get(JOINER) != null) {
				List<TransformationDTO> joinerDtos = sourceMap1.get(JOINER);
				tgtDto.get(0).setPreviousComponentName(joinerDtos.get(joinerDtos.size() - 1).getComponentName());
			} else if (sourceMap1.get(MULTI_JOINER) != null) {
				List<TransformationDTO> multiJoinerDtos = sourceMap1.get(MULTI_JOINER);
				
				String prevCompName = tgtDto.get(multiJoinerDtos.size() - 1).getPreviousComponentName();
				
				if(!prevCompName.isEmpty()) {
					tgtDto.get(multiJoinerDtos.size() - 1).setPreviousComponentName(prevCompName+";"+multiJoinerDtos.get(0).getComponentName());
				} else {
					tgtDto.get(multiJoinerDtos.size() - 1).setPreviousComponentName(multiJoinerDtos.get(0).getComponentName());
				}
				
			} else if (sourceMap1.get(SOURCE) != null) {
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(SOURCE).get(0).getComponentName());
			} 
		}
		  //setFilterPreviousCompName(sourceMap1);
		  return sourceMap1;
	}
	
	private Map<String, List<TransformationDTO>> setFilterPreviousCompName(Map<String, List<TransformationDTO>> sourceMap1) {
		
			  if (sourceMap1.get(FILTER) != null) {

				for (int i=0;i<sourceMap1.get(FILTER).size();i++) {
					if(sourceMap1.get(FILTER).size()>i+1) {
						sourceMap1.get(FILTER).get(i+1).setPreviousComponentName(sourceMap1.get(FILTER).get(i).getComponentName());
						sourceMap1.get(FILTER).get(i).setNextComponentName(sourceMap1.get(FILTER).get(i+1).getComponentName());
					}
				}
				List<TransformationDTO> tgtDto = sourceMap1.get(FILTER);
				
				int position = 0;
				if (sourceMap1.get(AGGREGATOR) != null) {
					tgtDto.get(position).setPreviousComponentName(sourceMap1.get(AGGREGATOR).get(0).getComponentName());
					//tgtDto.get(0).setNextComponentName(sourceMap1.get(AGGREGATOR).get(0).getComponentName());
				} else if (sourceMap1.get(EXPRESSION) != null) {
					tgtDto.get(position).setPreviousComponentName(sourceMap1.get(EXPRESSION).get(0).getComponentName());
				} else if (sourceMap1.get(JOINER) != null) {
					List<TransformationDTO> joinerDtos = sourceMap1.get(JOINER);
					tgtDto.get(position).setPreviousComponentName(joinerDtos.get(joinerDtos.size() - 1).getComponentName());
				} else if (sourceMap1.get(MULTI_JOINER) != null) {
					List<TransformationDTO> multiJoinerDtos = sourceMap1.get(MULTI_JOINER);
					String prevCompName = tgtDto.get(position).getPreviousComponentName();
					if(!prevCompName.isEmpty()) {
						tgtDto.get(position).setPreviousComponentName(prevCompName+";"+multiJoinerDtos.get(0).getComponentName());
					} else {
						tgtDto.get(position).setPreviousComponentName(multiJoinerDtos.get(0).getComponentName());
					}
				} else if (sourceMap1.get(SOURCE) != null) {
					tgtDto.get(position).setPreviousComponentName(sourceMap1.get(SOURCE).get(0).getComponentName());
				} 
				
				//Set Next Component for Filter
				int pos = sourceMap1.get(FILTER).size()-1;
				if (sourceMap1.get(SORTER) != null) {
					tgtDto.get(pos).setNextComponentName(sourceMap1.get(SORTER).get(0).getComponentName());
				} else if (sourceMap1.get(TARGET) != null) {
					tgtDto.get(pos).setNextComponentName(sourceMap1.get(TARGET).get(0).getComponentName());
				}
			}
			//setAggregatorPreviousCompName(sourceMap1);
			return sourceMap1;
		}

	private Map<String, List<TransformationDTO>> setAggregatorPreviousCompName(
			Map<String, List<TransformationDTO>> sourceMap1) {
		String prevCompName = "";
		if (sourceMap1.get(AGGREGATOR) != null) {
			List<TransformationDTO> tgtDto = sourceMap1.get(AGGREGATOR);
			if (sourceMap1.get(EXPRESSION) != null) {
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(EXPRESSION).get(0).getComponentName());
			} else if (sourceMap1.get(JOINER) != null) {
				List<TransformationDTO> joinerDtos = sourceMap1.get(JOINER);
				tgtDto.get(0).setPreviousComponentName(joinerDtos.get(joinerDtos.size() - 1).getComponentName());
			} else if (sourceMap1.get(MULTI_JOINER) != null) {
				List<TransformationDTO> multiJoinerDtos = sourceMap1.get(MULTI_JOINER);
				// modified the code for fixing the IndexOutofboundException
				// prevCompName = tgtDto.get(multiJoinerDtos.size() -1).getPreviousComponentName();
				if (tgtDto.size() > multiJoinerDtos.size() - 1) {
					prevCompName = tgtDto.get(multiJoinerDtos.size() - 1).getPreviousComponentName();
					if (prevCompName != null && !prevCompName.isEmpty()) {
						tgtDto.get(multiJoinerDtos.size() - 1).setPreviousComponentName(prevCompName + "," + multiJoinerDtos.get(0).getComponentName());
					} else {
						tgtDto.get(multiJoinerDtos.size() - 1).setPreviousComponentName(multiJoinerDtos.get(0).getComponentName());
					}
				}
			} else if (sourceMap1.get(SOURCE) != null) {
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(SOURCE).get(0).getComponentName());
			}
		}
		// setExpressionPreviousCompName(sourceMap1);
		return sourceMap1;
	}

	 private Map<String, List<TransformationDTO>> setExpressionPreviousCompName(Map<String, List<TransformationDTO>> sourceMap1) {
		  
		 
		  if (sourceMap1.get(EXPRESSION) != null) {
			List<TransformationDTO> tgtDto = sourceMap1.get(EXPRESSION);
			
			if (sourceMap1.get(JOINER) != null) {
				List<TransformationDTO> joinerDtos = sourceMap1.get(JOINER);
				tgtDto.get(0).setPreviousComponentName(joinerDtos.get(joinerDtos.size() - 1).getComponentName());
			} else if (sourceMap1.get(MULTI_JOINER) != null) {
				List<TransformationDTO> multiJoinerDtos = sourceMap1.get(MULTI_JOINER);
				String prevCompName = tgtDto.get(tgtDto.size() - 1).getPreviousComponentName();
				if(prevCompName != null && !prevCompName.isEmpty()) {
					tgtDto.get(tgtDto.size() - 1).setPreviousComponentName(prevCompName+";"+multiJoinerDtos.get(0).getComponentName());
				} else {
					tgtDto.get(tgtDto.size() - 1).setPreviousComponentName(multiJoinerDtos.get(0).getComponentName());
				}
			} else if (sourceMap1.get(SOURCE) != null) {
				List<TransformationDTO> list = sourceMap1.get(SOURCE);
				int counter = 0;
				StringBuffer sb = new StringBuffer("");
				for (TransformationDTO transformationDTO : list) {
					counter++;
					if(list.size()==counter) {
						sb.append(transformationDTO.getComponentName());
					} else {
						sb.append(transformationDTO.getComponentName() +";");
					}
				}
				tgtDto.get(0).setPreviousComponentName(sb.toString());
			} 
		}
		  return sourceMap1;
		}
	 
	 
	 private Map<String, List<TransformationDTO>> setJoinerPreviousCompName(Map<String, List<TransformationDTO>> sourceMap1) {
		  
		  if (sourceMap1.get(JOINER) != null) {
			List<TransformationDTO> tgtDto = sourceMap1.get(JOINER);
			if (sourceMap1.get(MULTI_JOINER) != null) {
				List<TransformationDTO> multiJoinerDtos = sourceMap1.get(MULTI_JOINER);
				String prevCompName="";
				try {
					prevCompName = tgtDto.get(tgtDto.size() - 1).getPreviousComponentName();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String cmpName[] = prevCompName.split("[;]");
				String s = multiJoinerDtos.get(0).getPreviousComponentName();
				
				for (String string : cmpName) {
					if(s.contains(string)) {
						prevCompName = prevCompName.replaceAll(string+";", "");
					}
				}
				
				tgtDto.get(tgtDto.size() - 1).setPreviousComponentName(prevCompName+"; "+multiJoinerDtos.get(0).getComponentName());
			} 
			
			/*else if (sourceMap1.get(SOURCE) != null) {
				tgtDto.get(0).setPreviousComponentName(sourceMap1.get(SOURCE).get(0).getComponentName());
			} */
		}
		//setSourcePreviousCompName(sourceMap1);
		  return sourceMap1;
		}
	 
	 private Map<String, List<TransformationDTO>> setSourcePreviousCompName(Map<String, List<TransformationDTO>> sourceMap1) {
		  if (sourceMap1.get(SOURCE) != null) {
			List<TransformationDTO> tgtDto = sourceMap1.get(SOURCE);
			for (TransformationDTO transformationDTO : tgtDto) {
				transformationDTO.setPreviousComponentName("");
			}
		}
		  return sourceMap1;
		}

}