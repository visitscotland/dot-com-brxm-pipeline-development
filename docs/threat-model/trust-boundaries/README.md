# Cybersecurity Trust Boundaries

Trust boundaries contain a set of cyber assets which are at an equivalent level of trust. Some of the trusted assets are connected via data flows to less trusted processes, entities, or data stores.

Threats can only be defined where data flows across a trust boundary.

Trust boundaries should be documented in the 'trust-boundaries' folder using the [trust boundary template](trust-boundary-template.md).

## Trust Boundary Index

| Trust Boundary                                         | Asset 1 | Data Flow | Asset 2 |
|--------------------------------------------------------|---------|-----------|---------|
| [TB07: Public User Account Authentication](TB07.md)    | P05     | DF07      | DS03    |
