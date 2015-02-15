package com.bistrov.bloomfilter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

import static java.util.Arrays.copyOfRange;

/**
 * Simple Bloom filter implementation that uses java.util.BitSet to store bits
 * and java.security.MessageDigest for hashing.
 * MD5 algorithm used as default.
 *
 * Wiki:
 * A Bloom filter is a space-efficient probabilistic data structure,
 * that is used to test whether an element is a member of a set
 */
public class BloomFilter {

    private MessageDigest digest;
    private int numberOfChunks = 1;

    private BitSet bitSet = new BitSet();

    public BloomFilter() {
        try {
            init(MessageDigest.getInstance("MD5"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor that accepts java.security.MessageDigest to work with
     *
     * @param digest MessageDigest to use for hashing
     */
    public BloomFilter(MessageDigest digest) {
        init(digest);
    }

    /**
     * Initializes BloomFilter, by validating MessageDigest and calculating number of chunks
     * Chunk is a byte array that represents integer value
     *
     * @param digest MessageDigest to use for hashing
     */
    private void init(MessageDigest digest) {
        if (digest == null) {
            throw new IllegalArgumentException("Digest cannot be null");
        }
        this.digest = digest;
        if (digest.getDigestLength() > 4) {
            numberOfChunks = digest.getDigestLength() / 4;
        }
    }

    /**
     * Consumes array of bytes to generate hash. Resulted hash is divided into chunks depending on
     * hashing algorithm. Chunks then converted to integer values assumed as an indexes
     * in BitSet which need to be set to true
     *
     * @param bytes element bytes to add to filter
     */
    public void add(byte[] bytes) {
        digest.update(bytes);
        byte[] bytesFromDigest = digest.digest();
        for (int i = 0; i < numberOfChunks; i++) {
            byte[] chunk = copyOfRange(bytesFromDigest, i * 4, (i * 4) + 4);
            int chunkHash = toInt(chunk);
            bitSet.set(chunkHash);
        }
    }

    private int toInt(byte[] chunk) {
        int chunkHash = chunk[0] << 24
                | chunk[1] << 16
                | chunk[2] << 8
                | chunk[3];
        chunkHash &= Integer.MAX_VALUE;
        return chunkHash;
    }

    /**
     * Checks whether or not element is in set
     *
     * @param bytes element bytes to check
     * @return return true if element possibly in set, return false if element definitely not in set
     */
    public boolean probablyContains(byte[] bytes) {
        digest.update(bytes);
        byte[] bytesFromDigest = digest.digest();
        for (int i = 0; i < bytesFromDigest.length / numberOfChunks; i++) {
            byte[] chunk = copyOfRange(bytesFromDigest, i * numberOfChunks, (i * numberOfChunks) + numberOfChunks);
            int chunkHash = toInt(chunk);
            if (!bitSet.get(chunkHash)) {
                return false;
            }
        }
        return true;
    }

    int getNumberOfChunks() {
        return numberOfChunks;
    }

}
