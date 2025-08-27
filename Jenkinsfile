pipeline {
    agent any

    environment {
        NEXUS_URL = "https://nexus.example.com/repository/maven-releases/"
        NEXUS_USER = credentials('nexus-user')
        NEXUS_PASSWORD = credentials('nexus-pass')
        SONARQUBE = credentials('sonar-token')  // Token de SonarQube
        SNYK_TOKEN = credentials('snyk-token')  // Token de Snyk
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Vexced/Ara_culture_backend'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh """
                mvn sonar:sonar \
                  -Dsonar.projectKey=Ara-culture-backend \
                  -Dsonar.organization=vexced \
                  -Dsonar.host.url=https://sonarcloud.io \
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
                mvn deploy -DskipTests \
                  -Dnexus.url=$NEXUS_URL \
                  -Dnexus.username=$NEXUS_USER \
                  -Dnexus.password=$NEXUS_PASSWORD
                """
            }
        }

        stage('Deploy to Server via Docker') {
            steps {
                script {
                    def REMOTE_USER = "vexced"
                    def REMOTE_HOST = "192.168.100.252"
                    def REMOTE_DIR = "/home/vexced/IdeaProjects/ara_culture_backend"
                    def IMAGE_NAME = "ara-culture-backend:latest"
                    def JAR_NAME = "ara-culture-backend-0.0.1-SNAPSHOT.jar"

                    // Construir la imagen
                    sh "docker build -t ${IMAGE_NAME} ."

                    // Enviar imagen al servidor
                    sh "docker save ${IMAGE_NAME} | bzip2 | ssh ${REMOTE_USER}@${REMOTE_HOST} 'bunzip2 | docker load'"

                    // Reiniciar contenedor remoto
                    sh """
                    ssh ${REMOTE_USER}@${REMOTE_HOST} '
                      docker stop ara-backend || true
                      docker rm ara-backend || true
                      docker run -d --name ara-backend -p 8080:8080 ${IMAGE_NAME}
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline completado correctamente.'
        }
        failure {
            echo 'Pipeline falló.'
        }
    }
}
