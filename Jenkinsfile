pipeline {
    agent any

    environment {
        NEXUS_URL = "https://nexus.example.com/repository/npm-releases/"
        NEXUS_USER = credentials('nexus-user')
        NEXUS_PASSWORD = credentials('nexus-pass')
        SONARQUBE = credentials('sonar-token')
        SNYK_TOKEN = credentials('snyk-token')
        JENKINS_HOST = "http://localhost:8981"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/tu-repo/ara-culture-frontend.git'
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'npm install'
            }
        }

        stage('Build') {
            steps {
                sh 'npm run build'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                sonar-scanner \
                  -Dsonar.projectKey=ara-culture-frontend \
                  -Dsonar.sources=src \
                  -Dsonar.host.url=${JENKINS_HOST}/sonarqube \
                  -Dsonar.login=${SONARQUBE}
                """
            }
        }

        stage('Snyk Scan') {
            steps {
                sh "export SNYK_TOKEN=${SNYK_TOKEN} && snyk test"
            }
        }

        stage('Deploy to Nexus') {
            steps {
                sh """
                npm config set registry $NEXUS_URL
                npm config set //nexus.example.com/repository/npm-releases/:_authToken=$NEXUS_PASSWORD
                npm publish --registry $NEXUS_URL
                """
            }
        }

        stage('Deploy to Server via Docker') {
            steps {
                script {
                    def REMOTE_USER = "usuario"
                    def REMOTE_HOST = "192.168.1.100"
                    def IMAGE_NAME = "ara-culture-frontend:latest"

                    sh """
                    docker build -t ${IMAGE_NAME} .
                    docker save ${IMAGE_NAME} | bzip2 | ssh ${REMOTE_USER}@${REMOTE_HOST} 'bunzip2 | docker load'
                    ssh ${REMOTE_USER}@${REMOTE_HOST} '
                      docker stop ara-frontend || true
                      docker rm ara-frontend || true
                      docker run -d --name ara-frontend -p 3000:80 ${IMAGE_NAME}
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Frontend pipeline completado correctamente.'
        }
        failure {
            echo 'Frontend pipeline falló.'
        }
    }
}
