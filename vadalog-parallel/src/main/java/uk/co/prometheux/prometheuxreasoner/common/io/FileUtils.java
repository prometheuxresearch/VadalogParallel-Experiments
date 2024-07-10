package uk.co.prometheux.prometheuxreasoner.common.io;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import uk.co.prometheux.prometheuxreasoner.PrometheuxRuntimeException;

/**
 * A util class to delete files locally or in HDFS
 * 
 * Copyright (C) Prometheux Limited. All rights reserved.
 * @author Prometheux Limited
 */

public class FileUtils {

	public static void deleteDirInHDFS(String dirToDelete) {
        Configuration conf = new Configuration();
        FileSystem fs;
		try {
			fs = FileSystem.get(conf);
	        Path path = new Path(dirToDelete);
			fs.delete(path, true);
		} catch (IOException e) {
			throw new PrometheuxRuntimeException("It is not possible to delete the file "+dirToDelete+", since it is not present in HDFS");
		}		
	}

	public static void deleteDirInLocal(String dirToDelete) {
		File directoryToBeDeleted = new File(dirToDelete);
		File checkPointDirFile = directoryToBeDeleted;
		if (checkPointDirFile.exists()) {
			deleteDirectoryRecursive(directoryToBeDeleted);
		}
	}

	private static void deleteDirectoryRecursive(File directoryToBeDeleted) {
		File[] allContents = directoryToBeDeleted.listFiles();
		if (allContents != null) {
			for (File file : allContents) {
				deleteDirectoryRecursive(file);
			}
		}
		directoryToBeDeleted.delete();
	}
	
	public static boolean existsFileInHdfs(String pathToFile) {
        Configuration conf = new Configuration();
        FileSystem fs;
		try {
			fs = FileSystem.get(conf);
	        Path path = new Path(pathToFile);
			return fs.exists(path);
		} catch (IOException e) {
			throw new PrometheuxRuntimeException("It is not possible to check existence of the path "+pathToFile);
		}	
	}
	
	public static boolean existsFileInLocal(String pathToFile) {
		File file = new File(pathToFile);
		if (file.exists()) {
			return true;
		}
		return false;
	}

}
