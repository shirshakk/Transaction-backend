
pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-south-1'
        AWS_ACCOUNT_ID = '829772413674'
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Source code checked out from GitHub.'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh '''
                    docker build -t digital-wallet-auth:latest ./auth
                    docker build -t digital-wallet-user-service:latest ./user-service
                    docker build -t digital-wallet-wallete-service:latest ./wallete-service
                    docker build -t digital-wallet-transaction-service:latest ./transaction-service
                    docker build -t digital-wallet-gateway-service:latest ./gateway-service
                '''
            }
        }

        stage('Login to Amazon ECR') {
            steps {
                sh '''
                    aws ecr get-login-password --region "$AWS_REGION" \
                    | docker login \
                        --username AWS \
                        --password-stdin "$ECR_REGISTRY"
                '''
            }
        }

        stage('Tag Docker Images') {
            steps {
                sh '''
                    docker tag digital-wallet-auth:latest \
                        "$ECR_REGISTRY/waller/auth-service:latest"

                    docker tag digital-wallet-user-service:latest \
                        "$ECR_REGISTRY/wallet/user-service:latest"

                    docker tag digital-wallet-wallete-service:latest \
                        "$ECR_REGISTRY/wallet/wallete-service:latest"

                    docker tag digital-wallet-transaction-service:latest \
                        "$ECR_REGISTRY/wallet/transaction-service:latest"

                    docker tag digital-wallet-gateway-service:latest \
                        "$ECR_REGISTRY/wallet/api-gateway:latest"
                '''
            }
        }

        stage('Push Images to ECR') {
            steps {
                sh '''
                    docker push "$ECR_REGISTRY/waller/auth-service:latest"
                    docker push "$ECR_REGISTRY/wallet/user-service:latest"
                    docker push "$ECR_REGISTRY/wallet/wallete-service:latest"
                    docker push "$ECR_REGISTRY/wallet/transaction-service:latest"
                    docker push "$ECR_REGISTRY/wallet/api-gateway:latest"
                '''
            }
        }
    }

    post {
        success {
            echo 'All five Docker images were built and pushed to Amazon ECR.'
        }
        failure {
            echo 'Pipeline failed. Check the Jenkins stage logs.'
        }
        always {
            sh 'docker logout "$ECR_REGISTRY" || true'
        }
    }
}