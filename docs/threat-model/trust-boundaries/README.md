# Cybersecurity Trust Boundaries

Trust boundaries contain a set of cyber assets which are at an equivalent level of trust. Some of the trusted assets are connected via data flows to less trusted processes, entities, or data stores.

Threats can only be defined where data flows across a trust boundary.

Trust boundaries should be documented in the 'trust-boundaries' folder using the [trust boundary template](trust-boundary-template.md).

## Trust Boundary Index

| Trust Boundary                                         | Asset 1                   | Data Flow                 | Asset 2                   |
|--------------------------------------------------------|---------------------------|---------------------------|---------------------------|
| [TB01](TB01.md) User Web Traffic                       | [P01](../assets/P01.md)   | [DF01](../assets/DF01.md) | [P06](../assets/P06.md)   |
| [TB02](TB02.md) 3rd Party Web Traffic                  | [P02](../assets/P02.md)   | [DF02](../assets/DF02.md) | [P06](../assets/P06.md)   |
| [TB03](TB03.md) Simpleview Availability Search         | [P03](../assets/P03.md)   | [DF03](../assets/DF03.md) | [P07](../assets/P07.md)   |
| [TB04](TB04.md) Accomodation Provider Industry Content | [P04](../assets/P04.md)   | [DF04](../assets/DF04.md) | [E01](../assets/E01.md)   |
| [TB05](TB05.md) Website Server Local Content           | [DS01](../assets/DS01.md) | [DF05](../assets/DF05.md) | [P08](../assets/P08.md)   |
| [TB06](TB06.md) Website Server Remote Feeds            | [DS02](../assets/DS02.md) | [DF06](../assets/DF06.md) | [E06](../assets/E06.md)   |
| [TB07](TB07.md) Public User Account Authentication     | [P05](../assets/P05.md)   | [DF07](../assets/DF07.md) | [DS03](../assets/DS03.md) |
| [TB08](TB08.md) Visit Scotland Industry Engagement     | [E01](../assets/E01.md)   | [DF08](../assets/DF08.md) | [E05](../assets/E05.md)   |
| [TB09](TB09.md) Visit Scotland Content Creation        | [E02](../assets/E02.md)   | [DF09](../assets/DF09.md) | [P08](../assets/P08.md)   |
| [TB10](TB10.md) Visit Scotland Content Moderation      | [E03](../assets/E03.md)   | [DF10](../assets/DF10.md) | [E06](../assets/E06.md)   |
| [TB11](TB11.md) Visit Scotland Account Management      | [E04](../assets/E04.md)   | [DF11](../assets/DF11.md) | [DS03](../assets/DS03.md) |
