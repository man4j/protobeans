pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -w /usr/src/mymaven/examples/mvc-security'
    }
    
  }
  stages {
    stage('build image') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml package -DskipTests=true'
      }
    }
  }
}