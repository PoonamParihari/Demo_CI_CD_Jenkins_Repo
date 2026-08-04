pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = "us-east-1"
        DEV_BUCKET  = "demo-static-dev"
        QA_BUCKET   = "demo-static-qa"
        PROD_BUCKET = "demo-static-prod"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Package') {
            steps {
                sh 'zip -r site.zip .'
                archiveArtifacts artifacts: 'site.zip', fingerprint: true
            }
        }

        stage('Deploy to Dev') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                  credentialsId: 'aws-creds']]) {
                    sh '''
                        aws s3 sync . s3://$DEV_BUCKET/ --delete
                        echo "Deployed to DEV: s3://$DEV_BUCKET/"
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
                        aws s3 sync . s3://$QA_BUCKET/ --delete
                        echo "Deployed to QA: s3://$QA_BUCKET/"
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
                        aws s3 sync . s3://$PROD_BUCKET/ --delete
                        echo "Deployed to PROD: s3://$PROD_BUCKET/"
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
