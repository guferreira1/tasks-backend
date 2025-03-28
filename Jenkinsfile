pipeline {
    agent any
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000'
        SONAR_PROJECT_KEY = 'DeployBack'
        SONAR_LOGIN = 'squ_57eeaf274335f1dc6ac084eba7a76521d6e0c09e'
        SONAR_COVERAGE_PATH = 'target/site/jacoco/jacoco.xml'
        SONAR_EXCLUSIONS = '**/.mvn/**,**/model/**,**/Application.java'
    }

    stages {
        stage ('Build Backend') {
            steps {
                sh 'mvn clean install package -DskipTests=true'
            }
        }

        stage ('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage ('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONAR_SCANNER'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    sh """
                        ${scannerHome}/bin/sonar-scanner \
                        -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                        -Dsonar.host.url=${SONAR_HOST_URL} \
                        -Dsonar.login=${SONAR_LOGIN} \
                        -Dsonar.java.binaries=target/classes \
                        -Dsonar.sources=src/main/java \
                        -Dsonar.tests=src/test/java \
                        -Dsonar.coverage.jacoco.xmlReportPaths=${SONAR_COVERAGE_PATH} \
                        -Dsonar.coverage.exclusions=${SONAR_EXCLUSIONS} \
                        -Dsonar.log.level=DEBUG \
                        -Dsonar.verbose=true
                    """
                }
            }
        }

        stage ('Quality Gate') {
            steps {
                script {
                    sleep(5)
                    timeout(time: 1, unit: 'MINUTES') {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage ('Deploy Backend') {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'TomcatLogin', path: '', url: 'http://host.docker.internal:8001/manager/text')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
            }
        }
    }
}