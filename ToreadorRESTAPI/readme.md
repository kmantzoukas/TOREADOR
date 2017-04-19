![TOREADOR logo](http://www.toreador-project.eu/wp-content/themes/acqualiofilizzata/images/logo-header.png)

The TOREADOR REST API is a collection of REST services that allows the execution of operations against the TOREADOR database. It enables the generation and coordinated execution of prism commands on prism models and prism properties.
## Installation
The TOREADOR REST API requires Apache Maven v4+ to run.
Ideally, you will also need to install Git to clone and build the project from the source.

Clone the TOREADOR repository locally. To do so use the following command:
```sh
 git clone https://github.com/kmantzoukas/TOREADOR.git
```

# REST API documentation
A detailed view of the API can be found [here](http://10.207.1.102:8080/swagger-ui.html).
The resources manipulated by the API are the following:

  - __User__ - A list with all the users of the system
  - __PrismRequest__ - A list with all the prism requests
  - __EverestRequest__ - A list with all the EVEREST requests

# Build the project from source
In order to build the project navigate to the project's directory and use Maven to build it.
```sh
$ cd TOREADOR/ToreadorRESTAPI
$ mvn clean package
```

# Deploy the project at an embedded Apache Tomcat 7
In order to deploy the project navigate to the project's directory and use Maven to deploy it.
```sh
$ cd TOREADOR/ToreadorRESTAPI
$ mvn clean install tomcat7:run
```

# Profiles
For conviniency and ease of deployment, during development two separate Maven profiles under the names **soi-vn-test01@city** and **localhost** have been created.**soi-vn-test01@city** is used for the deployment of the API on a Wildfly 8.2 installation at City's cluster and **localhost** for deployment at a local installation for test, debug and development.

  - Use the **soi-vn-test01@city** profile to delpoy the services remotely
```sh
$ cd TOREADOR/ToreadorRESTAPI
$ mvn -P soi-vn-test01@city wildfly:deploy
```

  - Use the **localhost** profile to delpoy the services localy
```sh
$ cd TOREADOR/ToreadorRESTAPI
$ mvn -P localhost wildfly:deploy
```
All the connection details, necessary for the deployment of the API such as IP addressses, usernames, password, etc. are included in the [pom.xml](https://github.com/kmantzoukas/TOREADOR/tree/master/ToreadorRESTAPI/pom.xml) file of the project in the profiles section. 

__IMPORTANT NOTE__: In order to perform any of the operations mentioned above, one needs to be connected at City's VPN at all times. In any other case all the IP addresses mentioned will be unreachable and the system will appear offline.