pipeline {
  agent any

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
