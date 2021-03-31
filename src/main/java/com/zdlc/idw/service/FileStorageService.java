package com.zdlc.idw.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.zdlc.idw.exception.FileStorageException;
import com.zdlc.idw.exception.MyFileNotFoundException;
import com.zdlc.idw.property.FileStorageProperties;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    public List<File> fetchFileFromMultipart(MultipartFile[] files) {
    	List<File> retVal = new ArrayList<File>();
    	for(MultipartFile file : files) {
    		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    	      
    	        try {
    	            if(fileName.contains("..")) {
    	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
    	            }

    	            Path targetLocation = this.fileStorageLocation.resolve(fileName);
    	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
    	            retVal.add(targetLocation.toFile());
    	            
    	        } catch (IOException ex) {
    	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
    	        }
    	}
    	return retVal;
    }
    
    public String storeOutPutFile(File f) {
    	try {
			Files.copy(f.toPath(), this.fileStorageLocation.resolve(f.getName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			 throw new FileStorageException("Could not store file " + f.getName() + ". Please try again!", e);
		}
    	return f.getName();
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}