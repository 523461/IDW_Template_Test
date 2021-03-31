package com.zdlc.idw.sas.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.zdlc.idw.sas.LineageReportConvertor;
import com.zdlc.idw.sas.LineageReportToCSV;

public class DIWTemplateGenerateService {

	private LineageReportConvertor lineageReportConvertor; 
	public List<File> generateDIWReport(List<File> uploadedFiles) throws Exception {
		lineageReportConvertor = new LineageReportConvertor();
		String csvPath="",outputpath="";
		List<File> outputFiles=null;
		for (File lineageFile : uploadedFiles) {
			File cs=new File(lineageFile.getParent()+File.separator+"CSV"+File.separator);
			outputpath=lineageFile.getParent()+File.separator+"OUT"+File.separator;
			cs.mkdir();
			csvPath=cs.getCanonicalPath()+File.separator;
			LineageReportToCSV.convertToCSV(lineageFile,csvPath );
		}
		try {
			 outputFiles=	lineageReportConvertor.generateDIWReport(csvPath,outputpath);
		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error in File Reading !!!");
			e.printStackTrace();
		}
		return outputFiles;
	}
}
