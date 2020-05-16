package com.raptors.dashboard

import scala.util.Random

trait Feeder {

  val adminCredentialsFeeder: Iterator[Map[String, String]] =
    Iterator.continually(
      Map(
        "adminLogin" -> getRandomString,
        "adminPassword" -> getRandomString,
        "adminKey" -> "395a4f34a0fec5df4b644cc480c8d861"))


  val instanceFeeder: Iterator[Map[String, String]] =
    Iterator.continually(
      Map(
        "name" -> getRandomString,
        "httpUri" -> getHttpUri,
        "instanceLogin" -> getRandomString,
        "instancePassword" -> getRandomString,
        "sshUri" -> getSshUri,
        "command" -> getRandomString,
        "hostLogin" -> getRandomString,
        "hostPassword" -> getRandomString))

  val getCredentialsFeeder: Iterator[Map[String, String]] =
    Iterator.continually(
      Map(
        "publicKey" -> ConfigLoader.config.getString("publicKey")))

  private def getRandomString: String = Random.alphanumeric.take(10).mkString

  private def getHttpUri: String = "http://" + getRandomString

  private def getSshUri: String = "ssh://" + getRandomString
}
