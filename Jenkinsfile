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
                                    sh 'mvn jacoco:report'
                                    sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                                }
                            } catch (Exception e) {
                                echo " SonarQube est indisponible, étape ignorée."
                            }
                        }
                    }
            }
         stage('Deploy to Nexus') {
                        steps {
                               configFileProvider([configFile(fileId: '8ed318fb-bfa6-4f6c-bb07-90b46f622502', variable: 'MAVEN_SETTINGS')]) {
                                             withCredentials([usernamePassword(credentialsId: 'nexus-creds', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
                                                 withEnv(["NEXUS_USERNAME=$NEXUS_USER", "NEXUS_PASSWORD=$NEXUS_PASS"]) {
                                                     sh 'mvn deploy -s $MAVEN_SETTINGS -Dnexus.username=$NEXUS_USERNAME -Dnexus.password=$NEXUS_PASSWORD'
                                                 }
                                             }
                                     }
                                }
          }
        stage('Create Docker Image') {
            steps {
                script {
                    sh 'docker build -t foyer-app:1.0 .'
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
