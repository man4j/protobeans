pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -v ${PWD}/.m2:/root/.m2'
    }
  }

  environment {
    SETTINGS_XML = credentials('settings.xml')
  }

  stages {
    stage('Clean') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml clean:clean'
      }
    }
    stage('Resources') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml resources:resources resources:testResources'
      }
    }
    stage('Compile') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml compiler:compile compiler:testCompile'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -s $SETTINGS_XML -f examples/mvc-security/pom.xml surefire:test'
      }
    }
    stage('Package') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml jar:jar shade:shade'
      }
    }
    stage('Deploy') {
      steps {
        sh 'mvn -s $SETTINGS_XML -f examples/mvc-security/pom.xml docker:build -DpushImage'
      }
    }
  }

  post {
    always {
      junit 'examples/mvc-security/target/surefire-reports/*.xml'
    }
  }
}
