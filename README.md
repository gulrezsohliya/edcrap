# eGov SmartCity eGovernance Suite [![License: MIT](https://img.shields.io/badge/license-MIT-green)](https://github.com/egovernments/frontend/blob/master/LICENSE)

[![Join the chat at https://gitter.im/egovernments/eGov](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/egovernments/eGov?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Build Status](http://ci.egovernments.org/buildStatus/icon?job=eGov-Github-Develop)](http://ci.egovernments.org/job/eGov-Github-Develop/) [![][codacy img]][codacy] [![][versioneye img]][versioneye]

eGovernments Foundation transforms urban governance with the use of scalable and replicable technology solutions that enable efficient and effective municipal operations, better decision making, and contact-less urban service delivery.

Our comprehensive software products enable Governments to put their resources to efficient use by minimising overheads. We also help bring in transparency, accountability and citizen centricity in the delivery of Government services.

eGovernments Foundation has been in the forefront of implementing eGovernance solutions since 2003. Our products have been serving over 325 ULBs across the country. Our time tested products have impacted the ULBs in a large way. We have also been involved in several eGovernance initiatives in the country.

Our primary business motivator is to increase the footprint of eGovernance across the country and help adoption in as many ULBs as possible. Going opensource with our products is a measure in this direction. It also gives us the ability to tap into the immense talent pool in India for strengthening and improving our cities. Open source also blends well with our ethical fabric of being open and transparent in our business.

#### Issue Tracking
Report issues via the [eGov Opensource JIRA][].
#### License
The eGov suit is released under version 3.0 of the [GPL][].
#### Powered By
<a href="https://www.atlassian.com/" target="_blank"><img src="http://downloads.egovernments.org/atlassian.png"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://github.com/" target="_blank"><img src="https://assets-cdn.github.com/images/modules/logos_page/Octocat.png" width="48"></a>

## User Guide
This section contains steps that are involved in build and deploy the application.
FAQ related to various deployment and development issues are discussed [here][FAQ]

## Setup with auto installer
* Clone the eGov repository.
```bash
$ mkdir -p ${HOME}/egovgithub && cd egovgithub
$ git clone  -b master --single-branch  git@github.com:egovernments/eGov-dcr-service.git
```
* First time setup which will install the stacks, build the source code, and deploys the artifact to Wildfly
```bash
$ cd ${HOME}/egovgithub/eGov-dcr-service && make all
```
-----
* To install the prerequisites eGov DCR Service Client stacks
```bash
$ cd ${HOME}/egovgithub/egov-dcr-client &&  make install
```
* To build the source code base
```bash
$ cd ${HOME}/egovgithub/egov-dcr-client && make build
```
* To deploy the artifact to WILDFLY
```bash
$ cd ${HOME}/egovgithub/egov-dcr-client && make deploy
```

## Manual Setup Instruction

#### Prerequisites

* Install [maven v3.2.x][Maven]
* Install [PostgreSQL v9.6][PostgreSQL]
* Install [Jboss Wildfly v11.x][Wildfly Customized]
* Install [Git 2.8.3][Git]
* Install [JDK 8 update 112 or higher][JDK8 build]
#### Database Setup
1. Create a database and user in postgres
2. Create the schema's called `state` & `generic`
3. Execute `ALTER ROLE <your_login_role> SET search_path TO state, generic, public;

```
NB: `<username>` user name of the loggedin system, enter the below command in terminal to find the username.
```bash 
$ id -un
```

#### Building Source
1. Clone the eGov repository.
```bash
$ mkdir egovgithub
$ cd egovgithub
$ git clone https://github.com/egovernments/egov-dcr-client.git
```
2. Change directory back to `<CLONED_REPO_DIR>/egov`

3. Run the following commands, this will cleans, compiles, tests, migrates database and generates ear artifact along with jars and wars appropriately

 ```bash
 mvn clean package -s settings.xml -Ddb.user=<db_username> -Ddb.password=<db_password> -Ddb.driver=org.postgresql.Driver -Ddb.url=<jdbc_url>
 ```

#### Redis Server Setup

By default eGov suit uses embedded redis server (work only in Linux & OSx), to make eGov suit works in Windows OS or if you want to run redis server as standalone then follow the installation steps below.
 
1. Installing redis server on Linux
 
 ```bash
 sudo apt-get install redis-server
 ```
2. Installing redis server on Windows :- There is no official installable available for Windows OS. To install redis on Windows OS, follow the instruction given in https://chocolatey.org/packages/redis-64

3. Once installed, Change directory to `<CLONED_REPO_DIR>/egov/egov-config/src/main/resources/config/` and create a file called `egov-erp-<username>.properties` and enter the following values based on your environment config.

  ```properties
 ## true by default
 redis.enable.embedded=false
 ```
 If required, you can override any default settings available in `/egov/egov-egi/src/main/resources/config/application-config.properties` by overriding the value in `egov-erp-<username>.properties`.

 to control the redis server host and port use the following property values (only required if installed with non default).

 ```properties
 ## Replace <your_redis_server_host> with your redis host, localhost by default
 redis.host.name=<your_redis_server_host>
 ## Replace <your_redis_server_port> with your redis port, 6379 by default
 redis.host.port=<your_redis_server_port>
 ```

#### Deploying Application

##### Configuring JBoss Wildfly

1. Download and unzip the customized JBoss Wildfly Server from [here][Wildfly Customized]. This server contains some additional jars that are required for the ERP.
2. In case properties needs to be overridden, edit the below file (This is only required if `egov-erp-<username>.properties` is not present)

  ```
  <JBOSS_HOME>/modules/system/layers/base/

  org
  └── egov
    └── settings
      └── main
          ├── config
          │   └── egov-erp-override.properties
          └── module.xml
  ```
3. Update settings in `standalone.xml` under `<JBOSS_HOME>/standalone/configuration`
 * Check Datasource setting is in sync with your database details.
  ```
  <connection-url>jdbc:postgresql://localhost:5432/<YOUR_DB_NAME></connection-url>
  <security>
    <user-name><YOUR_DB_USER_NAME></user-name>
    <password><YOUR_DB_USER_PASSWORD></password
  </security>
  ```
 * Check HTTP port configuration is correct in
  ```
  <socket-binding name="http" port="${jboss.http.port:8080}"/>
  ```
 * By default maximum post request size is 10MB. Please add and keep request size to as per necessary with the attribute max-post-size in the below location, 
  ```
  <server name="default-server">
                <http-listener name="default" socket-binding="http" max-post-size="104857600" redirect-socket="https" enable-http2="true"/>
                <https-listener name="https" socket-binding="https" security-realm="ApplicationRealm" enable-http2="true"/>
                <host name="default-host" alias="localhost">
                    <location name="/" handler="welcome-content"/>
                    <filter-ref name="server-header"/>
                    <filter-ref name="x-powered-by-header"/>
                </host>
            </server>
  ```
4. Change directory back to `<CLONED_REPO_DIR>/egov/dev-utils/deployment/` and run the below command
  ```
  $  chmod +x deploy.sh
  $ ./deploy.sh
  ```

 Alternatively this can be done manually by following the below steps.

  * Copy the generated exploded ear `<CLONED_REPO_DIR>/egov/egov-ear/target/egov-erp-edcr-impl-ear-<VERSION>.ear` in to your JBoss deployment folder `<JBOSS_HOME>/standalone/deployments`
  * Create or touch a file named `egov-erp-edcr-impl-ear-<VERSION>.ear.dodeploy` to make sure JBoss picks it up for auto deployment

5. Start the wildfly server by executing the below command

  ```
   $ cd <JBOSS_HOME>/bin/
   $ nohup ./standalone.sh -b 0.0.0.0 &

  ```
  In Mac OSx, it may also required to specify `-Djboss.modules.system.pkgs=org.jboss.byteman`
  
  `-b 0.0.0.0` only required if application accessed using IP address or  domain name.

6. Monitor the logs and in case of successful deployment, just hit `http://localhost:<YOUR_HTTP_PORT>/egi` in your favorite browser.
7. Login using username as `egovernments` and password `demo`
8. Use postman to scrutinize plan and fetch the scrutinized plan details.
    a) New Plan Scrutiny API - `http://<State_Domain_Name>:<YOUR_HTTP_PORT>/edcr/rest/dcr/scrutinizeplan`
    b) Fetch Scrutinized Plan Details API - `http://<State_Domain_Name>:<YOUR_HTTP_PORT>/edcr/rest/dcr/scrutinydetails`

