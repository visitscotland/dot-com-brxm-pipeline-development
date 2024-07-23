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

## Releasing the project

The commands to start and complete the release are the following

    ci/start-release.sh

    ci/finish-release.sh

You can check more in depth documentation in [this page](doc/how-to/release-process.md) 
    
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
 

## References
- [BloomReach Enterprise Documentation](https://xmdocumentation.bloomreach.com/library/enterprise/enterprise-features/enterprise-configuration-management/configuration-verifier.html)
- [How to run a standard BloomReach Project](doc/br-default.md)
