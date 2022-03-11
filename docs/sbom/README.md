# Software Bill of Materials

## Overview and References

[CERTCC SBOM Format Repository](https://github.com/CERTCC/SBOM)

[The Minimum Elements for an SBOM](https://www.ntia.doc.gov/files/ntia/publications/sbom_minimum_elements_report.pdf)

[NTIA Software Bill of Materials](https://www.ntia.gov/sbom)

[CycloneDX SBOM](https://cyclonedx.org/capabilities/sbom/)

[CycloneDX Examples](https://github.com/CycloneDX/bom-examples/tree/master/SBOM)

[Nexus IQ Support for CycloneDX Application Analysis](https://help.sonatype.com/iqserver/analysis/cyclonedx-application-analysis)

## SBOM Required Data Fields

*The Minimum Elements For a Software Bill of Materials (SBOM)*

*Pursuant to Executive Order 14028 on Improving the Nation’s Cybersecurity*

*The United States Department of Commerce*

*July 12, 2021*

> The core of an SBOM is a consistent, uniform structure that captures and presents information
> used to understand the components that make up software. Data fields contain baseline
> information about each component that should be tracked and maintained. The goal of these
> fields is to enable sufficient identification of these components to track them across the
> software supply chain and map them to other beneficial sources of data, such as vulnerability
> databases or license databases.

| Data Field               | Description                                       |
| :----------------------: | ------------------------------------------------- |
| Supplier Name            | The name of an entity that creates, defines, and identifies components |
| Component Name           | Designation assigned to a unit of software defined by the original supplier |
| Version of the Component | Identifier used by the supplier to specify a change in software from a previously identified version |
| Other Unique Identifiers | Other identifiers that are used to identify a component, or serve as a look-up key for relevant databases |
| Dependency Relationship  | Characterizing the relationship that an upstream component X is included in software Y |
| Author of SBOM Data      | The name of the entity that creates the SBOM data for this component |
| Timestamp                | Record of the date and time of the SBOM data assembly |

## Generating a Java Application SBOM with Maven

There exist Maven plugins which automate the creation of SBOMs. One such plugin is 
the [CycloneDX Maven Plugin](https://github.com/CycloneDX/cyclonedx-maven-plugin).

[Nexus IQ supports CycloneDX Application Analysis](https://help.sonatype.com/iqserver/analysis/cyclonedx-application-analysis)

The CycloneDX Maven plugin creates an aggregate of all direct and transitive 
dependencies of a project and creates a valid CycloneDX SBOM. CycloneDX is a 
lightweight software bill of materials (SBOM) specification designed for use 
in application security contexts and supply chain component analysis.

**Maven Usage**

```xml
<!-- uses default configuration -->
<plugins>
    <plugin>
        <groupId>org.cyclonedx</groupId>
        <artifactId>cyclonedx-maven-plugin</artifactId>
        <version>2.5.3</version>
    </plugin>
</plugins>
```

**Default Values**

```xml
<plugins>
    <plugin>
        <groupId>org.cyclonedx</groupId>
        <artifactId>cyclonedx-maven-plugin</artifactId>
        <version>2.5.3</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>makeAggregateBom</goal>
                </goals>
            </execution>
        </executions>
        <configuration>
            <projectType>library</projectType>
            <schemaVersion>1.3</schemaVersion>
            <includeBomSerialNumber>true</includeBomSerialNumber>
            <includeCompileScope>true</includeCompileScope>
            <includeProvidedScope>true</includeProvidedScope>
            <includeRuntimeScope>true</includeRuntimeScope>
            <includeSystemScope>true</includeSystemScope>
            <includeTestScope>false</includeTestScope>
            <includeLicenseText>false</includeLicenseText>
            <outputFormat>all</outputFormat>
            <outputName>bom</outputName>
        </configuration>
    </plugin>
</plugins>
```

## Generating a Node.js Application SBOM with Cyclone DX

[CycloneDX Node.js Module](https://www.npmjs.com/package/@cyclonedx/bom)

[CycloneDX Node Github](https://github.com/CycloneDX/cyclonedx-node-module)

The CycloneDX module for Node.js creates a valid CycloneDX Software Bill-of-Materials 
(SBOM) containing an aggregate of all project dependencies. CycloneDX is a lightweight 
SBOM specification that is easily created, human and machine readable, and simple to parse.

**Installing**

```sh
npm install -g @cyclonedx/bom
```

**Getting Help**

```text
$ cyclonedx-node -h
Usage: cyclonedx-node [options] [path]

Creates CycloneDX Software Bill of Materials (SBOM) from Node.js projects

Arguments:
  path                        Path to analyze

Options:
  -v, --version               output the version number
  -d, --include-dev           Include devDependencies (default: false)
  -l, --include-license-text  Include full license text (default: false)
  -o, --output <output>       Write BOM to file (default: "bom.xml")
  -t, --type <type>           Project type (default: "library")
  -ns, --no-serial-number     Do not include BOM serial number
  -h, --help                  display help for command
```

**Example (default: XML)**

```shell
cyclonedx-node
```

**Example (XML)**

```shell
cyclonedx-node -o bom.xml
```

**Usage with docker**

Run `cyclonedx/cyclonedx-node` docker image inside your project folder using:

```shell
docker run --rm \
  -v "$PWD":/src \
  -w /src \
  cyclonedx/cyclonedx-node -o /src/bom.xml
```

## SBOM and Cybersecurity Vulnerabilities

**A security risk report summarizing high-priority vulnerabilities should be**
**created by the DevOps team separately from the application SBOM(s).**

- Vulnerabilities can be enumerated by a scanning tool such as Nexus IQ which 
can take as an input either an actual application package (jar, zip, tgz) or an 
SBOM in a format such as CycloneDX

- The vulnerability report generated by such a scanning tool should then be 
audited by the DevOps team to determine which vulnerable dependencies warrant 
risk mitigation or further discussion

- This risk report should be in a brief format which facilitates communication 
with less technical stakeholders

- Risk reports are not static, but rather represent a snapshot in time because 
vulnerabilities are always changing with the evolving threat landscape

*The Minimum Elements For a Software Bill of Materials (SBOM)*

*Pursuant to Executive Order 14028 on Improving the Nation’s Cybersecurity*

*The United States Department of Commerce*

*July 12, 2021*

> **Vulnerabilities and SBOM**
> 
> The primary security use case for SBOM today is to identify known vulnerabilities and risks in
> the software supply chain. Some developers may choose to store vulnerability data inside the
> SBOM, and multiple SBOM data formats support this. There is clear value for the developer in
> this approach. However, SBOM data is primarily static. That is, it reflects the properties of the
> specific built software at a point in time. Vulnerability data, meanwhile, is dynamic and evolves
> over time. Software that was not previously deemed vulnerable may “become” vulnerable as new
> bugs are discovered.
> 
> Vulnerability data in the SBOM cannot be assumed to be complete and up-to-date, unless very
> specific conditions and processes are in place. This is unlikely across organizational boundaries.
> SBOM data will most likely have to ultimately be linked to vulnerability data sources. (This does
> not, however, limit the value of providing vulnerability, software weaknesses, and risk
> information to the consumer of the software).
> 
> It is recommended that vulnerability data be tracked in separate data structures from the SBOM.
> Operations should focus on mapping and linking between the two types of data as each evolve
> and the technologies mature. If vulnerability data is shared across organizations, both the
> vulnerability data and the SBOMs can use similar models for distribution, access control, and
> ingestion.
> 
> **Vulnerability and Exploitability in Dependencies**
> 
> While software vulnerabilities are a key component of understanding risk, not all vulnerabilities
> put users and organizations at risk. This is especially true when dealing with transitive
> dependencies. Not all vulnerabilities in components create risks in the software that depends on
> them. Some vendor data suggests that a relatively small percentage of vulnerable components
> have a security impact in the environment where that software is deployed. In the SBOM
> context, focusing on upstream vulnerable components that have been deemed not to have an
> impact on the downstream software will waste time and resources, without offering immediate
> security benefits.
> 
> Addressing this challenge requires two steps. First, the supplier must make some reliable
> determination that a vulnerability does not affect a specific piece of software. This could be for a
> range of reasons: the compiler might remove the affected code from the component, the
> vulnerability may not be reachable in the execution path, in-line protections exist, or a host of
> other reasons. These determinations are ideally already made today by product security incident
> response teams (PSIRTs) who track internal dependencies and risks.
> 
> The second step requires communication downstream to the next user of this SBOM data,
> asserting that the vulnerability does not put the organization at risk. This is straightforward,
> linking of a piece of software (the vulnerability in question) and the status of that vulnerability.
> The community refers to this as a "Vulnerability Exploitability eXchange," or VEX. The core of
> VEX is the communication of whether or not a given piece software is "affected" by a given
> vulnerability. In this case, if no action is deemed necessary, then the status is "not affected."
> VEX is being implemented today as a profile in the Common Security Advisory Framework,
> which enables machine-readable information about whether software is affected or not affected
> by a vulnerability and can link to specific SBOM data. Other implementations are possible. It is
> recommended that tools that analyze SBOM data for the customer build in the capability to
> automatically incorporate VEX data.

## Generating a Dependency Tree with Maven

The Maven goal `dependency:tree` can be used to display the dependency tree for a project:

https://maven.apache.org/plugins/maven-dependency-plugin/tree-mojo.html

This will combine all the project modules trees into a single text file:

```bash
$ mvn dependency:tree>dependency_tree.txt
```

This will create a text file called `dependency_tree.txt` under each project module directory:

```bash
$ mvn dependency:tree -DoutputFile="dependency_tree.txt" -DoutputType=text
```

This will create a text file called `dependency_tree.dot` under each project module directory:

```bash
$ mvn dependency:tree -DoutputFile="dependency_tree.dot" -DoutputType=dot
```
