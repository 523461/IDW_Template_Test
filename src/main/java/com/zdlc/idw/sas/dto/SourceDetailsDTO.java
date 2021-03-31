package com.zdlc.idw.sas.dto;

public class SourceDetailsDTO {

	private String sourceName;

	private String sourceSchema;
	
	private String stepNumber;

	public String getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(String stepNumber) {
		this.stepNumber = stepNumber;
	}

	private String tansformationLogic;

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceSchema() {
		return sourceSchema;
	}

	public void setSourceSchema(String sourceSchema) {
		this.sourceSchema = sourceSchema;
	}

	public String getTansformationLogic() {
		return tansformationLogic;
	}

	public void setTansformationLogic(String tansformationLogic) {
		this.tansformationLogic = tansformationLogic;
	}

}
