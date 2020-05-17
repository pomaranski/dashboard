package com.raptors.dashboard

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.request.builder.HttpRequestBuilder

import scala.language.postfixOps

trait Actions {

  val managementBaseUrl: String = ConfigLoader.config.getString("managementBaseUrl")

  def loginAdmin(): HttpRequestBuilder = {
    http("login admin")
      .post("/login")
      .body(StringBody(
        s"""
           |{
           |  "login": "$${adminLogin}",
           |  "password": "$${adminPassword}",
           |  "key": "$${adminKey}"
           |}
           |""".stripMargin))
      .check(status.is(200))
      .check(jsonPath("$.token").saveAs("token"))
      .check()
  }

  def addInstance(): HttpRequestBuilder = {
    http("add instance")
      .post("/user/instance")
      .header("Authorization", s"$${token}")
      .body(StringBody(
        s"""
           |{
           |  "name": "$${name}",
           |  "httpUri": "$${httpUri}",
           |  "instanceLogin": "$${instanceLogin}",
           |  "instancePassword": "$${instancePassword}",
           |  "sshUri": "$${sshUri}",
           |  "command": "$${command}",
           |  "hostLogin": "$${hostLogin}",
           |  "hostPassword": "$${hostPassword}"
           |}
           |""".stripMargin))
      .check(status.is(200))
      .check(jsonPath("$.uuid").saveAs("instanceUuid"))
      .check()
  }

  def removeInstance(): HttpRequestBuilder = {
    http("remove instance")
      .delete(s"/user/instance/$${instanceUuid}")
      .header("Authorization", s"$${token}")
      .check(status.is(200))
  }

  def getAllInstances(): HttpRequestBuilder = {
    http("get all instances")
      .get(s"/user/instance")
      .header("Authorization", s"$${token}")
      .check(status.is(200))
      .check()
  }

  def getCredentials(): HttpRequestBuilder = {
    http("get credentials")
      .post(s"/user/instance/$${instanceUuid}/credentials")
      .header("Authorization", s"$${token}")
      .body(StringBody(
        s"""
           |{
           |  "publicKey": "$${publicKey}"
           |}
           |""".stripMargin))
      .check(status.is(200))
      .check()
  }

  def registerAdmin(): HttpRequestBuilder = {
    http("register admin")
      .post(s"${managementBaseUrl}/register/admin")
      .body(StringBody(
        s"""
           |{
           |  "login": "$${adminLogin}",
           |  "password": "$${adminPassword}",
           |  "key": "$${adminKey}"
           |}
           |""".stripMargin))
      .check(status.is(200))
      .check()
  }

  def getUnassigned(): HttpRequestBuilder = {
    http("get unassigned")
      .get(s"/admin/instance/unassigned")
      .header("Authorization", s"$${token}")
      .check(status.is(200))
      .check()
  }
}
