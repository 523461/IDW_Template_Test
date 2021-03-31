package com.zdlc.idw.sas.dto;

public class DataPipelineDTO {
	
	private String sourceTable;
	private String targetTable;
	private String dataPipeLine;
	private String tableType;
	private String programName;
	private String fetchDistinct;
	private String srcPassThroughExpression;
	private String sqlOverrideQuery;
	private String sourceFilter;
	private String sourceFeedJoin;
	private String trgtPassThroughExpression;
	private String loadStrategy;
	private String updateOverrideSQL;
	private String loaderOption;
	private String targetLoadOrder;
	private String preSessionSQL;
	private String postSessionSQL;
	private String targetDataSetType;
	private String trgtPassThroughExpressionName;
	public String getTrgtPassThroughExpressionName() {
		return trgtPassThroughExpressionName;
	}

	public void setTrgtPassThroughExpressionName(
			String trgtPassThroughExpressionName) {
		this.trgtPassThroughExpressionName = trgtPassThroughExpressionName;
	}

	public String getTargetDataSetType() {
		if(targetDataSetType == null)
		{
			targetDataSetType = "";
		}
		return targetDataSetType;
	}

	public void setTargetDataSetType(String targetDataSetType) {
		this.targetDataSetType = targetDataSetType;
	}

	public String getSourceTable() {
		if(sourceTable == null)
		{
			sourceTable = "";
		}
		return sourceTable;
	}

	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}

	public String getTargetTable() {
		if(targetTable == null)
		{
			targetTable = "";
		}
		return targetTable;
	}

	public void setTargetTable(String targetTable) {
		this.targetTable = targetTable;
	}

	public String getDataPipeLine() {
		if(dataPipeLine == null)
		{
			dataPipeLine = "";
		}
		return dataPipeLine;
	}

	public void setDataPipeLine(String dataPipeLine) {
		this.dataPipeLine = dataPipeLine;
	}

	public String getTableType() {
		if(tableType == null)
		{
			tableType = "";
		}
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getProgramName() {
		if(programName == null)
		{
			programName = "";
		}
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getFetchDistinct() {
		if(fetchDistinct == null)
		{
			fetchDistinct = "";
		}
		return fetchDistinct;
	}

	public void setFetchDistinct(String fetchDistinct) {
		this.fetchDistinct = fetchDistinct;
	}

	public String getSrcPassThroughExpression() {
		if(srcPassThroughExpression == null)
		{
			srcPassThroughExpression = "";
		}
		return srcPassThroughExpression;
	}

	public void setSrcPassThroughExpression(String srcPassThroughExpression) {
		this.srcPassThroughExpression = srcPassThroughExpression;
	}

	public String getSqlOverrideQuery() {
		if(sqlOverrideQuery == null)
		{
			sqlOverrideQuery = "";
		}
		return sqlOverrideQuery;
	}

	public void setSqlOverrideQuery(String sqlOverrideQuery) {
		this.sqlOverrideQuery = sqlOverrideQuery;
	}

	public String getSourceFilter() {
		return sourceFilter;
	}

	public void setSourceFilter(String sourceFilter) {
		if(srcPassThroughExpression == null)
		{
			srcPassThroughExpression = "";
		}
		this.sourceFilter = sourceFilter;
	}

	public String getSourceFeedJoin() {
		if(sourceFeedJoin == null)
		{
			sourceFeedJoin = "";
		}
		return sourceFeedJoin;
	}

	public void setSourceFeedJoin(String sourceFeedJoin) {
		this.sourceFeedJoin = sourceFeedJoin;
	}

	public String getTrgtPassThroughExpression() {
		if(trgtPassThroughExpression == null)
		{
			trgtPassThroughExpression = "";
		}
		return trgtPassThroughExpression;
	}

	public void setTrgtPassThroughExpression(String trgtPassThroughExpression) {
		this.trgtPassThroughExpression = trgtPassThroughExpression;
	}

	public String getLoadStrategy() {
		if(loadStrategy == null)
		{
			loadStrategy = "";
		}
		return loadStrategy;
	}

	public void setLoadStrategy(String loadStrategy) {
		this.loadStrategy = loadStrategy;
	}

	public String getUpdateOverrideSQL() {
		if(updateOverrideSQL == null)
		{
			updateOverrideSQL = "";
		}
		return updateOverrideSQL;
	}

	public void setUpdateOverrideSQL(String updateOverrideSQL) {
		this.updateOverrideSQL = updateOverrideSQL;
	}

	public String getLoaderOption() {
		if(loaderOption == null)
		{
			loaderOption = "";
		}
		return loaderOption;
	}

	public void setLoaderOption(String loaderOption) {
		this.loaderOption = loaderOption;
	}

	public String getTargetLoadOrder() {
		if(targetLoadOrder == null)
		{
			targetLoadOrder = "";
		}
		return targetLoadOrder;
	}

	public void setTargetLoadOrder(String targetLoadOrder) {
		this.targetLoadOrder = targetLoadOrder;
	}

	public String getPreSessionSQL() {
		if(preSessionSQL == null)
		{
			preSessionSQL = "";
		}
		return preSessionSQL;
	}

	public void setPreSessionSQL(String preSessionSQL) {
		this.preSessionSQL = preSessionSQL;
	}

	public String getPostSessionSQL() {
		if(postSessionSQL == null)
		{
			postSessionSQL = "";
		}
		return postSessionSQL;
	}

	public void setPostSessionSQL(String postSessionSQL) {
		this.postSessionSQL = postSessionSQL;
	}


	

	
	

}
