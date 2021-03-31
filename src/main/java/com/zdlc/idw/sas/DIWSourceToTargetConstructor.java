package com.zdlc.idw.sas;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.DIWTemplateModel;
import com.zdlc.idw.sas.dto.LineageReportDTO;
import com.zdlc.idw.sas.dto.SchemaDTO;
import com.zdlc.idw.sas.dto.TransformationDTO;
import com.zdlc.idw.sas.helper.IDWTemplatePopulationHelper;
import com.zdlc.idw.sas.helper.TransformationLogicFormater;

public class DIWSourceToTargetConstructor extends IDWConstants {

	/**
	 * Method to populate SourceToTargetMapping
	 * 
	 * @param dtos
	 * @param dataMappingName
	 * @param workbook
	 */
	public void populateSourceToTargetmapping(DIWTemplateModel diwTemplateModel, XSSFWorkbook workbook) {

		Sheet requiredSheet = workbook.getSheet("SourceToTargetMapping");
		int rowCount = 2;
		// SchemaToIDW demoIDW = new SchemaToIDW();
		Map<String, SchemaDTO> schemaListMap = diwTemplateModel.getSchemaMap();
		;
		String dataMappingName = diwTemplateModel.getDataMappingName();
		Map<String, List<String>> groupByList = diwTemplateModel.getGroupByList();
		List<LineageReportDTO> dtos = diwTemplateModel.getSrcToTgtList();
		Set<String> transformationLogicMap = new HashSet<String>();
		Map<String, Map<String, List<TransformationDTO>>> srcMap = diwTemplateModel.getSrcMap();
	//	Map<String, Map<String, List<LineageReportDTO>>> ifConditionMap = diwTemplateModel.getIfConditionMap();
		Set<String> overrideSet = diwTemplateModel.getOverrideSet();

		Map<String, List<String>> map = diwTemplateModel.getTableColumnList();
		Map<String, List<TransformationDTO>> uniounOperation = diwTemplateModel.getUnionSetOperation();
		if(uniounOperation!=null)
		for (String stepNo : uniounOperation.keySet()) {
			List<TransformationDTO> list = uniounOperation.get(stepNo);
			for (TransformationDTO transDto : list) {
				String lst11[] = transDto.getPreviousComponentName().split("[;]");
				for (String temp : map.keySet()) {
					String table[] = temp.split("[|]");
					if (transDto.getPreviousComponentName().contains(table[1])) {
						List<String> colNames = map.get(temp);
						for (String colName : colNames) {
							Row row8 = requiredSheet.getRow(++rowCount);
							setCellData(row8, 1, "-");
							setCellData(row8, 2, dataMappingName);
							SchemaDTO schemaDTO = schemaListMap.get((table[1] + colName).toUpperCase());
							setCellData(row8, 3, transDto.getComponentName() + DOT + colName
									+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
							StringBuffer sb = new StringBuffer();
							for (String s : lst11) {
								sb.append(s + "." + colName + ",");
							}
							setCellData(row8, 4, sb.toString().substring(0, sb.toString().length() - 1));
							setCellData(row8, 5, TransformationLogicFormater.formatUnionExpression(sb.toString(),
									transDto.getComponentName()));

							Row row2 = requiredSheet.getRow(++rowCount);
							setCellData(row2, 1, "-");
							setCellData(row2, 2, dataMappingName);
							setCellData(row2, 3, transDto.getNextComponentName() + DOT + colName
									+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
							setCellData(row2, 4, transDto.getComponentName() + DOT + colName);
							setCellData(row2, 5, TransformationLogicFormater.formatDefaultExpression(
									transDto.getComponentName() + DOT + colName, transDto.getNextComponentName()));
						}
						break;
					}
				}
			}
		}

		Map<String, Map<String, List<LineageReportDTO>>> ifMap = diwTemplateModel.getIfConditionMap();
		/*for (String setpNo : ifMap.keySet()) {
			Map<String, List<LineageReportDTO>> transList = ifMap.get(setpNo);
			for (String transLogic : transList.keySet()) {
				List<LineageReportDTO> ifList = transList.get(transLogic);

				String componentName1 = "HARDCODED";
				
				 * if(repotDto.getTargetSchema().equalsIgnoreCase(WORK)) { componentName1 =
				 * EXP_TEMP+repotDto.getTargetTable(); } else { componentName1 =
				 * repotDto.getTargetTable() + TGT; }
				 

				if (!transformationLogicMap.contains(transLogic)) {

					transformationLogicMap.add(transLogic);
					StringBuffer sbSrc = new StringBuffer("");
					StringBuffer sbTgt = new StringBuffer("");

					for (LineageReportDTO reportDto : ifList) {
						// sb.append()
						if (!reportDto.getSourceTable().equalsIgnoreCase("NOT_APPLICABLE")
								&& !reportDto.getSourceTable().equalsIgnoreCase("VARIABLE")) {
							if (reportDto.getSourceSchema().equalsIgnoreCase("WORK")) {
								sbSrc.append(EXP_TEMP + reportDto.getSourceTable() + DOT + reportDto.getSourceColumn()
										+ ",");
							} else {
								sbSrc.append(
										reportDto.getSourceTable() + SRC + DOT + reportDto.getSourceColumn() + ",");
							}
						}
						if (!reportDto.getTargetSchema().equalsIgnoreCase("NOT_APPLICABLE")
								&& !reportDto.getTargetTable().equalsIgnoreCase("NOT_APPLICABLE")
								&& !reportDto.getTargetTable().equalsIgnoreCase("VARIABLE")) {
							if (!sbTgt.toString().contains(reportDto.getSourceColumn())) {
								if (reportDto.getTargetSchema().equalsIgnoreCase("WORK")) {
									sbTgt.append(EXP_TEMP + reportDto.getTargetTable() + DOT
											+ reportDto.getTargetColumn() + ",");
								} else {
									sbTgt.append(
											reportDto.getTargetTable() + SRC + DOT + reportDto.getTargetColumn() + ",");
								}
							}
						}

					}
					String logic = "";
					String expName = srcMap.get(setpNo).get(EXPRESSION).get(0).getComponentName();
					Row row1 = requiredSheet.getRow(++rowCount);
					setCellData(row1, 1, "-");
					setCellData(row1, 2, dataMappingName);
					if (!sbTgt.toString().isEmpty()) {
						setCellData(row1, 3, expName + sbTgt.toString().substring(sbTgt.toString().indexOf(DOT),
								sbTgt.toString().length() - 1));
					}
					if (!sbSrc.toString().isEmpty()) {
						setCellData(row1, 4, sbSrc.toString().substring(0, sbSrc.toString().length() - 1));
					}
					logic = TransformationLogicFormater.formatIfCondition(transLogic, componentName1, ifList,
							diwTemplateModel.getExpressionMap());
					setCellData(row1, 5, logic);

					Row row2 = requiredSheet.getRow(++rowCount);
					setCellData(row2, 1, "-");
					setCellData(row2, 2, dataMappingName);
					if (!sbTgt.toString().isEmpty()) {
						setCellData(row2, 3, sbTgt.toString().substring(0, sbTgt.toString().length() - 1));
					}

					String t1 = "";
					String t2 = "";
					if (!sbTgt.toString().isEmpty()) {
						setCellData(row2, 4, expName + sbTgt.toString().substring(sbTgt.toString().indexOf(DOT),
								sbTgt.toString().length() - 1));
						t1 = expName + sbTgt.toString().substring(sbTgt.toString().indexOf(DOT),
								sbTgt.toString().length() - 1);
						t2 = sbTgt.toString().substring(0, sbTgt.toString().indexOf(DOT) - 1);
					}

					setCellData(row2, 5, TransformationLogicFormater.formatGeneralExpression(t1, t2));

				}
				// }
			}

		}*/

		for (LineageReportDTO dto : dtos) {

			// TODO: logic needs to revisit
			if (dto.getTargetTable().equalsIgnoreCase(NOT_APPLICABLE) || dto.getTargetTable().contains(SUBQUERY)
					|| dto.getTargetColumn().equalsIgnoreCase(NOT_APPLICABLE)) {
				continue;
			}
			
			if(uniounOperation!=null)
			if (uniounOperation.containsKey(dto.getstepNumber())) {
				continue;
			}

			if (dto.getSourceTable().endsWith("_txt")) {
				dto.setSourceTable(dto.getSourceTable().replace("_txt", ""));
			}

			if (dto.getTargetTable().endsWith("_txt")) {
				dto.setTargetTable(dto.getTargetTable().replace("_txt", ""));
			}

			if (dto.getSourceColumn().equalsIgnoreCase(ALL_FIELDS)
					|| dto.getTargetColumn().equalsIgnoreCase(ALL_FIELDS)) {
				continue;
			}

			Row row = requiredSheet.getRow(++rowCount);

			SchemaDTO schemaDTO = schemaListMap.get((dto.getSourceTable() + dto.getSourceColumn()).toUpperCase());
			if (!schemaListMap.containsKey((dto.getTargetTable() + dto.getTargetColumn()).toUpperCase())) {
				schemaListMap.put((dto.getTargetTable() + dto.getTargetColumn()).toUpperCase(), schemaDTO);
			}

			setCellData(row, 1, "-");
			setCellData(row, 2, dataMappingName);

			String componentName = "";
			if (dto.getTargetSchema().equalsIgnoreCase(WORK)) {
				setCellData(row, 3, EXP_TEMP + dto.getTargetTable() + "." + dto.getTargetColumn()
						+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
				componentName = EXP_TEMP + dto.getTargetTable();
			} else {
				setCellData(row, 3, dto.getTargetTable() + TGT + "." + dto.getTargetColumn());
				componentName = dto.getTargetTable() + TGT;
			}

			String exp = "";/* constructTableName(dto.getSourceSchema(), dto.getSourceTable(), SOURCE); */
			if (dto.getSourceSchema().equalsIgnoreCase(WORK)) {
				setCellData(row, 4, EXP_TEMP + dto.getSourceTable() + "." + dto.getSourceColumn());
				exp = EXP_TEMP + dto.getSourceTable() + "." + dto.getSourceColumn();
			} else {
				String table = dto.getSourceTable();
				if (overrideSet != null && !overrideSet.isEmpty()) {
					if (overrideSet.contains(table)) {
						table = "SQL_OVERRIDE0";
					}
				}
				setCellData(row, 4, table + "_SRC." + dto.getSourceColumn());
				exp = table + "_SRC." + dto.getSourceColumn();
			}

			if (componentName.contains(EXP_TEMP)) {
				setCellData(row, 5, TransformationLogicFormater.formatDefaultExpression(exp, componentName));
			}

			// added to check nested functions logic
			/*
			 * int index=checkNestedFunctions(dto.getTransformationLogic()); if(index>3) {
			 * setCellData(row, 5, TransformationLogicFormater.formatGeneralExpression(dto.
			 * getTransformationLogic(), componentName)); }
			 */

			if (!dto.getTransformationLogic().contains("DIRECT")/* &&index<3 */) {

				if (dto.getTransformationLogic().contains("SUBSTR") || dto.getTransformationLogic().contains("DATEPART")
						|| dto.getTransformationLogic().contains("STRIP(")
						|| dto.getTransformationLogic().contains("STRIP (")
						|| dto.getTransformationLogic().contains("TRIMN(")
						|| dto.getTransformationLogic().contains("TRIMN (")
						|| dto.getTransformationLogic().contains("LOWCASE(")
						|| dto.getTransformationLogic().contains("LOWCASE (")
						|| dto.getTransformationLogic().contains("ROUND(")
						|| dto.getTransformationLogic().contains("ROUND (")
						|| dto.getTransformationLogic().contains("UPCASE(")
						|| dto.getTransformationLogic().contains("UPCASE (")
						|| dto.getTransformationLogic().contains("TRIM(")
						|| dto.getTransformationLogic().contains("TRANSLATE (")
						|| dto.getTransformationLogic().contains("TRANSLATE(")
						|| dto.getTransformationLogic().contains("TRIM (")
						|| dto.getTransformationLogic().contains("INTNX(")
						|| dto.getTransformationLogic().contains("INTNX (")
						|| dto.getTransformationLogic().contains("CATS(")
						|| dto.getTransformationLogic().contains("CATS (")
						|| dto.getTransformationLogic().contains("SYMPUT (")
						|| dto.getTransformationLogic().contains("SYMPUT(")) {
					String expName = srcMap.get(dto.getstepNumber()).get(EXPRESSION).get(0).getComponentName();
					String logic = "";
					if (dto.getOperationType().equalsIgnoreCase("CONDITION_STATEMENT")
							&& dto.getTransformationLogic().contains("IF")) {
						logic = "IF Condition";
					} else {
						logic = TransformationLogicFormater.formatTransformationLogic(dto.getTransformationLogic(),
								expName, exp);
					}

					setCellData(row, 3, expName + "." + dto.getTargetColumn()
							+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
					setCellData(row, 5, logic);
					Row row1 = requiredSheet.getRow(++rowCount);
					setCellData(row1, 1, "-");
					setCellData(row1, 2, dataMappingName);
					setCellData(row1, 3, componentName + "." + dto.getTargetColumn()
							+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
					setCellData(row1, 4, expName + DOT + dto.getTargetColumn());
					if (componentName.contains(EXP_TEMP)) {
						setCellData(row1, 5, TransformationLogicFormater
								.formatDefaultExpression(expName + "." + dto.getTargetColumn(), componentName));
					}

				} else if (dto.getTransformationLogic().contains("SUM(")
						|| dto.getTransformationLogic().contains("SUM (")
						|| dto.getTransformationLogic().contains("MAX(")
						|| dto.getTransformationLogic().contains("MAX (")
						|| dto.getTransformationLogic().contains("MIN(")
						|| dto.getTransformationLogic().contains("MIN (")
						|| dto.getTransformationLogic().contains("COUNT(")
						|| dto.getTransformationLogic().contains("COUNT (")) {

					String aggName = srcMap.get(dto.getstepNumber()).get(AGGREGATOR).get(0).getComponentName();
					setCellData(row, 3, aggName + DOT + dto.getTargetColumn()
							+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
					// String logic =
					// TransformationLogicFormater.formatSumAggregator(dto.getSourceTable() +
					// "_SRC." + dto.getSourceColumn(),aggName,groupByList);
					String logic = TransformationLogicFormater.formatAggTransformationLogic(
							dto.getTransformationLogic(), dto.getSourceTable() + "_SRC." + dto.getSourceColumn(),
							aggName, groupByList.get(dto.getstepNumber()));
					setCellData(row, 5, logic);
					Row row1 = requiredSheet.getRow(++rowCount);
					setCellData(row1, 1, "-");
					setCellData(row1, 2, dataMappingName);
					setCellData(row1, 3, componentName + "." + dto.getTargetColumn()
							+ IDWTemplatePopulationHelper.formDataType(schemaDTO));
					setCellData(row1, 4, aggName + DOT + dto.getTargetColumn());
					if (componentName.contains(EXP_TEMP)) {
						setCellData(row1, 5, TransformationLogicFormater
								.formatDefaultExpression(aggName + DOT + dto.getTargetColumn(), componentName));
					}
				}

				/*
				 * else
				 * if(dto.getTransformationLogic().contains("MAX(")||dto.getTransformationLogic(
				 * ).contains("MAX (")||
				 * dto.getTransformationLogic().contains("MIN(")||dto.getTransformationLogic().
				 * contains("MIN (") /*||
				 * dto.getTransformationLogic().contains("COUNT(")||dto.getTransformationLogic()
				 * .contains("COUNT (")) { String aggName =
				 * srcMap.get(dto.getstepNumber()).get(AGGREGATOR).get(0).getComponentName();
				 * setCellData(row, 3, aggName +DOT+
				 * dto.getTargetColumn()+IDWTemplatePopulationHelper.formDataType(schemaDTO));
				 * String logic = TransformationLogicFormater.formatTransformationLogic(dto.
				 * getTransformationLogic(),aggName,exp); setCellData(row, 5, logic); Row row1 =
				 * requiredSheet.getRow(++rowCount); setCellData(row1, 1, "-");
				 * setCellData(row1, 2, dataMappingName); setCellData(row1, 3, componentName
				 * +"."+
				 * dto.getTargetColumn()+IDWTemplatePopulationHelper.formDataType(schemaDTO));
				 * setCellData(row1, 4, aggName+DOT+ dto.getTargetColumn());
				 * if(componentName.contains(EXP_TEMP)) { setCellData(row1, 5,
				 * TransformationLogicFormater.formatDefaultExpression(aggName+DOT+
				 * dto.getTargetColumn(), componentName)); } }
				 */
			}

		}
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

	public static int checkNestedFunctions(String logic) {
		int index = 0;
		String arr[] = logic.split(",");
		for (String paranthesis : arr) {
			if (paranthesis.contains("(") || paranthesis.contains(")")) {
				index++;
			}
		}
		return index;
	}

}