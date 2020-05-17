package com.raptors.dashboard

import com.typesafe.config.{Config, ConfigFactory}

object ConfigLoader {

  val config: Config = ConfigFactory.load()

}
