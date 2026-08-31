pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "chitrika/medicure:${BUILD_NUMBER}"
        DOCKER_CREDENTIALS = "dockerhub-creds"
        K8S_MASTER = "172.31.43.245"
        SMOKE_TEST_HOST = "172.31.38.6"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: "${DOCKER_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push ${DOCKER_IMAGE}
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                    ssh -o StrictHostKeyChecking=no \
                    -i /home/ubuntu/projectkey.pem \
                    ubuntu@${K8S_MASTER} \
                    "kubectl set image deployment/medicure medicure=${DOCKER_IMAGE} && \
                     kubectl rollout status deployment/medicure --timeout=180s"
                '''
            }
        }

        stage('Smoke Test') {
            steps {
                sh '''
                    echo "Running Medicure smoke test..."

                    ssh -o StrictHostKeyChecking=no \
                    -i /home/ubuntu/projectkey.pem \
                    ubuntu@${K8S_MASTER} \
                    "curl --max-time 15 -f http://${SMOKE_TEST_HOST}:30082/"

                    echo "Smoke test PASSED"
                '''
            }
        }
    }

    post {
        success {
            echo 'Medicure CI/CD pipeline completed successfully!'
        }

        failure {
            echo 'Medicure CI/CD pipeline failed!'
        }
    }
}

