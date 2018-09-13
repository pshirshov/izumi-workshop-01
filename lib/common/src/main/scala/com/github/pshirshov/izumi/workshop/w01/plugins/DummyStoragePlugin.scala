package com.github.pshirshov.izumi.workshop.w01.plugins

import com.github.pshirshov.izumi.distage.model.definition.ModuleDef
import com.github.pshirshov.izumi.distage.model.reflection.universe.RuntimeDIUniverse._
import com.github.pshirshov.izumi.distage.plugins.PluginDef
import com.github.pshirshov.izumi.workshop.w01._
import scalaz.zio.IO

abstract class DummyStorageModule[F[+ _, + _] : TagKK] extends ModuleDef {
  make[AbstractStorage[F, UserId, User]].from[DummyStorage[F, UserId, User]]
  make[AbstractStorage[F, UserId, UserAccount]].from[DummyStorage[F, UserId, UserAccount]]
  make[UserStorage[F]].from[DummyUserStorage[F]]
  make[AccountStorage[F]].from[DummyAccountStorage[F]]
}

// this plugin will be dropped by tag
class DummyStoragePlugin extends DummyStorageModule[IO] with PluginDef {
  tag("dummy", "storage")
}

// this is just for demo purposes
class ProductionStorage[F[+_, +_] : BifunctorIO, K, V]
  extends DummyStorage[F, K, V]
class ProductionUserStorage[F[+_, +_] : BifunctorIO](storage: AbstractStorage[F, UserId, User])
  extends DummyUserStorage[F](storage)
class ProductionAccountStorage[F[+_, +_] : BifunctorIO](storage: AbstractStorage[F, UserId, UserAccount])
  extends DummyAccountStorage[F](storage)

abstract class ProductionStorageModule[F[+ _, + _] : TagKK] extends ModuleDef {
  make[AbstractStorage[F, UserId, User]].from[ProductionStorage[F, UserId, User]]
  make[AbstractStorage[F, UserId, UserAccount]].from[ProductionStorage[F, UserId, UserAccount]]
  make[UserStorage[F]].from[ProductionUserStorage[F]]
  make[AccountStorage[F]].from[ProductionAccountStorage[F]]
}

class ProductionStoragePlugin extends ProductionStorageModule[IO] with PluginDef {
  tag("production", "storage")
}
