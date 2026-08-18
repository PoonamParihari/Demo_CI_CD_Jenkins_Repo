pipeline {
    agent any

    parameters {
        string(name: 'BUILD_NUMBER', description: 'Build number to promote to PROD')
    }

    stages {
        stage('Approval Gate') {
            steps {
                input message: "Approve promotion of build #${params.BUILD_NUMBER} to PROD?"
            }
        }

        stage('Fetch Artifact from S3') {
            steps {
                echo "Fetching artifact for build #${params.BUILD_NUMBER}"
                sh """
                    aws s3 cp s3://my-ci-artifacts/${params.BUILD_NUMBER}/artifact.jar artifact.jar
                """
                sh "ls -l artifact.jar"
            }
        }

        stage('Deploy to PROD') {
            steps {
                echo "Deploying build #${params.BUILD_NUMBER} to PROD..."
                sh "echo Deploying artifact.jar to PROD"
            }
        }
    }

    post {
        success {
            echo "Build #${params.BUILD_NUMBER} promoted to PROD."
        }
    }
}
