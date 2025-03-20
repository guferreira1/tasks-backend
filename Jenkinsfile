pipeline {
    agent any
    stages {
        stage ('Build Backend') {
            steps {
                sh 'mvn clean install package -DskipTests=true'
            }
        }
    }
}