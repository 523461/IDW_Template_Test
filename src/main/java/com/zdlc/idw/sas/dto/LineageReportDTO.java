package com.zdlc.idw.sas.dto;

/**
 * POJO to hold the Lineage report properites.
 * 
 * @author 621119
 *
 */
public class LineageReportDTO {

	private String sourceSchema;
	private String sourceTable;
	private String sourceColumn;
	private String operationType;
	private String statementType;
	private String targetSchema;
	private String targetTable;
	private String targetColumn;
	private String transformationLogic;
	//private String dataType;
	//private String length;
	//private String position;
	private String stepNumber;
	private String sourcedataBase;

	/**
	 * @return the stepnumber
	 */

	public String getstepNumber() {
		return stepNumber;
	}

	public void setstepNumber(String stepNumber) {
		this.stepNumber = stepNumber;
	}


	/**
	 * @return the statementType
	 */
	public String getStatementType() {
		return statementType;
	}

	/**
	 * @param statementType
	 *            the statementType to set
	 */
	public void setStatementType(String statementType) {
		this.statementType = statementType;
	}

	/**
	 * @return the operationType
	 */
	public String getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType
	 *            the operationType to set
	 */
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	/**
	 * @return the sourceSchema
	 */
	public String getSourceSchema() {
		return sourceSchema;
	}

	/**
	 * @param sourceSchema
	 *            the sourceSchema to set
	 */
	public void setSourceSchema(String sourceSchema) {
		this.sourceSchema = sourceSchema;
	}

	/**
	 * @return the sourceTable
	 */
	public String getSourceTable() {
		return sourceTable;
	}

	/**
	 * @param sourceTable
	 *            the sourceTable to set
	 */
	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	/**
	 * @return the sourceColumn
	 */
	public String getSourceColumn() {
		return sourceColumn;
	}

	/**
	 * @param sourceColumn
	 *            the sourceColumn to set
	 */
	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}

	/**
	 * @return the targetSchema
	 */
	public String getTargetSchema() {
		return targetSchema;
	}

	/**
	 * @param targetSchema
	 *            the targetSchema to set
	 */
	public void setTargetSchema(String targetSchema) {
		this.targetSchema = targetSchema;
	}

	/**
	 * @return the targetTable
	 */
	public String getTargetTable() {
		return targetTable;
	}

	/**
	 * @param targetTable
	 *            the targetTable to set
	 */
	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	/**
	 * @return the targetColumn
	 */
	public String getTargetColumn() {
		return targetColumn;
	}

	/**
	 * @param targetColumn
	 *            the targetColumn to set
	 */
	public void setTargetColumn(String targetColumn) {
		this.targetColumn = targetColumn;
	}

	/**
	 * @return the transformationLogic
	 */
	public String getTransformationLogic() {
		return transformationLogic;
	}

	/**
	 * @param transformationLogic
	 *            the transformationLogic to set
	 */
	public void setTransformationLogic(String transformationLogic) {
		this.transformationLogic = transformationLogic;
	}

	/**
	 * @return the sourcedataBase
	 */
	public String getSourcedataBase() {
		return sourcedataBase;
	}

	/**
	 * @param sourcedataBase the sourcedataBase to set
	 */
	public void setSourcedataBase(String sourcedataBase) {
		this.sourcedataBase = sourcedataBase;
	}
	
		@Override
	public String toString() {
		return "LineageReportDTO [sourceSchema=" + sourceSchema + ", sourceTable=" + sourceTable + ", sourceColumn="
				+ sourceColumn + ", operationType=" + operationType + ", statementType=" + statementType
				+ ", targetSchema=" + targetSchema + ", targetTable=" + targetTable + ", targetColumn=" + targetColumn
				+ ", transformationLogic=" + transformationLogic + ", stepNumber=" + stepNumber + ", sourcedataBase="
				+ sourcedataBase + "]";
	}


}
