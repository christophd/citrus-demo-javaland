Demo for "Arquillian &amp; Citrus" JavaLand 2016 talk ![Logo][1]
==============

Arquillian &amp; Citrus JavaLand Demo
---------

The employee registry demo application is based on the JEE 7 sample application and manages a list of employees. Each employee has a name and an age property. In addition to that you can
specify optional email address and mobile number when creating a new employee. Based on the properties of the new employee an automatic welcome Mail message or a welcome SMS SOAP message should 
be sent out to the mocked Citrus Mail server or SOAP server then.

The application exposes a Http REST API for adding, listing and deleting employees. Further more the application is listening for JMS messages on an ActiveMQ message broker.

The demo application starts with a simple JEE application tested with Arquillian in a Wildfly 10 application server. After that Citrus is added and the application is enhanced with more
features like Mail sending and SOAP message handling. Each step in development can be reviewed using the step-by-step commit history. Check out the different commit stages in order to see what 
exactly has been added and changed.

At the end stage we have JEE application that is communicating with a Mail server and a SOAP SMS gateway backend for sending SMS messages. Everything is automatically tested via Arquillian
and Citrus.

Using the demo
---------

This sample is using a Wildfly 10 managed application server container by Arquillian. The Wildfly 10 distribution is automatically loaded and unzipped during the Maven build.
The Arquillian test deploys the employee registry JEE application as micro web archive deployment and runs all integration tests. Following command should do the trick:

```
mvn clean verify
```

You should see several Citrus tests cases reporting success at the end of the Maven build. Open the project in your Java IDE and checkout how the Arquillian and Citrus tests are designed. 

Information
---------

For more information on Arquillian see [www.arquillian.org][4].

For more information on Citrus see [www.citrusframework.org][2], including
a complete [reference manual][3].

 [1]: http://www.citrusframework.org/images/brand_logo.png "Citrus"
 [2]: http://www.citrusframework.org
 [3]: http://www.citrusframework.org/reference/html/
 [4]: http://www.arquillian.org