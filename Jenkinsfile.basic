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
//		stage ('Checkout Design System') {
//			steps {
//              	// create a directory for the checkout then run the Git command within that directory, the package.json file must be aware of this location which introduces fragility/cross-dependency, could this be improved?
//        		sh 'mkdir -p design-system'
//        		dir('design-system') {
//                    //git branch: '${DS_BRANCH}', credentialsId: '12a55ebf-608d-4b3e-811c-e4ad04f61f43', url: 'https://bitbucket.visitscotland.com/scm/vscom/design-system.git'
//                  	checkout([$class: 'GitSCM', branches: [[name: "*/${DS_BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [[$class: 'SparseCheckoutPaths', sparseCheckoutPaths:[[$class:'SparseCheckoutPath', path:'dist/']]]], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '12a55ebf-608d-4b3e-811c-e4ad04f61f43',url: 'https://bitbucket.visitscotland.com/scm/vscom/design-system.git']]])
//                }
//			}
//		}

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
