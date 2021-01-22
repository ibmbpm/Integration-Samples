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

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The bean information class for integration.file.Files.
 */
public class FilesBeanInfo extends java.beans.SimpleBeanInfo {

    private static final String CLASS_NAME = FilesBeanInfo.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    @SuppressWarnings("rawtypes")
    private final Class beanClass = Files.class;

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        final String METHOD_NAME = "getMethodDescriptors";
        try {
            MethodDescriptor descriptorList[] = { getTextFromFileMethodDescriptor(),
                    writeTextToFileMethodDescriptor() };
            return descriptorList;
        } catch (Exception e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Creating the method descriptor list failed.", e);
            return super.getMethodDescriptors();
        }
    }

    @SuppressWarnings("unchecked")
    private MethodDescriptor getTextFromFileMethodDescriptor() throws NoSuchMethodException {
        final String METHOD_NAME = "getTextFromFileMethodDescriptor";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Method method = beanClass.getMethod("getTextFromFile", String.class, Integer.class);

        ParameterDescriptor param1 = new ParameterDescriptor();
        param1.setShortDescription("The path name of the file.");
        param1.setDisplayName("pathname");

        ParameterDescriptor param2 = new ParameterDescriptor();
        param2.setShortDescription(
                "Throw an IOException if the data read exceeds the specified value. -1 or null indicates unlimited.");
        param2.setDisplayName("limit");

        MethodDescriptor methodDescriptor = new MethodDescriptor(method, new ParameterDescriptor[] { param1, param2 });

        logger.exiting(CLASS_NAME, METHOD_NAME, methodDescriptor);
        return methodDescriptor;
    }

    @SuppressWarnings("unchecked")
    private MethodDescriptor writeTextToFileMethodDescriptor() throws NoSuchMethodException {
        final String METHOD_NAME = "writeTextToFileMethodDescriptor";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Method method = beanClass.getMethod("writeTextToFile", String.class, String.class, boolean.class);

        ParameterDescriptor param1 = new ParameterDescriptor();
        param1.setShortDescription("The path name of the file.");
        param1.setDisplayName("pathname");

        ParameterDescriptor param2 = new ParameterDescriptor();
        param2.setShortDescription("The text to be written.");
        param2.setDisplayName("text");

        ParameterDescriptor param3 = new ParameterDescriptor();
        param3.setShortDescription("Append to an existing file if this flag is true.");
        param3.setDisplayName("append");

        MethodDescriptor methodDescriptor = new MethodDescriptor(method,
                new ParameterDescriptor[] { param1, param2, param3 });

        logger.exiting(CLASS_NAME, METHOD_NAME, methodDescriptor);
        return methodDescriptor;
    }

}
