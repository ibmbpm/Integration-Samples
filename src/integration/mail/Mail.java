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
package integration.mail;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Verifier;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * This class provides a sample implementation for email services.
 */
public class Mail {

    private static final String CLASS_NAME = Mail.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * Constant for Low Importance
     */
    private static final String IMPORTANCE_LOW = "low";
    /**
     * Constant for Normal Importance
     */
    private static final String IMPORTANCE_NORMAL = "normal";
    /**
     * Constant for High Importance
     */
    private static final String IMPORTANCE_HIGH = "high";
    /**
     * Constant for Importance header
     */
    private static final String IMPORTANCE_HEADER = "Importance";
    /**
     * Constants for the mail protocol
     */
    private static final String PROTOCOL_IMAP = "imap";
    private static final String PROTOCOL_POP = "pop3";

    private String _smtpHost = null;
    private List<InternetAddress> _to = null;
    private InternetAddress _from = null;
    private List<InternetAddress> _replyTo = null;
    private List<InternetAddress> _cc = null;
    private List<InternetAddress> _bcc = null;
    private String _subject = null;
    private ContentType _contentType = null;
    private String _content = null;
    private String _importance = IMPORTANCE_NORMAL;
    private List<String> _attachmentFileNames = null;

    /**
     * Send an email message to an SMTP server.
     *
     * @param smtpHost
     *            The host to connect to.
     * @param to
     *            Comma separated list of email addresses of the 'To' recipients.
     * @param from
     *            The email address of the sender.
     * @param replyTo
     *            Comma separated list of email addresses to which replies should be directed.
     * @param cc
     *            Comma separated list of email addresses of the 'Cc' recipients.
     * @param bcc
     *            Comma separated list of email addresses of the 'Bcc' recipients.
     * @param subject
     *            The subject of the email.
     * @param contentType
     *            The MIME content type; i.e. 'text/html' or 'text/plain'.
     * @param content
     *            The body of the email.
     * @param importance
     *            The importance of the email; i.e. 'high', 'normal', or 'low'. Invalid values are
     *            ignored.
     * @param attachmentFileNames
     *            Comma separated list of file names to be attached to the email.
     * @exception MessageingException
     *                If something went wrong.
     */
    public static void sendMessage(String smtpHost, String to, String from, String replyTo, String cc, String bcc,
            String subject, String contentType, String content, String importance, String attachmentFileNames)
            throws MessagingException {

        final String METHOD_NAME = "sendMessage";
        logger.entering(CLASS_NAME, METHOD_NAME,
                new Object[] { smtpHost, to, from, replyTo, cc, bcc, subject, contentType, content, importance,
                        attachmentFileNames });

        Mail mail = new Mail();
        mail.setSMTPHost(smtpHost);

        if (to == null || to.length() == 0) {
            throw new MessagingException("The 'To' field should not be empty.");
        }
        mail.setTo(to);

        if (from == null || from.length() == 0) {
            throw new MessagingException("The 'From' field should not be empty.");
        }
        mail.setFrom(from);

        // optional field: ReplyTo
        if (replyTo != null && !replyTo.equals("")) {
            mail.setReplyTo(replyTo);
        }

        // optional field: cc
        if (cc != null && !cc.equals("")) {
            mail.setCc(cc);
        }

        // optional field: bcc
        if (bcc != null && !bcc.equals("")) {
            mail.setBcc(bcc);
        }

        if (subject == null || subject.length() == 0) {
            throw new MessagingException("The 'Subject' field should not be empty.");
        }
        mail.setSubject(subject);

        if (contentType == null || contentType.equals("")) {
        	contentType = "text/html";
        }
        mail.setContentType(contentType);

        if (content == null || content.length() == 0) {
            throw new MessagingException("The 'Content' field should not be empty.");
        }
        mail.setContent(content);

        // optional importance
        if (importance != null && importance.length() > 0) {
            mail.setImportance(importance);
        }

        // optional attachment file names (comma separated list)
        if (attachmentFileNames != null && attachmentFileNames.length() > 0) {
            List<String> filenames = Arrays.asList(attachmentFileNames.split(","));
            mail.setattachmentFileNames(filenames);
        }

        mail.send();

        logger.exiting(CLASS_NAME, METHOD_NAME);
    }

