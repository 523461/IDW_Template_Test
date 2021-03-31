package com.zdlc.idw.sas;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zdlc.idw.sas.dto.SchemaDTO; 
public class SchemaToIDW {
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		Map<String, List<String>> TableDetailsMap =new HashMap<String, List<String>>();
		  List<String> tempList =new ArrayList<String>();
		  tempList.add("BOND_ID");
		  tempList.add("BOND_NUMBER");
		  tempList.add("BOND_EFF_DATE");
		  tempList.add("BOND_EXP_DATE");
		  tempList.add("BOND_AMT");
		  tempList.add("BOND_STATE_CODE");
		  tempList.add("PRINCIPAL_ID");
		  tempList.add("LU_MIGRATE_SOURCE");
		  tempList.add("LU_SOURCE");
		  tempList.add("LU_RATE_TYPE");
		  tempList.add("LU_BUSINESS_SEGMENT");
		  TableDetailsMap.put("ADMVSTR.BOND", tempList);
		
		 
		 SchemaToIDW demoIDW = new SchemaToIDW();
		 //Map<String, List<SchemaDTO>>  schemamap= demoIDW.getSchemaDetails(TableDetailsMap);
		 Map<String, SchemaDTO> schemaListMap = demoIDW.getSchemaDetails();
	}
	
	public Map<String,SchemaDTO> getSchemaDetails() {
		
		Map<String, SchemaDTO> schemaListMap = new HashMap<String,SchemaDTO>();
		FetchSchemaDetails fetchSchema = new FetchSchemaDetails();
		List<SchemaDTO> schemalist = new ArrayList<SchemaDTO>();
		try {
			schemalist = fetchSchema.FetchSchema();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (SchemaDTO schemaDTO : schemalist) {
			schemaListMap.put((schemaDTO.getSystemEntityName()+schemaDTO.getDataElementName()).toUpperCase(), schemaDTO);
		}
		return schemaListMap;
	}
	
	/*public  Map<String, List<SchemaDTO>> getSchemaDetails(Map<String, List<String>> TableDetailsMap) {
		FetchSchemaDetails fetchSchema = new FetchSchemaDetails();
		List<SchemaDTO> schemalist = new ArrayList<SchemaDTO>();
		try {
			schemalist = fetchSchema.FetchSchema();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, List<SchemaDTO>> SchemaMap =new HashMap<String, List<SchemaDTO>>();
		for (Entry<String, List<String>> set : TableDetailsMap.entrySet()) {
			List<SchemaDTO> schemas=new ArrayList<SchemaDTO>();
			for(SchemaDTO sche:schemalist) {
				String tblName[] = set.getKey().split("[|]");
				
				
				  try {
					if(sche.getSystemEntityName().equalsIgnoreCase(tblName[1])) {
						  
						  schemas.add(sche);
						  
					  }
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   }
			SchemaMap.put(set.getKey(),schemas);
		}
		return SchemaMap;
		
		
	}*/

		
	}




