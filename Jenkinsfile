pipeline {
  agent any
  stages {
    stage('clean compile') {
      steps {
        sh 'docker run --rm -v .:/mnt maven mvn -f /mnt/examples/mvc-security/pom.xml clean compile'
      }
    }
  }
}