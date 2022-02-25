# Overview

Lorem ipsom dolor sit amet ...

# Basic Jenkinsfile

Lorem ipsom dolor sit amet ...

```groovy
def DS_BRANCH = "feature/VS-955-ui-itineraries-itinerary-stops-changes-built-products"
def MAIL_TO = "jose.calcines@visitscotland.com, juanluis.hurtado@visitscotland.com, webops@visitscotland.net"

def thisAgent
if (BRANCH_NAME == "develop" && JOB_NAME == "develop.visitscotland.com-mb/develop") {
  thisAgent = "op-dev-brxwvcapp-01"
} else if (BRANCH_NAME == "develop" && JOB_NAME == "develop-nightly.visitscotland.com/develop") {
  thisAgent = "op-dev-brxwvcapp-02"
} else if (BRANCH_NAME == "develop" && JOB_NAME == "develop-stable.visitscotland.com/develop") {
  thisAgent = "op-dev-brxwvcapp-03"
} else {
  thisAgent = "docker-02"
}

pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
  agent {label thisAgent}
  environment {
    VS_SSR_PROXY_ON = 'FALSE'
  }
  tools {
        maven 'Maven 3.3.9'
        jdk 'jdk1.8.0'
  }
  stages {

// "Checkout Design System" stage now commented out as it's no longer required since VS-1081 - please merge this change as required but leave the block for reference
//        stage ('Checkout Design System') {
//            steps {
//                  // create a directory for the checkout then run the Git command within that directory, the package.json file must be aware of this location which introduces fragility/cross-dependency, could this be improved?
//                sh 'mkdir -p design-system'
//                dir('design-system') {
//                    //git branch: '${DS_BRANCH}', credentialsId: '12a55ebf-608d-4b3e-811c-e4ad04f61f43', url: 'https://bitbucket.visitscotland.com/scm/vscom/design-system.git'
//                      checkout([$class: 'GitSCM', branches: [[name: "*/${DS_BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths:[[$class:'SparseCheckoutPath', path:'dist/']]]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '12a55ebf-608d-4b3e-811c-e4ad04f61f43',url: 'https://bitbucket.visitscotland.com/scm/vscom/design-system.git']]])
//                }
//            }
//        }

    stage ('Pre-build') {
      steps {
        sh 'printenv'
      }
    }

    stage ('Build Application') {
      when {
          expression {
            return env.BRANCH_NAME != 'branch-to-skip';
          }
      }
            steps {
                sh 'mvn -f pom.xml clean package'
            }
            post {
                success {
                    //sh 'mvn -f pom.xml install -P !default'
                      sh 'mvn -f pom.xml install -P dist'
                    mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "SUCCESS CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
                }
                failure {
                    mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
                }
            }
        }

        stage ('Build environment'){
            steps{
                script{
                    sh 'sh ./infrastructure/scripts/docker.sh'
                }
            }
        }

        stage ('Availability notice'){

// "input" section commented out for now - useful for when there is genuinely a need to pause for an answer
//            input{
//                message "This environment will run until the next push is made the bitbucket repo."
//            }

            steps {
                sh 'echo "This environment will run until the next commit to bitbucket is detected."'
            }
        }
    }
    post{
        aborted{
            script{
                try{
                    sh ' '
                }catch(err){
                    sh 'echo "an error occurred in abort script"'
                }
            }
        }
    }
}
```

# Bloomreach Cloud Example

Lorem ipsom dolor sit amet ...

