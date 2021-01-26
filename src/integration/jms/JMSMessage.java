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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

/**
 * This class provides a sample implementation for JMS queue services. Use this class for all JMS
 * providers except IBM MQ. <br>
 * JMSMessage does not support the Secure Sockets Layer (SSL).
 */
public class JMSMessage {

    private static final String CLASS_NAME = JMSMessage.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * Add a message to a JMS Queue.
     * 
     * @param initialContext
     *            The name of the initial context factory class to use when performing JNDI lookups.
     *            This value is used to set the <code>java.naming.context.initial</code> property
     *            when creating the InitialContext. If null, the default initial context JNDI
     *            properties are used.
     * @param providerUrl
     *            A reference to the naming provider to be used for JNDI lookups. This value is used
     *            to set the <code>java.naming.provider.url</code> property when creating the
     *            InitialContext. If null, the default initial context JNDI properties are used.
     * @param messageContent
     *            The content of the message to be added to the specified queue.
     * @param connectionFactory
     *            The JNDI name of the connection factory to use when establishing a connection to
     *            the messaging provider.
     * @param queueName
     *            The name of the queue to which the message should be added.
     * @param lookup
     *            A flag which indicates whether to lookup the queue via JNDI or create it. If
     *            specified as <code>true</code>, then <code>queueName</code> represents the JNDI
     *            name of the queue to be looked up. If specified as <code>false</code>, then
     *            <code>queueName</code> represents the name of the queue to be created.
     * @param properties
     *            A collection of properties to be added to the JMS Message.
     * @throws NamingException
     * @throws JMSException
     */
    public static void putMessage(String initialContext, String providerUrl, String messageContent,
            String connectionFactory,
            String queueName, Boolean lookup, HashMap<String, Object> properties)
            throws NamingException, JMSException {

        final String METHOD_NAME = "putMessage";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { initialContext, providerUrl, messageContent,
                connectionFactory, queueName, lookup, properties });

        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        QueueSender queueSender = null;
        Queue queue = null;

        try {
            InitialDirContext initialDirContext = null;

            if (initialContext != null && providerUrl != null) {
                Hashtable<String, String> hashtable = new Hashtable<String, String>();
                hashtable.put("java.naming.factory.initial", initialContext);
                hashtable.put("java.naming.provider.url", providerUrl);
                hashtable.put("java.naming.referral", "throw");
                initialDirContext = new InitialDirContext(hashtable);
            } else {
                initialDirContext = new InitialDirContext();
            }

            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) initialDirContext
                    .lookup(connectionFactory);
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueConnection.start();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            if (!lookup.booleanValue()) {
                queue = queueSession.createQueue(queueName);
            } else {
                queue = (Queue) initialDirContext.lookup(queueName);
            }

            queueSender = queueSession.createSender(queue);

            TextMessage textmessage = queueSession.createTextMessage();
            textmessage.setText(messageContent);

