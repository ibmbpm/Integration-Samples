# IBM Business Automation Workflow Integration Samples

This repository provides a library of sample Java source code that you can either use as-is or
adapt to your project requirements. The primary targets of this library are IBM Business Automation Workflow on Containers
and IBM Cloud Pak for Business Automation but you can also use it with IBM Business Automation Workflow in the traditional WebSphere environment. 

* The `integration.file.Files` class provides methods to read and write text files on the file system.
* The `integration.jms.JMSMessage` class provides methods to send and receive data from a JMS queue. 
* The `integration.jms.JMSTopicMessage` class provides a method to publish data on a JMS topic.
* The `integration.mail.Mail` class provides methods to send and receive emails from a mail server.

The source code is provided in the **src** folder. To use the Java code in your process application or toolkit
you must create a JAR file, add as a Server File to your application, and create
an External Java Service that exposes the methods of the corresponding Java class.

As a replacement of the corresponding services in the traditional System Data toolkit, the 
functionality is also provided in the form of Service Flows and External Services in the
following toolkits in the **twx** folder.


## File Integration Toolkit

The File Integration toolkit **FileIntegration.twx** includes the following Service Flows: 

* Service Flow **Read Text File**
* Service Flow **Write Text File**

If you use the **Read Text File** Service Flow instead of the deprecated one that is contained in the traditional
System Data toolkit you might notice the following difference:

* `limit` - Additional optional input parameter. If specified, an exception is thrown if the size of the
read data exeeds the specified value. This parameter replaces the `<server> <limits> <data-retrieved> <hard>`
configuration parameter in *99Local.xml* that is used in the traditional System Data toolkit.


### Providing storage in Containers

To read from files or write data to files you must prepare a Persistent Volume, as described in the step 
[Prepare storage for IBM Business Automation Workflow](https://www.ibm.com/support/knowledgecenter/SS8JB4_20.x/com.ibm.wbpm.imuc.container.doc/topics/tsk_bawprep_storage.html)
in Preparing storage. See also 
[IBM Cloud Pak for Business Automation: Preparing storage](https://www.ibm.com/support/knowledgecenter/SSYHZ8_20.0.x/com.ibm.dba.install/op_topics/tsk_bawprep_storage.html). 


## JMS Integration Toolkit

The JMS Integration toolkit **JMSIntegration.twx** includes the following External Services that you can use instead
of the Java classes `teamworks.JMSMessage` and `teamworks.JMSTopicMessage`.

* External Service **JMSMessage**
* External Service **JMSTopicMessage**

If you use these services instead of the 'teamworks.JMSMessage` and `teamworks.JMSTopicMessage` Java classes
that are contained in the traditional System Data toolkit you might notice the following difference:

* `initialContext` and `providerUrl` - Now optional input parameters. If null, the default initial context JNDI
properties are used.

To invoke an operation of an External Service, select the External Service in the implementation
section of a Service Task, select the appropriate operation, and map the data that is used in the
service flow to the input and output for the Java method.


### Configuring a JMS client in Containers

The `wasJmsClient-2.0` and `wasJmsSecurity-1.0` features are already enabled to act as a secure JMS client in a distributed environment.
To perform JNDI lookups the `jndi-1.0` feature is also enabled. You still need to configure the required JMS resource: 

1. Define a JMS resource in a Liberty server.xml snippet, by using a custom resource. For more information, see
[Customizing runtime server properties](https://www.ibm.com/support/knowledgecenter/SSYHZ8_20.0.x/com.ibm.dba.install/op_topics/tsk_post_baw_runtime.html)
and [Configuring the client for enabling JMS messaging between multiple Liberty servers](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_msg_multi_client.html).


## Mail Integration Toolkit

The Mail Integration toolkit **MailIntegration.twx** includes the following Service Flows:

* Service Flow **Read email via IMAP**
* Service Flow **Read email via POP**
* Service Flow **Send email via SMTP**

If you use the **Read email via IMAP** and **Read email via POP** Service Flows instead of those that are contained
in the traditional System Data toolkit you might notice the following differences:

* `attachmentsDirectory` - Additional optional input parameter to specify the directory for mail attachments. 
* `messageLimit` - Additional optional input parameter. If specified, an exception is thrown if the number of messages read data exeeds the 
specified value. This parameter replaces the `<server> <limits> <rows-read> <hard>` configuration parameter
in *99Local.xml* that is used in the System Data toolkit.
* `dataLimit` - Additional optional input parameter. If specified, an exception is thrown if the size of the formatted messages exceeds
the specified value. This parameter replaces the `<server> <limits> <data-retrieved> <hard>` configuration parameter
in *99Local.xml* that is used in the System Data toolkit.

If you use the **Send email via SMTP** Service Flow instead of the one that is contained in the traditional
System Data toolkit you might notice the following difference:

* `attachmentFileNames` - In case of attachments the simple file names instead of the path
names are now passed to the email server.


### Configuring a mail client in Containers

The Mail Integration toolkit utilizes the JavaMail 1.5 API. On IBM Business Automation Workflow on Containers
and IBM Cloud Pak for Business Automation the `JavaMail 1.5` feature is already enabled. 

1. To send attachments with your email you need an *PersistentVolume*. See section *Providing storage in Containers*
for an example procedure to create a PV and PVC using NFS.

2. Enable secure communication between Business Automation Workflow and your email server.

   1. Obtain the public certificate from the email server. [analogues Procedure, step 1](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_add_trust_cert.html)

   2. Add the email server certificate to the Business Automation Workflow TLS trust list. [analogues Procedure, step 3](https://www.ibm.com/support/knowledgecenter/SSYHZ8_19.0.x/com.ibm.dba.install/k8s_topics/tsk_basconfig_baw.html)


### Providing storage in Containers

To send or receive mail attachments you must prepare a Persistent Volume, as described in the step 
[Prepare storage for IBM Business Automation Workflow](https://www.ibm.com/support/knowledgecenter/SS8JB4_20.x/com.ibm.wbpm.imuc.container.doc/topics/tsk_bawprep_storage.html)
in Preparing storage. See also 
[IBM Cloud Pak for Business Automation: Preparing storage](https://www.ibm.com/support/knowledgecenter/SSYHZ8_20.0.x/com.ibm.dba.install/op_topics/tsk_bawprep_storage.html). 

   
# License

See the **License folder** for more information about how this project is licensed.
