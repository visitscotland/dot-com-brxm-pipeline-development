VisitScotland 
=============

## Getting started
### Configure project's Git hooks

We use custom Git hooks to test the format of our commit messages to ensure that they meet the required standard. These custom hooks are stored in the folder `.custom-hooks` so that they can be maintained on BitBucket.

Use `git config core.hooksPath .custom-hooks` to configure Git to use the custom hooks directory instead of the default hooks.

***Note**: if you're using a GUI Git client, such as GitKraken, you'll need to manually copy the `.custom-hooks` files to `.git/hooks/` as these clients might not support `core.hooksPath`.*

## Running the project

This project uses the Maven Cargo plugin to run Essentials, the CMS and site locally in Tomcat.
From the project root folder, execute:

    mvn clean verify 
    mvn -P cargo.run

Alternatively, developers might prefer to run a quicker version were UI packages are not built.
Please, note that the full run is advised every time the branch is changed or when front end changes 
are expected.

Unix Based Console

    mvn clean verify -P \!fed-build -DskipTests && mvn -P cargo.run

or

    mvn clean verify -P !fed-build -DskipTests && mvn -P cargo.run

## Debug the project

### IntelliJ IDEA

To define the configuration follow the next steps:

1. Create a new running configuration by clicking *Edit Configuration...* in the drop down next to the *Run* Button
2. Click on Add new Configuration ("+" Button)
3. Select the type Remote JVM Debug
4. Change the port to 8000
5. Apply Changes
6. Click the Debug button next to the Run button in the bar.(The CMS needs to be running for this configuration to work)

### Other tips

Run the debugger in a different port:

    mvn -P cargo.run -Dcargo.debug.address=9000
    
### SpringBoot Actuators

The database actuator can be activated by adding the variable `cliOptions=dbActuator`

    mvn clean verify -P!fed-build -DskipTests -DcliOptions=dbActuator && mvn -Pcargo.run
    
## Navigating through the CMS

#### Useful URLs
This is a bunch of useful URLs for local development 

- http://localhost:8080/site: Display the site that would be presented to the final Internet User. Unpublished documents
will not be available.
- http://localhost:8080/cms: CMS (Content Management System) tool for managing the content
- http://localhost:8080/cms/console: JCR Console that contains the configuration and the data of the CMS
- http://localhost:8080/cms/repository: Query tool for the JCR Console. It can be queried through xPath or JCR
- http://localhost:8080/essentials: Out-of-the-box set of tools that add some extra capabilities to the CMS
- http://localhost:8080/actuator: Actuators functionality that exposes information about the architecture of the application

### Development credentials
- Username: admin
- Password: admin (Do not share it. It is a secret)

Windows Based Console

    mvn clean verify -P !fed-build -DskipTests
    mvn -P cargo.run

or

    mvn clean verify -P !fed-build -DskipTests &&mvn -P cargo.run


    
## Troubleshooting
**I get the following error when I try to clone the message: _fatal: cannot create directory at '{some big path}': Filename too long_**

Git has a limit of 260 characters for a filename in Windows when Git is compiled with msys. You can circumvent the issue by executing the following command:

     git config --system core.longpaths true

**The front-end build doesn't finish or finish with an exception**

To be documented

_QuickFix: Install NPM and Yarn manually with the versions specified in ui-integration/pom.xml_

Verify integrity of the Configuration
===============================
The Bloomreach Experience Manager Configuration Verifier, brXM CV or just CV in short, is a tool to support projects and developers during the upgrade between major Bloomreach Experience Manager releases, and is available to Bloomreach Experience Manager customers and partners.

## Steps
1. Execute the following commands:
         
       mvn -P create-configuration-verifier-config
       mvn -P cargo.run,verify-configuration
2. 

## References
- https://xmdocumentation.bloomreach.com/library/enterprise/enterprise-features/enterprise-configuration-management/configuration-verifier.html





BloomReach Instruction (Legacy) 
===============================

