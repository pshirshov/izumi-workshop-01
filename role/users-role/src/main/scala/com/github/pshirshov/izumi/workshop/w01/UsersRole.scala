package com.github.pshirshov.izumi.workshop.w01

import com.github.pshirshov.izumi.distage.roles.roles.{RoleId, RoleService}
import com.github.pshirshov.izumi.logstage.api.IzLogger

@RoleId("users")
class UsersRole(logger: IzLogger) extends RoleService {
  override def start(): Unit = {
    logger.info("Entrypoint reached: users role")
  }
}
