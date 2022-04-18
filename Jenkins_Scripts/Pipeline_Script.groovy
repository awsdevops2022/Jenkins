node{

//How many Builds to retain after trigger
properties([buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '2', daysToKeepStr: '', numToKeepStr: '2')), [$class: 'JobLocalConfiguration', changeReasonComment: ''], 
pipelineTriggers([pollSCM('* * * * *')])])    
 
//define the Maven   
def mavenHome= tool name: "maven-3.0.5"  
    
//Get the code from GitHub Repo
stage('checkout'){
 git credentialsId: '856cc414-ad5f-492b-a1ec-ce6256523297', url: 'https://github.com/SUBHANSHAIK212-com/mavenrepo.git'   
}


//Using maven doing the build
stage('Build'){
sh "${mavenHome}/bin/mvn clean package"
}

//Using sonar doing the quality check report
stage('sonar'){
sh "${mavenHome}/bin/mvn sonar:sonar"    
}

//Using upload Artifacts Into NexusRepository
stage('deployment'){
sh "${mavenHome}/bin/mvn deploy"    
}

//Using upload Application.war into production
 stage ('Tomcat-deployment'){
sshagent(['8b05b4bc-9e93-4526-b2a2-35e896fd501a']) {
sh "scp -o StrictHostKeyChecking=no target/studentapp-2.5-SNAPSHOT.war ubuntu@3.110.136.225:/opt/Tomcat/webapps/"
    }  
      }
 
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

}//pipe-line closing
