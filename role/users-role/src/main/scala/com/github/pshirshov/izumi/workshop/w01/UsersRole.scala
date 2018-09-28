package com.github.pshirshov.izumi.workshop.w01

import com.github.pshirshov.izumi.distage.roles.roles.{RoleId, RoleService}
import com.github.pshirshov.izumi.logstage.api.IzLogger
import BifunctorIO._

@RoleId("users")
class UsersRole[F[+ _, + _] : BifunctorIO : BiRunnable]
(userStorage: UserStorage[F], logger: IzLogger)
  extends RoleService {
  override def start(): Unit = {
    logger.info("Entrypoint reached: users role")

    val logic = for {
      _ <- userStorage.saveUser(
        User(UserId("user-01"), PersonName("John", "Doe"), Email("john.doe@gmail.com"))
      )
      _ <- userStorage.saveUser(
        User(UserId("user-02"), PersonName("Will", "Smith"), Email("will.smith@gmail.com"))
      )
      john <- userStorage.findByEmail(Email("john.doe@gmail.com"))
    } yield {
      john
    }

    logger.info(s"User: ${BiRunnable[F].unsafeRunSync(logic) -> "john"}")

    logger.info("TODO: here we may start an RPC or HTTP server and start serving requests...")
  }

  override def stop(): Unit = {
    logger.info("Exit reached: users role. TODO: here we may stop our RPC server")
  }
}
