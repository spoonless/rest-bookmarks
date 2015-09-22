bookmarks Web API
=================

A Web API that allows you to store and share bookmarks online. 
A bookmark is a URL associated with a name and a description.

This service is only deployed for educational purposes. 
More specifically, this service demonstrates the usage of REST constraints, 
hypermedia and some HTTP advanced features like content negotiation, conditional requests and cache management.

All resources created with this API are only temporarily stored and the total amount of bookmarks is limited. 
Hence, once the limit is reached, a new resource will automatically destroy the oldest one.

The documentation is kept as minimal as possible on purpose.
You should use your knowledge of HTTP, REST architecture to find out how to use this API.

Running the server
------------------

The project is based on Java 8, Jetty and Maven 3. To run the server, you can use the command

  mvn jetty:run

After few seconds, the service should be available on

  http://localhost:8080
 