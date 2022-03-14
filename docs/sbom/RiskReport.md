# Cybersecurity Risk Report

**Application Version**

    com.visitscotland:dot-com-brxm:1.0.1

**Vulnerability Scoring Method**

[CVSS v3.1](https://www.first.org/cvss/calculator/3.1)

**Vulnerability Enumeration Framework**

STRIDE:

**S**poofing: 🎭 *impersonating something or someone else*

**T**ampering: ✂️ *modifying data or code*

**R**epudiation: ❔ *claiming to have not performed an action*

**I**nformation Disclosure: 👀 *exposing information to someone not authorized to see it*

**D**enial of Service: 🚫 *deny or degrade service to users*

**E**levation of Privilege: ⏫ *gain capabilities without proper authorization*

**Package Scanning Tool**

[Sonatype Nexus IQ](https://help.sonatype.com/iqserver/product-information/licensing-and-features)

**Scan Report Links**

[VSCOM\visitscotland-2019\site.war](https://iq.visitscotland.com/assets/index.html#/applicationReport/visitscotland-site/cc224f72876d4fa690926b4adf01db56/policy)

## Application: VSCOM\visitscotland-2019\site.war 

**Summary of Highly Vulnerable Maven Dependencies**

| Package | Threat Level | CVSS Score | STRIDE | Dependency Type |
| ------- | :----------: | :--------: | :----: | :-------------: |
| [org.freemarker:freemarker:2.3.28](#orgfreemarkerfreemarker2328) | HIGH 🔥 | 7.7 🟠 | Elevation of Privilege ⏫ | Direct ➡️ |
| [org.apache.commons:commons-configuration2:2.2](#orgapachecommonscommons-configuration222) | CRITICAL 🔥🔥🔥 | 10.0 🔴 | Elevation of Privilege ⏫ | Indirect 🔀 |
| [com.google.code.gson:gson:2.2.4](#comgooglecodegsongson224) | HIGH 🔥 | 7.5 🟠 | Denial of Service 🚫 | Indirect 🔀 |
| [org.codehaus.jackson:jackson-mapper-asl:1.9.12](#orgcodehausjacksonjackson-mapper-asl1912) | CRITICAL 🔥🔥🔥 | 9.8 🔴 | Elevation of Privilege ⏫ | Indirect 🔀 |
| [org.jsoup:jsoup:1.8.3](#orgjsoupjsoup183) | HIGH 🔥 | 7.5 🟠 | Denial of Service 🚫 | Indirect 🔀 |
| [org.thymeleaf:thymeleaf-spring5:3.0.12.RELEASE](#orgthymeleafthymeleaf-spring53012release) | CRITICAL 🔥🔥🔥 | 9.8 🔴 | Elevation of Privilege ⏫ | Indirect 🔀 |

**Maven Package Dependency Trees**

*com.visitscotland:site.war*

---

- ...
- [org.freemarker:freemarker:2.3.28](#orgfreemarkerfreemarker2328)
- ...
- com.visitscotland:utils:jar:2.0.1
- - [org.apache.commons:commons-configuration2:2.2](#orgapachecommonscommons-configuration222)
- ...
- com.visitscotland:data-service-api:jar:1.2.1
- - [com.google.code.gson:gson:2.2.4](#comgooglecodegsongson224)
- - [org.codehaus.jackson:jackson-mapper-asl:1.9.12](#orgcodehausjacksonjackson-mapper-asl1912)
- - [org.jsoup:jsoup:1.8.3](#orgjsoupjsoup183)
- ...

*com.visitscotland:dot-com-brxm-cms:war*

---

- ...
- org.springframework.boot:spring-boot-starter-thymeleaf:jar:2.4.12
- - [org.thymeleaf:thymeleaf-spring5:jar:3.0.12.RELEASE](#orgthymeleafthymeleaf-spring53012release)
- ...

**Summary of Highly Vulnerable NPM Dependencies**

| Package       | Versions Detected | Parent Library                | Usage                               | STRIDE Category            | CVE                                                               |
| ------------- | :---------------: | :---------------------------: | :---------------------------------: | :------------------------: | :---------------------------------------------------------------: |
| [lodash](#lodash)        | 4.17.15, 4.17.20  | cheerio@1.0.0-rc.3            | ssr\server\ssr.js                   | Elevation of Privilege ⏫  | [CVE-2021-23337](https://nvd.nist.gov/vuln/detail/CVE-2021-23337) |
| [ansi-html](#ansi-html)     | 0.0.7             | webpack-hot-middleware@2.25.0 | ssr\server\setup-dev-server.js      | Denial of Service 🚫       | [CVE-2021-23424](https://nvd.nist.gov/vuln/detail/CVE-2021-23424) |
| [axios](#axios)         | 0.21.1            | axios@0.21.1                  | src\components\...\CannedSearch.vue | Denial of Service 🚫       | [CVE-2021-3749](https://nvd.nist.gov/vuln/detail/CVE-2021-3749)   |

---

### org.freemarker:freemarker:2.3.28

---

<details>

  <summary>Security Threat Level: 🔥 (HIGH)</summary>

    Lorem ipsom dolor sit amet!

</details>

<details>

  <summary>CVSS Score: 7.7 🟠</summary>

    CVSS:3.1/AV:N/AC:H/PR:H/UI:N/S:C/C:H/I:H/A:N

</details>

<details>
  <summary>Dependency Type: Direct ➡️</summary>

    - VSCOM\visitscotland-2019\pom.xml
    - VSCOM\visitscotland-2019\repository-data\webfiles\pom.xml
    - VSCOM\visitscotland-2019\site\components\pom.xml

</details>

<details>
  <summary>Primary STRIDE Category: Elevation of Privilege ⏫</summary>

> Apache FreeMarker is vulnerable to Code Injection. The createUnsafeMethodsSet method in UnsafeMethods.class implements a deny list to prevent access to various unsafe methods. However the deny list does not include java.lang.ClassLoader.getResourceAsStream and java.security.ProtectionDomain.getClassLoader among other unsafe methods. An attacker with template editing permissions can use these methods as part of an attack to access files in the application's classpath and load references to arbitrary classes, ultimately resulting in potential arbitrary code execution.
>
> **Detection**
>
> The application is vulnerable by using this component.
>
> **Recommendation**
>
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue. Additionally, while fixed versions may not be vulnerable to this specific issue, the ability to create and edit templates carries inherent security risks, and such permissions should only be given to trusted users.
>
> *Reference: https://freemarker.apache.org/docs/app_faq.html#faq_template_uploading_security*
>
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
>
> **Root Cause**
> freemarker-2.3.28.jarfreemarker/ext/beans/UnsafeMethods.class[2.3.21, 2.3.30)
>
> **Advisories**
>
> Attack: https://ackcent.com/in-depth-freemarker-template-injection/
>
> Project: https://github.com/apache/freemarker/pull/62
>
> Project: https://issues.apache.org/jira/browse/FREEMARKER-124

</details>

---

### org.apache.commons:commons-configuration2:2.2

---

<details>

  <summary>Security Threat Level: 🔥🔥🔥 (CRITICAL)</summary>

    Lorem ipsom dolor sit amet!

</details>

<details>
  <summary>CVSS Score: 10.0 🔴</summary>

    CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:C/C:H/I:H/A:H

</details>

<details>

  <summary>Dependency Type: Indirect 🔀</summary>

    Parent Package: 
    
    com.visitscotland:utils:jar:2.0.1
    
    Parent Included By:
    
    - VSCOM\visitscotland-2019\site\pom.xml

</details>

<details>

  <summary>Primary STRIDE Category: Elevation of Privilege ⏫</summary>

> Apache Commons Configuration uses a third-party library to parse YAML files which by default allows the instantiation of classes if the YAML includes special statements. Apache Commons Configuration versions 2.2, 2.3, 2.4, 2.5, 2.6 did not change the default settings of this library. So if a YAML file was loaded from an untrusted source, it could therefore load and execute code out of the control of the host application.
>
> **Explanation**
>
> Apache Commons Configuration is vulnerable to Arbitrary Code Execution. The read() method in the YAMLConfiguration class fails to control class instantiation when loading YAML Files, allowing configurations with special statements to load and execute code. A remote attacker can craft a malicious YAML configuration to exploit this vulnerability and execute arbitrary code.
>
> **Detection**
>
> The application is vulnerable by using this component.
>
> **Recommendation**
>
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue.
>
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
>
> **Root Cause**
>
> commons-configuration2-2.2.jarorg/apache/commons/configuration2/YAMLConfiguration.class[2.2-RC1, 2.7-RC1)
>
> **Advisories**
>
> Third Party: https://www.openwall.com/lists/oss-security/2020/03/13/1

</details>

---

### com.google.code.gson:gson:2.2.4

---

<details>

  <summary>Security Threat Level: 🔥 (HIGH)</summary>

    Lorem ipsom dolor sit amet!

</details>

<details>

  <summary>CVSS Score: 7.5 🟠</summary>

    CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H

</details>

<details>

  <summary>Dependency Type: Indirect 🔀</summary>

    Parent Package: 
    
    com.visitscotland:data-service-api:jar:1.2.1
    
    Parent Included By:
    
    - VSCOM\visitscotland-2019\cms\pom.xml
    - VSCOM\visitscotland-2019\pom.xml
    - VSCOM\visitscotland-2019\site\pom.xml

</details>

<details>

  <summary>Primary STRIDE Category: Denial of Service 🚫</summary>

> The gson package is vulnerable Deserialization of Untrusted Data. The serializable LazilyParsedNumber, LinkedHashTreeMap, and LinkedTreeMap classes permit unsafe deserialization due to use of the default Serializable.readObject() implementation. A remote attacker can exploit this vulnerability by serializing and supplying any of the aforementioned objects to an affected application. This will result in a Denial of Service (DoS) condition or other unexpected behavior when the malicious object is deserialized.
Detection
>
>The application is vulnerable by using this component if it deserializes LazilyParsedNumber, LinkedHashTreeMap, or LinkedTreeMap objects from untrusted sources.
>
> **Recommendation**
>
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue.
>
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
>
> **Root Cause**
>
> gson-2.2.4.jarcom/google/gson/internal/LazilyParsedNumber.class[2.2.3, 2.8.9)
>
> **Advisories**
>
> Project: https://github.com/google/gson/pull/1991

</details>

---

### org.codehaus.jackson:jackson-mapper-asl:1.9.12

---

<details>

  <summary>Security Threat Level: 🔥🔥🔥 (CRITICAL)</summary>

    Lorem ipsom dolor sit amet!

</details>

<details>

  <summary>CVSS Score: 9.8 🔴</summary>

    CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H

</details>

<details>

  <summary>Dependency Type: Indirect 🔀</summary>

    Parent Package: 
    
    com.visitscotland:data-service-api:jar:1.2.1
    
    Parent Included By:

    - VSCOM\visitscotland-2019\cms\pom.xml
    - VSCOM\visitscotland-2019\pom.xml
    - VSCOM\visitscotland-2019\site\pom.xml

</details>

<details>

  <summary>Primary STRIDE Category: Elevation of Privilege ⏫</summary>

> A deserialization flaw was discovered in the jackson-databind, versions before 2.6.7.1, 2.7.9.1 and 2.8.9, which could allow an unauthenticated user to perform code execution by sending the maliciously crafted input to the readValue method of the ObjectMapper.
>
> **Explanation**
>
> jackson-databind is vulnerable to Remote Code Execution (RCE). The createBeanDeserializer() function in the BeanDeserializerFactory class allows untrusted Java objects to be deserialized. A remote attacker can exploit this by uploading a malicious serialized object that will result in RCE if the application attempts to deserialize it.
>
> **NOTE**: This vulnerability is also tracked by the Apache Struts team as S2-055
>
> **NOTE**: This CVE is a part of the series of CVEs (e.g. CVE-2019-16943, CVE-2017-15095, CVE-2017-17485, CVE-2018-5968, ...) that have been, and continue to be released by the Jackson Databind project, for the exact same deserialization vulnerability. To reduce unnecessary noise arising from duplicates and false positives, we have strategically implicated components so that they show the minimal number of CVEs necessary to represent the same vulnerability.
Detection
>
> The application is vulnerable by using this component, when default typing is enabled.
>
> **Note**: Spring Security has provided their own fix for this vulnerability (CVE-2017-4995). If this component is being used as part of Spring Security, then you are not vulnerable if you are running Spring Security 4.2.3.RELEASE or greater for 4.x or Spring Security 5.0.0.M2 or greater for 5.x.
>
> **Recommendation**
>
> Update: As of version 2.10.0, Jackson now provides a safe default typing solution that fully mitigates this vulnerability.
>
> Reference: https://medium.com/@cowtowncoder/jackson-2-10-features-cd880674d8a2
>
> In order to mitigate this vulnerability, we recommend upgrading to at least version 2.10.0 and changing any usages of enableDefaultTyping() to activateDefaultTyping().
>
> Alternatively, if upgrading is not a viable option, this vulnerability can be mitigated by disabling default typing. Instead, you will need to implement your own:
>
> > It is also possible to customize global defaulting, using ObjectMapper.setDefaultTyping(...) -- you just have to implement your own TypeResolverBuilder (which is not very difficult); and by doing so, can actually configure all aspects of type information. Builder itself is just a short-cut for building actual handlers.
>
> Reference: https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization
>
> Examples of implementing your own typing can be found by looking at Spring Security's fix or this Stack Overflow article
>
> **Root Cause**
>
> jackson-mapper-asl-1.9.12.jarorg/codehaus/jackson/map/TypeDeserializer.class[1.5.0 , 1.9.13.redhat-00006)
>
> **Advisories**
>
> Project: https://github.com/FasterXML/jackson-databind/issues/1599
>
> Third Party: https://blog.sonatype.com/jackson-databind-remote-code-execution
>
> Third Party: https://blog.sonatype.com/jackson-databind-the-end-of-the-blacklist
>
> Third Party: https://bugzilla.redhat.com/show_bug.cgi?id=CVE-2017-7525

</details>

---

### org.jsoup:jsoup:1.8.3

---

<details>

  <summary>Security Threat Level: 🔥 (HIGH)</summary>

    Lorem ipsom dolor sit amet!

</details>

<details>

  <summary>CVSS Score: 7.5 🟠</summary>

    CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H

</details>

<details>

  <summary>Dependency Type: Indirect 🔀</summary>

    Parent Package: 
    
    com.visitscotland:data-service-api:jar:1.2.1
    
    Parent Included By:

    - VSCOM\visitscotland-2019\cms\pom.xml
    - VSCOM\visitscotland-2019\pom.xml
    - VSCOM\visitscotland-2019\site\pom.xml

</details>

<details>

  <summary>Primary STRIDE Category: Denial of Service 🚫</summary>

> jsoup is a Java library for working with HTML. Those using jsoup versions prior to 1.14.2 to parse untrusted HTML or XML may be vulnerable to DOS attacks. If the parser is run on user supplied input, an attacker may supply content that causes the parser to get stuck (loop indefinitely until cancelled), to complete more slowly than usual, or to throw an unexpected exception. This effect may support a denial of service attack. The issue is patched in version 1.14.2. There are a few available workarounds. Users may rate limit input parsing, limit the size of inputs based on system resources, and/or implement thread watchdogs to cap and timeout parse runtimes.
>
> **Explanation**
>
> The jsoup is vulnerable due to Uncaught Exception. The appendTagName function in the Token.class incorrectly processes user input that contains unexpected characters in variable amounts. An attacker could leverage this behavior to crash the application by sending malformed input to the application, which may include multiple null characters.
>
> *Advisory Deviation Notice*: The Sonatype security research team discovered that the vulnerability is present from version 1.6.0 before 1.14.2, not all prior versions to 1.14.2 as the advisory states.
Detection
>
> The application is vulnerable by using this component.
>
> **Recommendation**
>
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue.
>
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
>
> **Root Cause**
>
> jsoup-1.8.3.jarorg/jsoup/parser/Token.class[1.6.0 , 1.14.2)
>
> **Advisories**
>
> Project: https://github.com/jhy/jsoup/security/advisories/GHSA-m72m-mhq2-9p6c

</details>

---

### org.thymeleaf:thymeleaf-spring5:3.0.12.RELEASE

---

<details>

  <summary>Security Threat Level: 🔥🔥🔥 (CRITICAL)</summary>

    Lorem ipsom dolor sit amet!

</details>

<details>

  <summary>CVSS Score: 9.8 🟠</summary>

    CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H

</details>

<details>

  <summary>Dependency Type: Indirect 🔀</summary>

    Parent Package: 
    
    org.springframework.boot:spring-boot-starter-thymeleaf:2.4.12
    
    Parent Included By:

    - VSCOM\visitscotland-2019\cms\pom.xml

</details>

<details>

  <summary>Primary STRIDE Category: Elevation of Privilege ⏫</summary>

> In the thymeleaf-spring5:3.0.12 component, thymeleaf combined with specific 
> scenarios in template injection may lead to remote code execution.
> 
> **Explanation**
> 
> The thymeleaf-spring5, thymeleaf-spring4 and thymeleaf-spring5 packages are 
> vulnerable due to Improper Input Validation. The getExpression() method in 
> the SPELVariableExpressionEvaluator class fails to prevent instantiation of 
> objects and the use of static methods in forbidden contexts. An attacker can 
> exploit this behavior by submitting a crafted request with a fragment 
> expression in the view name field, potentially leading to Remote Code Execution (RCE).
> 
> **Detection**
> 
> The application is vulnerable by using this component and having a controller 
> configuration that uses an unfiltered request parameter as the name of the 
> view to be rendered.
> 
> Reference: https://github.com/thymeleaf/thymeleaf-spring/issues/263#issuecomment-977199524
> 
> **Recommendation**
> 
> We recommend upgrading to a version of this component that is not vulnerable 
> to this specific issue.
> 
> **Note**: If this component is included as a bundled/transitive dependency of 
> another component, there may not be an upgrade path. In this instance, we 
> recommend contacting the maintainers who included the vulnerable package. 
> Alternatively, we recommend investigating alternative components or a 
> potential mitigating control.
> 
> **Root Cause**
> 
> thymeleaf-spring5-3.0.12.RELEASE.jarorg/thymeleaf/spring5/util/SpringStandardExpressionUtils.class[3.0.12.RELEASE, 3.0.13.RELEASE)
> 
> **Advisories**
> 
> Project: https://github.com/thymeleaf/thymeleaf-spring/issues/263
> 
> Project: https://github.com/thymeleaf/thymeleaf/issues/809
> 
> Third Party: https://github.com/advisories/GHSA-qcj6-jqrg-4wp2

</details>

---

### lodash

---

<details>
  <summary>Primary STRIDE Category: Elevation of Privilege ⏫</summary>

> Lodash versions prior to 4.17.21 are vulnerable to Command Injection via the template function.
> 
> **Explanation**
> 
> The lodash package is vulnerable to Command Injection. The template function in lodash.js and template.js does not properly ensure the variable property of the options parameter to be a valid EcmaScript identifier. An attacker can exploit this by passing in a malicious command as the variable property, which would then be executed.
> 
> **Detection**
> 
> The application is vulnerable by using this component.
> 
> **Recommendation**
> 
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue.
> 
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
> 
> **Root Cause**
> 
> lodash-4.17.20.tgzpackage/lodash.js[4.0.0, 4.17.21)
> 
> **Advisories**
> 
> Project: https://github.com/lodash/lodash/pull/5085
> 
> Third Party: https://bugzilla.redhat.com/show_bug.cgi?id=1928937
> 
> CVE CVSS 3: 7.2
> 
> CVSS Vector: CVSS:3.1/AV:N/AC:L/PR:H/UI:N/S:U/C:H/I:H/A:H

</details>

---

### ansi-html

---

<details>
  <summary>Primary STRIDE Category: Denial of Service 🚫</summary>

> This affects all versions of package ansi-html. If an attacker provides a malicious string, it will get stuck processing the input for an extremely long time.
> 
> **Explanation**
> 
> The ansi-html package is vulnerable to Regular expression Denial Of Service (ReDoS). The ansiHTML function in the index.js file uses an insecure regular expression to replace markup text in a user supplied input. A remote attacker can leverage this behavior by crafting that input that exploits this issue to cause a DoS condition.
> 
> **Detection**
> 
> The application is vulnerable by using this component.
> 
> **Recommendation**
> 
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue.
> 
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
> 
> **Root Cause**
> 
> ansi-html-0.0.7.tgzpackage/index.js( , 0.0.8)
> 
> **Advisories**
> 
> Project: https://github.com/Tjatse/ansi-html/issues/19
> 
> Project: https://github.com/Tjatse/ansi-html/pull/20
> 
> Third Party: https://github.com/advisories/GHSA-whgm-jr23-g3j9
> 
> CVE CVSS 3: 7.5
> 
> CVSS Vector: CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H

</details>

---

### axios

---

<details>
  <summary>Primary STRIDE Category: Denial of Service 🚫</summary>

> axios is vulnerable to Inefficient Regular Expression Complexity
> 
> **Explanation**
> 
> The axios package is vulnerable to Regular Expression Denial of Service (ReDoS). The trim function in the utils.js and axios.js files use an insecure regular expression to remove whitespaces from the beginning and end of a user supplied input. A remote attacker who can supply malicious input to the affected function can exploit this vulnerability to cause a Denial of Service condition.
> 
> **Detection**
> 
> The application is vulnerable by using this component.
> 
> **Recommendation**
> 
> We recommend upgrading to a version of this component that is not vulnerable to this specific issue.
> 
> **Note**: If this component is included as a bundled/transitive dependency of another component, there may not be an upgrade path. In this instance, we recommend contacting the maintainers who included the vulnerable package. Alternatively, we recommend investigating alternative components or a potential mitigating control.
> 
> **Root Cause**
> 
> axios-0.21.1.tgzpackage/lib/utils.js( , 0.21.2)
> 
> **Advisories**
> 
> Project: https://github.com/axios/axios/pull/3980
> 
> Third Party: https://huntr.dev/bounties/1e8f07fc-c384-4ff9-8498-0690de2e8c31/
> 
> CVE CVSS 3: 7.5
> 
> CVSS Vector: CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:N/A:H

</details>