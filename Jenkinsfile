pipeline{
    agent any

    environment {
        IMAGE_TAG = 'v1'
        DOCKERHUB_PWD = credentials('dockerhup-pwd')
    }

    tools{
        maven 'maven_3_5_0'
    }

    stages{
        stage('Build maven'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/LG-BaoPTIT/admin-service']])
                sh 'mvn clean install'
            }

        }

        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t lgbptit/admin-service:${IMAGE_TAG} .'

                }
            }

        }
        stage('Push docker image to Hub'){
            steps{
                script{
                    sh 'docker login -u lgbptit -p ${DOCKERHUB_PWD}'
                    sh 'docker push lgbptit/admin-service:${IMAGE_TAG}'
                }
            }

        }
        stage('Stop and remove old container'){
            steps{
                script{
                    sh 'docker stop admin-service-container || true'
                    sh 'docker rm admin-service-container || true'
                }
            }

        }
        stage('Run docker container'){
            steps{
                script{
                    sh 'docker run -d --name admin-service-container -p 9001:9001 --network myNetwork --ip 172.19.0.6 lgbptit/admin-service:${IMAGE_TAG}'
                }
            }
        }
        // stage('Remove old image'){
        //     steps{
        //         script{
        //             sh 'docker rmi -f lgbptit/admin-service:${IMAGE_TAG} || true'
        //         }
        //     }
        // }


    }
}


