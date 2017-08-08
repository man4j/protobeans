pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -v ${PWD}/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Maven Build') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml clean package -DskipTests=true'
      }
    }
  }
}