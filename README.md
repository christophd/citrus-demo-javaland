Demo for "Arquillian &amp; Citrus" JavaLand 2016 talk ![Logo][1]
==============

Arquillian &amp; Citrus JavaLand Demo
---------

The employee registry demo application is based on a javaee 7 sample application and manages a list of employees. Each employee has a name and an age property. In addition to that you can
specify optional email address when creating a new employee. The automatic welcome mail message should be sent out to the mocked Citrus mail server then.

The application exposes a Http REST API for adding, listing and deleting employees. Further more the application is listening for JMS messages on an ActiveMQ message broker.

Using the demo
---------

This sample is using wildfly application server which is managed by Arquillian. The wildfly distribution is automatically loaded and unzipped during the Maven build.
The Arquillian test deploys the employee registry application as micro web archive deployment and runs all integration tests. Following command should do the trick:

```
mvn clean verify
```

You should see several tests cases reporting success at the end of the Maven build.

Information
---------

For more information on Arquillian see [www.arquillian.org][4].

For more information on Citrus see [www.citrusframework.org][2], including
a complete [reference manual][3].

 [1]: http://www.citrusframework.org/images/brand_logo.png "Citrus"
 [2]: http://www.citrusframework.org
 [3]: http://www.citrusframework.org/reference/html/
 [4]: http://www.arquillian.org