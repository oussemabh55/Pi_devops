pipeline {
    agent {
        docker {
            image 'docker:latest'             // Utilise l'image officielle Docker client
            args '-v /var/run/docker.sock:/var/run/docker.sock' // Monte le socket Docker
        }
    }

    environment {
        DOCKER_HOST = 'tcp://docker-cli-helper:2375'
        SONARQUBE_SCANNER_HOME = tool 'sonar-scanner'
        SONAR_TOKEN = credentials('sonar-token')
        NEXUS_CREDS = credentials('nexus-creds')
    }

    tools {
        jdk 'JDK 17'
        maven 'Maven'
    }

    stages {
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

        stage('Build Docker Image') {  // <== Stage manquant
            steps {
                script {
                    sh 'docker version'               // Vérifie la connexion Docker
                    sh 'docker build -t foyer-app:1.0 .'  // Build de l'image Docker
                }
            }
        }
    }

    post {
        success {
            echo 'Build terminé avec succès !'
        }
        failure {
            echo 'Échec du pipeline.'
        }
    }
}
