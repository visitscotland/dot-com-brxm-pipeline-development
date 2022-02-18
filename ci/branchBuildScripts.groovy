// Get the key for this branch
String getBranchKey() {

  // Get the environment variables defined in the calling context
  String gitBranch = env.GIT_BRANCH
  String changeBranch = env.CHANGE_BRANCH
  String branchName = env.BRANCH_NAME

  // Return null by default
  String branchKey = null
  
  // For multi-branch pipelines, env.BRANCH_NAME changes to PR-XYZ when branches 
  // move to PR status, so get env.VS_BRANCH_KEY via env.CHANGE_BRANCH when this 
  // branch is in PR status. For single-branch pipelines, BRANCH_NAME is also not
  // defined, so we need to check env.GIT_BRANCH.
  if( (changeBranch == null) && (branchName == null) ) {
    // This is running in a single-branch pipeline
    branchKey = gitBranch
  } else {
    // This is running in a multi-branch pipeline
    if( changeBranch == null ) {
      branchKey = branchName
    } else {
      branchKey = changeBranch
    }
  }

  // We need to strip off everything before (and including) the last '/'
  //
  // NOTE: String.lastIndexOf(String key) returns -1 if the key is not found,
  //       which means that we will return the entire string if no '/' are
  //       present in the branch path (for example, if this was 'develop')
  if( branchKey != null) {
    branchKey = branchKey.substring(
      branchKey.lastIndexOf('/') + 1, 
      branchKey.length()
    )
  }

  return branchKey
}

// If buildprops file is present, return any property overrides defined in the file
Map loadPropOverrides (String ciFolderPath, String branchKey) {
  String fileName = ciFolderPath + branchKey + ".buildprops"
  return ( fileExists(fileName) ? load(fileName) : null )
}

// Defines a map of the supported build property keys and closures with which to 
// validate the property value and set the associated environment variable
//
// NOTE: By default Groovy creates an instance of java.util.LinkedHashMap. 
//       This default can be overridden by using the 'as' operator.
//
// NOTE: Unlike a regular Groovy method, for closures:
//       https://www.baeldung.com/groovy-closures
//       https://groovy-lang.org/closures.html
//
//       - We can pass a Closure as an argument to a method
//       - Unary closures can use the implicit 'it' parameter
//       - We can assign a Closure to a variable and execute it later like a method
//       - Groovy determines the return type of the closures at runtime
//       - We can declare and invoke closures inside a closure
//       - Closures always return a value
//
// NOTE: Groovy toBoolean() coerces a string value to boolean true iff the string
//       value equals 'true' or '1' (case-insensitive). All other values are coerced to
//       boolean false. We must convert toString() first in case an integer value
//       such as 0 or 1 is set. The Integer class does not have a toBoolean() method.
//
// https://docs.groovy-lang.org/latest/html/groovy-jdk/java/lang/String.html#toBoolean()
Map getPropParsers () {

  Map parsers = [
    VS_BUILD_FEATURE_ENVIRONMENT : [ 
      default: "false", 
      parser : { rawValue ->
        // NOTE: We have to convert toString() first to parse the raw value for a couple
        //       of reasons. First, the rawValue may be an instance of a class which does
        //       not have a toBoolean() method defined, such as Integer. Second, even if 
        //       rawValue's class does have a toBoolean() method, the Jenkins server may
        //       not have permission in the Groovy Sandbox to call toBoolean() on that 
        //       class. We do know that we can call Object.toString(), String.toBoolean(),
        //       and Boolean.toString(), so we can safely chain those calls to coerce the
        //       the environment variable to either 'true' or 'false' like we need.
        String parsedValue = rawValue?.toString()?.toBoolean()?.toString()
        if ( parsedValue == null ) {
          error("Could not parse build property VS_BUILD_FEATURE_ENVIRONMENT (value: ${rawValue}) to boolean!")
        }
        return parsedValue
      }
    ],

    VS_SKIP_LIGHTHOUSE_TESTS : [ 
      default: "false", 
      parser : { rawValue ->
        // NOTE: We have to convert toString() first to parse the raw value for a couple
        //       of reasons. First, the rawValue may be an instance of a class which does
        //       not have a toBoolean() method defined, such as Integer. Second, even if 
        //       rawValue's class does have a toBoolean() method, the Jenkins server may
        //       not have permission in the Groovy Sandbox to call toBoolean() on that 
        //       class. We do know that we can call Object.toString(), String.toBoolean(),
        //       and Boolean.toString(), so we can safely chain those calls to coerce the
        //       the environment variable to either 'true' or 'false' like we need.
        String parsedValue = rawValue?.toString()?.toBoolean()?.toString()
        if ( parsedValue == null ) {
          error("Could not parse build property VS_BUILD_FEATURE_ENVIRONMENT (value: ${rawValue}) to boolean!")
        }
        return parsedValue
      }
    ],

    VS_BUILD_DESCRIPTION : [
      default: "VS.COM Jenkins Build",
      parser: { rawValue ->
        String parsedValue = rawValue?.toString()
        if( parsedValue == null ) {
          error("Could not parse build property VS_BUILD_DESCRIPTION (value: null) to non-empty string!")
        } else {
          if( parsedValue.isEmpty() ) {
            error("Could not parse build property VS_BUILD_DESCRIPTION (value: empty string) to non-empty string!")
          }
        }
        return parsedValue
      }
    ],
  ]

  return parsers
}

return [
  loadPropOverrides: this.&loadPropOverrides,
  getPropParsers: this.&getPropParsers,
  getBranchKey: this.&getBranchKey,
]