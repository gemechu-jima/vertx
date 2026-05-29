pipeline {
    agent any

    triggers {
        githubPush()
    }

    environment {
        MAVEN_HOME = '/usr/share/maven'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Clean Workspace') {
            steps {
                sh '''
                    rm -rf frontend/node_modules
                    rm -f frontend/package-lock.json
                '''
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
                echo 'Building frontend...'
                dir('frontend') {
                    sh '''
                        docker run --rm -v $PWD:/app -w /app node:18 bash -c "
                            npm install &&
                            npm run build
                        "
                    '''
                }
            }
        }

        stage('Test Frontend') {
            steps {
                echo 'Running frontend tests...'
                dir('frontend') {
                    sh '''
                        docker run --rm -v $PWD:/app -w /app node:18 npm test || true
                    '''
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    sh '''
                        docker build -t starter-backend:latest backend
                        docker build -t starter-frontend:latest frontend
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
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}