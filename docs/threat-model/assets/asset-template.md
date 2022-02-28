# [UID]: [Short Title]

*(briefly describe the asset)*

## Asset Definition

Assets can be described in any format useful for the team, including simple plain text. 

The format in which assets are documented should be determined by how the information is used in the wider threat model.

For instance, if assets are to be processed by a 3rd party modelling tool or custom in-house script used to create declarative threat models, one possible format which could be adopted is YAML:

```yaml
id: user-client
description: User Web Client
type: external-entity # values: external-entity, process, datastore
usage: business # values: business, devops
used_as_client_by_human: true
out_of_scope: true
justification_out_of_scope: Owned and managed by enduser user
size: component # values: system, service, application, component
technology: browser # values: see help
tags:
internet: true
machine: physical # values: physical, virtual, container, serverless
encryption: none # values: none, transparent, data-with-symmetric-shared-key, data-with-asymmetric-shared-key, data-with-enduser-individual-key
owner: User
confidentiality: internal # values: public, internal, restricted, confidential, strictly-confidential
integrity: operational # values: archive, operational, important, critical, mission-critical
availability: operational # values: archive, operational, important, critical, mission-critical
justification_cia_rating: >
  The client used by the user to access the system.
multi_tenant: false
redundant: false
custom_developed_parts: false
data_assets_processed: # sequence of IDs to reference
  - user-accounts
  - user-operational-data
  - user-info
  - client-application-code
  - marketing-material
data_assets_stored: # sequence of IDs to reference
data_formats_accepted: # sequence of formats like: json, xml, serialization, file, csv
communication_links:
  user-traffic:
    target: load-balancer
    description: Link to the load balancer
    protocol: https # values: see help
    authentication: session-id # values: none, credentials, session-id, token, client-certificate, two-factor
    authorization: enduser-identity-propagation # values: none, technical-user, enduser-identity-propagation
    tags:
    vpn: false
    ip_filtered: false
    readonly: false
    usage: business # values: business, devops
    data_assets_sent: # sequence of IDs to reference
      - user-accounts
      - user-operational-data
    data_assets_received: # sequence of IDs to reference
      - user-accounts
      - user-operational-data
      - user-info
      - client-application-code
      - marketing-material
    #diagram_tweak_weight: 1
    #diagram_tweak_constraint: false
```