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

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The bean information class for integration.mail.Mail.
 */
public class MailBeanInfo extends SimpleBeanInfo {

    private static final String CLASS_NAME = MailBeanInfo.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    @SuppressWarnings("rawtypes")
    private final Class beanClass = Mail.class;

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        final String METHOD_NAME = "getMethodDescriptors";
        try {
            MethodDescriptor descriptorList[] = {
                    getIMAPMessagesMethodDescriptor(), getPOPMessagesMethodDescriptor(),
                    sendMessageMethodDescriptor() };
            return descriptorList;
        } catch (Exception e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Creating the method descriptor list failed.", e);
            return super.getMethodDescriptors();
        }
    }

    @SuppressWarnings("unchecked")
    private MethodDescriptor getIMAPMessagesMethodDescriptor()
            throws NoSuchMethodException {
        final String METHOD_NAME = "getIMAPMessagesMethodDescriptor";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Method method = beanClass.getMethod("getIMAPMessages", String.class, String.class, String.class, String.class,
                boolean.class, String.class, boolean.class, boolean.class, Integer.class, Integer.class);

        ParameterDescriptor param1 = new ParameterDescriptor();
        param1.setShortDescription("The hostname of the IMAP server.");
        param1.setDisplayName("server");

        ParameterDescriptor param2 = new ParameterDescriptor();
        param2.setShortDescription("The IMAP account name.");
        param2.setDisplayName("username");

        ParameterDescriptor param3 = new ParameterDescriptor();
        param3.setShortDescription("The IMAP account password.");
        param3.setDisplayName("password");

        ParameterDescriptor param4 = new ParameterDescriptor();
        param4.setShortDescription("The mail box to read messages from.");
        param4.setDisplayName("mailBox");

        ParameterDescriptor param5 = new ParameterDescriptor();
        param5.setShortDescription(
                "If set to true, return attachment pathnames and store attachments in the OS specific temporary directory or the specified attachmentsDirectory.");
        param5.setDisplayName("returnAttachments");

        ParameterDescriptor param6 = new ParameterDescriptor();
        param6.setShortDescription(
                "If specified and returnAttachments is true, attachments are stored in this directory.");
        param6.setDisplayName("attachmentsDirectory");

        ParameterDescriptor param7 = new ParameterDescriptor();
        param7.setShortDescription("Return variables that are embedded in the narrative if this flag is true.");
        param7.setDisplayName("scanForVariables");

        ParameterDescriptor param8 = new ParameterDescriptor();
        param8.setShortDescription("Delete messages after they have been read if this flag is true.");
        param8.setDisplayName("deleteAfterReading");

        ParameterDescriptor param9 = new ParameterDescriptor();
        param9.setShortDescription(
                "Throw an IOException if the number of messages exceeds the specified value. -1 or null indicates unlimited.");
        param9.setDisplayName("messageLimit");

        ParameterDescriptor param10 = new ParameterDescriptor();
        param10.setShortDescription(
                "Throw an IOException if the size of the formatted messages exceeds the specified value. -1 or null indicates unlimited.");
        param10.setDisplayName("dataLimit");

        ParameterDescriptor params[] = { param1, param2, param3, param4, param5, param6, param7, param8, param9,
                param10 };
        MethodDescriptor methodDescriptor = new MethodDescriptor(method, params);
        logger.exiting(CLASS_NAME, METHOD_NAME, methodDescriptor);
        return methodDescriptor;
    }

    @SuppressWarnings("unchecked")
    private MethodDescriptor getPOPMessagesMethodDescriptor()
            throws NoSuchMethodException {
        final String METHOD_NAME = "getPOPMessagesMethodDescriptor";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Method method = beanClass.getMethod("getPOPMessages", String.class, String.class, String.class, boolean.class,
                String.class, boolean.class, boolean.class, Integer.class, Integer.class);

        ParameterDescriptor param1 = new ParameterDescriptor();
        param1.setShortDescription("The hostname of the POP server.");
        param1.setDisplayName("server");

        ParameterDescriptor param2 = new ParameterDescriptor();
        param2.setShortDescription("The POP account name.");
        param2.setDisplayName("username");

        ParameterDescriptor param3 = new ParameterDescriptor();
        param3.setShortDescription("The POP account password.");
        param3.setDisplayName("password");

        ParameterDescriptor param4 = new ParameterDescriptor();
        param4.setShortDescription(
                "If set to true, return attachment pathnames and store attachments in the OS specific temporary directory or the specified attachmentsDirectory.");
        param4.setDisplayName("returnAttachments");

        ParameterDescriptor param5 = new ParameterDescriptor();
        param5.setShortDescription(
                "If specified and returnAttachments is true, attachments are stored in this directory.");
        param5.setDisplayName("attachmentsDirectory");

        ParameterDescriptor param6 = new ParameterDescriptor();
        param6.setShortDescription("Return variables that are embedded in the narrative if this flag is true.");
        param6.setDisplayName("scanForVariables");

        ParameterDescriptor param7 = new ParameterDescriptor();
        param7.setShortDescription("Delete messages after they have been read if this flag is true.");
        param7.setDisplayName("deleteAfterReading");

        ParameterDescriptor param8 = new ParameterDescriptor();
        param8.setShortDescription(
                "Throw an IOException if the number of messages exceeds the specified value. -1 or null indicates unlimited.");
        param8.setDisplayName("messageLimit");

        ParameterDescriptor param9 = new ParameterDescriptor();
        param9.setShortDescription(
                "Throw an IOException if the size of the formatted messages exceeds the specified value. -1 or null indicates unlimited.");
        param9.setDisplayName("dataLimit");

        ParameterDescriptor params[] = { param1, param2, param3, param4, param5, param6, param7, param8, param9 };
        MethodDescriptor methodDescriptor = new MethodDescriptor(method, params);
        logger.exiting(CLASS_NAME, METHOD_NAME, methodDescriptor);
        return methodDescriptor;
    }

    @SuppressWarnings("unchecked")
    private MethodDescriptor sendMessageMethodDescriptor()
            throws NoSuchMethodException {
        final String METHOD_NAME = "sendMessageMethodDescriptor";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Method method = beanClass.getMethod("sendMessage", String.class, String.class, String.class, String.class,
                String.class, String.class, String.class, String.class, String.class, String.class, String.class);

        ParameterDescriptor param1 = new ParameterDescriptor();
        param1.setShortDescription("The host to connect to.");
        param1.setDisplayName("smtpHost");

        ParameterDescriptor param2 = new ParameterDescriptor();
        param2.setShortDescription("Comma separated list of email addresses of the 'To' recipients.");
        param2.setDisplayName("to");

        ParameterDescriptor param3 = new ParameterDescriptor();
        param3.setShortDescription("The email address of the sender.");
        param3.setDisplayName("from");

        ParameterDescriptor param4 = new ParameterDescriptor();
        param4.setShortDescription("Comma separated list of email addresses to which replies should be directed.");
        param4.setDisplayName("replyTo");

        ParameterDescriptor param5 = new ParameterDescriptor();
        param5.setShortDescription("Comma separated list of email addresses of the 'Cc' recipients.");
        param5.setDisplayName("cc");

        ParameterDescriptor param6 = new ParameterDescriptor();
        param6.setShortDescription("Comma separated list of email addresses of the 'Bcc' recipients.");
        param6.setDisplayName("bcc");

        ParameterDescriptor param7 = new ParameterDescriptor();
        param7.setShortDescription("The subject of the email.");
        param7.setDisplayName("subject");

        ParameterDescriptor param8 = new ParameterDescriptor();
        param8.setShortDescription("The MIME content type; i.e. 'text/html' or 'text/plain'.");
        param8.setDisplayName("contentType");

        ParameterDescriptor param9 = new ParameterDescriptor();
        param9.setShortDescription("The body of the email.");
        param9.setDisplayName("body");

        ParameterDescriptor param10 = new ParameterDescriptor();
        param10.setShortDescription(
                "The importance of the email; i.e. 'high', 'normal', or 'low'. Invalid values are ignored.");
        param10.setDisplayName("importance");

        ParameterDescriptor param11 = new ParameterDescriptor();
        param11.setShortDescription("Comma separated list of file names to be attached to the email.");
        param11.setDisplayName("attachmentFileNames");

        ParameterDescriptor params[] = { param1, param2, param3, param4, param5, param6, param7, param8, param9,
                param10, param11 };
        MethodDescriptor methodDescriptor = new MethodDescriptor(method, params);
        logger.exiting(CLASS_NAME, METHOD_NAME, methodDescriptor);
        return methodDescriptor;
    }

}
