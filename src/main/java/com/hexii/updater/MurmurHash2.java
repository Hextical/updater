/*
 * original code can be found at:
 * https://github.com/sangupta/murmur/blob/master/src/main/java/com/sangupta/murmur/Murmur2.java
 */

package com.hexii.updater;

public class MurmurHash2 {
    
    private MurmurHash2() {}

    // Helps convert long to its unsigned value
    // private static final long LONG_MASK = 0xFFFFFFFFFFFFFFFFL;

    // Helps convert integer to its unsigned value
    private static final long UINT_MASK = 0xFFFFFFFFl;

    // Helps convert a byte into its unsigned value
    private static final int UNSIGNED_MASK = 0xff;

    public static long hash(byte[] data, long seed) {

	final int length = data.length;
	final long m = 0x5bd1e995l;
	final int r = 24;

	// Initialize the hash to a 'random' value
	long hash = ((seed ^ length) & UINT_MASK);

	// Mix 4 bytes at a time into the hash
	int length4 = length >>> 2;
	for (int i = 0; i < length4; i++) {
	    final int i4 = i << 2;

	    long k = (data[i4] & UNSIGNED_MASK);
	    k |= (data[i4 + 1] & UNSIGNED_MASK) << 8;
	    k |= (data[i4 + 2] & UNSIGNED_MASK) << 16;
	    k |= (data[i4 + 3] & UNSIGNED_MASK) << 24;

	    k = ((k * m) & UINT_MASK);
	    k ^= ((k >>> r) & UINT_MASK);
	    k = ((k * m) & UINT_MASK);

	    hash = ((hash * m) & UINT_MASK);
	    hash = ((hash ^ k) & UINT_MASK);
	}

	// Handle the last few bytes of the input array
	int offset = length4 << 2;
	switch (length & 3) {
	case 3:
	    hash ^= ((data[offset + 2] << 16) & UINT_MASK);
	case 2:
	    hash ^= ((data[offset + 1] << 8) & UINT_MASK);
	case 1:
	    hash ^= (data[offset] & UINT_MASK);
	    hash = ((hash * m) & UINT_MASK);
	}

	hash ^= ((hash >>> 13) & UINT_MASK);
	hash = ((hash * m) & UINT_MASK);
	hash ^= hash >>> 15;

	return hash;
    }

}
