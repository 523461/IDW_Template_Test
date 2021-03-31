package com.zdlc.idw.sas.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class DIWTemplateModel {

	private String dataMappingName;
	private Map<String,List<String>> tableColumnList;
	private Set<String> srcToTgtMappingValues;
	private List<LineageReportDTO> srcToTgtList;
	private Map<String,List<String>> groupByList;
	private Map<String,Map<String,List<LineageReportDTO>>> multiJoinerMap;
	private Map<String, String> otherinputs;
	private Map<String,Map<String,List<TransformationDTO>>> srcMap;
	private Map<String,String> expressionMap; 
	private Map<String,Map<String,List<LineageReportDTO>>> ifConditionMap;
	private Map<String,Map<String,List<LineageReportDTO>>> caseWhenMap;
	private Map<String,List<LineageReportDTO>> stepwiseList;
	private Map<String,String> codeStatement;
	private Map<String,Map<String,Set<String>>> tableDBMap;
	private boolean isFileCatalog;
	private boolean isSQLOverrideAvailable;
	private Map<String, Map<String, Set<String>>> overrideSetValues;
	private Set<String> overrideSet;
	private Map<String,SchemaDTO> schemaMap;
	private Map<String,List<TransformationDTO>> unionSetOperation;
	
	public String getDataMappingName() {
		return dataMappingName;
	}
	public void setDataMappingName(String dataMappingName) {
		this.dataMappingName = dataMappingName;
	}
	public Map<String, List<String>> getTableColumnList() {
		return tableColumnList;
	}
	public void setTableColumnList(Map<String, List<String>> tableColumnList) {
		this.tableColumnList = tableColumnList;
	}
	public Set<String> getSrcToTgtMappingValues() {
		return srcToTgtMappingValues;
	}
	public void setSrcToTgtMappingValues(Set<String> srcToTgtMappingValues) {
		this.srcToTgtMappingValues = srcToTgtMappingValues;
	}
	public List<LineageReportDTO> getSrcToTgtList() {
		return srcToTgtList;
	}
	public void setSrcToTgtList(List<LineageReportDTO> srcToTgtList) {
		this.srcToTgtList = srcToTgtList;
	}
	
	public Map<String, List<String>> getGroupByList() {
		return groupByList;
	}
	public void setGroupByList(Map<String, List<String>> groupByList) {
		this.groupByList = groupByList;
	}
	public Map<String, Map<String, List<LineageReportDTO>>> getMultiJoinerMap() {
		return multiJoinerMap;
	}
	public void setMultiJoinerMap(Map<String, Map<String, List<LineageReportDTO>>> multiJoinerMap) {
		this.multiJoinerMap = multiJoinerMap;
	}
	public Map<String, String> getOtherinputs() {
		return otherinputs;
	}
	public void setOtherinputs(Map<String, String> otherinputs) {
		this.otherinputs = otherinputs;
	}
	public Map<String, Map<String, List<TransformationDTO>>> getSrcMap() {
		return srcMap;
	}
	public void setSrcMap(Map<String, Map<String, List<TransformationDTO>>> srcMap) {
		this.srcMap = srcMap;
	}
	public Map<String, Map<String, List<LineageReportDTO>>> getIfConditionMap() {
		return ifConditionMap;
	}
	public void setIfConditionMap(Map<String, Map<String, List<LineageReportDTO>>> ifConditionMap) {
		this.ifConditionMap = ifConditionMap;
	}
	public Map<String, String> getExpressionMap() {
		return expressionMap;
	}
	public void setExpressionMap(Map<String, String> expressionMap) {
		this.expressionMap = expressionMap;
	}
	public Map<String, Map<String, List<LineageReportDTO>>> getCaseWhenMap() {
		return caseWhenMap;
	}
	public void setCaseWhenMap(Map<String, Map<String, List<LineageReportDTO>>> caseWhenMap) {
		this.caseWhenMap = caseWhenMap;
	}
	/**
	 * @return the stepwiseList
	 */
	public Map<String, List<LineageReportDTO>> getStepwiseList() {
		return stepwiseList;
	}
	/**
	 * @param stepwiseList the stepwiseList to set
	 */
	public void setStepwiseList(Map<String, List<LineageReportDTO>> stepwiseList) {
		this.stepwiseList = stepwiseList;
	}
	/**
	 * @return the codeStatement
	 */
	public Map<String, String> getCodeStatement() {
		return codeStatement;
	}
	/**
	 * @param codeStatement the codeStatement to set
	 */
	public void setCodeStatement(Map<String, String> codeStatement) {
		this.codeStatement = codeStatement;
	}
	/**
	 * @return the tableDBMap
	 */
	public Map<String, Map<String, Set<String>>> getTableDBMap() {
		return tableDBMap;
	}
	/**
	 * @param tableDBMap the tableDBMap to set
	 */
	public void setTableDBMap(Map<String, Map<String, Set<String>>> tableDBMap) {
		this.tableDBMap = tableDBMap;
	}
	public boolean isFileCatalog() {
		return isFileCatalog;
	}
	public void setFileCatalog(boolean isFileCatalog) {
		this.isFileCatalog = isFileCatalog;
	}
	public boolean isSQLOverrideAvailable() {
		return isSQLOverrideAvailable;
	}
	public void setSQLOverrideAvailable(boolean isSQLOverrideAvailable) {
		this.isSQLOverrideAvailable = isSQLOverrideAvailable;
	}
	public Map<String, Map<String, Set<String>>> getOverrideSetValues() {
		return overrideSetValues;
	}
	public void setOverrideSetValues(Map<String, Map<String, Set<String>>> overrideSetValues) {
		this.overrideSetValues = overrideSetValues;
	}
	public Set<String> getOverrideSet() {
		return overrideSet;
	}
	public void setOverrideSet(Set<String> overrideSet) {
		this.overrideSet = overrideSet;
	}
	public Map<String, SchemaDTO> getSchemaMap() {
		return schemaMap;
	}
	public void setSchemaMap(Map<String, SchemaDTO> schemaMap) {
		this.schemaMap = schemaMap;
	}
	/**
	 * @return the unionSetOperation
	 */
	public Map<String, List<TransformationDTO>> getUnionSetOperation() {
		return unionSetOperation;
	}
	/**
	 * @param unionSetOperation the unionSetOperation to set
	 */
	public void setUnionSetOperation(Map<String, List<TransformationDTO>> unionSetOperation) {
		this.unionSetOperation = unionSetOperation;
	}
	
}