pipeline {
  agent any
  stages {
    stage('clean compile') {
      steps {
        sh '''docker run --rm maven \
-v .:/mnt \
mvn -f /mnt/examples/mvc-security/pom.xml clean compile'''
      }
    }
  }
}