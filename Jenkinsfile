pipeline {
    agent {
        docker {
            image 'maven:3.9-eclipse-temurin-17'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    environment {
        AWS_DEFAULT_REGION = "us-east-1"
        DEV_BUCKET  = "demo-dev-bucket"
        QA_BUCKET   = "demo-qa-bucket"
        PROD_BUCKET = "demo-prod-bucket"
        APP_NAME    = "demo-ci-cd-app"
    }

    options {
        timeout(time: 20, unit: 'MINUTES')
        disableConcurrentBuilds()
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }

        stage('Package Artifact') {
            steps {
                sh 'mvn clean package -DskipTests'
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Deploy to Dev') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                  credentialsId: 'aws-creds']]) {
                    sh '''
                        JAR=$(ls target/*.jar | head -n 1)
                        VERSION=$(date +%Y%m%d%H%M%S)
                        aws s3 cp "$JAR" s3://$DEV_BUCKET/$APP_NAME-$VERSION.jar
                    '''
                }
            }
        }

        stage('Approve QA Promotion') {
            steps {
                input message: "Promote to QA?"
            }
        }

        stage('Deploy to QA') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                  credentialsId: 'aws-creds']]) {
                    sh '''
                        JAR=$(ls target/*.jar | head -n 1)
                        VERSION=$(date +%Y%m%d%H%M%S)
                        aws s3 cp "$JAR" s3://$QA_BUCKET/$APP_NAME-$VERSION.jar
                    '''
                }
            }
        }

        stage('Approve Prod Promotion') {
            steps {
                input message: "Promote to Prod?"
            }
        }

        stage('Deploy to Prod') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                  credentialsId: 'aws-creds']]) {
                    sh '''
                        JAR=$(ls target/*.jar | head -n 1)
                        VERSION=$(date +%Y%m%d%H%M%S)
                        aws s3 cp "$JAR" s3://$PROD_BUCKET/$APP_NAME-$VERSION.jar
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed!"
        }
    }
}
