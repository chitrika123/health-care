pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "chitrika/medicure:${BUILD_NUMBER}"
        K8S_MASTER = "172.31.43.245"
        SMOKE_TEST_HOST = "172.31.42.92"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/chitrika123/health-care.git'
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
                sh 'docker push ${DOCKER_IMAGE}'
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
                    ubuntu@${K8S_MASTER} "
                    
                    curl --max-time 15 -f -X POST \
                    http://${SMOKE_TEST_HOST}:30082/registerDoctor \
                    -H 'Content-Type: application/json' \
                    -d '{\"doctorRegistrationId\":\"SMOKE001\",\"doctorName\":\"Smoke Test\",\"doctorSpeciality\":\"Testing\",\"doctorExperience\":\"1 Year\"}' &&

                    curl --max-time 15 -f \
                    http://${SMOKE_TEST_HOST}:30082/searchDoctor/Smoke%20Test &&

                    curl --max-time 15 -f -X PUT \
                    http://${SMOKE_TEST_HOST}:30082/updateDoctor/SMOKE001 \
                    -H 'Content-Type: application/json' \
                    -d '{\"doctorName\":\"Smoke Test\",\"doctorSpeciality\":\"Updated Testing\",\"doctorExperience\":\"2 Years\"}' &&

                    curl --max-time 15 -f \
                    http://${SMOKE_TEST_HOST}:30082/searchDoctor/Smoke%20Test &&

                    curl --max-time 15 -f -X DELETE \
                    http://${SMOKE_TEST_HOST}:30082/deletePolicy/SMOKE001
                    
                    "

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
