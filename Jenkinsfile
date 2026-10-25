pipeline {
    agent any

    parameters {
        choice(
            name: 'SERVICE',
            choices: [
                'auth',
                'user-service',
                'wallete-service',
                'transaction-service',
                'gateway-service'
            ],
            description: 'Select one microservice to build and push to ECR'
        )
    }

    environment {
        AWS_REGION = 'ap-south-1'
        AWS_ACCOUNT_ID = '829772413674'
        ECR_REGISTRY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"
    }

    stages {
        stage('Build Selected Image') {
            steps {
                script {
                    def services = [
                        'auth': [
                            context: './auth',
                            image: 'digital-wallet-auth',
                            repository: 'waller/auth-service'
                        ],
                        'user-service': [
                            context: './user-service',
                            image: 'digital-wallet-user-service',
                            repository: 'wallet/user-service'
                        ],
                        'wallete-service': [
                            context: './wallete-service',
                            image: 'digital-wallet-wallete-service',
                            repository: 'wallet/wallete-service'
                        ],
                        'transaction-service': [
                            context: './transaction-service',
                            image: 'digital-wallet-transaction-service',
                            repository: 'wallet/transaction-service'
                        ],
                        'gateway-service': [
                            context: './gateway-service',
                            image: 'digital-wallet-gateway-service',
                            repository: 'wallet/api-gateway'
                        ]
                    ]

                    def service = services[params.SERVICE]

                    sh "docker build -t ${service.image}:latest ${service.context}"

                    sh """
                        docker tag ${service.image}:latest \
                        ${ECR_REGISTRY}/${service.repository}:latest
                    """
                }
            }
        }

        stage('Login to ECR and Push') {
            steps {
                sh '''
                    aws ecr get-login-password --region "$AWS_REGION" |
                    docker login --username AWS --password-stdin "$ECR_REGISTRY"
                '''

                script {
                    def repositories = [
                        'auth': 'waller/auth-service',
                        'user-service': 'wallet/user-service',
                        'wallete-service': 'wallet/wallete-service',
                        'transaction-service': 'wallet/transaction-service',
                        'gateway-service': 'wallet/api-gateway'
                    ]

                    sh "docker push ${ECR_REGISTRY}/${repositories[params.SERVICE]}:latest"
                }
            }
        }
    }

    post {
        success {
            echo "Successfully built and pushed ${params.SERVICE} to ECR."
        }
        failure {
            echo "Build or push failed for ${params.SERVICE}. Check Console Output."
        }
        always {
            sh 'docker logout "$ECR_REGISTRY" || true'
        }
    }
}