#### Accessing the application using IP address and domain name

This section is to be referred only if you want the application to run using any ip address or domain name.

###### 1. To access the application using IP address:
* Have an entry in eg_city table in database with an IP address of the machine where application server is running (for ex: domainurl="172.16.2.164") to access application using IP address.
* Access the application using an url http://172.16.2.164:8080/egi/ where 172.16.2.164 is the IP and 8080 is the port of the machine where application server is running.

###### 2. To access the application using domain name:

* Have an entry in eg_city table in database with domain name (for ex: domainurl= "www.stateedcrscrutiny.org") to access application using domain name.
* Add the entry in hosts file of your system with details as 172.16.2.164    www.stateedcrscrutiny.org (This needs to be done both in server machine as well as the machines in which the application needs to be accessed since this is not a public domain).
* Access the application  using an url http://www.stateedcrscrutiny.org:8080/egi/ where www.stateedcrscrutiny.org is the domain name and 8080 is the port of the machine where application server is running.

Always start the wildfly server with the below command to access the application using IP address or  domain name.
```
 nohup ./standalone.sh -b 0.0.0.0 &
```

## Developer Guide
This section gives more details regarding developing and contributing to eGov suit.

#### Repository Structure
`egov` - folder contains all the source code of eGov opensource projects
#### Check out sources
`git clone git@github.com:egovernments/egov-dcr-client.git` or `https://github.com/egovernments/egov-dcr-client.git`
#### Prerequisites

