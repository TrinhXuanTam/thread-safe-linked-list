# Thread Safe Singly Linked List

Implementation of a thread safe singly linked list in Java 17 & Spring Boot 3.
The list is implemented using a lock-free algorithm, which is based on the
CAS (Compare And Swap) operation. The list is thread safe and supports
the following operations:

- getAll: Returns all elements in the list
- push: Adds an element to the end of the list
- pop: Removes the last element from the list
- insertAfter: Inserts an element after a given element

## Getting Started

These instructions will give you a copy of the project up and running on
your local machine for development and testing purposes.

### Prerequisites

In order to run the application you need to have the following installed:

- Java 17
- Maven 3.8.3

Alternatively, you can use docker compose to run the application. In this case
you need to have Docker and Docker Compose installed.

## Documentation

This project uses Swagger to document the API. You can access the documentation
by running the application and navigating to the following URL:

```
localhost:8080/api/docs
```

Additionally, you can find a generated source code documentation in the `docs` folder.
