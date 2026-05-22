pipeline {
    agent any
    
    environment {
        MAVEN_HOME = '/usr/share/maven'
        NODE_VERSION = '18'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }
        
        stage('Build Backend') {
            steps {
                echo 'Building backend with Maven...'
                dir('backend') {
                    sh '''
                        export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
                        echo "JAVA_HOME=$JAVA_HOME"
                        ./mvnw clean compile
                    '''
                }
            }
        }
        
        stage('Build Frontend') {
            agent {
                docker {
                    image 'node:18'
                }
            }
            steps {
                echo 'Building frontend with Angular CLI...'
                dir('frontend') {
                    sh '''
                        npm install
                        npm run build
                    '''
                }
            }
        }
        
        // stage('Test Backend') {
        //     steps {
        //         echo 'Running backend tests...'
        //         dir('backend') {
        //             sh '''
        //                 export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
        //                 echo "JAVA_HOME=$JAVA_HOME"
        //             '''
        //         }
        //     }
        //     post {
        //         always {
        //             junit 'backend/target/surefire-reports/*.xml'
        //         }
        //     }
        // }
        
        stage('Test Frontend') {
            agent {
                docker {
                    image 'node:18'
                }
            }
            steps {
                echo 'Running frontend tests...'
                dir('frontend') {
                    sh 'npm install && npm run test -- --watch=false --browsers=ChromeHeadless'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                echo 'Building Docker images...'
                script {
                    sh '''
                        # Backend Docker image
                        docker build -f backend/Dockerfile -t starter-backend:latest ./backend
                        
                        # Frontend Docker image
                        docker build -f frontend/Dockerfile -t starter-frontend:latest ./frontend
                    '''
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying application...'
                script {
                    // Configure your deployment steps here
                    // Examples:
                    // sh 'docker push starter-backend:latest'
                    // sh 'docker push starter-frontend:latest'
                    // sh 'kubectl apply -f deployment.yaml'
                    echo 'Deployment configuration needed - update with your deployment target'
                }
            }
        }
    }
    
    post {
        always {
            echo 'Cleaning up...'
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed! Check logs for details.'
        }
    }
}
