
## About The Project (Learning Project)
Using Spring Boot 3, adding more and more features over time, like docker, postgresql, unit tests/mocks with testcontainers/mockito, integration tests, authentication with Spring Security and JWT tokens, and UI with ReactJs.

### CustomerManager

Manage customer authentification and incorparating all building blocks to manage our customers.

![Screenshot 2023-09-30-204737-ui-customers-managing-2](https://github.com/ducsonledev/CustomerManager/assets/72577766/b455f01b-4392-4f8a-95a0-464744fdad0d)


### Work in Progress

- [ ] frontend with typescript/ReactJS
- [ ] deployment with terraform and AWS ECS

### Repository Structure

We present the repository structure as well as the functionality of the different major components in the following.

### File Overview

```
├── Dockerrun.aws.json
├── HELP.md
├── README.md
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── demospringfullstack
│   │   │           └── springbootexample
│   │   │               ├── SpringBootExampleApplication.java
│   │   │               ├── customer
│   │   │               │   ├── Customer.java
│   │   │               │   ├── CustomerController.java
│   │   │               │   ├── CustomerDAO.java
│   │   │               │   ├── CustomerJPADataAccessService.java
│   │   │               │   ├── CustomerListDataAccessService.java
│   │   │               │   ├── CustomerRegistrationRequest.java
│   │   │               │   ├── CustomerRepository.java
│   │   │               │   ├── CustomerService.java
│   │   │               │   └── CustomerUpdateRequest.java
│   │   │               └── exception
│   │   │                   ├── DuplicateResourceException.java
│   │   │                   ├── RequestValidationException.java
│   │   │                   └── ResourceNotFoundException.java
│   │   └── resources
│   │       ├── application.yml
│   │       ├── db
│   │       │   └── migration
│   │       │       ├── V1__Initial_Setup.sql
│   │       │       └── V2__Add_Datatype_Unique_To_Email.sql
│   │       ├── static
│   │       └── templates
│   └── test
│       └── java
│           └── com
│               └── demospringfullstack
│                   └── springbootexample
│                       ├── AbstractTestcontainers.java
│                       ├── TestcontainersTest.java
│                       ├── customer
│                       │   ├── CustomerJPADataAccessServiceTest.java
│                       │   ├── CustomerRepositoryTest.java
│                       │   └── CustomerServiceTest.java
│                       └── journey
│                           └── CustomerIT.java
└── target
    ├── classes
    │   ├── application.yml
    │   ├── com
    │   │   └── demospringfullstack
    │   │       └── springbootexample
    │   │           ├── SpringBootExampleApplication.class
    │   │           ├── customer
    │   │           │   ├── Customer.class
    │   │           │   ├── CustomerController.class
    │   │           │   ├── CustomerDAO.class
    │   │           │   ├── CustomerJPADataAccessService.class
    │   │           │   ├── CustomerListDataAccessService.class
    │   │           │   ├── CustomerRegistrationRequest.class
    │   │           │   ├── CustomerRepository.class
    │   │           │   ├── CustomerService.class
    │   │           │   └── CustomerUpdateRequest.class
    │   │           └── exception
    │   │               ├── DuplicateResourceException.class
    │   │               ├── RequestValidationException.class
    │   │               └── ResourceNotFoundException.class
    │   └── db
    │       └── migration
    │           ├── V1__Initial_Setup.sql
    │           └── V2__Add_Datatype_Unique_To_Email.sql
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── jib-cache
    │   ├── jib-classpath-file
    │   ├── jib-main-class-file
    │   ├── layers
    │   └── ...
    ├── jib-image.digest
    ├── jib-image.id
    ├── jib-image.json
    ├── maven-archiver
    │   └── pom.properties
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
                    └── journey
                        ├── CustomerIT$1.class
                        ├── CustomerIT$2.class
                        ├── CustomerIT$3.class
                        ├── CustomerIT$4.class
                        └── CustomerIT.class


```
### Data

Random User Image: https://randomuser.me/

### Prerequisites

See and copy dependencies to `pom.xml`.
