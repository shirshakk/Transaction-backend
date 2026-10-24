
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                echo 'Jenkins has checked out the Digital Wallet repository.'
                sh 'pwd'
                sh 'ls -la'
            }
        }

        stage('Verify Java and Maven') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }
    }
}
