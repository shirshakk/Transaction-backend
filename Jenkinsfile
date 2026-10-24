
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Source code checked out from GitHub.'
            }
        }

        stage('Build Auth') {
            steps {
                dir('auth') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build User Service') {
            steps {
                dir('user-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Wallet Service') {
            steps {
                dir('wallete-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Transaction Service') {
            steps {
                dir('transaction-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Gateway') {
            steps {
                dir('gateway-service') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
    }
}
