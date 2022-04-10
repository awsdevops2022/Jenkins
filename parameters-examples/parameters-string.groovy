
pipeline{
    agent any
    environment {
        name = 'Krishna'
    }
    parameters {
        //providing interactive inputs 

        string(name: 'USERID', defaultValue: 'Admin', description: 'Enter your username')
    }
    stages {
        stage('name') { //prompts user whether to execute job 
            input {
                message "should we continue?"
                ok "Yes, We should"
            }
            steps{
                sh 'echo "Hello $name"'
            }
        }
        stage('username') {
            steps{
                echo "Active user is now ${params.USERID}"
            }
        }
    }
    post { //post-build step
        always { //sends mail notification
            mail (body: "${env.BUILD_URL}", subject: 'Test mail', to: 'saikrishnna29@gmail.com')
        }
    }
}
