pipeline {

//here if you dont mention your particular agent it will take master as default agent 
agent any 

// Here i am configuring my Build tool 
        tools {
  maven 'maven-3.0.5'
}

//here i given Corn-job as poll-SCM instruction when to perfom the task  
triggers {
  //poll SCM
  pollSCM '* * * * *'
}

// by this you can discrad the old builds and retain how many Builds you want  
options {
  buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '2', daysToKeepStr: '', numToKeepStr: '2')
  timestamps()
}

//start the acutally task now by stages wise 
stages {
//GitCheckout 
    stage('GitCheckout') {
    steps {
     git credentialsId: '856cc414-ad5f-492b-a1ec-ce6256523297', url: 'https://github.com/eswar-devops-21-com/mavenrepo.git'   
        }
     }
     
//Build       
    stage('MavenBuild') {
            steps {
                  sh 'mvn clean package'
            }     
    }
   
//Execute SonarQube Report      
    stage('sonarqube Report') {
            steps {
                  sh 'mvn sonar:sonar'
            }     
    }
    
//Upload Artifact Nexus Repository
    stage('Deploy Into NexusRepo') {
            steps {
                  sh 'mvn deploy'
            }     
    }
    
  
//Using upload Application.war into production
    stage ('Tomcat deployment'){
          steps{
   sshagent(['8b05b4bc-9e93-4526-b2a2-35e896fd501a']) {
   sh "scp -o StrictHostKeyChecking=no target/studentapp-2.5-SNAPSHOT.war ubuntu@3.110.136.225:/opt/Tomcat/webapps/"
    }
        }
            }
      
    }//stage closing 

//Here starting the alert Via email notified if any issue raised 
post {
  always {
    // One or more steps need to be included within each condition's block.
    emailtext body: '''build is over... always


Regards
subhan shaik

''', subject: 'Build is over', to: 'sksubhani2121@gmail.com,eswargandhi9808@gmail.com'
  }
  aborted {
    // One or more steps need to be included within each condition's block.
    emailext body: '''build is over...aborted


Regards
subhan shaik

''', subject: 'Build is over', to: 'sksubhani2121@gmail.com,eswargandhi9808@gmail.com'
  }
  success {
    // One or more steps need to be included within each condition's block.
    emailext body: '''build is over... success


Regards
subhan shaik

''', subject: 'Build is over', to: 'sksubhani2121@gmail.com,eswargandhi9808@gmail.com'
  }
  failure {
    // One or more steps need to be included within each condition's block.
    emailext body: '''build is over...failure


Regards
subhan shaik

''', subject: 'Build is over', to: 'sksubhani2121@gmail.com,eswargandhi9808@gmail.com'
  }
}

}//pipeline closing
