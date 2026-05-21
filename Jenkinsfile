pipeline {
    agent any
    
    environment {
        MAVEN_HOME = '/usr/share/maven'
        JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
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
                    sh './mvnw clean compile'
                }
            }
        }
        
        stage('Build Frontend') {
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
        
        stage('Test Backend') {
            steps {
                echo 'Running backend tests...'
                dir('backend') {
                    sh './mvnw test'
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Test Frontend') {
            steps {
                echo 'Running frontend tests...'
                dir('frontend') {
                    sh 'npm run test -- --watch=false'
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
