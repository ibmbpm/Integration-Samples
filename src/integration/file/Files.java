/**********************************************************************
 * Licensed Materials - Property of IBM
 * 5737-H41
 * (C) Copyright IBM Corporation 2021. All Rights Reserved.
 * This sample program is provided AS IS and may be used, executed, copied 
 * and modified without royalty payment by customer (a) for its own instruction
 * and study, (b) in order to develop applications designed to run with an IBM
 * product, either for customer's own internal use or for redistribution by
 * customer, as part of such an application, in customer's own products.
 **********************************************************************/
package integration.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class provides a sample implementation for file services.
 */
public class Files {

    private static final String CLASS_NAME = Files.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);
    private static final String lineSeparator = System.getProperties().getProperty("line.separator");

    /**
     * Read data from a file and return it as a String object using the platform's default charset.
     * 
     * @param pathname
     *            The file's path name.
     * @param limit
     *            Throw an IOException if the data read exceeds the specified value. -1 or null
     *            indicates unlimited.
     * @return The file content. A new line is replaced by '\n'.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public static String getTextFromFile(String pathname, Integer limit) throws IOException {
        final String METHOD_NAME = "getTextFromFile";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { pathname, limit });

        File file = new File(pathname);

        if (!file.exists()) {
            IOException ioException = new IOException("File '" + pathname + "' does not exist.");
            logger.throwing(CLASS_NAME, METHOD_NAME, ioException);
            throw ioException;
        }

        RandomAccessFile raf = null;
        String result;

        try {
            raf = new RandomAccessFile(file, "r");
            byte[] data = new byte[(int) file.length()];
            raf.readFully(data);

            // Replace all operating system line separators with '\n'
            result = (new String(data)).replaceAll(lineSeparator, "\n");

            // Check the limit on the amount of data retrieved.
            if ((limit != null) && (limit > 0) && (result.length() > limit)) {
                IOException ioException = new IOException("The data read has exceeded the limit of " + limit
                        + ". The current value is " + result.length());
                logger.throwing(CLASS_NAME, METHOD_NAME, ioException);
                throw ioException;
            }
        } finally {
            if (raf != null) {
                raf.close();
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, result == null ? "<null>" : "<non-null>");
        return result;
    }

    /**
     * Write data to a file using the platform's default charset.
     * 
     * @param pathname
     *            The file's path name.
     * @param text
     *            The text to be written. '\n' is replaced by a new line.
     * @param append
     *            Append to an existing file if this flag is true.
     * @return True if the data was written successfully.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public static boolean writeTextToFile(String pathname, String text, boolean append) throws IOException {
        final String METHOD_NAME = "writeTextToFile";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { pathname, text, append });

        boolean result = true;
        RandomAccessFile raf = null;

        try {
            File fl = new File(pathname);
            if (fl.exists() && !append) {
                logger.logp(Level.FINE, CLASS_NAME, METHOD_NAME,
                        "File exists '" + pathname + "' but will be overwitten.");
                fl.delete();
                fl.createNewFile();
            } else if (!fl.exists()) {
                logger.logp(Level.FINE, CLASS_NAME, METHOD_NAME,
                        "File '" + pathname + "' does not exist. Creating a new one.");
                fl.createNewFile();
            } else if (!fl.canWrite()) {
                IOException ioException = new IOException("File '" + pathname + "' is read only.");
                throw ioException;
            }
            raf = new RandomAccessFile(fl, "rw");
            if (append) {
                raf.seek(raf.length());
            }

            // Replace '\n' with the operating system line separator
            text = text.replaceAll("\\n", lineSeparator);

            raf.write(text.getBytes());

        } catch (IOException io) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Cannot write text to file '" + pathname + "':", io);
            result = false;
        } finally {
            if (raf != null) {
                raf.close();
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, result);
        return result;
    }

}
