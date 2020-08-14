package com.tom_roush.pdfbox.io;

import java.io.IOException;

/**
 * An interface to allow PDF files to be stored completely in memory or
 * to use a scratch file on the disk.
 *
 * @author Ben Litchfield
 */
public interface RandomAccess extends RandomAccessRead
{

    /**
     * Write a byte to the stream.
     *
     * @param b The byte to write.
     * @throws IOException If there is an IO error while writing.
     */
    void write(int b) throws IOException;

    /**
     * Write a buffer of data to the stream.
     *
     * @param b The buffer to get the data from.
     * @param offset An offset into the buffer to get the data from.
     * @param length The length of data to write.
     * @throws IOException If there is an error while writing the data.
     */
    void write(byte[] b, int offset, int length) throws IOException;
}
