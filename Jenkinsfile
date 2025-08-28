pipeline {
    agent any

    environment {
        NEXUS_URL = "https://nexus.example.com/repository/maven-releases/"
        SONAR_HOST = "https://sonarcloud.io"
        IMAGE_NAME = "ara-culture-backend"
        JAR_NAME = "ara-culture-backend-0.0.1-SNAPSHOT.jar"
        REMOTE_USER = "vexced"
        REMOTE_HOST = "192.168.100.252"
        REMOTE_DIR = "/home/vexced/IdeaProjects/ara_culture_backend"
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

        stage('Test SonarCloud Connection') {
            steps {
                sh 'curl -v https://sonarcloud.io/api/system/status'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                    sh """
                    mvn sonar:sonar \
                      -Dsonar.projectKey=Ara-culture-backend \
                      -Dsonar.organization=vexced \
                      -Dsonar.host.url=${SONAR_HOST} \
                      -Dsonar.login=${SONAR_TOKEN}
                    """
                }
            }
        }

        stage('Snyk Scan') {
            steps {
                withCredentials([string(credentialsId: 'snyk-token', variable: 'SNYK_TOKEN')]) {
                    withEnv(["SNYK_TOKEN=${SNYK_TOKEN}"]) {
                        sh '''
                            # Instala snyk localmente en el workspace si no existe
                            if [ ! -d "node_modules/snyk" ]; then
                                npm install snyk
                            fi

                            # Ejecuta el scan usando npx
                            npx snyk auth $SNYK_TOKEN
                            npx snyk test
                        '''
                    }
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                withCredentials([
                    usernamePassword(credentialsId: 'nexus-credentials',
                                     usernameVariable: 'NEXUS_USER',
                                     passwordVariable: 'NEXUS_PASSWORD')
                ]) {
                     configFileProvider([configFile(fileId: 'ea1001b7-bea8-4df7-b20a-68ae4c92b09d', variable: 'MAVEN_SETTINGS')]) {
                                    sh """
                                    mvn deploy -DskipTests --settings $MAVEN_SETTINGS
                                    """
                    }
                }
            }
        }

        stage('Deploy to Server via Docker') {
            steps {
                script {
                    def REMOTE = "${REMOTE_USER}@${REMOTE_HOST}"
                    def IMAGE_TAG = "${IMAGE_NAME}:latest"

                    sh "docker build -t ${IMAGE_TAG} ."
                    sh "docker save ${IMAGE_TAG} | bzip2 | ssh ${REMOTE} 'bunzip2 | docker load'"

                    sh """
                    ssh ${REMOTE} '
                      set -e
                      docker stop ara-backend || true
                      docker rm ara-backend || true
                      docker run -d --name ara-backend -p 8081:8080 ${IMAGE_TAG}
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