```groovy
def DS_BRANCH = "feature/VS-955-ui-itineraries-itinerary-stops-changes-built-products"
//def MAIL_TO = "jose.calcines@visitscotland.com, juanluis.hurtado@visitscotland.com, webops@visitscotland.net"
def MAIL_TO = "gavin@visitscotland.net"

def thisAgent
def VS_CONTAINER_BASE_PORT_OVERRIDE
if (BRANCH_NAME == "develop" && (JOB_NAME == "develop.visitscotland.com/develop" || JOB_NAME == "develop.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8099"
} else if (BRANCH_NAME == "develop" && (JOB_NAME == "develop-nightly.visitscotland.com/develop" || JOB_NAME == "develop-nightly.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8098"
  String cron_string = BRANCH_NAME == "develop" ? "@midnight" : ""
} else if (BRANCH_NAME == "develop" && (JOB_NAME == "develop-stable.visitscotland.com/develop" || JOB_NAME == "develop-stable.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8097"
} else if (BRANCH_NAME == "feature/VS-1865-feature-environments-enhancements" && (JOB_NAME == "feature.visitscotland.com-mb/feature%2FVS-1865-feature-environments-enhancements")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8096"
  String cron_string = BRANCH_NAME == "feature/VS-1865-feature-environments-enhancements" ? "@hourly" : ""
} else {
  thisAgent = "docker-02"
}

import groovy.json.JsonSlurper

pipeline {
  options {buildDiscarder(logRotator(numToKeepStr: '5'))}

  agent {label thisAgent}

  environment {
    VS_SSR_PROXY_ON = 'TRUE'
    VS_BRXM_PERSISTENCE_METHOD = 'h2'
    VS_SKIP_BUILD_FOR_BRANCH = 'eg:feature/VS-1865-feature-environments-enhancements'
    VS_RUN_BRC_STAGES = 'FALSE'
    // -- 20200712: TEST and PACKAGE stages might need VS_SKIP set to TRUE as they just run the ~4 minute front-end build every time
    VS_SKIP_BRC_BLD = 'FALSE'
    VS_SKIP_BRC_TST = 'FALSE'
    VS_SKIP_BRC_PKG = 'FALSE'
    VS_SKIP_BRC_CXN = 'FALSE'
    VS_SKIP_BRC_UPL = 'FALSE'
    VS_BRC_STACK_URI = 'visitscotland'
    VS_BRC_ENV = 'demo'
    VS_BRC_STACK_URL = "https://api-${VS_BRC_STACK_URI}.onehippo.io"
    VS_BRC_STACK_API_VERSION = 'v3'
  }

  tools {
    maven 'Maven 3.3.9'
    jdk 'jdk1.8.0'
  }

  stages {

//  -- "Checkout Design System". This stage now commented out as it's no longer required since VS-1081 - please merge this change as required but leave the block for reference
//  stage ('Checkout Design System') {
//    steps {
//      // -- create a directory for the checkout then run the Git command within that directory, the package.json file must be aware of this location which introduces fragility/cross-dependency, could this be improved?
//      sh 'mkdir -p design-system'
//      dir('design-system') {
//        //git branch: '${DS_BRANCH}', credentialsId: '12a55ebf-608d-4b3e-811c-e4ad04f61f43', url: 'https://bitbucket.visitscotland.com/scm/vscom/design-system.git'
//        checkout([$class: 'GitSCM', branches: [[name: "*/${DS_BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths:[[$class:'SparseCheckoutPath', path:'dist/']]]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '12a55ebf-608d-4b3e-811c-e4ad04f61f43',url: 'https://bitbucket.visitscotland.com/scm/vscom/design-system.git']]])
//      }
//    }
//  }

    stage ('Pre-build') {
      steps {
        sh 'printenv'
      }
    }

    stage ('vs compile & package') {
      when {
        allOf {
          expression {return env.VS_RUN_BRC_STAGES != 'TRUE'}
      expression {return env.VS_SKIP_VS_BLD != 'TRUE'}
          expression {return env.BRANCH_NAME != env.VS_SKIP_BUILD_FOR_BRANCH}
        }
      }
      steps {
        // -- 20200712: QUESTION FOR SE, "why do we not build with-development-data?"
        sh 'mvn -f pom.xml clean package'
      }
      post {
        success {
          sh 'mvn -f pom.xml install -P dist'
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "SUCCESS CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
        failure {
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
      }
    }


    // -- 20200712: The three 'brxm' and the two 'brc' stages are based on https://developers.bloomreach.com/blog/2019/set-up-continuous-deployment-of-your-brxm-project-in-brcloud-using-jenkins.html
    // --           in time, the connect, upload and deploy stages will be moved into bash scripts and run from a different Jenkins server

    // -- 20200712: QUESTION FOR SE, "why do each of the next three profiles run a UI step that takes ~3.5 minutes?"

    stage ('brxm compile') {
      when {
        allOf {
          expression {return env.VS_RUN_BRC_STAGES == 'TRUE'}
          expression {return env.VS_SKIP_BRC_BLD != 'TRUE'}
          expression {return env.BRANCH_NAME != env.VS_SKIP_BUILD_FOR_BRANCH}
          }
      }
      steps {
        withMaven(maven: 'Maven 3.3.9', options: [artifactsPublisher(disabled: true)]) {
          sh '$MVN_CMD clean compile -Pdefault -DskipTests'
        }
      }
    } //end stage

    stage ('brxm unit-test') {
      when {
        allOf {
          expression {return env.VS_RUN_BRC_STAGES == 'TRUE'}
          expression {return env.VS_SKIP_BRC_TST != 'TRUE'}
          expression {return env.BRANCH_NAME != env.VS_SKIP_BUILD_FOR_BRANCH}
        }
      }
      steps {
        sh 'mvn test -Pdefault -P!fed-build'
      }
    } //end stage

    stage ('brxm package') {
      when {
        allOf {
          expression {return env.VS_RUN_BRC_STAGES == 'TRUE'}
          expression {return env.VS_SKIP_BRC_PKG != 'TRUE'}
          expression {return env.BRANCH_NAME != env.VS_SKIP_BUILD_FOR_BRANCH}
        }
      }
      steps {
        // -- 20200712: QUESTION FOR SE, "brC does not recognise the package, maybe it needs Enterprise Features?"
        sh 'mvn verify && mvn -Pdist -P!fed-build -DskipTests'
      }
      post {
        success {
          //sh 'mvn -f pom.xml install -P !default'
      // -- 20200712: extra install step removed 
          //sh 'mvn -f pom.xml install -P dist'
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "SUCCESS CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
        failure {
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
      }
    } //end stage

    stage ('vs build feature env') {
      steps{
        script{
          //sh 'sh ./infrastructure/scripts/docker.sh'
          sh 'sh ./infrastructure/scripts/infrastructure.sh --debug'
        }
      }
    } //end stage

// -- 20200712: entire section commented out as it currently serves no purpose
//    stage ('Availability notice'){
//    // -- "input" section commented out for now - useful for when there is genuinely a need to pause for an answer
//    //input{
//    //  message "This environment will run until the next push is made the bitbucket repo."
//    //}
//      steps {
//        sh 'echo "This environment will run until the next commit to bitbucket is detected."'
//      }
//    }

    stage ('brC cxn test') {
      when {
        allOf {
          expression {return env.VS_RUN_BRC_STAGES == 'TRUE'}
      expression {return env.VS_SKIP_BRC_CXN != 'TRUE'}
        }
      }
      steps {
        script {
          // Login to get the access token
          echo "Login to brC and obtain token:"
          withCredentials([usernamePassword(credentialsId: 'brCloud', passwordVariable: 'VS_BRC_PASSWORD', usernameVariable: 'VS_BRC_USERNAME')]) {
            def json = "{\"username\": \"${VS_BRC_USERNAME}\", \"password\": \"${VS_BRC_PASSWORD}\"}"
            loginResult = post("${VS_BRC_STACK_URL}/${VS_BRC_STACK_API_VERSION}/authn/access_token", json)
          }
          echo "Login result ${loginResult}"
          String access_token = "Bearer " + parseJson(loginResult).access_token

          // Get the environment ID
          echo "Get the environments"
          environments = get("${VS_BRC_STACK_URL}/${VS_BRC_STACK_API_VERSION}/environments/", access_token)

          // We require an existing environment. Alternative is to delete/create one
          def environmentID = getEnvironmentID(environments, VS_BRC_ENV)
          echo "Environments result: ${environments}"
          echo "Environment ID: ${environmentID}"
        }
      }
    } //end stage

    stage ('brC upload') {
      when {
        allOf {
          expression {return env.VS_RUN_BRC_STAGES == 'TRUE'}
      expression {return env.VS_SKIP_BRC_UPL != 'TRUE'}
        }
      }
      steps {
        script {
          withCredentials([usernamePassword(credentialsId: 'brCloud', passwordVariable: 'VS_BRC_PASSWORD', usernameVariable: 'VS_BRC_USERNAME')]) {
            loginResponse = login("${VS_BRC_STACK_URL}/${VS_BRC_STACK_API_VERSION}/authn/access_token", VS_BRC_USERNAME, VS_BRC_PASSWORD)
          }

          access_token = "Bearer " + parseJson(loginResponse).access_token
          refresh_token = parseJson(loginResponse).refresh_token

          String projectName = readMavenPom(file: "${workspace}/pom.xml").getArtifactId()
          String projectVersion = readMavenPom(file: "${workspace}/pom.xml").getVersion()
          String distribution = "target/${projectName}-${projectVersion}-distribution.tar.gz"
          echo "Upload the distribution ${distribution}"
          uploadResult = postMultipart("${VS_BRC_STACK_URL}/${VS_BRC_STACK_API_VERSION}/distributions/", "dist_file", "${workspace}/${distribution}", access_token)
          echo "Upload result: ${uploadResult}"
          distID = parseJson(uploadResult).id
          echo "distID: ${distID}"
        }
      }
    } //end stage

  } //end stages

  post{
    aborted{
      script{
        try{
          sh ' '
        }catch(err){
          sh 'echo "an error occurred in abort script"'
        }
      }
    }
  } //end post
} //end pipeline

private String login(url, VS_BRC_USERNAME, VS_BRC_PASSWORD) {
   echo "Login and obtain access token:"
   def json = "{\"username\": \"${VS_BRC_USERNAME}\", \"password\": \"${VS_BRC_PASSWORD}\"}"
   loginResult = post(url, json)
   echo "Login result ${loginResult}"
   return loginResult
}

private boolean verify_token(url, access_token) {
    if (access_token) {
        echo "Verify access token:"
        verifyResult = get(url, access_token)
        echo "Verify result ${verifyResult}"
        if (parseJson(verifyResult).error_code) {
            echo "Token is invalid"
            echo "Error code: " + parseJson(verifyResult).error_code
            echo "Error detail: " + parseJson(verifyResult).error_detail
            return false;
        }
        echo "Access token is valid"
        return true;
    } else {
        echo "Access token is null"
        return false;
    }
}

private String refresh_token(url, refresh_token) {
    echo "Refresh access token:"
    def json = "{\"grant_type\": \"refresh_token\", \"refresh_token\": \"${refresh_token}\"}"
    refreshResult = post(url, json)
    echo "Refresh result ${refreshResult}"
    return "Bearer " + parseJson(refreshResult).access_token;
}
 

@NonCPS
private String get(url, access_token = null) {
   return curl("GET", url, access_token)
}

@NonCPS
private String post(url, json, access_token = null) {
   return curl("POST", url, access_token, json)
}

@NonCPS
private String postMultipart(url, String fileName, file, String access_token = null) {
   return curl("POST", url, access_token, null, fileName, file, null, "multipart/form-data")
}

@NonCPS
private String put(url, json, String access_token = null) {
   return curl("PUT", url, access_token, json, null, null, "-i --http1.1")
}

@NonCPS
private String  delete(url, access_token = null) {
   return curl("DELETE", url, access_token, null, null, null, "--http1.1")
}

@NonCPS
private String curl(method, url, access_token, json = null, fileName = null, file = null, extraParams = null, contentType = "application/json") {
   return sh(script: "curl ${extraParams?:""} \
           -X ${method} '${url}' \
           ${access_token?"-H 'Authorization: ${access_token}'":""} \
           -H 'Content-Type: ${contentType}' \
           ${json?"-d '${json}'":""} \
           ${(fileName && file)?"-F '${fileName}=@${file}'":""}",
           returnStdout: true)
}

@NonCPS
def parseJson(text) {
   return new JsonSlurper().parseText(text)
}


@NonCPS
def getEnvironmentID(environments, VS_BRC_ENV) {
   result = null
   parseJson(environments).items.each() { env ->
       if(env.name.toString() == VS_BRC_ENV) {
           result = env.id
       }
   }
   return result
}
```
