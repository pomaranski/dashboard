package com.raptors.dashboard

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

import scala.concurrent.duration._
import scala.language.postfixOps

class RecordedSimulation extends Simulation with Actions with Feeder {

  private val baseUrl = ConfigLoader.config.getString("baseUrl")
  private val addInstanceRepeats = ConfigLoader.config.getString("addInstanceRepeats")
  private val removeInstancesNumber = ConfigLoader.config.getString("removeInstancesNumber")
  private val getInstancesNumber = ConfigLoader.config.getString("getInstancesNumber")
  private val getInstancesRepeats = ConfigLoader.config.getString("getInstancesRepeats")
  private val getCredentialsInstances = ConfigLoader.config.getString("getCredentialsInstances")
  private val getCredentialsRepeats = ConfigLoader.config.getString("getCredentialsRepeats")

  val addInstanceScenario: ScenarioBuilder = scenario("Add instance")
    .feed(adminCredentialsFeeder)
    .feed(instanceFeeder)
    .exec(registerAdmin())
    .exec(loginAdmin())
    .repeat(addInstanceRepeats) {
      feed(instanceFeeder)
        .exec(addInstance())
    }

  val removeInstanceScenario: ScenarioBuilder = scenario("Remove instance")
    .feed(adminCredentialsFeeder)
    .feed(instanceFeeder)
    .exec(registerAdmin())
    .exec(loginAdmin())
    .repeat(removeInstancesNumber) {
      feed(instanceFeeder)
        .exec(addInstance())
    }
    .exec(removeInstance())

  val getAllInstancesScenario: ScenarioBuilder = scenario("Get all instances")
    .feed(adminCredentialsFeeder)
    .feed(instanceFeeder)
    .exec(registerAdmin())
    .exec(loginAdmin())
    .repeat(getInstancesNumber) {
      feed(instanceFeeder)
        .exec(addInstance())
    }
    .repeat(getInstancesRepeats) {
      feed(instanceFeeder)
        .exec(addInstance())
    }

  val getCredentialsScenario: ScenarioBuilder = scenario("Get credentials")
    .feed(adminCredentialsFeeder)
    .feed(getCredentialsFeeder)
    .exec(registerAdmin())
    .exec(loginAdmin())
    .repeat(getCredentialsInstances) {
      feed(instanceFeeder)
        .exec(addInstance())
    }
    .repeat(getCredentialsRepeats) {
      feed(instanceFeeder)
        .exec(getCredentials())
    }

  val getUnassignedScenario: ScenarioBuilder = scenario("Get unassigned")
    .feed(adminCredentialsFeeder)
    .feed(instanceFeeder)
    .exec(registerAdmin())
    .exec(loginAdmin())
    .exec(addInstance())
    .exec(getUnassigned())


  private val scenarioBuilders = List(
    addInstanceScenario,
    removeInstanceScenario,
    getAllInstancesScenario,
    getCredentialsScenario,
    getUnassignedScenario
  )

  private val JSON_VALUE = "application/json;charset=UTF-8"

  val httpProtocol: HttpProtocolBuilder = http
    .baseUrl(baseUrl)
    .contentTypeHeader(JSON_VALUE)
    .acceptHeader(JSON_VALUE)

  setUp(scenarioBuilders.map(scenario => scenario.inject(
    incrementConcurrentUsers(5)
      .times(5)
      .eachLevelLasting(10 seconds)
      .separatedByRampsLasting(10 seconds)
      .startingFrom(10)
  ).protocols(httpProtocol)))

}