## Running Locally
This project uses the Maven Cargo plugin to run Essentials, the CMS and site locally in Tomcat.
From the project root folder, execute:

    mvn clean verify
    mvn -P cargo.run

By default, this includes and bootstraps repository data from the repository-data/development module,
which is deployed by cargo to the Tomcat shared/lib.
If you want or need to start *without* bootstrapping the development data, for example when testing
against an existing repository, you can specify the *additional* Maven profile without-development-data to do so:

    mvn -P cargo.run,without-development-data

This additional profile will modify the target location for the development module to the Tomcat temp/ folder so that
it won't be seen and picked up during the repository bootstrap process.

Access the BloomReach setup application at <http://localhost:8080/essentials>.
After your project is set up, access the CMS at <http://localhost:8080/cms> and the site at <http://localhost:8080/visitscotland>.
Logs are located in target/tomcat9x/logs


## Best Practice for Development

Use the option `-Drepo.path=/some/path/to/repository` during start up. This will avoid
your repository to be cleared when you do a mvn clean.

For example start your project with:

    mvn -P cargo.run -Drepo.path=/home/usr/tmp/repo


Automatic Export
================

Automatic export of repository changes to the filesystem is turned on by default. To control this behavior, log into
<http://localhost:8080/cms/console> and press the "Enable/Disable Auto Export" button at the top right. To set this
as the default for your project edit the file
./repository-data/application/src/main/resources/hcm-config/configuration/modules/autoexport-module.yaml


Building Distributions
======================

To build Tomcat distribution tarballs:

    mvn clean verify
    mvn -P dist
      or
    mvn -P dist-with-development-data

The `dist` profile will produce in the /target directory a distribution tarball, containing the main deployable wars and
shared libraries.

The `dist-with-development-data` profile will produce a distribution-with-development-data tarball, also containing the
repository-data-development jar in the shared/lib directory. This kind of distribution is meant to be used for
deployments to development environments, for instance local deployments or deployments to a continuous integration (CI)
system. (Initially, this module contains only "author" and "editor" example users for use in testing. Other data must be
placed in this module explicitly by developers, for demo or testing purposes, etc.)

See also src/main/assembly/*.xml if you need to customize the distributions.


Distributing Additional Site Projects
=====================================

Note that if your organization is using multiple site projects, you must configure the assembly of a distribution to
include all the separate site webapps for deployment. This project is designed for stand-alone use and does not
automatically include any additional, externally-maintained site webapps.


Running the brXM Project in a Docker Container
======================

To run the brXM project in a docker container, you must install the project, build the docker image and run the docker
image respectively.

First install the project:

    mvn clean install

Then build the brXM docker image:

    mvn -Pdocker.build

This maven profile will create a docker image and add it to the local docker registry. The new image will be tagged
as com.visitscotland/visitscotland:0.1.0-SNAPSHOT

To run the image with in-memory h2 database:

    mvn -Pdocker.run


Running with an embedded MySQL database. To create & run environment containing builtin MySQL DB just run:

    mvn -Pdocker.run,docker.mysql

As a result, default db credentials will be used (admin/admin) and DB name will be the same as project's artifactId (e.g. myproject)

Running with an embedded PostgreSQL database. To create & run environment containing builtin PostgreSQL DB just run:

    mvn -Pdocker.run,docker.postgres

As a result, default db credentials will be used (admin/admin) and DB name will be the same as project's artifactId (e.g. myproject)

To run the image with an external mysql database, add the provided database name, username and password below to the properties
section of your project's pom.xml:

    <docker.db.host>DATABASE_HOSTNAME</docker.db.host>
    <docker.db.port>DATABASE_PORT</docker.db.port>
    <docker.db.schema>DATABASE_NAME</docker.db.schema>
    <docker.db.username>DATABASE_USERNAME</docker.db.username>
    <docker.db.password>DATABASE_PASSWORD</docker.db.password>

Then run:

    mvn -Pdocker.run,mysql

To run the image with an external postgresql database, add the same db properties as above, then run:

    mvn -Pdocker.run,postgres

After running the docker image, application logs will be shown on the terminal window.
