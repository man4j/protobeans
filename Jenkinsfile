pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -v ${PWD}/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Clean') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml clean'
      }
    }
    stage('Compile') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml compile -DskipTests=true'
      }
    }
    stage('Jar') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml jar:jar'
      }
    }
    stage('Shade') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml shade:shade'
      }
    }
  }
}