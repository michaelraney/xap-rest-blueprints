#!/usr/bin/env groovy
//Requires Http Request Plugin https://plugins.jenkins.io/http_request
//Deploys an Empty Space to the Grid and prints space status 
stage('available host check'){
  def availableHostResponse =    httpRequest httpMode:"GET", contentType:"APPLICATION_JSON", consoleLogResponseBody:true, url:"http://172.17.0.3:8090/v1/hosts"
  println('availableHostResponse Response Code'+availableHostResponse.status)
  println("availableHostResponse Content: "+availableHostResponse.content)
}

stage('create container'){
  def createContainerResponse =  httpRequest httpMode:"POST", contentType:"APPLICATION_JSON", requestBody:"{ \"host\": \"172.17.0.3\", \"memory\": \"512m\" }", consoleLogResponseBody:true, url:"http://172.17.0.3:8090/v1/containers"
  println('createContainerResponse Response Code: '+createContainerResponse.status)
  sleep(10)// Create Container is asynchronous
}

stage('deploy empty grid'){
  def deployEmptySpaceResponse = httpRequest httpMode:"POST", consoleLogResponseBody:true, url:"http://172.17.0.3:8090/v1/spaces?name=dataGrid&partitions=1&backups=false&requiresIsolation=true"
  println('deployEmptySpaceResponse Response Code: '+deployEmptySpaceResponse.status)
  sleep(10)// Deploy Space is asynchronous
}

stage('check available spaces'){
  def availableSpacesResponse =  httpRequest consoleLogResponseBody:true, url:"http://172.17.0.3:8090/v1/spaces"
  println('availableSpacesResponse Response Code: '+availableSpacesResponse.status)
  println("availableSpacesResponse Content: "+availableSpacesResponse.content)
}