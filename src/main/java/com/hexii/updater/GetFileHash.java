package com.hexii.updater;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GetFileHash {

    // Gets the 32-bit MurmurHash2 of the .jar file
    public static long getFileHash(Path path) throws IOException {

	// Reads .jar file and turns it into byte[]
	byte[] fileByteArray = Files.readAllBytes(path);

	// ArrayList with no whitespace
	List<Byte> noWs = new ArrayList<Byte>();

	// Remove any whitespace characters
	for (byte b : fileByteArray) {
	    if (!isWhitespaceCharacter(b)) {
		noWs.add(b);
	    }
	}

	// Convert Byte ArrayList to byte array
	byte[] result = new byte[noWs.size()];
	for (int i = 0; i < noWs.size(); i++) {
	    result[i] = noWs.get(i).byteValue();
	}

	return MurmurHash2.hash(result, 1);

    }

    // Determines if the byte is a whitespace character (look at an ASCII table)
    private static boolean isWhitespaceCharacter(byte b) {
	return b == 9 || b == 10 || b == 13 || b == 32;
    }

}
