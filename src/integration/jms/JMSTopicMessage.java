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

import java.util.Hashtable;
import java.util.logging.Logger;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

/**
 * This class provides a sample implementation for JMS topic services. <br>
 * JMSTopicMessage does not support the Secure Sockets Layer (SSL).
 */
public class JMSTopicMessage {

    private static final String CLASS_NAME = JMSTopicMessage.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);

    /**
     * Add a message to a JMS Topic.
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
     *            The content of the message to be added to the specified topic.
     * @param connectionFactory
     *            The JNDI name of the connection factory to use when establishing a connection to
     *            the messaging provider.
     * @param topicName
     *            The name of the topic to which the message should be added.
     * @throws NamingException
     * @throws JMSException
     */
    public static void putTextMessage(String initialContext, String providerUrl, String messageContent,
            String connectionFactory, String topicName)
            throws NamingException, JMSException {

        final String METHOD_NAME = "putTextMessage";
        logger.entering(CLASS_NAME, METHOD_NAME, new Object[] { initialContext, providerUrl, messageContent,
                connectionFactory, topicName });

        TopicConnection topicConnection = null;
        TopicSession topicSession = null;
        Topic topic = null;
        TopicPublisher publisher = null;

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

            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) initialDirContext
                    .lookup(connectionFactory);
            topicConnection = topicConnectionFactory.createTopicConnection();
            topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            topic = (Topic) initialDirContext.lookup(topicName);
            publisher = topicSession.createPublisher(topic);
            TextMessage msg = topicSession.createTextMessage();
            msg.setText(messageContent);
            publisher.publish(msg, DeliveryMode.PERSISTENT, 9, 0);

        } catch (JMSException | NamingException e) {
            logger.throwing(CLASS_NAME, METHOD_NAME, e);
            throw e;

        } finally {
            JMSException exceptionToThrow = null;

            if (publisher != null) {
                try {
                    publisher.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (topicSession != null) {
                try {
                    topicSession.close();
                } catch (JMSException e) {
                    exceptionToThrow = e;
                }
            }

            if (topicConnection != null) {
                try {
                    topicConnection.close();
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

}