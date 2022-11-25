# Offer technical test
 
This project implemented in Spring boot is a REST API that exposes 2 services:
- The first allows to subscribe/register a user
- The second allows to display the details of a subscribed user
### Architecture 
The project contains several layers:
-	**Domain**: It represents data and can be persisted to database (H2: accessible via this link: http://localhost:8091/h2-console).
-	**DTO**: *Data Transfer Object* is used to transfer data from one processor context to another. It’s like a copy of the entity. The conversion of the entity (User) to a UserDto (mapping) is done thanks to the library mapstruct.
-	**Repositories**: It is the layer that deals with CRUD operations.
-	**Services**: It provides some services. Its utility is to make business logic.
-	**Controllers**:  It is in charge of user request handling.
### Other fonctionalities
- Spring AOP Logging: An aspect is implemented to log execution of the controller using Spring AOP.  It logs input and output arguments of each method of the controller as well as the processing time.
- Exceptions: A controller advice is used to handle exceptions across the whole application in one global handling component.
### Tests
-	**Unit tests:** Unit tests were realized on all Controller and service methods (junit4).
-	**Integration test:** An integration test, that focuses on integrating different layers of the application, was realized. 
### Documentation 
-	To generate the api documentation, I used the *springdoc-openapi* Java library then I integrated springdoc-openapi with *Swagger UI* so that I can interact with the API. The Swagger UI page is available at: http://localhost:8091/swagger-ui.html and the OpenAPI description is available at the following url for json format: http://localhost:8091/user-api-doc/.
-	The documentation is available in yaml format as well, on the following path : http://localhost:8091/user-api-doc.yaml. I also put it in the root of the project.

