pipeline {
    agent any

    environment {
        K8S_MASTER = '172.31.43.245'
        SMOKE_TEST_HOST = '172.31.38.6'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t chitrika/medicure:${BUILD_NUMBER} .'
            }
        }

        stage('Docker Push') {
            steps {
                sh 'docker push chitrika/medicure:${BUILD_NUMBER}'
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                sh '''
                ssh -o StrictHostKeyChecking=no \
                -i /home/ubuntu/projectkey.pem \
                ubuntu@${K8S_MASTER} \
                "kubectl set image deployment/medicure medicure=chitrika/medicure:${BUILD_NUMBER}"
                '''
            }
        }

        stage('Smoke Test') {
            steps {
                echo "Running Medicure smoke test..."

                sh '''
                for i in {1..12}; do
                    echo "Smoke test attempt $i/12..."

                    if ssh -o StrictHostKeyChecking=no \
                        -i /home/ubuntu/projectkey.pem \
                        ubuntu@${K8S_MASTER} \
                        "curl --max-time 5 -f http://${SMOKE_TEST_HOST}:30082/"; then

                        echo "Smoke test PASSED"
                        exit 0
                    fi

                    echo "Application not ready yet. Waiting 5 seconds..."
                    sleep 5
                done

                echo "Smoke test FAILED after 60 seconds"
                exit 1
                '''
            }
        }
    }

    post {
        success {
            echo 'Medicure pipeline completed successfully.'
        }

        failure {
            echo 'Medicure pipeline failed.'
        }
    }
}
