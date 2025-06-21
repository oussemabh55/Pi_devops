pipeline {
  agent {
    docker {
      image 'maven:3.9.6-eclipse-temurin-17'
    }
  }

  stages {
    stage('Clone') {
      steps {
        git 'https://github.com/oussemabh55/Pi_devops.git'
      }
    }

    stage('Clean') {
      steps {
        sh 'mvn clean'
      }
    }
  }
}
