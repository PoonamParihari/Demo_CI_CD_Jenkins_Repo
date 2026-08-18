pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo "Building application..."
                sh 'mvn -version'
                sh 'echo Running tests...'
            }
        }

        stage('Package Artifact') {
            steps {
                echo "Packaging artifact..."
                sh 'echo artifact-${BUILD_NUMBER}.jar > artifact.jar'
            }
        }

        stage('Upload to S3') {
            steps {
                echo "Uploading artifact to S3..."
                sh """
                    aws s3 cp artifact.jar s3://my-ci-artifacts/${BUILD_NUMBER}/artifact.jar
                """
            }
        }
    }

    post {
        success {
            echo "Build #${BUILD_NUMBER} stored in S3 under folder ${BUILD_NUMBER}"
        }
    }
}
