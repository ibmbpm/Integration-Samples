# IBM Business Automation Workflow Integration Samples

This repository provides a library of sample Java source code that you can either use as-is or
adapt to your project requirements. The primary targets of this library are IBM Business Automation Workflow on Containers
and IBM Cloud Pak for Automation but you can also use it with IBM Business Automation Workflow in the traditional WebSphere environment. 

* The `integration.file.Files` class provides methods to read and write text files on the file system.

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
[IBM Cloud Pak for Automation: Preparing storage](https://www.ibm.com/support/knowledgecenter/SSYHZ8_20.0.x/com.ibm.dba.install/op_topics/tsk_bawprep_storage.html). 

   
# License

See the **License folder** for more information about how this project is licensed.