* Install your favorite IDE for java project. Recommended Eclipse or IntelliJ IDEA
* Install [maven >= v3.2.x][Maven]
* Install [PostgreSQL >= v9.6 ][PostgreSQL]
* Install [Jboss Wildfly v11.x][Wildfly Customized]
* Install [Git 2.8.3][Git]
* Install [JDK 8 update 112 or later][JDK8 build]

__Note__: Please check in [eGov Tools Repository] for any of the above software installables before downloading from internet.


##### 1. Eclipse Deployment

* Install [Eclipse Photon]
* Import the cloned git repo using maven Import Existing Project.
* Install Jboss Tools and configure Wildfly Server.
* Since jasperreport related jar's are not available in maven central, we have to tell eclipse to find jar's in alternative place for that navigate to `Windows -> Preference -> Maven -> User Settings -> Browse Global Settings` and point settings.xml available under egov-erp/
* Now add your EAR project into the configured Wildfly server.
* Start Wildfly in debug mode, this will enable hot deployment.

##### 2. Intellij Deployment

* Install Intellij
* Open project
* In project settings set JDK to 1.8
* Add a run configuration for JBoss and point the JBOSS home to the wildfly unzipped folder
* Run

##### 3. Database Migration Procedure

* Any new sql files created should be added under directory `<CLONED_REPO_DIR>/egov/egov-<javaproject>/src/main/resources/db/migration`
* Core product DDL and DML should be added under `<CLONED_REPO_DIR>/egov/egov-<javaproject>/src/main/resources/db/migration/main`
* Core product sample data DML should be added under `<CLONED_REPO_DIR>/egov/egov-<javaproject>/src/main/resources/db/migration/sample`
* All sql scripts should be named with following format.
* Format `V<timestamp-in-YYYYMMDDHHMMSS-format>__<module-name>_<description>.sql`
* DB migration will automatically happen when application server starts, incase required while maven build use the above given maven command.

###### Migration file name sample
```
V20150918161507__egi_initial_data.sql

```
For more details refer [Flyway]

#  
Note: This system is supported

OS:-
* Linux (Recommended)
* Mac
* Windows (If Redis server standalone installed). 

Browser:-
* Chrome (Recommended)
* Firefox
* Internet Explorer

[Git]: https://git-scm.com/downloads
[JDK8 build]: http://www.oracle.com/technetwork/java/javase/downloads
[eGov Opensource JIRA]: http://issues.egovernments.org
[Wildfly Customized]: https://devops.egovernments.org/Downloads/wildfly/wildfly-11.0.0.Final.zip
[Eclipse Photon]: https://www.eclipse.org/downloads/packages/release/photon/r
[Spring Profiles]: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-environment
[Flyway]: http://flywaydb.org/documentation/
[eGov Tools Repository]: https://devops.egovernments.org/Downloads/
[PostgreSQL]: http://www.postgresql.org/download/
[Maven]: http://maven.apache.org/download.cgi
[GPL]: http://www.gnu.org/licenses/
[FAQ]:https://digit-discuss.atlassian.net/wiki/spaces/FAQ/overview
[GHPAGE]:https://egovernments.org/solutions.php
[versioneye]:https://www.versioneye.com/user/projects/5a0e82590fb24f00104d87b2
[versioneye img]:https://www.versioneye.com/user/projects/5a0e82590fb24f00104d87b2/badge.svg?style=flat-square
[codacy]:https://www.codacy.com/app/egovernments/eGov
[codacy img]:https://api.codacy.com/project/badge/Grade/8e3a009a64a44d1a9d75f78261272987


