pipeline {
  agent any

  stages {
    stage('Clone') {
      steps {
        git 'https://github.com/oussemabh55/Pi_devops.git'
      }
    }

    stage('Build dans conteneur Maven') {
      steps {
        script {
          docker.image('maven:3.9.6-eclipse-temurin-17').inside {
            sh 'mvn clean'
          }
        }
      }
    }
  }
}
