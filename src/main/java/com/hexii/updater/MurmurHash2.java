/**
 *
 * murmurhash - Pure Java implementation of the Murmur Hash algorithms. Copyright (c) 2014, Sandeep
 * Gupta
 * 
 * http://sangupta.com/projects/murmur
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 */

package com.hexii.updater;

public final class MurmurHash2 {

  // Helps convert integer to its unsigned value
  private static final long UINT_MASK = 0xFFFFFFFFL;

  // Helps convert a byte into its unsigned value
  private static final int UNSIGNED_MASK = 0xff;

  private MurmurHash2() {}

  public static long hash(byte[] data, long seed) {

    final int length = data.length;
    final long M = 0x5bd1e995L;
    final int R = 24;

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

      k = ((k * M) & UINT_MASK);
      k ^= ((k >>> R) & UINT_MASK);
      k = ((k * M) & UINT_MASK);

      hash = ((hash * M) & UINT_MASK);
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
        hash = ((hash * M) & UINT_MASK);
    }

    hash ^= ((hash >>> 13) & UINT_MASK);
    hash = ((hash * M) & UINT_MASK);
    hash ^= hash >>> 15;

    return hash;
  }

}
