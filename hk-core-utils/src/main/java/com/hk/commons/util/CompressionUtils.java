package com.hk.commons.util;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.*;

/**
 * @author kevin
 * @date 2019-5-6 18:06
 */
@Slf4j
public class CompressionUtils {

    private static final int INFLATED_ARRAY_LENGTH = 10000;

    /**
     * Deflate the given bytes using zlib.
     *
     * @param bytes the bytes
     * @return the converted string
     */
    public static String deflate(final byte[] bytes) {
        final var data = new String(bytes, StandardCharsets.UTF_8);
        return deflate(data);
    }

    /**
     * Deflate the given string via a {@link java.util.zip.Deflater}.
     *
     * @param data the data
     * @return base64 encoded string
     */
    public static String deflate(final String data) {
        final var deflater = new Deflater();
        deflater.setInput(data.getBytes(StandardCharsets.UTF_8));
        deflater.finish();
        final var buffer = new byte[data.length()];
        final var resultSize = deflater.deflate(buffer);
        final var output = new byte[resultSize];
        System.arraycopy(buffer, 0, output, 0, resultSize);
        return Base64Utils.encodeToString(output);
    }

    /**
     * Inflate the given byte array by {@link #INFLATED_ARRAY_LENGTH}.
     *
     * @param bytes the bytes
     * @return the array as a string with {@code UTF-8} encoding
     */
    public static String inflate(final byte[] bytes) {
        final var inflater = new Inflater(true);
        final var xmlMessageBytes = new byte[INFLATED_ARRAY_LENGTH];

        final var extendedBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, extendedBytes, 0, bytes.length);
        extendedBytes[bytes.length] = 0;
        inflater.setInput(extendedBytes);

        try {
            final var resultLength = inflater.inflate(xmlMessageBytes);
            inflater.end();
            if (!inflater.finished()) {
                throw new RuntimeException("buffer not large enough.");
            }
            inflater.end();
            return new String(xmlMessageBytes, 0, resultLength, StandardCharsets.UTF_8);
        } catch (final DataFormatException e) {
            return null;
        }
    }

    /**
     * First decode base64 String to byte array, then use ZipInputStream to revert the byte array to a
     * string.
     *
     * @param zippedBase64Str the zipped base 64 str
     * @return the string, or null
     */
    @SneakyThrows
    public static String decompress(final String zippedBase64Str) {
        final var bytes = Base64Utils.decodeFromString(zippedBase64Str);
        @Cleanup final var zi = new GZIPInputStream(new ByteArrayInputStream(bytes));
        return IOUtils.toString(zi, Charset.defaultCharset());
    }

    /**
     * Use ZipOutputStream to zip text to byte array, then convert
     * byte array to base64 string, so it can be transferred via http request.
     *
     * @param srcTxt the src txt
     * @return the string in UTF-8 format and base64'ed, or null.
     */
    @SneakyThrows
    public static String compress(final String srcTxt) {
        @Cleanup final var rstBao = new ByteArrayOutputStream();
        @Cleanup final var zos = new GZIPOutputStream(rstBao);
        zos.write(srcTxt.getBytes(StandardCharsets.UTF_8));
        zos.flush();
        zos.finish();
        final var bytes = rstBao.toByteArray();
        final var base64 = StringUtils.remove(Base64Utils.encodeToString(bytes), '\0');
        return new String(StandardCharsets.UTF_8.encode(base64).array(), StandardCharsets.UTF_8);
    }

    /**
     * Decode the byte[] in base64 to a string.
     *
     * @param bytes the data to encode
     * @return the new string
     */
    public static String decodeByteArrayToString(final byte[] bytes) {
        final var bais = new ByteArrayInputStream(bytes);
        final var baos = new ByteArrayOutputStream();
        final var buf = new byte[bytes.length];
        try (var iis = new InflaterInputStream(bais)) {
            var count = iis.read(buf);
            while (count != -1) {
                baos.write(buf, 0, count);
                count = iis.read(buf);
            }
            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } catch (final Exception e) {
            log.error("Base64 decoding failed", e);
            return null;
        }
    }
}
