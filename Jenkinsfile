pipeline {
    agent any

    environment {
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

                            sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                        }
                    } catch (Exception e) {
                        echo " SonarQube est indisponible, étape ignorée."
                    }
                }
            }
        }


    }

    post {
        success {
            echo ' Build terminé avec succès !'
        }
        failure {
            echo ' Échec du pipeline.'
        }
    }
}
