/**
 * 
 */
package com.zdlc.idw.sas.helper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.zdlc.idw.sas.constant.IDWConstants;
import com.zdlc.idw.sas.dto.LineageReportDTO;
import com.zdlc.idw.sas.dto.TransformationDTO;

/**
 * @author 229575
 *
 */
public class TransformationLogicFormater extends IDWConstants {
	
	public static String formatTransformationLogic(String logic,String expName,String compName) {
		String transRule = "";
		if(logic.toLowerCase().contains("substr")) {
			transRule = formatSubstringExpression(logic, expName,compName);
		} else if (logic.contains("STRIP(") || logic.contains("STRIP (")||logic.contains("TRIMN(")||logic.contains("TRIMN (")) {
			transRule = formatTrimExpression(expName,compName);
		} else if (logic.contains("LOWCASE")) {
			transRule = formatLowercaseExpression(expName,compName);
		}else if(logic.contains("UPCASE")) {
			transRule = formatUppercaseExpression(expName,compName);
		} else if (logic.contains("DATEPART")) {
			transRule = formatDatePart(compName, expName);
		}else if (logic.contains("ROUND(")||logic.contains("ROUND (")) {
			transRule = formatRoundExpression(logic, compName, expName);
		}else if (logic.contains("TRIM(")||logic.contains("TRIM (")) {
			transRule = formatRtrimExpression(compName, expName);
		}else if (logic.contains("TRANSLATE(")||logic.contains("TRANSLATE (")) {
			transRule = formatTranslateExpression(logic,compName, expName);
		}else if (logic.contains("CATS(")||logic.contains("CATS (")) {
			transRule = formatCatsExpression(logic, compName, expName);
		}else if(logic.contains("INTNX(")||logic.contains("INTNX (")) {
        	transRule = formatIntnxExpression(logic, compName, expName);
        }else if(logic.contains("SYMPUT(")||logic.contains("SYMPUT (")) {
        	transRule=formatSymPutExpression(logic, expName);
        }else if (logic.contains("IF")) {
			transRule = formatDatePart(logic, expName);
		}
		return transRule;
	}
	private static String formatIntnxExpression(String logic, String compName, String expName) {
//		System.out.println(logic);
		String format="";
		if(logic.trim().endsWith("]")) {
			
			 format=logic.substring(logic.indexOf('[')+1, logic.lastIndexOf(']'));
		}
		logic=logic.substring(logic.indexOf('(')+1, logic.lastIndexOf(')'));
			if(logic.trim().endsWith(")")) {
				logic=logic.substring(logic.indexOf('(')+1, logic.lastIndexOf(')'));
			}
		String inParameters[]=logic.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Expression/>");
		if(inParameters.length>3) {
			String year=inParameters[0];
			String currentDate=inParameters[1];
			String decideDate=inParameters[2];
			String bigOrEnd=inParameters[3];
			if(currentDate.contains("TODAY()")) {
				currentDate="sysdate";
			}
			if(decideDate.contains("0")) {
				decideDate="";
			}else {
				decideDate=decideDate+"*";
			}
			
			if(format.contains("YYMMDD10")&&bigOrEnd.contains("END")) {
				sb.append("<Expression:-idwgetdate(idwAddToDate("+currentDate+", 'MM',"+decideDate+"3),'YY') idwPipe '-' idwPipe (idwStrToInt(idwDateToStr(idwAddToDate("+currentDate+", 'MM',3), 'Q')) * 3) idwPipe '-' idwPipe '31' type:-Exp/>");
			}else if(format.contains("YYMMDD10")&&bigOrEnd.contains("BEGINNING")) {
				sb.append("<Expression:-idwgetdate(idwAddToDate("+currentDate+", 'MM',"+decideDate+"3),'YY') idwPipe '-' idwPipe (idwStrToInt(idwDateToStr(idwAddToDate("+currentDate+", 'MM',3), 'Q')) * 3)-2 idwPipe '-' idwPipe '01' type:-Exp/>");
			}else if(format.contains("YYQ6")&&bigOrEnd.contains("END")) {
				sb.append("<Expression:-idwgetdate(idwAddToDate("+currentDate+", 'MM',"+decideDate+"3),'YY') idwPipe 'Q' idwPipe idwDateToStr(idwAddToDate("+currentDate+", 'MM',"+decideDate+"3),'Q') type:-Exp/>");
			}else if(format.contains("")&&bigOrEnd.contains("BEGINNING")) {
				sb.append("<Expression:-idwgetdate(idwAddToDate("+currentDate+", 'MM',"+decideDate+"),'YY') idwPipe '-' idwPipe (idwStrToInt(idwDateToStr(idwAddToDate("+currentDate+", 'MM',"+decideDate+"), 'MM) idwPipe '-' idwPipe '01' type:-Exp/>");
			}
		}
		
		sb.append("<Component Name:-" + expName + "/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	public static String formatSymPutExpression(String logic,String componentName) {
		logic=logic.substring(logic.indexOf('(')+1, logic.lastIndexOf(')'));
		String inParameters[]=logic.split(",");
		if(inParameters.length>1) {
			logic=inParameters[1];
		}
		logic=logic.replace("||", " idwpipe ").replace("\"\"", "\"");
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Expression/>");
		sb.append("<Expression:-"+logic+"type:-Exp/>");
		sb.append("<Component Name:-"+componentName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	private static String formatCatsExpression(String logic, String compName, String expName) {
		logic = logic.substring(logic.indexOf('='), logic.indexOf(';'));
		String res = StringUtils.substringBetween(logic, "(", ")");
		res = res.replace(",", " idwpipe " + compName + ".");
		logic = logic.replace("CATS(", "idwLTRIM(idwRTRIM(").replace("CATS (", "idwLTRIM(idwRTRIM(");
		System.out.println(logic);

		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Expression/>");
		sb.append("<Expression:-idwLTRIM(idwRTRIM((" + compName + "." + res + ") type:-Exp/>");
		sb.append("<Component Name:-" + compName + "/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	public static String formatAggTransformationLogic(String logic,String var,String aggName,List<String> groupByList) {
		String transRule = "";
		if (logic.contains("MAX(")||logic.contains("MAX (")) {
			transRule = formatMaximumAggregator(var,aggName,groupByList);
		}else if (logic.contains("MIN(")||logic.contains("MIN (")) {
			transRule = formatMinimumAggregator(var,aggName,groupByList);
		}else if (logic.contains("COUNT(")||logic.contains("COUNT (")) {
			transRule = formatCountAggregator(var,aggName,groupByList);
		} else if (logic.contains("SUM(")||logic.contains("SUM (")) {
			transRule = formatSumAggregator(var, aggName, groupByList);
		}
		return transRule;
	}
	

	private static String formatSubstringExpression(String logic, String expName,String compName) {
		//SUBSTRING (ERR_TKT.TKT_NO FROM 1 FOR 3)
		logic = logic.replace("FROM", ",").replace("FOR", ",");
		String val = logic.substring(logic.indexOf("(")+1, logic.indexOf(")"));
		StringBuilder sb = new StringBuilder("");
		if(logic.contains(",")) {
		String param[] = val.split("[,]");
		sb.append("<Rule1:-Substring/>");
		sb.append("<var:-"+compName+" type:-DE/>");
		if(param.length>2) {
			sb.append("<Starting Position:-"+param[1]+" type:-NC/>");
			sb.append("<Number of Characters:-"+param[2]+" type:-NC/>");
		}
		sb.append("<Component Name:-"+expName+"/>");
		sb.append("</Rule1>");
		}
		return sb.toString();
	}
	
	public static String formatDefaultExpression(String exp,String componentName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Expression/>");
		sb.append("<Expression:-"+exp+" type:-DE/>");
		sb.append("<Component Name:-"+componentName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	
	public static String formatIfCondition(String logic,String componentName,List<LineageReportDTO> lineageIfDtos,Map<String, String> expressionMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Expression/>");
		
		String ifStatement[] = logic.split("ELSE");
		StringBuffer formLineage = new StringBuffer("");
		String prevForm = "";
		for (int i=ifStatement.length-1;i>=0;i--) {
			String s = ifStatement[i];
			
			String temp1 = "";
			if(s.contains("IF")) {
			try {
				temp1 = s.substring(s.indexOf("IF")+2,s.indexOf("THEN"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			s=s.replace(temp1, "");
			formLineage.append("idwiif");
			formLineage.append("(");
			String temp="";
			try {
				temp = s.substring(s.indexOf("=")+1,s.indexOf(";"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			formLineage.append(temp1+","+temp+","+prevForm);
			formLineage.append(")");
			} else {
				String temp="";
				try {
					temp = s.substring(s.indexOf("=")+1,s.indexOf(";"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				formLineage.append(temp);
			}
			
			prevForm = formLineage.toString();
			formLineage = new StringBuffer("");
			if(i==0) {
				//System.out.println("");
			}
		}
		prevForm = prevForm.replace("^=", "!=");
		prevForm = prevForm.replace("LENGTH", "idwLENGTH");
		
		
		for (String key : expressionMap.keySet()) {
			if(prevForm.contains(key+DOT)) {
				prevForm = prevForm.replace(key+DOT, expressionMap.get(key)+DOT);
			}
		}
		
		sb.append("<Expression:-"+prevForm+" type:-Exp/>");
		sb.append("<Component Name:-"+componentName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	
	public static String formatCaseWhenCondition(String logic,String componentName,Map<String, String> expressionMap) {
		//logic = "CASE WHEN PAYMNT_TYPE_CD LIKE 'EX' THEN 'EX' WHEN PAYMNT_TYPE_CD LIKE 'EF' THEN 'EX' WHEN PAYMNT_TYPE_CD LIKE 'CA' THEN 'CA' WHEN PAYMNT_TYPE_CD IN ( 'VD', 'CX') THEN 'VD' WHEN PAYMNT_TYPE_CD IN ( 'CC') THEN 'CC' WHEN PAYMNT_TYPE_CD LIKE 'MS' THEN 'CC' ELSE 'OT' END";
		//logic = logic.replace("CASE", "");
		//String cond[] = logic.split("WHEN");
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Check - Condition/>");
		String condition = logic.substring(logic.indexOf("CASE WHEN"), logic.indexOf("THEN")).replace("CASE WHEN", "").trim();
		sb.append("<Condition:-"+condition+" type:-Exp/>");
		String truePart = logic.substring(logic.indexOf("THEN"), logic.indexOf("ELSE")).replace("THEN", "");
		sb.append("<Return Value If True:-"+truePart+" type:-Exp/>");
		String falsePart = logic.substring(logic.indexOf("ELSE"), logic.indexOf("END")).replace("ELSE", "");
		sb.append("<Return Value If False:-"+falsePart+" type:-Exp/>");
		sb.append("<Component Name:-"+componentName+"/>");
		sb.append("</Rule1>");
		String prevForm = sb.toString();
		for (String key : expressionMap.keySet()) {
			if(prevForm.contains(key+DOT)) {
				prevForm = prevForm.replace(key+DOT, expressionMap.get(key)+DOT);
			}
		}
		return prevForm;
	}
	
	public static String formatGeneralExpression(String logic,String componentName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Expression/>");
		sb.append("<Expression:-"+logic+"type:-Exp/>");
		sb.append("<Component Name:-"+componentName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	public static String formatDatePart(String logic,String expName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Get Date/Time Part/>");
		sb.append("<Date Part:-"+logic+" type:-DE/>");
		sb.append("<Component Name:-"+expName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	
	
	public static String formatSumAggregator(String var,String aggName,List<String> groupByList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Sum/>");
		sb.append("<var:-"+var+" type:-DE/>");
		for (String s : groupByList) {
			sb.append("<Group By:-"+s+" type:-DE/>");
		}
		sb.append("<Component Name:-"+aggName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	
	public static String formatCountAggregator(String var,String aggName,List<String> groupByList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Count/>");
		sb.append("<var:-"+var+" type:-DE/>");
		for (String s : groupByList) {
			sb.append("<Group By:-"+s+" type:-DE/>");
		}
		sb.append("<Component Name:-"+aggName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	public static String formatSumAggregatorDeprecated(String var,String aggName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Sum/>");
		sb.append("<var:-"+var+" type:-DE/>");
		sb.append("<Component Name:-"+aggName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}

	
	public static String formatTrimExpression(String expName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Trim Leading and Trailing Spaces/>");
		sb.append("<var:-BOND_SRC.CLASS_CODE_ID type:-DE/>");
		sb.append("<Component Name:-"+expName+"/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	public static String formatLowercaseExpression(String expName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Rule1:-Convert to Lowercase/>");
		sb.append("<var:-BOND_SRC.BOND_NUMBER type:-DE/>");
		sb.append("<Component Name:-EXP_CLASS_CODE_SURETY/>");
		sb.append("</Rule1>");
		return sb.toString();
	}
	
	
	public static String formatSourceTransformation(String tableName,String catalog) {
		
		if(tableName.endsWith("_txt")) {
			tableName = tableName.replace("_txt", "");
			catalog = CATALOG_FILE;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<Catalog: "+catalog+"/>");
		sb.append("<Entity: "+tableName+"/>");
		return sb.toString();
	}
	
	
	public static String formatSorterTransformation(List<LineageReportDTO> orderByList) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Distinct: No/>");
		for (LineageReportDTO dto : orderByList) {
			String orderByType ="-Ascending";
			if(dto.getOperationType().equalsIgnoreCase("DESCENDING")) {
				orderByType = "-Descending";
			}
			String table = "";
			if(dto.getSourceSchema().equalsIgnoreCase(WORK)) {
				table = EXP_TEMP+dto.getSourceTable();
			} else {
				table = dto.getSourceTable()+SRC;
			}
			sb.append("<Sortkey: "+table+DOT+dto.getSourceColumn()+orderByType+"/>");
		}
		
		return sb.toString();
	}
	
	
	public static String formatTargetTransformation(String tableName,String catalog,int counter) {
		
		if(tableName.endsWith("_txt")) {
			tableName = tableName.replace("_txt", "");
			catalog = CATALOG_FILE;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<Catalog: "+catalog+"/>");
		sb.append("<Entity: "+tableName+"/>");
		sb.append("<LoadStrategy: truncate and load/>");
		sb.append("<LoadOrder: "+counter+"/>");
		return sb.toString();
	}
	
	
	public static String formatJoinerTransformation(String joinType, String masCompName,String masElementName,String detailCompName, String detailElementName) {
		StringBuilder sb = new StringBuilder();
		sb.append("<Type: "+ joinType +"/>");
		sb.append("<SortedInputs: No/>");
		sb.append("<MasterCompName: "+masCompName.trim()+"/>");
		sb.append("<MasterElementName: "+masElementName.trim()+"/>");
		sb.append("<DetailCompName: "+detailCompName.trim()+"/>");
		sb.append("<DetailElementName: "+detailElementName.trim()+"/>");
		return sb.toString();
	}
	
	
	
	public static String formatMultiJoinerTransformation1(String masCompName, List<LineageReportDTO> dtos) {
		StringBuilder sb = new StringBuilder();
		sb.append("<MasterComponent: "+masCompName+"/>");
		for (LineageReportDTO dto : dtos) {
			sb.append("<MultiJoiner>");
			sb.append("<Type: "+IDWTemplatePopulationHelper.setJoinType(dto.getStatementType())+"/>");
			sb.append("<SortedInputs: No/>");
			String name[] = dto.getTransformationLogic().split("=");
			String rightTableColumn[] = name[1].split("[.]");
			//sb.append(","+rightTableColumn[0]);
			sb.append("<DetailComponent: "+rightTableColumn[0].trim()+SRC+"/>");
			String temp = dto.getTransformationLogic().replace(DOT, SRC+DOT);
			sb.append("<JoinCondition: "+temp+"/>");
			sb.append("</MultiJoiner>");
		}
		return sb.toString();
	}
	
	public static String formatMultiJoinerTransformation(String masCompName, List<LineageReportDTO> dtos,Map<String, String> expressionMap) {
		StringBuilder sb = new StringBuilder();
		Set<String> multiJoinerFilter = new HashSet<String>();
		sb.append("<MasterComponent: "+masCompName+"/>");
		for (LineageReportDTO dto : dtos) {
			if(!multiJoinerFilter.contains(dto.getTransformationLogic())) {
			sb.append("<MultiJoiner>");
			sb.append("<Type: "+IDWTemplatePopulationHelper.setJoinType(dto.getStatementType())+"/>");
			sb.append("<SortedInputs: No/>");
			String join[] = dto.getTransformationLogic().split(" AND | OR ");
			for(int i=0;i<join.length;i++) {
					multiJoinerFilter.add(dto.getTransformationLogic());
					if(!join[i].contains("<") && !join[i].contains(">")) {
						String name[] = join[i].split("=");
						String leftTableColumn[] = name[0].split("[.]");
						String rightTableColumn[] = name[1].split("[.]");
						//sb.append(";" + fetchTemporaryTable(expressionMap,rightTableColumn[0].trim()));
						sb.append("<DetailComponent: "+fetchTemporaryTable(expressionMap,rightTableColumn[0].trim())+"/>");
						join[i] = join[i].replaceAll(rightTableColumn[0].trim(), fetchTemporaryTable(expressionMap,rightTableColumn[0].trim()));
						join[i] = join[i].replaceAll(leftTableColumn[0].trim(), fetchTemporaryTable(expressionMap,leftTableColumn[0].trim()));
						
						String temp = join[i].replace(DOT, SRC+DOT);
						sb.append("<JoinCondition: "+temp+"/>");
					} 
			}
			sb.append("</MultiJoiner>");
			}
		}
		return sb.toString();
	}
	
	
	
	public static String formatMappletTransformation() {
		StringBuilder sb = new StringBuilder();
		sb.append("<Type: Mapplet/>");
		sb.append("<Name: PROC_FORMAT/>");
		sb.append("<InputElements: dummy string(100,0)/>");
		sb.append("<OutputElements: dummy string(100,0)/>");
		return sb.toString();
	}

	
	
	public static String formatFilterTransformation(LineageReportDTO dto) {
		
		
		String filter = dto.getTransformationLogic();
		if(dto.getOperationType().equalsIgnoreCase("JOIN")) {
			String join[] = dto.getTransformationLogic().split(" AND | OR ");
			for(int i=0;i<join.length;i++) {
					//multiJoinerFilter.add(dto.getTransformationLogic());
					if(join[i].contains("<") || join[i].contains(">") || join[i].toLowerCase().contains("like")) {
						//filter logic captured here
						filter = dto.getTransformationLogic().substring(dto.getTransformationLogic().indexOf(join[i]),dto.getTransformationLogic().length());
						break;
					}
				}	
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("<Condition: ");
		String s[] = filter.split("[ ]");
		for (int i = 0; i < s.length; i++) {
			if(s[i].equalsIgnoreCase("IN")) {
				if(s[i-1].equalsIgnoreCase("NOT")) {
					s[i+1] = s[i+1]+s[i-2]+",";
					s[i-2]="";
				} else {
					s[i+1] = s[i+1]+s[i-1]+",";
					s[i-1]="";
				}
				s[i] = "idwIN";
			} else if(s[i].equalsIgnoreCase("NOT")) {
				s[i] = "idwNOT";
			} else if(s[i].equalsIgnoreCase("AND")) {
				s[i] = "idwAND";
			} else if(s[i].equalsIgnoreCase("OR")) {
				s[i] = "idwOR";
			}
			
		}  
		StringBuffer sbuf = new StringBuffer();
		for (String string : s) {
			sbuf.append(string).append(" ");
		}
		sb.append(sbuf.toString());
		sb.append("/>");
		return sb.toString();
	}
	public static String formatIntnxFilterTransformation(LineageReportDTO dto,String expName,String comp) {
		String logic=dto.getTransformationLogic();
		StringBuilder sb = new StringBuilder();
		sb.append("<Condition: ");
		logic=logic.substring(logic.indexOf('(')+1,logic.length());
		String splitLogic[] = logic.split("=");
		if(splitLogic.length>0) {
			String firstHalf=splitLogic[0];
			sb.append(expName+firstHalf+"="+comp+"."+dto.getTargetColumn());
		}
		sb.append("/>");
		return sb.toString();
	}
	//Code Changes for couple of Expression Started
		public static String formatLowercaseExpression(String expName,String compName) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Convert to Lowercase/>");
			sb.append("<var:-"+compName+" type:-DE/>");
			sb.append("<Component Name:-"+expName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		
		public static String formatUppercaseExpression(String expName,String compName) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Convert to Uppercase/>");
			sb.append("<var:-"+compName+" type:-DE/>");
			sb.append("<Component Name:-"+expName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		
		public static String formatRoundExpression(String logic, String expName,String compName) {
			String val = logic.substring(logic.indexOf("(")+1, logic.indexOf(")"));
			String Precision[]=val.split(",");
			StringBuilder sb= new StringBuilder();
			sb.append("<Rule1:-Round/>");
			if(Precision.length>1) {
				sb.append("<Precision:-("+expName+ ", "+Precision[1]+") type:-Exp/>");
			}
			sb.append("<Component Name:-"+compName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		public static String formatTrimExpression(String expName,String compName) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Trim Leading and Trailing Spaces/>");
			sb.append("<var:-"+compName+" type:-DE/>");
			sb.append("<Component Name:-"+expName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		
		public static String formatMaximumAggregator(String var,String aggName,List<String> groupByList) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Maximum/>");
			sb.append("<var:-"+var+" type:-DE/>");
			for (String s : groupByList) {
				sb.append("<Group By:-"+s+" type:-DE/>");
			}
			sb.append("<Component Name:-"+aggName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		
		public static String formatMinimumAggregator(String var,String aggName,List<String> groupByList) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Minimum/>");
			sb.append("<var:-"+var+" type:-DE/>");
			for (String s : groupByList) {
				sb.append("<Group By:-"+s+" type:-DE/>");
			}
			sb.append("<Component Name:-"+aggName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		
		public static String formatRtrimExpression(String compName, String expName) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Trim Trailing Spaces/>");
			sb.append("<var:-"+expName+" type:-DE/>");
			sb.append("<Component Name:-"+compName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}

		public static String formatTranslateExpression(String logic, String expName,String compName) {
			StringBuilder sb = new StringBuilder();
			String val = logic.substring(logic.indexOf("(")+1, logic.indexOf(")"));
			
			String values[]=val.split(",");
			sb.append("<Rule1:-Replace String/>");
			if(values.length>1) {
				sb.append("<Old String:-"+expName+"."+values[2].trim() +" type:-DE/>");
				sb.append("<Old String:-"+expName+"."+values[1].trim() +" type:-DE/>");
			}
			sb.append("< Case Sensitivity Flag:-NO type:-SC/>");
			sb.append("<Component Name:-"+compName+"/>");
			sb.append("</Rule1>");
			return sb.toString();
		}
		
		
		public static String formatSQLOverideSource(String code,String catalogName, Set<String> overrideSet) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Catalog: "+catalogName+"/>");
			sb.append("<Entity: SQL_OVERRIDE0/>");
			String primaryEntity = "";
			for (String string : overrideSet) {
				primaryEntity = string;
				break;
			}
			sb.append("<PrimaryEntity: "+primaryEntity+"/>");
			sb.append("<SqlOverride: "+code+"/>");
			for (String table : overrideSet) {
				sb.append("<AssociatedSource>");
				sb.append("<SrcName: "+table+"/>");
				sb.append("<SrcCatalog: "+catalogName+"/>");
				sb.append("<SrcEntity: "+table+"/>");
				sb.append("<isPrimary: No/>");
				sb.append("</AssociatedSource>");
			}
			return sb.toString();
		}

		//Code Changes for couple of Expression ended
		
		private static String fetchTemporaryTable(Map<String, String> componentLink, String tableName) {
			String compName = "";
			if(componentLink.containsKey(tableName)) {
				compName = componentLink.get(tableName);
			} else {
				compName = tableName+SRC;
			}
			return compName;
		}
		
		
		public static String formatUnionTransformation(List<TransformationDTO> list) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Type: Union ALL/>");
			for (TransformationDTO dto : list) {
				sb.append("<"+dto.getComponentName().replace(SRC, "")+":"+dto.getComponentName()+"/>");
			}
			return sb.toString();
		}
		
		
		public static String formatUnionExpression(String table,String unionName) {
			StringBuilder sb = new StringBuilder();
			sb.append("<Rule1:-Special - Union/>");
			String names[] = table.split("[,]");
			for (int i = 0; i < names.length; i++) {
				sb.append("<var:-"+names[i]+" type:-DE/>");
			}
			sb.append("<Component Name:-"+unionName+"/></Rule1>");
			return sb.toString();
		}
}