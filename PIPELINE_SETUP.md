# Jenkins Pipeline Configuration Guide

## Setup Instructions

### 1. **Jenkins Installation**
- Install Jenkins on your server
- Install required plugins: Git, Docker, Maven, NodeJS

### 2. **Create Pipeline Job**
- New Item → Pipeline
- Configure → Pipeline section
- Select "Pipeline script from SCM"
- Repository URL: `<your-repo-url>`
- Script Path: `Jenkinsfile`

### 3. **Configure Webhook (Optional)**
- GitHub/GitLab → Settings → Webhooks
- Add Jenkins URL: `http://jenkins-server:8080/github-webhook/`

### 4. **Deployment Configuration**
Update the Deploy stage in Jenkinsfile with your target:

#### Docker Registry Push
```groovy
stage('Deploy') {
    steps {
        script {
            withCredentials([usernamePassword(credentialsId: 'docker-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh '''
                    echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin
                    docker push starter-backend:latest
                    docker push starter-frontend:latest
                '''
            }
        }
    }
}
```

#### Kubernetes Deployment
```groovy
stage('Deploy') {
    steps {
        script {
            sh 'kubectl apply -f deployment.yaml'
        }
    }
}
```

#### SSH Deployment
```groovy
stage('Deploy') {
    steps {
        sshagent(['deploy-ssh-key']) {
            sh '''
                ssh user@server "cd /app && docker-compose pull && docker-compose up -d"
            '''
        }
    }
}
```

## Local Development

### Build and Run with Docker Compose
```bash
docker-compose up --build
```

- Backend: http://localhost:8080
- Frontend: http://localhost

### Without Docker
```bash
# Backend
cd backend && ./mvnw spring-boot:run

# Frontend (new terminal)
cd frontend && npm install && npm start
```

## Pipeline Stages

1. **Checkout** - Clone repository
2. **Build Backend** - Compile Java code
3. **Build Frontend** - Install dependencies and build Angular app
4. **Test Backend** - Run Maven tests
5. **Test Frontend** - Run Angular tests
6. **Build Docker Images** - Create container images
7. **Deploy** - Deploy to your target environment (configure as needed)

## Troubleshooting

### Maven builds fail
- Ensure Java 11+ is installed
- Check `mvn -version` in Jenkins workspace

### Frontend tests timeout
- Increase timeout in package.json test script
- Use `--watch=false` for CI environments

### Docker builds fail
- Verify Docker daemon is running
- Check Jenkins has Docker access: `sudo usermod -aG docker jenkins`
