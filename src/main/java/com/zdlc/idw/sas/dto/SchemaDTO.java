package com.zdlc.idw.sas.dto;

public class SchemaDTO {
	
	private  String database;
	private  String schema;
	private  String systemEntityName;
	private  String dataElementName;
	private  String dataElementBusinessName;
	private  String dataElementDescription;
	private  String datatype;
	private  String length;
	private  String precision;
	private  String scale;
	private  String isNullable;
	private  String dataFormat;
	private  String columnPosition;
	private  String keyType;
	private  String characterSetName;
	private  String collationName;
	
	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSystemEntityName() {
		return systemEntityName;
	}

	public void setSystemEntityName(String systemEntityName) {
		this.systemEntityName = systemEntityName;
	}

	public String getDataElementName() {
		return dataElementName;
	}

	public void setDataElementName(String dataElementName) {
		this.dataElementName = dataElementName;
	}

	public String getDataElementBusinessName() {
		return dataElementBusinessName;
	}

	public void setDataElementBusinessName(String dataElementBusinessName) {
		this.dataElementBusinessName = dataElementBusinessName;
	}

	public String getDataElementDescription() {
		return dataElementDescription;
	}

	public void setDataElementDescription(String dataElementDescription) {
		this.dataElementDescription = dataElementDescription;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getNullable() {
		return isNullable;
	}

	public void setNullable(String nullable) {
		isNullable = nullable;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getColumnPosition() {
		return columnPosition;
	}

	public void setColumnPosition(String columnPosition) {
		this.columnPosition = columnPosition;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getCharacterSetName() {
		return characterSetName;
	}

	public void setCharacterSetName(String characterSetName) {
		this.characterSetName = characterSetName;
	}

	public String getCollationName() {
		return collationName;
	}

	public void setCollationName(String collationName) {
		this.collationName = collationName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return database+"-->"+schema+"-->"+systemEntityName+"-->"+dataElementName+"-->"+dataElementBusinessName+"-->"+dataElementDescription+"-->"+datatype+"-->"+length+"-->"+precision+"-->"+scale+"-->"+isNullable+"-->"+dataFormat+"-->"+columnPosition+"-->"+keyType+"-->"+characterSetName+"-->"+collationName+"";
		
	}

	
	}