    /**
     * Get all email messages from the user's INBOX folder on a POP-server.
     *
     * @param server
     *            The hostname of the POP server.
     * @param username
     *            The POP account name.
     * @param password
     *            The POP account password.
     * @param returnAttachments
     *            If set to true, return attachment pathnames and store attachments in the OS
     *            specific temporary directory or the specified attachmentsDirectory.
     * @param attachmentsDirectory
     *            If specified and returnAttachments is true, attachments are stored in this
     *            directory.
     * @param scanForVariables
     *            Return variables that are embedded in the narrative if this flag is true.
     * @param deleteAfterReading
     *            Delete messages after they have been read if this flag is true.
     * @param messageLimit
     *            Throw an IOException if the number of messages exceeds the specified value. -1 or
     *            null indicates unlimited.
     * @param dataLimit
     *            Throw an IOException if the size of the formatted messages exceeds the specified
     *            value. -1 or null indicates unlimited.
     * @return The XML formatted email messages.
     * @exception MessageingException
     *                If something went wrong, e.g. if the mailbox does not exist.
     */
    public static Element getPOPMessages(String server, String username, String password, boolean returnAttachments,
            String attachmentsDirectory, boolean scanForVariables, boolean deleteAfterReading, Integer messageLimit,
            Integer dataLimit) throws MessagingException, IOException {

        return getMessages(PROTOCOL_POP, server, username, password, "INBOX", returnAttachments, attachmentsDirectory,
                scanForVariables, deleteAfterReading, messageLimit, dataLimit);
    }

    /**
     * Get all messages from the user's given mail box folder on an IMAP-server.
     *
     * @param server
     *            The hostname of the IMAP server.
     * @param username
     *            The IMAP account name.
     * @param password
     *            The IMAP account password.
     * @param mailBox
     *            The mail box to read messages from.
     * @param returnAttachments
     *            If set to true, return attachment pathnames and store attachments in the OS
     *            specific temporary directory or the specified attachmentsDirectory.
     * @param attachmentsDirectory
     *            If specified and returnAttachments is true, attachments are stored in this
     *            directory.
     * @param scanForVariables
     *            Return variables that are embedded in the narrative if this flag is true.
     * @param deleteAfterReading
     *            Delete messages after they have been read if this flag is true.
     * @param messageLimit
     *            Throw an IOException if the number of messages exceeds the specified value. -1 or
     *            null indicates unlimited.
     * @param dataLimit
     *            Throw an IOException if the size of the formatted messages exceeds the specified
     *            value. -1 or null indicates unlimited.
     * @return The XML formatted messages.
     * @exception MessagingException
     *                If something went wrong, e.g. if the mailbox does not exist.
     */
    public static Element getIMAPMessages(String server, String username, String password, String mailBox,
            boolean returnAttachments, String attachmentsDirectory, boolean scanForVariables,
            boolean deleteAfterReading, Integer messageLimit, Integer dataLimit)
            throws MessagingException, IOException {

        return getMessages(PROTOCOL_IMAP, server, username, password, mailBox, returnAttachments, attachmentsDirectory,
                scanForVariables, deleteAfterReading, messageLimit, dataLimit);
    }

