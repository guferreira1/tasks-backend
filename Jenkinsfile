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
                    timeout(time: 1, unit: 'MINUTES') {
                        def qualityGate = waitForQualityGate()
                        if (qualityGate.status != 'OK') {
                            error "Pipeline failed due to Quality Gate: ${qualityGate.status}"
                        }
                    }
                }
            }
        }
    }
}
