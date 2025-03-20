pipeline {
    agent any
    stages {
        stage ('Build Backend') {
            steps {
                sh 'mvn clean install package -DskipTests=true'
            }
        }

        stage ('Unit Testes') {
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
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=squ_57eeaf274335f1dc6ac084eba7a76521d6e0c09e -Dsonar.java.binaries=target -Dsonar.sources=src/main/java -Dsonar.tests=src/test/java -Dsonar.java.binaries=target/classes -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml -Dsonar.coverage.exclusions='**/.mvn/**,**/model/**,**/Application.java' -Dsonar.log.level=DEBUG -Dsonar.verbose=true"
                }

            }
        }

        stage ('Quality Gate') {
            steps {
                waitForQualityGate abortPipeline: true, credentialsId: 'SonarToken'
            }
        }
    }
}