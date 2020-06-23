package com.opentext.explore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class FileUtil {
	/**
	 * Get file from classpath, resources folder
	 * SEE: Java – Read a file from resources folder
	 * https://www.mkyong.com/java/java-read-a-file-from-resources-folder/
	 * @param fileName
	 * @return
	 */
	public static File getFileFromResources(String fileName) {
        URL resource = FileUtil.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            return new File(resource.getFile());
        }

    }	
	
	public static boolean isFile(String path) {
		boolean isFile = false;
		
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
		    isFile = true;
		}
		
		return isFile;
	}
	
	public static boolean deleteFile(String path) {
		boolean deleted = false;
		
		File f = new File(path);
		if(f.exists()) { 
		    deleted = f.delete();
		}
		
		return deleted;
	
	}
	
	public static Properties loadProperties(String propFileName) {
		Properties prop = null;
		
		File propFile = FileUtil.getFileFromResources(propFileName); 

		InputStream file;
		try {
			file = new FileInputStream(propFile.getAbsolutePath());
			prop = new Properties();
			prop.load(file);
		} 
		catch (FileNotFoundException e) {
			System.err.println("Properties file not found");
		}
		catch (IOException e) {
			System.err.println("Properties file: " + e.getMessage());
		}
		
		return prop;
	}
}
