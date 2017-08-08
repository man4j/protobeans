pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -v ${PWD}/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Maven Build') {
      environment {
        SETTINGS_XML = credentials('settings.xml')
      }
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml clean deploy -DskipTests=true'
      }
    }
  }
}