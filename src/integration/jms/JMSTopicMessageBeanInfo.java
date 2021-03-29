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
package integration.jms;

import java.beans.MethodDescriptor;
import java.beans.ParameterDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The bean information class for integration.jms.JMSTopicMessage.
 */
public class JMSTopicMessageBeanInfo extends SimpleBeanInfo {

    private static final String CLASS_NAME = JMSTopicMessageBeanInfo.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    @SuppressWarnings("rawtypes")
    private final Class beanClass = JMSTopicMessage.class;

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
        final String METHOD_NAME = "getMethodDescriptors";
        try {
            MethodDescriptor descriptorList[] = { putTextMessageMethodDescriptor() };
            return descriptorList;
        } catch (Exception e) {
            logger.logp(Level.SEVERE, CLASS_NAME, METHOD_NAME, "Creating the method descriptor list failed.", e);
            return super.getMethodDescriptors();
        }
    }

    @SuppressWarnings("unchecked")
    private MethodDescriptor putTextMessageMethodDescriptor() throws NoSuchMethodException {
        final String METHOD_NAME = "putTextMessageMethodDescriptor";
        logger.entering(CLASS_NAME, METHOD_NAME);

        Method method = beanClass.getMethod("putTextMessage", String.class, String.class, String.class, String.class,
                String.class);

        ParameterDescriptor param1 = new ParameterDescriptor();
        param1.setShortDescription(
                "The name of the initial context factory class to use when performing JNDI lookups. If null, the default initial context JNDI properties are used.");
        param1.setDisplayName("initialContext");

        ParameterDescriptor param2 = new ParameterDescriptor();
        param2.setShortDescription(
                "A reference to the naming provider to be used for JNDI lookups. If null, the default initial context JNDI properties are used.");
        param2.setDisplayName("providerUrl");

        ParameterDescriptor param3 = new ParameterDescriptor();
        param3.setShortDescription("The content of the message to be added to the specified topic.");
        param3.setDisplayName("messageContent");

        ParameterDescriptor param4 = new ParameterDescriptor();
        param4.setShortDescription(
                "The JNDI name of the connection factory to use when establishing a connection to the messaging provider.");
        param4.setDisplayName("connectionFactory");

        ParameterDescriptor param5 = new ParameterDescriptor();
        param5.setShortDescription("The name of the topic to which the message should be added.");
        param5.setDisplayName("topicName");

        MethodDescriptor methodDescriptor = new MethodDescriptor(method,
                new ParameterDescriptor[] { param1, param2, param3, param4, param5 });
        logger.exiting(CLASS_NAME, METHOD_NAME, methodDescriptor);
        return methodDescriptor;
    }

}