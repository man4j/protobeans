pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock'
    }
    
  }
  stages {
    stage('build image') {
      steps {
        sh 'mvn clean package -DskipTests=true'
      }
    }
  }
}