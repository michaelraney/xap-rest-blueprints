#!/usr/bin/env groovy
//Jenkins Pipleline example demonstrating XAP Deployment using REST services
//Requires Http Request Plugin https://plugins.jenkins.io/http_request
//Creates new container and Deploys an Empty Space to the Service Grid

//Print Available Host in Service Grid
stage("Available Hosts Check"){
  def availableHostResponse =    httpRequest httpMode:"GET", consoleLogResponseBody:true, contentType:"APPLICATION_JSON",  url:"http://172.17.0.3:8090/v1/hosts"
  println("availableHostResponse Response Code "+availableHostResponse.status)
  println("availableHostResponse Content: "     +availableHostResponse.content)
}

//Create a new GSC Container Resource
stage("Create Container"){
  def createContainerResponse =  httpRequest httpMode:"POST", consoleLogResponseBody:true, contentType:"APPLICATION_JSON", requestBody:"{ \"host\": \"172.17.0.3\", \"memory\": \"512m\" }", url:"http://172.17.0.3:8090/v1/containers"
  println("createContainerResponse Response Code: "+createContainerResponse.status)
  sleep(10)//Create Container is asynchronous
}

//Deploy an Empty Space (Datagrid) to available Container Resource
stage("Deploy Empty Space"){
  def deployEmptySpaceResponse = httpRequest httpMode:"POST", consoleLogResponseBody:true, contentType:"APPLICATION_JSON", url:"http://172.17.0.3:8090/v1/spaces?name=dataGrid&partitions=1&backups=false&requiresIsolation=true"
  println("deployEmptySpaceResponse Response Code: "+deployEmptySpaceResponse.status)
  sleep(10)//Deploy Space is asynchronous
}

//Confirm Deployment of Empty Space 
stage("Available Spaces Check"){
  def availableSpacesResponse =  httpRequest httpMode:"GET",  consoleLogResponseBody:true, contentType:"APPLICATION_JSON", url:"http://172.17.0.3:8090/v1/spaces"
  println("availableSpacesResponse Response Code: "+availableSpacesResponse.status)
  println("availableSpacesResponse Content: "      +availableSpacesResponse.content)
}