    /**
     * Get messages from POP and IMAP-servers.<br>
     * The messages are returned in XML format with the following structure:
     * 
     * <resultSet>
     *    <record>
     *       <column name="MSG_SUBJECT" />
     *       <column name="MSG_TO" />
     *       <column name="MSG_FROM />
     *       <column name="MSG_DATE />
     *       <column name="MSG_CC" />
     *       <column name="MSG_REPLY />
     *       <column name="MSG_NARRATIVE />
     *       <column name="MSG_ATTACHMENTS" />
     *    </record>
     * </resultSet>
     *
     * @param protcol
     *            The mail protocol, i.e. PROTOCOL_IMAP or PROTOCOL_POP
     * @param server
     *            The hostname of the server.
     * @param username
     *            The username.
     * @param password
     *            The user's password.
     * @param mailBox
     *            The mail box to read messages from.
     * @param returnAttachments
     *            If set to true, return attachment pathnames and store attachments in the OS
     *            specific temporary directory or the specified attachmentsDirectory.
     * @param attachmentsDirectory
     *            If specified and returnAttachments is true, attachments are stored in this
     *            directory.
     * @param scanForVariables
     *            Return variables that are embedded in the narrative if this flag is true.
     * @param deleteAfterReading
     *            Delete messages after they have been read if this flag is true.
     * @param messageLimit
     *            Throw an IOException if the number of messages exceeds the specified value. -1 or
     *            null indicates unlimited.
     * @param dataLimit
     *            Throw an IOException if the size of the formatted messages exceeds the specified
     *            value. -1 or null indicates unlimited.
     * @return the XML formatted messages
     * @exception MessagingException
     *                If something went wrong, e.g. if the mailbox does not exist.
     */
    private static Element getMessages(String protocol, String server, String username, String password, String mailBox,
            boolean returnAttachments, String attachmentsDirectory, boolean scanForVariables,
            boolean deleteAfterReading, Integer messageLimit, Integer dataLimit)
            throws MessagingException, IOException {

        final String METHOD_NAME = "getMessages";
        logger.entering(CLASS_NAME, METHOD_NAME,
                new Object[] { protocol, server, username, password == null ? "<null>" : "<non-null>", mailBox,
                        returnAttachments, attachmentsDirectory, scanForVariables, deleteAfterReading,
                        messageLimit, dataLimit });

        // Get the session object
        Properties props = new Properties();

        if (returnAttachments && attachmentsDirectory != null) {
            // check the existence of the attachments directory
            if ((new File(attachmentsDirectory).exists()) == false) {
                throw new MessagingException("The attachments directory '" + attachmentsDirectory + "' does not exist.");
            }
        }

        Session session = Session.getInstance(props);
        Store store = null;
        Folder folder = null;
        Message[] msgs = null;
        Element resultSetElement = new Element("resultSet");

        try {
            // Get the store object and connect with the server
            store = session.getStore(protocol);
            store.connect(server, username, password);

            // Open up the appropriate folder
            folder = store.getFolder(mailBox);
            folder.open(Folder.READ_WRITE);

            // Get all the messages from the folder
            msgs = folder.getMessages();

            // Check the message limit
            if ((messageLimit != null) && (messageLimit > 0) && (msgs.length > messageLimit)) {
                throw new IOException("The number of messages read has exceeded the limit of " + messageLimit
                        + ". The current value is " + msgs.length);
            }

            // Loop through each message and add to our record set
            for (int i = 0; i < msgs.length; i++) {
                Element record = convertMessageToXML(msgs[i], returnAttachments, attachmentsDirectory, scanForVariables,
                        deleteAfterReading);
                resultSetElement.addContent(record);
            }
            resultSetElement.setAttribute(new Attribute("recordCount", Integer.toString(msgs.length)));
            // Can be variable, so assign to -1
            resultSetElement.setAttribute(new Attribute("columnCount", Integer.toString(-1)));

            // Check the data limit
            if ((dataLimit != null) && (dataLimit > 0)) {
                // Convert JDOM to String
                StringWriter sw = new StringWriter();
                XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                outputter.output(resultSetElement, sw);
                String result = sw.toString();

                if (result.length() > dataLimit) {
                    throw new IOException("The size of the formatted messages has exceeded the limit of " + dataLimit
                            + ". The current value is " + result.length());
                }
            }
        } finally {
            // Close the connection
            if (folder != null && folder.isOpen()) {
                logger.logp(Level.FINE, CLASS_NAME, METHOD_NAME,
                        "Close the folder. Expunge deleted messages = " + deleteAfterReading);
                folder.close(deleteAfterReading);
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, "received messages: " + msgs.length);
        return resultSetElement;
    }

    /**
     * Convert a message into an XML element.
     *
     * @param msg
     *            The message.
     * @param includeAttachmentNames
     *            Include a comma separated list of attachment pathnames in the XML element if this
     *            flag is true.
     * @param attachmentsDirectory
     *            If specified and includeAttachmentNames is true, attachments are stored in this
     *            directory. If not specified and includeAttachmentNames is true, attachments are
     *            stored in the OS specific temporary directory location.
     * @param includeEmbeddedVariables
     *            Include variables that are embedded in the narrative in the XML element if this
     *            flag is true.
     * @param deleteAfterReading
     *            Delete messages after they have been read if this flag is true.
     * @return the XML formatted message
     * @exception MessagingException
     *                If something went wrong, e.g. if the mailbox does not exist.
     * @exception IOException
     *                If an I/O error occurs when getting the message content.
     */
    private static Element convertMessageToXML(final Message msg, boolean includeAttachmentNames,
            String attachmentsDirectory, boolean includeEmbeddedVariables, boolean deleteAfterReading)
            throws MessagingException, IOException {

        final String METHOD_NAME = "convertMessageToXML";
        logger.entering(CLASS_NAME, METHOD_NAME,
                new Object[] { msg, includeAttachmentNames, attachmentsDirectory, includeEmbeddedVariables,
                        deleteAfterReading });

        Element record = new Element("record");

        record.addContent(convertMessageAttributeToXML("MSG_SUBJECT", msg.getSubject()));

        record.addContent(convertMessageAttributeToXML("MSG_TO",
                InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO))));

