pipeline {
  agent any
  stages {
    stage('Checkout sources') {
      steps {
        git(url: 'https://github.com/bepoland-academy/authentication-service.git', branch: 'development')
      }
    }
  }
}