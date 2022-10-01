pipeline {
    agent any

    stages {

        stage ('Compile Stage'){
            steps{
                withMaven(maven : 'apache-maven-3.8.1'){
                                bat 'mvn clean compile'
                            }
                }
            }


        stage ('Testing Stage'){
            steps{
                withMaven(maven : 'apache-maven-3.8.1'){
                                bat 'mvn test'
                            }
                }
            }


        stage ('Deployment Stage'){
            steps{
                withMaven(maven : 'apache-maven-3.8.1'){
                                bat 'mvn deploy'
                            }
                }
            }
    }
}