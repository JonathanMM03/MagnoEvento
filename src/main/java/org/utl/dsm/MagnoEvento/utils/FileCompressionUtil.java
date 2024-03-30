package org.utl.dsm.MagnoEvento.utils;

import java.io.*;
import java.util.zip.*;

public class FileCompressionUtil {

    public byte[] compressFile(byte[] input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(input);
            gzipOS.flush();
        }
        return bos.toByteArray();
    }

    public byte[] decompressFile(byte[] input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ByteArrayInputStream bis = new ByteArrayInputStream(input);
             GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        }
        return bos.toByteArray();
    }
}
