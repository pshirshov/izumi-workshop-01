package com.github.pshirshov.izumi.workshop.w01.plugins

import com.github.pshirshov.izumi.distage.model.definition.ModuleDef
import com.github.pshirshov.izumi.distage.model.reflection.universe.RuntimeDIUniverse._
import com.github.pshirshov.izumi.distage.plugins.PluginDef
import com.github.pshirshov.izumi.workshop.w01._
import scalaz.zio.IO

abstract class DummyStorageModule[F[+ _, + _] : TagKK] extends ModuleDef {
  make[AbstractStorage[F, UserId, User]].from[DummyStorage[F, UserId, User]]
  make[UserStorage[F]].from[DummyUserStorage[F]]
}

class DummyStoragePlugin extends DummyStorageModule[IO] with PluginDef {
//  tag("dummy", "storage")
}
