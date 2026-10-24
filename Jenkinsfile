
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Source code checked out from GitHub.'
            }
        }

        stage('Build Auth Image') {
            steps {
                sh 'docker build -t digital-wallet-auth:latest ./auth'
            }
        }

        stage('Build User Service Image') {
            steps {
                sh 'docker build -t digital-wallet-user-service:latest ./user-service'
            }
        }

        stage('Build Wallet Service Image') {
            steps {
                sh 'docker build -t digital-wallet-wallete-service:latest ./wallete-service'
            }
        }

        stage('Build Transaction Service Image') {
            steps {
                sh 'docker build -t digital-wallet-transaction-service:latest ./transaction-service'
            }
        }

        stage('Build Gateway Image') {
            steps {
                sh 'docker build -t digital-wallet-gateway-service:latest ./gateway-service'
            }
        }
    }

    post {
        success {
            echo 'All five Docker images built successfully.'
        }
        failure {
            echo 'Docker image build failed. Check the stage logs.'
        }
    }
}
