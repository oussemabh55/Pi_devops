pipeline {
    agent any

    environment {
       SONAR_TOKEN = credentials('jenkins-token')
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
                            withSonarQubeEnv('SonarQubeServer') {

                                sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                            }
                        }
                    }
                }




    post {
        success {
            echo ' Build terminé avec succès !'
        }
        failure {
            echo 'Échec du pipeline.'
        }
    }
}