        Address[] from = msg.getFrom();
        String fromText = null;
        if (from != null) {
            fromText = InternetAddress.toString(from);
        }
        record.addContent(convertMessageAttributeToXML("MSG_FROM", fromText));

        if (msg.getSentDate() == null) {
            record.addContent(convertMessageAttributeToXML("MSG_DATE", ""));
        } else {
            record.addContent(convertMessageAttributeToXML("MSG_DATE", msg.getSentDate().toString()));
        }
        Address[] cc = msg.getRecipients(Message.RecipientType.CC);
        String ccText = null;
        if (cc != null) {
            ccText = InternetAddress.toString(cc);
        }
        record.addContent(convertMessageAttributeToXML("MSG_CC", ccText));

        Address[] replyTo = msg.getReplyTo();
        String replyToText = null;
        if (replyTo != null) {
            replyToText = InternetAddress.toString(replyTo);
        }
        record.addContent(convertMessageAttributeToXML("MSG_REPLY", replyToText));

        // Now lets deal with the body and any attachments
        String narrative = null;
        String attachments = null;
        String msgType = msg.getContentType();

        Object o = msg.getContent();

        if (o instanceof String) {
            // The narrative is just this object
            narrative = (String) o;
            msgType = "String";
        } else if (o instanceof Multipart) {
            // Get the narrative from the multipart message
            narrative = getNarrative((Multipart) o);
            msgType = "Multipart";

            // Get the attachments for this multipart message
            if (includeAttachmentNames) {
                attachments = getAttachments((Multipart) o, attachmentsDirectory);
            }
        } else if (o instanceof InputStream && msgType.toLowerCase().startsWith("text/plain")) {
            // We have an input stream, so lets just convert this to a string.
            // Nothing else we can do about it...
            // This now becomes the narrative of the message
            narrative = convertInputStream2String((InputStream) o);
            msgType = "InputStream";
        }

        record.addContent(convertMessageAttributeToXML("MSG_NARRATIVE", narrative));
        record.addContent(convertMessageAttributeToXML("MSG_ATTACHMENTS", attachments));

        // We also need to scan the narrative and pull out any attached parameters and store in our
        // record set as well
        if (includeEmbeddedVariables && (narrative != null)) {
            addEmbeddedVariablesToXML(narrative, record);
        }

