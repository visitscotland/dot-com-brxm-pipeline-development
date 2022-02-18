# Introduction

Continuous integration files

# Setting branch-local build property overrides

You can create a file in the `ci` directory called BRANCH_NAME.buildprops to override certain build properties just for your work-in-progress (feature) branch

The BRANCH_NAME.buildprops file format is not a standard Java properties file because we cannot work with Properties() objects securely in the Jenkins Groovy Sandbox Environment.

The properties are parsed and validated by the closures defined in supportedBranchBuildProps.parsers.

BRANCH_NAME.buildprops Template

```groovy
// ci/BRANCH_NAME.buildprops
//
// Use this file as a template for creating a branch-local buildprops file to override
// the default build properties defined here. You only need to specify the overrides in
// the branch-local buildprops file. Otherwise, the defaults will be used.
//
// Add a file called 'BRANCH_NAME.buildprops' to your branch's 'ci' directory which
// sets any build properties which you want to override for this work-in-progress branch.

return [

// Build Property: VS_BUILD_FEATURE_ENVIRONMENT 
// Type: Boolean
// Supported Values: true, false
// Description: If true then Jenkins will create a feature environment for testing this branch prior to moving to a pull request
VS_BUILD_FEATURE_ENVIRONMENT : false,

// Build Property: VS_SKIP_LIGHTHOUSE_TESTS 
// Type: Boolean
// Supported Values: true, false
// Description: If true then Jenkins will skip running Lighthouse tests even if a feature environment was built
VS_SKIP_LIGHTHOUSE_TESTS : false,

// Build Property: VS_BUILD_DESCRIPTION 
// Type: String
// Valid Values: any non-empty string (in quotes)
// Description: Describes this build for logging purposes
VS_BUILD_DESCRIPTION : "VS.COM Jenkins Build",

]
```