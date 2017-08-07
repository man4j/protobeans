pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -w /usr/src/mymaven/examples/mvc-security -w examples/mvc-security'
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