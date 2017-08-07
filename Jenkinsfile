pipeline {
  agent any
  stages {
    stage('build image') {
      steps {
        sh '''docker run --rm \
-v ${PWD}/.m2:/root/.m2 \
-v ${PWD}:/usr/src/mymaven \
-v /var/run/docker.sock:/var/run/docker.sock \
-w /usr/src/mymaven/examples/mvc-security \
maven mvn clean package -DskipTests=true'''
      }
    }
  }
}