        // Set the delete flag if we need to remove from server after reading
        if (deleteAfterReading) {
            msg.setFlag(Flags.Flag.DELETED, true);
            msg.saveChanges();
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, (new XMLOutputter()).outputString(record));
        return record;
    }

    /**
     * Convert a message attribute into an XML "column" element.
     *
     * @param name
     *            The message attribute name.
     * @param value
     *            The message attribute value.
     * @return The XML formatted message attribute.
     */
    private static Element convertMessageAttributeToXML(String name, String value) {
        Element column = new Element("column");
        column.setAttribute("name", name);
        try {
            column.setText(value);
        } catch (Exception e) {
            // Odds are we had some out of band chars in there.
            // Let's try to clean it up and try again.
            column.setText(removeIllegalChars(value));
        }
        return column;
    }

    /**
     * Remove those characters that are illegal in XML text nodes or attribute values.
     *
     * @param text
     *            The original value.
     * @return The fixed value with all illegal characters removed.
     */
    private static String removeIllegalChars(String text) {
        final String METHOD_NAME = "removeIllegalChars";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { text });

        if ((text == null) || (text.trim().length() == 0)) {
            return text;
        }
        if (Verifier.checkCharacterData(text) == null) {
            return text;
        }
        // It seems we have to filter out invalid XML characters...
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
            if (Verifier.isXMLCharacter(text.charAt(i))) {
                sb.append(text.charAt(i));
            }
        }
        logger.exiting(CLASS_NAME, METHOD_NAME, sb.toString());
        return sb.toString();
    }

    /**
     * Get the narrative from the multipart message. Search for the first part which is a string and has no filename
     * associated which is usually the first one.
     *
     * @param mp
     *            The multipart message.
     * @return The narrative.
     */
    private static String getNarrative(Multipart mp) {
        final String METHOD_NAME = "getNarrative";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { mp });

        String narrative = "";
        try {
            int numPart = mp.getCount();
            for (int i = 0; i < numPart; i++) {
                BodyPart bp = mp.getBodyPart(i);
                Object o = bp.getContent();

                if (o instanceof Multipart) {
                    narrative = getNarrative((Multipart) o);
                } else {
                    String filename = bp.getFileName();
                    if ((o instanceof String) && (filename == null)) {
                        narrative = (String) o;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME,
                    "Getting the narrative from the multipart message failed.", e);
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, narrative);
        return narrative;
    }

    /**
     * Get a comma separated list of attachments associated with the message. This list contains the
     * locations on the local drive of where the attachments are.
     *
     * @param mp
     *            The multipart message.
     * @param attachmentsDirectory
     *            If specified attachments are stored in this directory, otherwise they are stored
     *            in the OS specific temporary directory location.
     * @return Comma separated list of attachment file names.
     */
    private static String getAttachments(Multipart mp, String attachmentsDirectory) {
        final String METHOD_NAME = "getAttachments";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { mp, attachmentsDirectory });

        StringBuffer sb = new StringBuffer(1024);
        byte data[] = new byte[1024];
        InputStream ins = null;
        FileOutputStream fos = null;
        try {
            if (attachmentsDirectory == null) {
                // Find out the OS specific temporary directory location
                attachmentsDirectory = System.getProperty("java.io.tmpdir");
            }

            int numPart = mp.getCount();
            for (int i = 0; i < numPart; i++) {
                BodyPart bp = mp.getBodyPart(i);
                String filename = bp.getFileName();

                // If we have a filename then this is an attachment we need to save to disk
                if ((filename != null)) {
                    try {
                        filename = MimeUtility.decodeText(filename);
                        ins = bp.getDataHandler().getInputStream();
                        File f = new File(attachmentsDirectory, filename);

                        // Check if the file exists. To avoid over writing,
                        // we shall rename the file with a suffix of _ and a number
                        // We check if the renamed file also exists till we get a
                        // file name which does not exists.
                        if (f.exists()) {
                            int fileSuffix = 1;
                            boolean isFileFound = true;
                            while (isFileFound) {
                                // extract index of "."
                                // Eg: if filename is abc.txt, we want to create a
                                // new name say abc_1.txt
                                // to insert "_1" in between the name and the
                                // extension we will substring the name
                                // and insert the new suffix and create a new name.
                                // if the filename does not contain a "."
                                // we append the suffix at the end of the filename.
                                String newFilename;
                                int indexOfDot = filename.indexOf(".");
                                if (indexOfDot > 0) {
                                    newFilename = filename.substring(0, indexOfDot) + "_" + fileSuffix
                                            + filename.substring(indexOfDot);
                                } else {
                                    newFilename = filename + "_" + fileSuffix;
                                }

                                f = new File(attachmentsDirectory, newFilename);
                                if (f.exists()) {
                                    fileSuffix++;
                                } else {
                                    isFileFound = false;
                                }
                            }
                        }

                        fos = new FileOutputStream(f);
                        int bytes_read;
                        while ((bytes_read = ins.read(data)) > 0) {
                            fos.write(data, 0, bytes_read);
                        }

                        // Add current filename to our list to return
                        sb.append(f.getAbsolutePath());
                        if (i < (numPart - 1)) {
                            sb.append(",");
                        }
                    } finally {
                        if (ins != null) {
                            ins.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME,
                    "Getting the attachments from the multipart message failed.", e);
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, sb.toString());
        return sb.toString();
    }

    /**
     * Convert an InputStream object into a String object.
     *
     * @param ins
     *            The input stream to convert.
     * @return The converted string.
     */
    private static String convertInputStream2String(InputStream ins) {
        final String METHOD_NAME = "convertInputStream2String";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data[] = new byte[1024];

        // Read our input stream bytes in a byte array output stream
        try {
            try {
                int bytes_read;
                while ((bytes_read = ins.read(data)) > 0) {
                    baos.write(data, 0, bytes_read);
                }
            } finally {
                ins.close();
            }
        } catch (IOException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Cannot read input data.", e);
        }
        // Convert the output stream into a string
        return baos.toString();
    }

    /**
     * Scan the narrative and add any embedded variables as key/value pair to the XML formatted
     * message.
     *
     * @param narrative
     *            The message narrative.
     * @param formattedMessage
     *            The XML formatted message.
     */
    private static void addEmbeddedVariablesToXML(String narrative, Element formattedMessage) {
        final String METHOD_NAME = "addEmbeddedVariablesToXML";

        try {
            // Read each line of the narrative in. An attached parameter should
            // be on a line by itself
            BufferedReader br = new BufferedReader(new StringReader(narrative));
            String line = null;
            while ((line = br.readLine()) != null) {
                int pos = line.indexOf("=");
                // If we found an equals sign then this is interpreted as an attached variable
                if (pos >= 0) {
                    String key = line.substring(0, pos).trim();
                    String value = line.substring(pos + 1).trim();

                    // Store the attached variable into the record set
                    if (key.length() > 0) {
                        formattedMessage.addContent(convertMessageAttributeToXML(key, value));
                    }
                }
            }
        } catch (IOException e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Cannot read input data.", e);
        }
    }

    /**
     * Set the list of attachment file names.
     * 
     * @param attachmentFileNames
     *            List of file names.
     */
    private void setattachmentFileNames(List<String> attachmentFileNames) {
        _attachmentFileNames = attachmentFileNames;
    }

    /**
     * Set the bcc recipients list.
     * 
     * @param addresses
     *            Comma separated list of email addresses.
     * @exception AddressException
     *                If the addresses cannot be parsed.
     */
    private void setBcc(String addresses) throws AddressException {
        // Parse the given sequence of addresses into InternetAddress objects.
        // If strict is true, many (but not all) of the RFC822 syntax
        // rules are enforced.
        _bcc = Arrays.asList(InternetAddress.parse(addresses, true));
    }

    /**
     * Set the cc recipients list.
     * 
     * @param addresses
     *            Comma separated list of email addresses.
     * @exception AddressException
     *                If the addresses cannot be parsed.
     */
    private void setCc(String addresses) throws AddressException {
        // Parse the given sequence of addresses into InternetAddress objects.
        // If strict is true, many (but not all) of the RFC822 syntax
        // rules are enforced.
        _cc = Arrays.asList(InternetAddress.parse(addresses, true));
    }

    /**
     * Set message body.
     * 
     * @param messageText
     *            Text of the message.
     */
    private void setContent(String newContent) {
        _content = newContent;
    }

    /**
     * Set the content type.
     * 
     * @param contentType
     *            Mime type of the message @see ContentType.
     * @exception ParseException
     *                If the content type cannot be parsed.
     */
    private void setContentType(String contentType) throws ParseException {
        _contentType = new ContentType(contentType);
    }

    /**
     * Set the from.
     * 
     * @param address
     *            Sender of the email.
     * @exception AddressException
     *                If the address cannot be parsed.
     */
    private void setFrom(String address) throws AddressException {
        _from = new InternetAddress(address);
    }

    /**
     * Set the importance.
     * 
     * @param importance
     *            Should be high, normal, or low.
     */
    private void setImportance(String importance) {
        _importance = importance;
    }

    /**
     * Set the reply to.
     * 
     * @param addresses
     *            Comma separated list of email addresses.
     * @exception AddressException
     *                If the addresses cannot be parsed.
     */
    private void setReplyTo(String addresses) throws AddressException {
        // Parse the given sequence of addresses into InternetAddress objects.
        // If strict is true, many (but not all) of the RFC822 syntax
        // rules are enforced.
        _replyTo = Arrays.asList(InternetAddress.parse(addresses, true));
    }

    /**
     * Set the SMTP host.
     * 
     * @param host
     *            The host name.
     */
    private void setSMTPHost(String host) {
        _smtpHost = host;
    }

    /**
     * Set the subject.
     * 
     * @param subject
     *            Subject of the message.
     */
    private void setSubject(String subject) {
        _subject = subject;
    }

    /**
     * Set the to.
     * 
     * @param addresses
     *            Comma separated list of email addresses.
     * @exception AddressException
     *                If the addresses cannot be parsed.
     */
    private void setTo(String addresses) throws AddressException {
        // Parse the given sequence of addresses into InternetAddress objects.
        // If strict is true, many (but not all) of the RFC822 syntax
        // rules are enforced.
        _to = Arrays.asList(InternetAddress.parse(addresses, true));
    }

    /**
     * Send this message.
     * 
     * @exception MessagingException
     *                If something went wrong.
     */
    private void send() throws MessagingException {
        final String METHOD_NAME = "send";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Properties properties = new Properties();
        properties.put("mail.smtp.host", _smtpHost);

        Session session = Session.getInstance(properties);

        MimeMessage message = new MimeMessage(session);
        message.setSentDate(new Date());
        message.setFrom(_from);
        message.addRecipients(Message.RecipientType.TO, _to.toArray(new Address[0]));

        // optional field: _cc
        if (_cc != null && !_cc.isEmpty()) {
            message.addRecipients(Message.RecipientType.CC, _cc.toArray(new Address[0]));
        }

        // optional field: _bcc
        if (_bcc != null && !_bcc.isEmpty()) {
            message.addRecipients(Message.RecipientType.BCC, _bcc.toArray(new Address[0]));
        }

        message.setSubject(_subject);

        // optional field: _replyTo
        if (_replyTo != null && !_replyTo.isEmpty()) {
            message.setReplyTo(_replyTo.toArray(new Address[0]));
        }

        // add attachments
        if (_attachmentFileNames == null || _attachmentFileNames.size() < 1) {
            // Single part Message
            message.setContent(_content, _contentType.toString());
        } else {
            // multipart message
            MimeMultipart multipart = new MimeMultipart();

            // Create a body part and attach it first, all subsequent parts
            // are attachments.
            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(_content, _contentType.toString());
            multipart.addBodyPart(bodyPart);

            MimeBodyPart attachmentBodyPart = null;
            // Attach each file
            for (int i = 0; i < _attachmentFileNames.size(); i++) {
                String filename = _attachmentFileNames.get(i);
                // Get the attachment
                DataSource source = new FileDataSource(filename);
                // Set the data handler to the attachment
                attachmentBodyPart = new MimeBodyPart();
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                // The filename should usually be a simple name, not including
                // directory components
                attachmentBodyPart.setFileName((new File(filename)).getName());
                multipart.addBodyPart(attachmentBodyPart);
            }

            message.setContent(multipart);
        }

        // Only set the importance flag if it is specified as high or low.
        if (IMPORTANCE_LOW.equalsIgnoreCase(_importance) || IMPORTANCE_HIGH.equalsIgnoreCase(_importance)) {
            message.addHeader(IMPORTANCE_HEADER, _importance);
        }

        Transport.send(message);

        logger.exiting(CLASS_NAME, METHOD_NAME);
    }
}
