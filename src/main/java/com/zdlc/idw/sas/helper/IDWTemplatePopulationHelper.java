/**
 * 
 */
package com.zdlc.idw.sas.helper;

import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.SchemaDTO;




/**
 * Helper class contain the common method can used across the utility.
 * 
 * @author 229575
 *
 */
public class IDWTemplatePopulationHelper extends IDWConstants {
	
	public static String setJoinType(String statementType) {
		String joinType = "Inner";
		if(statementType.equalsIgnoreCase(LEFT_JOIN_STATEMENT)) {
			joinType = "Left Outer";
		} else if (statementType.equalsIgnoreCase(RIGHT_JOIN_STATEMENT)) {
			joinType = "Right Outer";
		} 
		
		//Enable the below statement based on the Full join condition
		/*else if (dto.getStatementType().equalsIgnoreCase("FULL JOIN STATEMENT")) {
			joinType = "Full Outer";
		}*/
		return joinType;
	}
	
	public static String formDataType(SchemaDTO schemaDTO) {
		
		//string (20,0)
		StringBuffer sb = new StringBuffer(" ");
		if (schemaDTO!=null) {
			if (schemaDTO.getDatatype().equalsIgnoreCase("VARCHAR")) {
				sb.append("string");
			} else if (schemaDTO.getDatatype().equalsIgnoreCase("NUMBER")) {
				sb.append("number");
			} else if (schemaDTO.getDatatype().equalsIgnoreCase("DATE")) {
				sb.append("date");
			} if (schemaDTO.getDatatype().equalsIgnoreCase("STRING")) {
				sb.append("string");
			}
			sb.append(" (");
			sb.append(schemaDTO.getLength());
			sb.append(",0");
			sb.append(")");
		} else {
			sb.append("string (50,0)");
		}
		return sb.toString();
	}
	
	
	public static String formatTempTable(String schemaName,String tableName) {
		if(schemaName.equalsIgnoreCase("WORK")) {
			
		}
		return tableName;
		
	}

}
