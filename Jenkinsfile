def MAIL_TO = "webops@visitscotland.net"

def thisAgent
def VS_CONTAINER_BASE_PORT_OVERRIDE
cron_string = ""
if (BRANCH_NAME == "develop" && (JOB_NAME == "develop.visitscotland.com/develop" || JOB_NAME == "develop.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8099"
  env.VS_RELEASE_SNAPSHOT = "FALSE"
} else if (BRANCH_NAME == "develop" && (JOB_NAME == "develop-nightly.visitscotland.com/develop" || JOB_NAME == "develop-nightly.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8098"
  cron_string = "@midnight"
} else if (BRANCH_NAME == "develop" && (JOB_NAME == "develop-stable.visitscotland.com/develop" || JOB_NAME == "develop-stable.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8100"
} else if (BRANCH_NAME == "develop" && (JOB_NAME == "feature.visitscotland.com/develop" || JOB_NAME == "feature.visitscotland.com-mb/develop")) {
  thisAgent = "op-dev-xvcdocker-01"
  env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8097"
} else if (BRANCH_NAME == "feature/VS-1865-feature-environments-enhancements" && (JOB_NAME == "feature.visitscotland.com-mb/feature%2FVS-1865-feature-environments-enhancements")) {
  thisAgent = "op-dev-xvcdocker-01"
  //env.VS_CONTAINER_BASE_PORT_OVERRIDE = "8096"
  //cron_string = "*/2 * * * *"
} else {
  env.VS_RELEASE_SNAPSHOT = "FALSE"
  // thisAgent should always be set to op-dev-xvcdocker-01 unless you have been informed otherwise!
  thisAgent = "op-dev-xvcdocker-01"
  //thisAgent = "docker-02"
}

import groovy.json.JsonSlurper

pipeline {
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
    // to-do
    // gp: investigate milestone caclulation to cancel current build if a new one starts
    // - see: https://stackoverflow.com/questions/40760716/jenkins-abort-running-build-if-new-one-is-started/44326216
    // - see: https://www.jenkins.io/doc/pipeline/steps/pipeline-milestone-step/#pipeline-milestone-step
    // gp: investigate the use of stash/unstash to make build artefacts available to other nodes
    // - see: https://www.cloudbees.com/blog/parallelism-and-distributed-builds-jenkins
    // - this could potentially allow the running of all Lighthouse tests on a separate node
    // - experiment with a simple echo on a different node (stash/unstash)
    // gp: change sonarqube project target to a short version of the project name

    disableConcurrentBuilds()
  }
  agent {label thisAgent}
  triggers { cron( cron_string ) }
  environment {
    MAVEN_SETTINGS = credentials('maven-settings')
    // from 20200804 VS_SSR_PROXY_ON will only affect whether the SSR app is packaged and sent to the container, using or bypassing will be set via query string
    VS_SSR_PROXY_ON = 'TRUE'
    // VS_CONTAINER_PRESERVE is set to TRUE in the ingrastructure build script, if this is set to FALSE the container will be rebuilt every time and the repository wiped
    VS_CONTAINER_PRESERVE = 'TRUE'
    // VS_BRXM_PERSISTENCE_METHOD can be set to either 'h2' or 'mysql' - do not change during the lifetime of a container or it will break the repo
    VS_BRXM_PERSISTENCE_METHOD = 'h2'
    // VS_SKIP_BUILD_FOR_BRANCH is useful for testing, only ever set to your working branch name - never to a variable!
    VS_SKIP_BUILD_FOR_BRANCH = 'feature/VS-1865-feature-environments-enhancements-log4j'
    // VS_COMMIT_AUTHOR is required by later stages which will fail if it's not set, default value of jenkins@visitscotland.net
    // turns out if you set it here it will not be overwritten by the load later in the pipeline
    //VS_COMMIT_AUTHOR = 'jenkins@visitscotland.net'
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
    maven 'Maven 3.5.0'
    jdk 'jdk1.8.0'
  }

  stages {

    stage ('Pre-build') {

      steps {
        // Set any defined build property overrides for this work-in-progress branch
        script {

          // Set any supported build property overrides defined in ci/BRANCH_NAME.buildprops
          branchBuildScripts = load("./ci/branchBuildScripts.groovy")

          // Set the buildprop environment variables either to their default values or any specified overrides
          Map buildProps = branchBuildScripts.loadPropOverrides("${env.WORKSPACE}" + "/ci/", branchBuildScripts.getBranchKey())
          Map buildPropParsers = branchBuildScripts.getPropParsers()
          buildPropParsers.each {
            k, v ->
              String parsedValue = ( buildProps?.containsKey(k) ? v.parser(buildProps[k]) : v.default )
              env."${k}" = parsedValue
          }
        }

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
        sh 'sh ./infrastructure/scripts/infrastructure.sh setvars'
        // -- 20200712: QUESTION FOR SE, "why do we not build with-development-data?"
        sh 'mvn -f pom.xml clean package'
      }
      post {
        success {
          sh 'mvn -f pom.xml install -Pdist-with-development-data'
          // Save frontend NPM package for use in later deploy steps
          archiveArtifacts artifacts: 'ui-integration/vs-frontend-integration*.tgz', fingerprint: true
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Maven build succeeded at ${env.STAGE_NAME} for ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
        failure {
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Maven build FAILED at ${env.STAGE_NAME} for  ${env.JOB_NAME}", to: "${MAIL_TO}";
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
        withMaven(maven: 'Maven 3.5.0', options: [artifactsPublisher(disabled: true)]) {
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
        sh 'mvn verify && mvn -Pdist-with-development-data -P!fed-build -DskipTests'
      }
      post {
        success {
          //sh 'mvn -f pom.xml install -P !default'
	  // -- 20200712: extra install step removed
          //sh 'mvn -f pom.xml install -Pdist-with-development-data'
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "SUCCESS CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
        failure {
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "${MAIL_TO}";
        }
      }
    } //end stage

    stage ('vs build feature env') {
      when {
        anyOf {
          // Always build the feature environment for 'develop' builds
          branch 'develop'

          // Always build the feature environment for pull requests
          changeRequest()

          // If requested, build feature environment for feature branches prior to PR
          environment name: 'VS_BUILD_FEATURE_ENVIRONMENT', value: 'true'
        }
      }
      steps{
        script{
          //sh 'sh ./infrastructure/scripts/docker.sh'
          sh 'sh ./infrastructure/scripts/infrastructure.sh --debug'
        }
        // make all VS_ variables available to pipeline, load file must be in env.VARIABLE="VALUE" format
        script {
          if (fileExists("$WORKSPACE/vs-last-env.quoted")) {
            echo "loading environment variables from $WORKSPACE/vs-last-env.quoted"
            load "$WORKSPACE/vs-last-env.quoted"
            echo "found ${env.VS_COMMIT_AUTHOR}"
          } else {
            echo "cannot load environment variables, file does not exist"
          }
        }
      }
    } //end stage

    stage ('Build Actions'){
      parallel {

        stage('SonarQube BE Scan') {
          when {
            branch 'develop' 
          }
          steps {
            withSonarQubeEnv(installationName: 'SonarQube', credentialsId: 'sonarqube') {
              sh "PATH=/usr/bin:$PATH; mvn sonar:sonar -Dsonar.host.url=http://172.28.87.209:9000 -s $MAVEN_SETTINGS"
              // setting PATH=/usr/bin:$PATH; above allows NodeJS 10.16.3 to be the default and prevents and error at the CSS scan
            }
          }
        }

        stage('SonarQube FE scan') {
          when {
            branch 'develop' 
          }
          environment {
            scannerHome = tool 'SonarQube_4.0'
          }
          steps {
            withSonarQubeEnv(installationName: 'SonarQube', credentialsId: 'sonarqube') {
              sh '''
                PATH=/usr/bin:$PATH; ${scannerHome}/bin/sonar-scanner \
                -Dsonar.sources=./frontend \
                -Dsonar.projectKey=VS2019-FE \
                -Dsonar.host.url=http://172.28.87.209:9000 \
                -Dsonar.login=9fa63cfd51d94fb8e437b536523c15a9b45ee2c1
              '''
              // setting PATH=/usr/bin:$PATH; above allows NodeJS 10.16.3 to be the default and prevents and error at the CSS scan
            }
          }
        }

        stage('Nexus IQ Scan: Site') {
          when {
            anyOf {
              // Always run Nexus IQ scan for builds on 'develop'
              branch 'develop' 
 
              // Always run Nexus IQ scan for pull requests
              changeRequest()
            }
          }
          steps {
            script{
              try {
                def policyEvaluation = nexusPolicyEvaluation failBuildOnNetworkError: true, enableDebugLogging: true, iqApplication: selectedApplication('visitscotland-site'), iqScanPatterns: [[scanPattern: '**/site.war']], iqStage: 'build', jobCredentialsId: 'nexusiq'
                echo "Nexus IQ scan succeeded: ${policyEvaluation.applicationCompositionReportUrl}"
                IQ_SCAN_URL = "${policyEvaluation.applicationCompositionReportUrl}"
              } 
              catch (error) {
                def policyEvaluation = error.policyEvaluation
                echo "Nexus IQ scan vulnerabilities detected', ${policyEvaluation.applicationCompositionReportUrl}"
                throw error
              }
            }
          }
        } //end stage

        stage('Nexus IQ Scan: CMS') {
          when {
            anyOf {
              // Always run Nexus IQ scan for builds on 'develop'
              branch 'develop' 
 
              // Always run Nexus IQ scan for pull requests
              changeRequest()
            }
          }
          steps {
            script{
              try {
                def policyEvaluation = nexusPolicyEvaluation failBuildOnNetworkError: true, enableDebugLogging: true, iqApplication: selectedApplication('visitscotland-cms'), iqScanPatterns: [[scanPattern: '**/cms.war']], iqStage: 'build', jobCredentialsId: 'nexusiq'
                echo "Nexus IQ scan succeeded: ${policyEvaluation.applicationCompositionReportUrl}"
                IQ_SCAN_URL = "${policyEvaluation.applicationCompositionReportUrl}"
              } 
              catch (error) {
                def policyEvaluation = error.policyEvaluation
                echo "Nexus IQ scan vulnerabilities detected', ${policyEvaluation.applicationCompositionReportUrl}"
                throw error
              }
            }
          }
        } //end stage

        stage('Nexus IQ Scan: SSR') {
          when {
            anyOf {
              // Always run Nexus IQ scan for builds on 'develop'
              branch 'develop' 
 
              // Always run Nexus IQ scan for pull requests
              changeRequest()
            }
          }
          steps {
            script{
              try {
                def policyEvaluation = nexusPolicyEvaluation failBuildOnNetworkError: true, iqApplication: selectedApplication('visitscotland-ssr'), iqScanPatterns: [[scanPattern:  '**/yarn.lock' ]], iqStage:  'build', jobCredentialsId: 'nexusiq'
                echo "Nexus IQ scan succeeded: ${policyEvaluation.applicationCompositionReportUrl}"
                IQ_SCAN_URL = "${policyEvaluation.applicationCompositionReportUrl}"
              } 
              catch (error) {
                def policyEvaluation = error.policyEvaluation
                echo "Nexus IQ scan vulnerabilities detected', ${policyEvaluation.applicationCompositionReportUrl}"
                throw error
              }
            }
          }
        } //end stage

        stage ('Snapshot to Nexus'){
              when {
                allOf {
                  expression {return env.VS_RELEASE_SNAPSHOT == 'TRUE'}
                }
              }
              steps{
                  script{
                      sh 'mvn -B -f pom.xml deploy -Pdist-with-development-data -s $MAVEN_SETTINGS'
                  }
              }
          }

        stage('Release to Nexus') {
          when {
                branch 'PR-145' // to-do - hd: change this to develop when ready
          }
          steps {
              script {
                NEW_TAG = "${env.JOB_NAME}-${env.BUILD_NUMBER}"
              }
              echo "Creating tag $NEW_TAG"
              sh "git tag -m \"CI tagging\" $NEW_TAG"
              echo "Uploading tag $NEW_TAG to Bitbucket"
              withCredentials([usernamePassword(credentialsId: 'jenkins-ssh', usernameVariable: 'USER', passwordVariable: 'PASSWORD')]) {
                sh """
                git config --local credential.username ${USER}
                git config --local credential.helper "!echo password=${PASSWORD}; echo"
                git push origin $NEW_TAG --repo=${env.GIT_URL}
                """
              }
              echo "Uploading version $NEW_TAG to Nexus"
              sh "mvn versions:set -DremoveSnapshot"
              sh "mvn -B clean  deploy -Pdist -Drevision=$NEW_TAG -Dchangelist= -DskipTests -s $MAVEN_SETTINGS"
              // Publish frontend package to private Nexus NPM repository
              // NOTE: 'yarn publish' is the syntax for Yarn v1.x. Yarn v2.x has breaking
              //        changes to the publish CLI interface, so upgrade with care.
              //withCredentials([secretText(credentialsId: 'npm-registry', variable: 'NPM_TOKEN')]) {
              //  sh "yarn publish --cwd ./ui-integration --registry https://npm.visitscotland.com/:_authToken=${NPM_TOKEN} --non-interactive --new-version 1.2.3 --access public --tag ${NEW_TAG}"
              //}
          }
        }
      }
    }
 
    stage('Lighthouse Testing'){
      when {
        allOf {
          not {
            // Allow lighthouse to be skipped even if a feature environment was built
            environment name: 'VS_SKIP_LIGHTHOUSE_TESTS', value: 'true'
          }
          anyOf {
            // Always run the Lighthouse Tests for 'develop' builds
            branch 'develop' 

            // Always run the Lighthouse Tests for pull requests
            changeRequest()

            // If the feature environment was forced to be built, run the Lighthouse tests
            environment name: 'VS_BUILD_FEATURE_ENVIRONMENT', value: 'true'
          }
        }
      }
      steps{
        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') { 
          echo "Lighthouse test failure notification will be emailed to ${env.VS_COMMIT_AUTHOR}"
          script{
            // to-do: replace this sleep with a "wait for 200" in the script
            sleep time: 120, unit: 'SECONDS'
            sh 'sh ./testing/lighthouse.sh'
          }
        }
      }
      post {
        success {
          publishHTML (target: [
            allowMissing: false,
            alwaysLinkToLastBuild: false,
            keepAll: false,
            reportDir: 'frontend/.lighthouseci',
            reportFiles: 'lhr-**.html',
            reportName: "LH Report"
          ])
        }
        failure {
          echo "sending failure notice to ${env.VS_COMMIT_AUTHOR}"
          mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> build URL: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "Lighthouse failure: ${env.JOB_NAME}", to: "${env.VS_COMMIT_AUTHOR}";
        }
      }
    }

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

  } //end stages

  post{
    success{
      script{
        sh 'sh ./infrastructure/scripts/infrastructure.sh displayreport'
      }
    }
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

// function to read in a properties file (see https://medium.com/@dhamodharakkannan/jenkins-loading-variables-from-a-file-for-different-environments-d442a2a48bce)
// may not be required if simple "load" command works
def readEnvironmentVariables(path){
  def properties = readProperties file: path
  keys= properties.keySet()
  for(key in keys) {
    value = properties["${key}"]
    env."${key}" = "${value}"
  }
}
