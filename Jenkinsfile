pipeline {
    agent any

    environment {
        DOCKER_HOST = 'tcp://docker-cli-helper:2375'  // Pour utiliser Docker dans Jenkins
        SONARQUBE_SCANNER_HOME = tool 'sonar-scanner'
        SONAR_TOKEN = credentials('sonar-token')
        NEXUS_CREDS = credentials('nexus-creds')
    }

    tools {
        jdk 'JDK 17'
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                 git 'https://github.com/oussemabh55/Pi_devops.git'
             }
        }

        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    try {
                        withSonarQubeEnv('SonarQubeServer') {
                            sh 'mvn jacoco:report'
                            sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                        }
                    } catch (Exception e) {
                        echo "SonarQube est indisponible, étape ignorée."
                    }
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package'
            }
        }

        stage('Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: '25d03788-e469-4627-b668-ebc57dcdcdcf', variable: 'MAVEN_SETTINGS')]) {
                    withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                        withEnv(["NEXUS_USERNAME=$NEXUS_USER", "NEXUS_PASSWORD=$NEXUS_PASS"]) {
                            sh 'mvn deploy -s $MAVEN_SETTINGS -Dnexus.username=$NEXUS_USERNAME -Dnexus.password=$NEXUS_PASSWORD'
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker version || echo "Docker non disponible"'
                    sh 'docker build -t foyer-app:1.0 .'
                }
            }
        }

        // ✅ Étape 8 : Push vers DockerHub
        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    script {

                        def imageName = "$DOCKER_USER/foyer-app:1.0"

                        sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                        sh "docker tag foyer-app:1.0 ${imageName}"
                        sh "docker push ${imageName}"
                        sh "docker logout"
                    }
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }


        stage('Generate Test Report') {
            steps {
                sh 'mvn surefire-report:report-only'
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site',
                    reportFiles: 'surefire-report.html',
                    reportName: "Test Report"
                ])
            }
        }

    }
    post {
        always {
                junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo '✅ Build terminé avec succès !'
        }
        failure {
            echo '❌ Échec du pipeline.'
        }
    }
}
