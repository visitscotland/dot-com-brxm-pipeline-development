pipeline {
 options {
    buildDiscarder(logRotator(numToKeepStr: '5'))
  }
 agent {label 'docker-02'}
 tools {
        maven 'Maven 3.3.9'
        jdk 'jdk1.8.0'
    }
    stages {
		stage ('Checkout dependencies') {
			steps {
        		sh 'mkdir -p dvc'
        		dir('dvc') {
                    checkout resolveScm(source: git('https://bitbucket.visitscotland.com/scm/vscom/design-system.git'), targets: [BRANCH_NAME, 'feature/VS-560-ui-meganav-with-build-products'])
                }
			}
		}
       stage ('Build Application') {
            steps {
                sh 'mvn -f pom.xml clean package'
            }
            post {
                success {
                    sh 'mvn install -P !default'
                }
                failure {
                    mail bcc: '', body: "<b>Notification</b><br>Project: ${env.JOB_NAME} <br>Build Number: ${env.BUILD_NUMBER} <br> URL de build: ${env.BUILD_URL}", cc: '', charset: 'UTF-8', from: '', mimeType: 'text/html', replyTo: '', subject: "ERROR CI: Project name -> ${env.JOB_NAME}", to: "gavin.park@visitscotland.com";
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
            input{
                message "This environment will run until the next push is made the bitbucket repo."
            }
            steps {
                sh 'echo "Keeping environment running until the next push to bitbucket."'
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
