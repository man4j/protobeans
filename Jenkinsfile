pipeline {
  agent any
  stages {
    stage('clean compile') {
      steps {
        sh 'docker run --rm -v ${PWD}/.m2:/root/.m2 -v ${PWD}:/usr/src/mymaven -w=/usr/src/mymaven maven -w /usr/src/mymaven/examples/mvc-security/pom.xml mvn clean compile'
      }
    }
  }
}