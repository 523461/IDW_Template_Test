package com.zdlc.idw.sas.dto;

public class TransformationDTO {
	
	private String dataMappingName;
	
	private String transformationType;
	
	private String transformationDetails;
	
	private String componentName;
	
	private String previousComponentName;
	
	private String nextComponentName;
	
	private String catalogName;

	public String getDataMappingName() {
		return dataMappingName;
	}

	public void setDataMappingName(String dataMappingName) {
		this.dataMappingName = dataMappingName;
	}

	public String getTransformationType() {
		return transformationType;
	}

	public void setTransformationType(String transformationType) {
		this.transformationType = transformationType;
	}

	public String getTransformationDetails() {
		return transformationDetails;
	}

	public void setTransformationDetails(String transformationDetails) {
		this.transformationDetails = transformationDetails;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getPreviousComponentName() {
		return previousComponentName;
	}

	public void setPreviousComponentName(String previousComponentName) {
		this.previousComponentName = previousComponentName;
	}

	public String getNextComponentName() {
		return nextComponentName;
	}

	public void setNextComponentName(String nextComponentName) {
		this.nextComponentName = nextComponentName;
	}

	public String getCatalogName() {
		return catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}
	
	

}
