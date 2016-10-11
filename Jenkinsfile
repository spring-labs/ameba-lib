#!groovy

node {
   def mvnHome
   stage('Preparation') {
      git 'git@github.com:abraxas-labs/ameba-lib.git'
      mvnHome = tool 'M3'
   }
   parallel (
     "default-build": {
       stage('Build') {
          sh "'${mvnHome}/bin/mvn' clean install -U"
       }
     },
     "sonar-build": {
       stage('Sonar') {
          sh "'${mvnHome}/bin/mvn' clean org.jacoco:jacoco-maven-plugin:prepare-agent sonar:sonar -Djacoco.propertyName=jacocoArgLine -Dbuild.number=${BUILD_NUMBER} -Dbuild.date=${BUILD_ID} -Pjenkins"
       }
     }
   )
   stage('Results') {
      archive 'target/*.jar'
   }
}

