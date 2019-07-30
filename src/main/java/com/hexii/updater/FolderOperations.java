package com.hexii.updater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FolderOperations {
    
    private FolderOperations() {}

    private final static String extension = ".jar";

    private static final Logger log = LogManager.getLogger(FolderOperations.class);

    // Converts a folder full of .jar files into List<Long> hashes.
    public static List<Long> folderToHash(String path) throws IOException {

	log.info("Getting folder hashes: " + path);

	List<Path> files = readFolder(path);
	List<Long> hashes = jarHash(files);
	
	log.info("Hash collection complete.");
	
	return hashes;

    }

    // Reads a folder and returns a List<Path> of only .jar files with a depth of
    // 10.
    private static List<Path> readFolder(String path) throws IOException {

	log.info("Reading folder: " + path);

	List<Path> subfolder = Files.walk(Paths.get(path), 10).filter(p -> p.toString().endsWith(extension))
		.collect(Collectors.toList());

	return subfolder;

    }

    // Takes a List<Path> and returns all the hashes associated with those .jar
    // files.
    private static List<Long> jarHash(List<Path> filePaths) throws IOException {

	List<Long> hashesTS = Collections.synchronizedList(new ArrayList<Long>());

	for (Path path : filePaths) {

	    log.info("Computing JAR hash: " + path.getFileName());
	    long hash = GetFileHash.getFileHash(path);
	    log.info("Hash: " + hash);

	    hashesTS.add(hash);

	}

	return hashesTS;

    }

}
