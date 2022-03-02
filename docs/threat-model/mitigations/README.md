# Mitigations

If product owners or other stakeholders decide to implement proposed cybersecurity mitigations, specific and traceable product requirements should be created to enforce implementation and testing of the mitigations in the relevant products and services.

For example, if a mitigation is proposed and accepted to 'encrypt GDPR-protected data at rest', there should be a product requirement created to this effect. In addition, there should be some form of testing and assurance that the mitigation is implemented effectively which produces reports that can be audited in case of a cybersecurity incident. For instance, following a data breach, stakeholders may be required to provide evidence in court that an appropriate level of care was taken to protect user data from being accessed by unauthorized parties, such as a threat actor selling stolen user personally identifiable informatino (PII) on the dark web.

## Required

These are required to mitigate vulnerabilities classified as CRITICAL and HIGH or to meet regulatory and quality standards.

| Mitigation                                                                  | ID   | Properties Improved                      | Vulnerabilities Mitigated                                     |
|-----------------------------------------------------------------------------|------|------------------------------------------|---------------------------------------------------------------|
| Require TLS Encryption for All Web Traffic (deny HTTP traffic - HTTPS only) | M001 | Authentication, Integrity                | V08, V10, V26, V28, V49, V50, V112, … [going to be lots here] |
| SonarQube Vulnerability Static Analysis Tool                                | M003 |                                          |                                                               |
| Sonatype Nexus Package Vulnerability Scanner                                | M004 |                                          |                                                               |
| Manual Code Review prior to Release                                         | M006 |                                          |                                                               |
| Manual Acceptance Testing                                                   | M007 |                                          |                                                               |
| VS IT system security tools (antivirus)                                     | M010 |                                          |                                                               |
| Network segmentation                                                        | M011 |                                          |                                                               |
| Firewall and Routing Rules                                                  | M012 |                                          |                                                               |
| Frequent (daily) backups (and testing those backups)                        | M017 |                                          | V050, V077, V111                                              |
| Design for least privilege                                                  | M018 |                                          | [V112](V112.md)                                               |
| Encrypt and salt password stores                                            | [M019](M019.md)                                 | [V112](V112.md)                                               |
| Encrypt data stores                                                         | M020 |                                          |                                                               |
| Sanitize Data Inputs                                                        | M022 | Integrity, Availability                  | [V112](V112.md)                                               |
| Design failover modes for when 3rd party services and API's go down         | M023 | Availability                             | V53                                                           |
| Strong User Authentication for Jenkins Pipeline Configuration Access        | M024 | Confidentiality, Integrity, Availability |                                                               |

## Suggested

These are suggested to mitigate vulnerabilities classified as MEDIUM or to improve product security and reliability.

| Mitigation                                                                  | ID   | Properties Improved                      | Vulnerabilities Mitigated                                     |
|-----------------------------------------------------------------------------|------|------------------------------------------|---------------------------------------------------------------|
| Use CloudFlare for Web Traffic QoS Throttling                               | M002 | Availability                             | V011, [V112](V112.md)                                                    |
| Bloomreach Container Security Scanner                                       | M005 |                                          |                                                               |
| Automated unit and integration testing                                      | M008 |                                          |                                                               |
| Release Pipeline Container Scanner                                          | M009 |                                          |                                                               |
| Automated Threat Hunting                                                    | M013 |                                          |                                                               |
| Manual Threat Hunting                                                       | M014 |                                          |                                                               |
| Periodic Log Review Process                                                 | M015 |                                          |                                                               |
| Automated Log Integrity Checks                                              | M016 |                                          | V111                                                          |
| Rate-limit logon attempts                                                   | M021 |                                          |                                                               |


## Optional

These may be optionally implemented to mitigate vulnerabilities classified as LOW. Generally these mitigations will only be implemented if they come 'for free', for example if a 3rd party tool or library has built-in features which mitigate vulnerabilities. Such mitigations are unlikely to need to be documented, but might be if, for example, the mitigations are part of the reason a 3rd party tool or library was chosen.

| Mitigation                                                                  | ID   | Properties Improved                      | Vulnerabilities Mitigated                                     |
|-----------------------------------------------------------------------------|------|------------------------------------------|---------------------------------------------------------------|