            if (properties != null) {
                Set<String> keySet = properties.keySet();
                for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
                    String propertyKey = (String) i.next();
                    Object value = (Object) properties.get(propertyKey);
                    if (value instanceof Integer) {
                        textmessage.setIntProperty(propertyKey, Integer.parseInt(value.toString()));
                    } else if (value instanceof String) {
                        textmessage.setStringProperty(propertyKey, value.toString());
                    } else if (value instanceof Boolean) {
                        textmessage.setBooleanProperty(propertyKey, Boolean.parseBoolean(value.toString()));
                    } else if (value instanceof Double) {
                        textmessage.setDoubleProperty(propertyKey, Double.parseDouble(value.toString()));
                    } else if (value instanceof Float) {
                        textmessage.setFloatProperty(propertyKey, Float.parseFloat(value.toString()));
                    } else if (value instanceof Long) {
                        textmessage.setLongProperty(propertyKey, Long.parseLong(value.toString()));
                    } else if (value instanceof Short) {
                        textmessage.setShortProperty(propertyKey, Short.parseShort(value.toString()));
                    } else if (value instanceof Byte) {
                        textmessage.setByteProperty(propertyKey, Byte.parseByte(value.toString()));
                    }
                }
            }

            queueSender.send(textmessage);

        } catch (JMSException | NamingException e) {
            logger.throwing(CLASS_NAME, METHOD_NAME, e);
            throw e;

        } finally {
            JMSException exceptionToThrow = null;

            if (queueSender != null) {
                try {
                    queueSender.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (queueSession != null) {
                try {
                    queueSession.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (exceptionToThrow != null) {
                logger.throwing(CLASS_NAME, METHOD_NAME, exceptionToThrow);
                throw exceptionToThrow;
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME);
    }

    /**
     * Retrieve a message from a JMS Queue.
     * 
     * @param initialContext
     *            The name of the initial context factory class to use when performing JNDI lookups.
     *            This value is used to set the <code>java.naming.context.initial</code> property
     *            when creating the InitialContext. If null, the default initial context JNDI
     *            properties are used.
     * @param providerUrl
     *            A reference to the naming provider to be used for JNDI lookups. This value is used
     *            to set the <code>java.naming.provider.url</code> property when creating the
     *            InitialContext. If null, the default initial context JNDI properties are used.
     * @param connectionFactory
     *            The JNDI name of the connection factory to use when establishing a connection to
     *            the messaging provider
     * @param queueName
     *            The name of the queue to which the message should be added.
     * @param lookup
     *            A flag which indicates whether to lookup the queue via JNDI or create it. If
     *            specified as <code>true</code>, then <code>queueName</code> represents the JNDI
     *            name of the queue to be looked up. If specified as <code>false</code>, then
     *            <code>queueName</code> represents the name of the queue to be created.
     * @param filter
     *            The filter to use when receiving the message from the queue. If specified as null,
     *            then no filter is used.
     * @return The message text.
     * @throws NamingException
     * @throws JMSException
     */
    public static String getMessage(String initialContext, String providerUrl, String connectionFactory,
            String queueName, Boolean lookup, String filter)
            throws NamingException, JMSException {

        final String METHOD_NAME = "getMessage";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { initialContext, providerUrl, connectionFactory,
                queueName, lookup, filter });

        String data = null;

        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        QueueReceiver queueReceiver = null;
        Queue queue = null;

        try {
            InitialDirContext initialDirContext = null;

            if (initialContext != null && providerUrl != null) {
                Hashtable<String, String> hashtable = new Hashtable<String, String>();
                hashtable.put("java.naming.factory.initial", initialContext);
                hashtable.put("java.naming.provider.url", providerUrl);
                hashtable.put("java.naming.referral", "throw");
                initialDirContext = new InitialDirContext(hashtable);
            } else {
                initialDirContext = new InitialDirContext();
            }

            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) initialDirContext
                    .lookup(connectionFactory);

            queueConnection = queueConnectionFactory.createQueueConnection();
            queueConnection.start();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

            if (!lookup.booleanValue()) {
                queue = queueSession.createQueue(queueName);
            } else {
                queue = (Queue) initialDirContext.lookup(queueName);
            }

            if (filter != null && !filter.equals("")) {
                queueReceiver = queueSession.createReceiver(queue, filter);
            } else {
                queueReceiver = queueSession.createReceiver(queue);
            }

            Message message = queueReceiver.receive(1000L);

            if (message instanceof TextMessage) {
                data = ((TextMessage) message).getText();
            }

        } catch (JMSException | NamingException e) {
            logger.throwing(CLASS_NAME, METHOD_NAME, e);
            throw e;

        } finally {
            JMSException exceptionToThrow = null;

            if (queueReceiver != null) {
                try {
                    queueReceiver.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (queueSession != null) {
                try {
                    queueSession.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (exceptionToThrow != null) {
                logger.throwing(CLASS_NAME, METHOD_NAME, exceptionToThrow);
                throw exceptionToThrow;
            }
        }

        logger.exiting(CLASS_NAME, METHOD_NAME, data);
        return data;
    }

}