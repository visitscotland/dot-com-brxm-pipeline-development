# Commit message style guide

Version 0.1.1
Last updated: Wednesday 15 May 2019

The terms MUST, MUST NOT, SHOULD, SHOULD NOT, and MAY are used in this document with the meanings found in [RFC 2119: Key words for use in RFCs to indicate requirement levels](https://www.ietf.org/rfc/rfc2119.txt).

<!-- MarkdownTOC -->

- [1. Introduction](#1-introduction)
- [2. Commit messages](#2-commit-messages)
    - [2.1 Subject line](#21-subject-line)
        - [i. JIRA issue id](#i-jira-issue-id-(automatically-generated))
        - [ii. Type](#ii-type)
        - [iii. Component](#iii-component)
        - [iv. Brief summary](#iv-brief-summary)
    - [2.2 Description \(optional\)](#22-description-optional)
    - [2.3 Commit message examples](#23-commit-message-examples)
- [3. Tags](#3-tags)
    - [3.1 Semantic versioning](#31-semantic-versioning)
        - [Major versions](#major-versions)
        - [Minor versions](#minor-versions)
        - [Patch versions](#patch-versions)
    - [3.2 Pre-releases](#32-pre-releases)
- [References](#references)

<!-- /MarkdownTOC -->


---

## 1. Introduction

This guide outlines how messages SHOULD be formatted when committing or tagging code in any version control system, such as Git or Subversion (SVN).

Commit messages are an integral part of each project's history, along with the changesets themselves. We write commit messages for multiple audiences: 

* Colleagues (developers on the same project)
* Future contributors
* Computers
 
Good commit messages serve each of these audiences well. They describe the what and the why of the changeset; the how is described by the diff itself.




---

## 2. Commit messages

Each commit message SHOULD include:

* **Subject line** — with JIRA issue id, TYPE, Component, and a Brief summary.
* **Description** (optional) — always preceded by a blank line.

A well-formed commit message SHOULD look something like this:

```
<JIRA issue id> <TYPE> (<Component>) <Brief summary>

Longer description with more details, such as a `code` being introduced
within the context of a `function`.

Multiple paragraphs may be added as required.

Bulleted lists:
* are
* also
* okay
```

Only the top line is required, so a minimal commit message might look like

```
<JIRA issue id> <TYPE> (<Component>) <Brief summary>
```

The formatting of each commit message will be tested by the `commit-msg` git hook to make sure it complies with this style guide. A commit will be aborted if a commit message does not match the required format.


### 2.1. Subject line

Generally, the subject line comprises four elements:

* `JIRA issue id` (automatically generated)
* `TYPE`
* `(Component)`
* `Brief summary`

The only **exception** is when populating a **new repository**. All that is required is a subject line of `Initial commit`.

#### i. JIRA issue id (automatically generated)

Every commit subject line MUST begin with a JIRA issue id, this is found at the beginning of the branch name, e.g. `BAU-3623`. You MUST NOT add this to the commit message as it will be generated and prepended to the commit message by the `prepare-commit-msg` git hook. The only **exceptions** is when committing to a branch that does not contain a JIRA issue id, these will be prepended with `NON-JIRA`, and when merging the develop branch into master, the JIRA issue id is not required in this case.

#### ii. Type

Every commit subject line MUST contain a TYPE in all CAPS and be separated from the JIRA issue id with a single space. The recognised types are:

| TYPE   | Description                                                        |
| ------ | ------------------------------------------------------------------ |
| BREAK  | A breaking change such as removing a feature.                      |
| DOCS   | Changes to the documentation (readme, API docs, etc.).             |
| FEAT   | New feature in production code.                                    |
| FIX    | Bug fix in production code.                                        |
| FORMAT | Code formatting, code comment change, etc; (compiled-code neutral).|
| MAINT  | Updating dev-related maintenance ("non-production code") files.    |
| MERGE  | Pull request to merge branch into another branch or master.        |
| TEST   | Adding missing tests or editing tests.                             |


#### iii. Component

Briefly describe the component, feature or module being developed; in the case of a pull request MERGE simply add the branch name.

Consistency is important. Make sure you have a definitive list of the various components being developed somewhere in your project notes so that all code contributors are aware of them. Adding a new component should be a considered decision, not just a whim when making your commit.

* MUST be separated from the TYPE with a single space.
* MUST be enclosed in parentheses/round brackets, i.e. `()`.
* MUST be consistently named throughout a project. For example, don't name it `(Long-form content)` in one commit but `(Long form)` in another.
* MUST be written in sentence case, unless you have a very good reason not to. For example, write `(Google maps)` not `(google maps)` or `(google-maps)`.


#### iv. Brief summary

The first line of a commit message is a brief summary of the changeset. 

The brief summary is used for email subject lines, changelogs, and features prominently in most version control system log formats such as `git log --format=oneline`.

The high visibility of the summary makes it critical to craft something that is as descriptive as possible within space limits.

* MUST be one line; no line breaks.
* MUST begin with a capital.
* MUST NOT include a dot (full stop/period) at the end.
* SHOULD use the [imperative mood][wikipedia-imperative] when possible, e.g. "change" not "changed" nor "changes".
* SHOULD aim for **around 50 characters or fewer, stopping at 70**. This is important because log-viewing tools nearly all expect the first line of commit messages to fit within these limits. This difficult restriction may force you to think critically about the essence of your commit; if you can’t describe the change in a short sentence, the commit may not be atomic enough. Note that the TYPE counts toward the 50/70 character count.

In the case of a pull request MERGE, the fix version SHOULD be added to the end of the commit message.


### 2.2 Description (optional)

The longer, multi-line description is optional, but if you wish to include it always separate it from the subject line by a blank line. In other words, the second line in your commit message SHOULD always be blank if you are making a multi-line commit.

The longer description is a chance to elaborate on the change you made. **Focus on the motivation and extra considerations for the change rather than detailing what you changed**. The details of what changed can be seen in the file diff. But extra considerations such as side effects or migration hints are less obvious from the code, and could be helpful to detail here.

* MUST be separated from the subject line by a blank line.
* SHOULD describe the _what_ and the _why_ of the changeset, rather than the _how_.
* SHOULD Use the [imperative mood][wikipedia-imperative].
* MAY be multiple paragraphs if required, separated by blank lines for clarity.
* MAY include bulleted lists (using asterisks).


### 2.3 Commit message examples

```
BAU-3644 FIX (Destinations menu) Fix map size on smaller screens

Reduced size of map as it was overflowing the menu on smaller screen sizes.
```

```
BAU-3250 FEAT (Product search) Adjusted size of filter button on mobile
```

```
BAU-3250 MERGE (Destinations menu) Merge into develop fix version: 1.3.39

Updates to the destinations menu completed and tested. Final testing is required before 
merging into master.
```


---

## 3. Tags

In most version control systems, such as Git and Subversion, you can tag specific points in history to mark these as significant. Many developers use this to functionality to mark release points (v1.0.0, v1.1.0, v.1.1.1, etc.): stable versions that may be rolled back to if required.

All significant code releases MUST be tagged using a sequential, semantic version number in the format: `vMAJOR.MINOR.PATCH`, for example

```
v0.9.5
v1.0.0
```

* The version number MUST be prefixed with a lowercase `v`.
* There MUST be three numbers, in accordance with the semantic versioning standard: MAJOR.MINOR.PATCH.
* Pre-release information MAY be included (see below).
* There MUST be no other information given in a tag name; additional information MAY be included in a commit message.


### 3.1 Semantic versioning

You MUST follow the [semantic versioning][semver] format:

> Given a version number MAJOR.MINOR.PATCH, increment the:
>
> MAJOR version when you make incompatible API changes,
> MINOR version when you add functionality in a backwards-compatible manner, and
> PATCH version when you make backwards-compatible bug fixes.


#### Major versions

Major version X (X.MINOR.PATCH), where MAJOR is greater than zero,MUST be incremented if any backwards-incompatible changes are introduced to the public API.

It MAY include minor and patch level changes. Patch and minor version MUST be reset to 0 when major version is incremented.


##### Major version zero

Major version zero (0.MINOR.PATCH) is for initial development. Anything may change at any time. The public API should not be considered stable.


##### Major version one

Version 1.0.0 defines the public API. The way in which the version number is incremented after this release is dependent on this public API and how it changes.


#### Minor versions

Minor version Y (MAJOR.Y.MINOR), where MAJOR is greater than zero, MUST be incremented if new, backwards-compatible functionality is introduced to the public API.

It MUST be incremented if any public API functionality is marked as deprecated.

It MAY be incremented if substantial new functionality or improvements are introduced within the private code. It MAY include patch level changes. Patch version MUST be reset to 0 when minor version is incremented.


#### Patch versions

Patch version Z (MAJOR.MINOR.Z), where MAJOR is greater than zero, MUST be incremented if only backwards-compatible bug fixes are introduced.

A bug fix is defined as an internal change that fixes incorrect behavior.


### 3.2 Pre-releases

In accordance with semantic versioning:

> A pre-release version MAY be denoted by appending a hyphen and a series of dot separated identifiers immediately following the patch version. 

For example

```
v0.9.5-alpha
v0.9.5-alpha.1
v0.9.5-rc1
v0.9.5-rc2
v0.9.5-rc3
v0.9.5
```

* Pre-release identifiers MUST comprise only of ASCII alphanumerics, hyphen and dot `regex([0-9A-Za-z-\.])`.
* Pre-release identifiers MUST NOT be empty.
* Numeric identifiers MUST NOT include leading zeroes. (`v0.9.5-rc1` NOT `v0.9.5-rc01`.)

As semantic versioning notes: "Pre-release versions have a lower precedence than the associated normal version. A pre-release version indicates that the version is unstable and might not satisfy the intended compatibility requirements as denoted by its associated normal version."




---

## References

This is a modified version of the [University of St Andrew's commit message style guide][stacommit].


[stacommit]: https://github.com/standrewsdigital/digital-code-style-guide/blob/master/commit-messages.md "University of St Andrew's commit message style guide"
[wikipedia-imperative]: https://en.wikipedia.org/wiki/Imperative_mood "The imperative is a grammatical mood that forms commands or requests, including the giving of prohibition or permission, or any other kind of advice or exhortation."
[semver]: http://semver.org/ "Semantic versioning"