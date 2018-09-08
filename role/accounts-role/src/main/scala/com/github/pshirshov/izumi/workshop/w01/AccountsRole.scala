package com.github.pshirshov.izumi.workshop.w01

import com.github.pshirshov.izumi.distage.roles.roles.RoleService
import com.github.pshirshov.izumi.logstage.api.IzLogger

class AccountsRole(logger: IzLogger) extends RoleService {
  override def start(): Unit = {
    logger.info("Entrypoint reached: accounts role")
  }
}
