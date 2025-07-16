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
                    withSonarQubeEnv('SonarQubeServer') {
                        sh 'mvn jacoco:report'
                        sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN}"
                    }
                }
            }
        }


        stage('Deploy to Nexus') {
            steps {
                configFileProvider([configFile(fileId: '8ed318fb-bfa6-4f6c-bb07-90b46f622502', variable: 'MAVEN_SETTINGS')]) {
                    sh """
                        mvn deploy -s $MAVEN_SETTINGS \
                        -Dnexus.username=${NEXUS_CREDS_USR} \
                        -Dnexus.password=${NEXUS_CREDS_PSW}
                    """
                }
            }
        }

    }

    post {
        success {
            echo 'Build terminé avec succès !'
        }
        failure {
            echo ' Échec du pipeline.'
        }
    }
}
