package com.zdlc.idw.sas;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zdlc.idw.sas.dto.SchemaDTO;

public class FetchSchemaDetails {

	public  List<SchemaDTO> FetchSchema() throws IOException { 
		List<SchemaDTO>  schemalist=new ArrayList<SchemaDTO>();
		try {
			Properties prop = new Properties();
			try {
			//Path path = Paths.get(filePath);
			File childFile = new File("../conf/DIWconfig.config");
			FileInputStream input = new FileInputStream(childFile);
			prop.load(input);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
			//File childFile = new File("../conf/DIWconfig.config");
			
		    String inputfilepath = prop.getProperty("schemaPath");
			FileInputStream file = new FileInputStream(new File(inputfilepath));
		    XSSFWorkbook workbook = new XSSFWorkbook(file);
		    XSSFSheet sheet = workbook.getSheetAt(0);
		    Iterator < Row > rowIterator = sheet.iterator();
		    rowIterator.next();
		    
		    while (rowIterator.hasNext()) {
		        Row row = rowIterator.next();
		        SchemaDTO schemaDto=new SchemaDTO();
		        Iterator < Cell > cellIterator = row.cellIterator();
		        schemaDto.setDatabase(cellIterator.next().getStringCellValue());
		        schemaDto.setSchema(cellIterator.next().getStringCellValue());
		        schemaDto.setSystemEntityName(cellIterator.next().getStringCellValue());
		        //System.out.println(schemaDto.getSystemEntityName());
		        schemaDto.setDataElementName(cellIterator.next().getStringCellValue());
		        schemaDto.setDataElementBusinessName(cellIterator.next().getStringCellValue());
		        schemaDto.setDataElementDescription(cellIterator.next().getStringCellValue());
		        schemaDto.setDatatype(fetchCellValue(cellIterator.next()));
		        try {
					schemaDto.setLength(fetchCellValue(cellIterator.next()));
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		        try {
					schemaDto.setPrecision(cellIterator.next().getStringCellValue());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        schemaDto.setScale(cellIterator.next().getStringCellValue());
		        schemaDto.setNullable(cellIterator.next().getStringCellValue());
		        schemaDto.setDataFormat(cellIterator.next().getStringCellValue());
		        schemaDto.setColumnPosition(fetchCellValue(cellIterator.next()));
		        try {
					schemaDto.setKeyType(cellIterator.next().getStringCellValue());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        schemaDto.setCharacterSetName(cellIterator.next().getStringCellValue());
		        try {
					schemaDto.setCollationName(cellIterator.next().getStringCellValue());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        schemalist.add(schemaDto);
		    }

		   } catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
			System.out.println("Sucessfully read the Schema file !!!");
			return schemalist; 
	}
	
	private static String fetchCellValue(Cell cell) {
        String cellValue = null;
        if (cell != null) {
            CellType cellType = cell.getCellTypeEnum();
            if (cellType.toString().equalsIgnoreCase("STRING")) {
                cellValue = cell.getStringCellValue();
            } else if (cellType.toString().equalsIgnoreCase("NUMERIC")) {
                int num = (int)cell.getNumericCellValue();
                cellValue = Integer.toString(num);
            }else if (cellType.toString().equalsIgnoreCase("BOOLEAN")) {
                boolean num = cell.getBooleanCellValue();
                cellValue = Boolean.toString(num);
            }
            else {
                cellValue = cell.getStringCellValue();
            }
        } else {
            cellValue = "";
        }
        return cellValue;
        
    }
}
