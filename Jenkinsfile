pipeline {
  agent {
    docker {
      image 'maven'
      args '-v /var/run/docker.sock:/var/run/docker.sock -v ${PWD}/.m2:/root/.m2'
    }
    
  }
  stages {
    stage('Compile') {
      steps {
        sh '''printenv
mvn -f examples/mvc-security/pom.xml clean compile'''
      }
    }
    stage('Test') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml test'
      }
    }
    stage('Package') {
      steps {
        sh 'mvn -f examples/mvc-security/pom.xml package'
      }
    }
    stage('Deploy') {
      environment {
        SETTINGS_XML = credentials('settings.xml')
      }
      steps {
        sh 'mvn -s $SETTINGS_XML -f examples/mvc-security/pom.xml deploy'
      }
    }
  }
  environment {
    gmailUser = credentials('gmailUser')
    gmailPassword = credentials('gmailPassword')
    facebookSecret = credentials('facebookSecret')
  }
  post {
    always {
      junit 'examples/mvc-security/target/surefire-reports/*.xml'
      
    }
    
  }
}