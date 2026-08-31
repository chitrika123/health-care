pipeline {

    agent any

    environment {
        DOCKER_IMAGE = "chitrika/medicure:${BUILD_NUMBER}"
        DOCKER_CREDS = "dockerhub-creds"
        K8S_MASTER = "172.31.43.245"
        SMOKE_TEST_HOST = "172.31.42.92"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build & Test') {
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
                        credentialsId: "${DOCKER_CREDS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy to Kubernetes TEST') {
            steps {
                sh '''
                    ssh -o StrictHostKeyChecking=no -i /var/lib/jenkins/projectkey.pem ubuntu@${K8S_MASTER} \
                    "kubectl set image deployment/medicure medicure=${DOCKER_IMAGE} && \
                     kubectl rollout status deployment/medicure --timeout=180s"
                '''
            }
        }

        stage('Smoke Test') {
            steps {
                sh '''
                    echo "Running Medicure smoke test..."

                    curl --max-time 15 -f -X POST http://${SMOKE_TEST_HOST}:30082/registerDoctor \
                    -H "Content-Type: application/json" \
                    -d '{"doctorRegistrationId":"SMOKE001","doctorName":"Smoke Test","doctorSpeciality":"Testing","doctorExperience":"1 Year"}'

                    curl --max-time 15 -f http://${SMOKE_TEST_HOST}:30082/searchDoctor/Smoke%20Test

                    curl --max-time 15 -f -X PUT http://${SMOKE_TEST_HOST}:30082/updateDoctor/SMOKE001 \
                    -H "Content-Type: application/json" \
                    -d '{"doctorName":"Smoke Test","doctorSpeciality":"Updated Testing","doctorExperience":"2 Years"}'

                    curl --max-time 15 -f http://${SMOKE_TEST_HOST}:30082/searchDoctor/Smoke%20Test

                    curl --max-time 15 -f -X DELETE http://${SMOKE_TEST_HOST}:30082/deletePolicy/SMOKE001

                    echo "Smoke test PASSED"
                '''
            }
        }

        stage('Deploy to Kubernetes PROD') {
            steps {
                sh '''
                    ssh -o StrictHostKeyChecking=no -i /var/lib/jenkins/projectkey.pem ubuntu@${K8S_MASTER} \
                    "kubectl set image deployment/medicure medicure=${DOCKER_IMAGE} && \
                     kubectl rollout status deployment/medicure --timeout=180s"
                '''
            }
        }
    }

    post {
        success {
            echo 'Medicure CI/CD pipeline completed successfully.'
        }

        failure {
            echo 'Medicure CI/CD pipeline failed.'
        }
    }
}
